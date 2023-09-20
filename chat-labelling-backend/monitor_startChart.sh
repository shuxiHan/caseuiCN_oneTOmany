#!/bin/bash

file="backend.txt"
count=0

while true; do
  # 使用grep命令查找匹配的行并存储到变量中
  result=$(tail -n 7 "$file")
  if [[ "$result" == *"partner=null"* ]]; then
    # 如果包含 "partner=null"，表示未匹配到用户
    user_info=$(echo "$result" | grep -oE "user=[^,]+,role=[a-zA-Z]+")
    user=$(echo "$user_info" | grep -oP "user=\K[^,]+")
    role=$(echo "$user_info" | grep -oP "role=\K[a-zA-Z]+")
    echo "user$user, role$role 未匹配到用户，请快登录和他聊天吧"
  elif [[ "$result" == *"partner="* ]]; then
    # 如果包含 "partner="，表示匹配成功
    user_info=$(echo "$result" | grep -oE "user=[^,]+,role=[a-zA-Z]+")
    user=$(echo "$user_info" | grep -oP "user=\K[^,]+")
    role=$(echo "$user_info" | grep -oP "role=\K[a-zA-Z]+")
    partner_info=$(echo "$result" | grep -oP "partner=\K[^,]+")
    echo "user$user, role$role 已经匹配到用户{partner=$partner_info}，请继续等待其他用户吧"
  fi

  sleep 5
done

