package com.anryus.publish.client;

import com.alibaba.nacos.common.codec.Base64;
import com.alibaba.nacos.common.utils.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public AwsClient() {
        client = createS3Client();
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
                PutObjectResponse putObjectResponse;
                try {
                    putObjectResponse = client.putObject(objectRequest, requestBody);
                    logger.info("Upload success: "+putObjectResponse.toString());

                }catch (AwsServiceException e){
                    logger.error("Upload fail: "+e.getMessage());
                    //TODO 数据表回滚 消息队列

                }
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
