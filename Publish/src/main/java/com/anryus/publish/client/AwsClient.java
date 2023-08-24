package com.anryus.publish.client;

import com.alibaba.nacos.common.codec.Base64;
import com.alibaba.nacos.common.utils.StringUtils;
import com.anryus.publish.exception.CanNotUploadException;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class AwsClient {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final S3Client client;
    private static final ExecutorService executor = Executors.newFixedThreadPool(16);

    private static final String BUCKETNAME = "tiktak-demo";
    final RabbitTemplate rabbitTemplate;
    public AwsClient(RabbitTemplate rabbitTemplate) {
        client = createS3Client();
        this.rabbitTemplate = rabbitTemplate;
    }

    public static S3Client createS3Client() {
        var builder = S3Client.builder().region(Region.AP_EAST_1).credentialsProvider(ProfileCredentialsProvider.create());
        return builder.build();
    }


    public String upload(String objectKey,
                                    MultipartFile file,
                                    InputStream fis) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKETNAME)
                .key(objectKey)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(Duration.ofDays(7))
                .build();

        URL url;
        try (S3Presigner presigner = S3Presigner.builder().build()) {
            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
            url = presignedRequest.url();
        }

        Runnable task =  new Runnable(){
            private static int retryCount = 0;
            private static final int maxRetryCount = 3;

            @Override
            public void run() {

                String md5 = null;
                try {
                    md5 = new String(Base64.encodeBase64(DigestUtils.md5(fis)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Map<String,String> metadata = new HashMap<>();
                metadata.put(file.getName(),file.getOriginalFilename());
                PutObjectRequest objectRequest = PutObjectRequest.builder()
                        .bucket(BUCKETNAME)
                        .key(objectKey)
                        .metadata(metadata)
                        .contentType(file.getContentType())
                        .contentMD5(md5)
                        .build();

                RequestBody requestBody = null;
                try {
                    requestBody = RequestBody.fromBytes(file.getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                try {
                    PutObjectResponse putObjectResponse = doPut(objectRequest, requestBody);
                    logger.info("Upload success: "+putObjectResponse.toString());
                }catch (CanNotUploadException e){
                    logger.error("Upload fail: "+e.getMessage());
                    //发送回滚消息
                    rabbitTemplate.convertAndSend(objectKey);
                }

            }

            private PutObjectResponse doPut(PutObjectRequest por ,RequestBody req)throws CanNotUploadException {
                PutObjectResponse putObjectResponse;
                try {
                    putObjectResponse = client.putObject(por, req);


                }catch (AwsServiceException e){
                    logger.error("Upload retry: "+e.getMessage());
                    if (retryCount <= maxRetryCount){
                        //重试四次
                        retryCount++;
                        putObjectResponse = doPut(por, req);
                    }else {
                        throw new CanNotUploadException();
                    }
                }

                return putObjectResponse;

            }
        };

        executor.submit(task);



        return url.toString();
    }




    public boolean doesObjectExist(String objectKey) {

        if (StringUtils.isAnyBlank(objectKey) ){
            return false;
        }

        try {
            if (StringUtils.isAnyBlank(BUCKETNAME, objectKey)) {
                return false;
            }
            HeadObjectRequest objectRequest = HeadObjectRequest
                    .builder()
                    .key(objectKey)
                    .bucket(BUCKETNAME)
                    .build();
            client.headObject(objectRequest);
            return true;
        }catch (NoSuchKeyException e){
            return false;
        }


    }

}
