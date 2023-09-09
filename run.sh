#!/usr/bin/env bash

# 获取当前目录
current_dir=$(pwd)

log_dir="$current_dir/log"
mkdir -p "$log_dir"

# 在当前目录及其子目录中查找匹配的目录
find "$current_dir" -type d -name "build" -not -path "$current_dir/Common/build" -exec sh -c '
  # 进入目录
  echo "Enter: $0/libs"
  cd "$0/libs"

  # 执行所有的jar文件
  for jar_file in *.jar; do
    echo "Run: $jar_file"
    nohup java -jar "$jar_file" > "$1/$jar_file.log" 2>&1 &
  done
' {} "$log_dir" \;