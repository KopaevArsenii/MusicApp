# MusicApp
To start application you need to add application.yml file inside resources folder with this code: 

```yml
spring:
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB
  datasource:
    url: <value>
    username: <value>
    password: <value>
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    database: postgresql
application:
  jwt-secret: <value>
```
