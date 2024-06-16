# 工程简介

# 延伸阅读


【Http接口参数】
用户类型:短信
提交地址:http://116.62.235.125:8001
提交地址:https://116.62.235.125:18001
用户名:300003
密码:AVBR7Zr7
绑定IP:117.152.146.8,47.99.222.242

```

# TODO

1. docker
2. 连接池配置
3. AiRestManager 中的 Sleep
4. template_flow template_stat 中的统计  --
5. sql 日志处理
6. template_promt 的 token 统计  --


docker:
```
    docker run -d -p 8764:8080 -v /usr/aiform/logs:/logs -e spring.profile.active=debug --restart=always ai-temp-20230520 --name ai-temp
```