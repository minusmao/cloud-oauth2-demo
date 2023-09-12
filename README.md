# cloud-oauth2-demo
spring-cloud  结合 oauth2 的单点登录示例，方案（spring-cloud-alibaba + spring-security + oauth2）

## 项目结构
| 模块            | 描述           |
|---------------|--------------|
| cloud-auth    | 认证授权服务       |
| cloud-common  | 通用代码模块       |
| cloud-gateway | 网关服务         |
| cloud-user    | 用户服务（资源服务 1） |

## 工作流程
![工作流程](./doc/cloud-oauth2-demo工作流程.png)

## 参考文档
- [Spring Cloud实战系列(十) -单点登录JWT与Spring Security OAuth](https://zhuanlan.zhihu.com/p/56401253)
