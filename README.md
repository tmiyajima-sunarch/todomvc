# TodoMVC サンプル

Vue.jsを用いたTodoMVCの実装サンプルです。

- Vue.jsによるシングルページアプリケーションをSpring Bootで配信
- Spring BootでのRest APIと連携
- Spring Securityによる認証
- [frontend-maven-plugin]によるシングルページアプリケーションのビルド

## 実行方法

以下のコマンドで、サーバーを起動します。

```
./mvnw spring-boot:run
```

起動後、`http://localhost:8080`を開いてください。

※Spring Securityで認証がかかっているため、`user:user`でログインしてください。

## 使用技術

### サーバーサイド

- Spring Boot
- Spring MVC
- Spring Security
- Spring Data JPA

### フロントエンド

- Vue.js
- Vuex
- Vue Router
- Bootstrap Vue
- Axios
- vue-cli (Ver. 3)


[frontend-maven-plugin]: https://github.com/eirslett/frontend-maven-plugin