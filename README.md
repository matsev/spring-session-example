# Spring Session Example

This is a small sample project used to demonstrate [Spring Session](http://projects.spring.io/spring-session/). It consists
of an Embedded Tomcat that has a single `HelloServlet` servlet. When issuing a `GET` request, the servlet will respond with 
either the default `Hello World!` or if the `name` session attribute has been set with `Hello [name]`. The `name` session
attribute can be changed by issuing a `POST` request with a `name` parameter. For more information, please read my blog post
[Scaling out with Spring Session](https://www.jayway.com/2015/05/31/scaling-out-with-spring-session/).


### Redis 

Download and install [Redis](http://redis.io). For example if you are using OS X you can use Brew, e.g. 
    
```sh  
$ brew install redis  
```  
   
(If you use Linux, you can use `yum` or `apt-get`. Alternatively, read the [Quick Start Guide](http://redis.io/topics/quickstart).)   

Start Redis by calling the `redis-server` command, e.g. if you use Brew on OS X: 

```sh
$ redis-server /usr/local/etc/redis.conf
[...]
Port: 6379
[...]
Server started, Redis version 3.0.1
[...]
```


### 2. Starting a Server

Build this project in another terminal:

```sh
$ mvn package
```

Start the first Tomcat instance on port 8080 (the default)

```sh
$ target/bin/main
[...]
INFO: Sarting ProtocolHandler ["http-nio-8080"]
```

Check the default, non session, response by issuing a `GET` request:
```sh
$ curl http://localhost:8080
Hello World!
```


### 3. Change the Session State

Change the value of the session attribute `name` by issuing a `POST` request with the `name` set as a parameter:
  
```sh
$ curl -i -d "name=Mattias" http://localhost:8080
[...]
Set-Cookie: SESSION=12b70435-9e6a-4e67-b544-01394dd59da0; Path=/; HttpOnly
[...]
```

Verify that the session attribute has been changed by issuing a `GET` request:
```sh
$ curl -H "Cookie: SESSION=12b70435-9e6a-4e67-b544-01394dd59da0" localhost:8080
Hello Mattias!
```


### 4. Session Replication to a Second Server

Start the second Tomcat instance on port 8081:

```sh
$ target/bin/main 8081    
[...]    
INFO: Starting ProtocolHandler ["http-nio-8081"]
```


Verify that the session attribute is available by issuing a `GET`  request to the other Tomcat:

```sh
$ curl -H "Cookie: SESSION=12b70435-9e6a-4e67-b544-01394dd59da0" localhost:8081
Hello Mattias!
```


### 5. Failover to New Server

You can simulate server outage and failover by first shutting down your running servers and then restart a third server: 
```sh
$ target/bin/main 8082    
[...]    
INFO: Starting ProtocolHandler ["http-nio-8082"]
$ curl -H "Cookie: SESSION=12b70435-9e6a-4e67-b544-01394dd59da0" localhost:8082
Hello Mattias!
```

Conclusion, the session state is preserved in all these cases.
