package com.emqtt.webchat.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 *  EMQTT 连接前的验证 和 发布订阅 权限校验接口类
 * </p>
 * @author luffy
 * @since 2020-04-07 21:30:21
 */
@Slf4j
@Controller
@RequestMapping("mqtt")
public class MQTTAuthAPI {

    /**
     * <p>
     *  EMQ X 在设备连接事件中使用当前客户端相关信息作为参数，向用户自定义的认证服务发起请求查询权限
     *  通过返回的 HTTP 响应状态码 (HTTP Response Code) 来处理认证请求。
     *      认证成功，API 返回 200 状态码
     *      认证失败，API 返回 4xx 状态码
     *  比如：
     *      当连接的 clientId，username，password 中任意一个为空的时候，返回状态码 SC_BAD_REQUEST (400) ，表示参数有问题
     * ## Variables:
     * ##  - %u: username
     * ##  - %c: clientid
     * ##  - %a: ipaddress
     * ##  - %r: protocol
     * ##  - %P: password
     * ##  - %p: sockport of server accepted
     * ##  - %C: common name of client TLS cert
     * ##  - %d: subject of client TLS cert
     * </p>
     * @author luffy
     * @since 2020-04-07 18:53:48
     * @param rsp
     */
    @PostMapping("auth")
    public void auth(HttpServletResponse rsp) throws IOException {
        log.info("auth");
        rsp.setStatus(HttpServletResponse.SC_OK);
        rsp.getWriter().println("OK");

    }

    /**
     * <p>
     *  当用户发起 订阅 或者 发布 动作的时候调用该接口
     *  超级用户的实现代码，当认为该用户是超级用户不需要校验 acl 就返回 OK,否则返回 SC_FORBIDDEN
     *  当 super 接口返回 SC_FORBIDDEN 之后，会调用 acl 接口
     *  认为该用户为超级用户；否则返回 SC_FORBIDDEN，表示不允许访问。超级用户不受 ACL 的控制
     *  当一个用户需要执行 发布或者订阅的时候 请求该函数,验证当前用户是否有订阅或者发布的权限
     *  默认不设置超级用户,都需要校验权限
     * ## Variables:
     * ##  - %u: username
     * ##  - %c: clientid
     * ##  - %a: ipaddress
     * ##  - %r: protocol
     * ##  - %P: password
     * ##  - %p: sockport of server accepted
     * ##  - %C: common name of client TLS cert
     * ##  - %d: subject of client TLS cert
     * </p>
     * @author luffy
     * @since 2020-04-07 18:56:10
     * @param rsp
     */
    @PostMapping("super")
    public void superuser(HttpServletResponse rsp) {
        log.info("super");
        /*
            此处返回 不允许访问，因为当前系统不允许存在超级用户
            也不允许任何用户通过客户端发布 TOPIC
            或者随意订阅某个TOPIC
         */
        rsp.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    /**
     * <p>
     *  EMQ X 在发布/订阅的时候使用当前客户端相关信息作为参数，发起请求查询设备权限
     *  通过 HTTP 响应状态码 (HTTP Status) 来处理事件。
     *      ACL 成功，API 返回 200 状态码
     *      ACL 失败，API 返回 4xx 状态码
     *   返回200，表示可以操作；否则返回403（Forbidden），表示不可以操作
     *   此处主要是当 supser 接口 返回值不是 OK 的时候调用的，用来校验当前用户是否具有 发布或者订阅某个 topic 的权限
     *   此处可以禁用掉 %A = 2  不让用户通过前端直接推送消息到MQ,推送通过 后台接口
     *  ## Variables:
     * ##  - %A: 1 | 2, 1 = sub, 2 = pub
     * ##  - %u: username
     * ##  - %c: clientid
     * ##  - %a: ipaddress
     * ##  - %r: protocol
     * ##  - %m: mountpoint
     * ##  - %t: topic
     * </p>
     * @author luffy
     * @since 2020-04-07 18:53:48
     * @param rsp
     */
    @PostMapping("acl")
    public void acl(HttpServletResponse rsp) throws IOException {
        /*
            此处做鉴权
            1.所有发布TOPIC 禁止,不允许任何人直连 MQTT 去推送消息
            2.通过用户关系 service 检查该用户是否有权利订阅某个 TOPIC
            3.返回客户端 OK  执行订阅
            4.此功能相当于阉割了  2 = pub 功能，不允许任何用户 通过客户端 推送TOPIC
         */
        log.info("acl");
        rsp.setStatus(HttpServletResponse.SC_OK);
        rsp.getWriter().println("OK");
    }
}
