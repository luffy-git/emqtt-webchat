# 安装EMQTT地址
https://www.emqx.io/cn/downloads

# 授权认证参考地址
https://docs.emqx.io/tutorial/latest/cn/security/acl.html#http-%E8%AE%BF%E9%97%AE%E6%8E%A7%E5%88%B6
https://www.cnblogs.com/shihuc/p/10679800.html

# MQTT 安全
http://rui0.cn/archives/975

# Nginx 配置
https://blog.csdn.net/wuxiaolongah/article/details/90740392

# 温习一下鉴权流程(此处默认你是安装了 EMQTT,也会使用通讯协议连接MQTT)
* 使用 ws/wss/mqtt/mqtts等协议，请注意各客户端支持的协议,开始连接MQTT server
* 调用配置好的鉴权接口  auth/super/acl 三个接口
* 三个接口的调用流程如下
    * 连接的时候 调用 auth 接口,如果 auth 接口返回成功,则 MQTT server连接成功
    * 当客户端发起订阅或者发布的时候首先调用 super 接口，判断当前用户是否是超级用户，如果是超级用户则返回 OK，不再调用 acl 接口。
    * 如果 super 接口返回失败,则继续调用 acl 接口。在 acl 接口中验证当前用户是否有权限 发布 或者 订阅当前 TOPIC


# Nginx 详细配置
``` Nginx
server {
		listen 80;
		listen 443 ssl;
		#填写绑定证书的域名
		server_name ***;  
		#证书文件名称
		ssl_certificate https/emqtt/***.crt; 
		#私钥文件名称
		ssl_certificate_key https/emqtt/***.key; 
		ssl_session_timeout 5m;
		ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:ECDHE:ECDH:AES:HIGH:!NULL:!aNULL:!MD5:!ADH:!RC4;
		ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
		ssl_prefer_server_ciphers on;
		add_header Access-Control-Allow-Origin *;
		add_header Access-Control-Allow-Methods 'GET, POST, OPTIONS';
		add_header Access-Control-Allow-Headers 'DNT,X-Mx-ReqToken,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Authorization';
		charset utf-8;
                access_log logs/host.access.log;
		location / {
		   proxy_pass http://127.0.0.1:18083/;
		}
		location /mqtt {
		    proxy_redirect off;
			# 反向代理到 EMQ 非加密 WebSocket   ws
			proxy_pass http://127.0.0.1:8083;
			proxy_set_header Host $host;
		        # 反向代理保留客户端地址
			proxy_set_header X-Real_IP $remote_addr;
			proxy_set_header X-Forwarded-For $remote_addr:$remote_port;
			# WebSocket 额外请求头
			proxy_http_version 1.1;
			proxy_set_header Upgrade $http_upgrade;
			proxy_set_header Connection "upgrade";
		}
}
```

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/maven-plugin/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

