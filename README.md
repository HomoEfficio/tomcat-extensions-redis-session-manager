# Tomcat-Redis Session Manager

## Acknowledgement

This extension is originated from [https://github.com/jcoleman/tomcat-redis-session-manager](https://github.com/jcoleman/tomcat-redis-session-manager).

The original one does NOT support [Tomcat 8](https://github.com/jcoleman/tomcat-redis-session-manager#tomcat-8), so I fixed it a bit to support Tomcat 8.

## Usage

This extension needs **Java 7 or higher**.

### Pre-requisites

Redis server should be run before the Tomcat Web Application starts.

### Dependencies

Put the following files into the `CATALINA_BASE/lib` directory:

- efficio-tomcat-extensions-redis-session-manager-VERSION.jar
- jedis-2.8.0.jar
- commons-pool2-2.4.2.jar

### Configuration

Add the following inside the `<Context>` element of your `CATALINA_BASE/conf/context.xml`(NOT `server.xml`).
```xml
<Valve className="homo.efficio.tomcat.extensions.session.redis.RedisSessionValve" />
<Manager className="homo.efficio.tomcat.extensions.session.redis.RedisSessionManager"
         host="localhost" <!-- optional: defaults to "localhost" -->
         port="6379" <!-- optional: defaults to "6379" -->
         database="0" <!-- optional: defaults to "0" -->
         maxInactiveInterval="60" <!-- optional: defaults to "60" (in seconds) -->
         sessionPersistPolicies="PERSIST_POLICY_1,PERSIST_POLICY_2,.." <!-- optional -->
         sentinelMaster="SentinelMasterName" <!-- optional -->
         sentinels="sentinel-host-1:port,sentinel-host-2:port,.." <!-- optional --> />
```

For further information, please refer to [https://github.com/jcoleman/tomcat-redis-session-manager/blob/master/README.markdown](https://github.com/jcoleman/tomcat-redis-session-manager/blob/master/README.markdown).