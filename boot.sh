#!/usr/bin/env bash

# 获取当前目录
current_dir=$(pwd)

gradle booJar

cd build/target

log_dir="$current_dir/out/log"
mkdir -p "$log_dir"

# 遍历目标文件夹下的所有jar文件
for jar_file in "$target_dir"/*.jar; do
    if [ -f "$jar_file" ]; then
        # 获取文件名（不包含路径和扩展名）
        file_name=$(basename "$jar_file" .jar)

        # 获取当前时间作为启动时间
        start_time=$(date +"%Y-%m-%d_%H-%M-%S")

        # 执行jar文件，并将日志输出到日志文件
        java -jar "$jar_file" > "$log_dir/$file_name-$start_time.log" 2>&1 &
    fi
done

echo "Completed."
