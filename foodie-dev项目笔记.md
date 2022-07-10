1、父工程的打包方式<packaging>pom</packaging>
子工程如果不写，打包方式默认是<packaging>jar</packaging>

【pom、jar、war的区别】
 （1）pom工程：用在父级工程或聚合工程中。用来做jar包的版本控制。父工程<packaging>pom</packaging>
 （2）war工程：将会打包成war，发布在服务器上的工程，如网站或服务。所以service通常打包成war包。
 （3）jar工程：将会打包成jar用作jar包使用。子工程如果不写，默认是<packaging>jar</packaging>
 <!--注：基于SpringBoot开发的项目代码可以打包成可执行jar，也可以打包成war，这个视不同需求而定-->

 2、【强制】不得使用外键与级联，一切外键概念必须在应用层解决。（阿里巴巴规范）
 小型项目无所谓，互联网项目一般不设物理外键，但逻辑上要考虑。
 设置外键性能会降低、影响热更新、不利于分库分表

 3、聚合工程中，子工程相互依赖，父工程要先install才能建立依赖关系。有代码修改都要clean、install才能运行。

 4、Maven聚合工程整合Springboot
（1）引入parent（备注：直接新建springboot项目无parent，应该是可选的）

```
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.3.12.RELEASE</version>
    <relativePath />
</parent>
```

（2）、设置资源属性

	  <properties>
	        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
	        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
	        <java.version>1.8</java.version>
	  </properties>

（3）、引入依赖dependency

```
<dependencies>
    <!--spring-boot-starter-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
        <!--排除自带日志组件jar包，便于后续引入log4j-->
        <exclusions>
            <exclusion>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-logging</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    <!--spring-boot-starter-web-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!--spring-boot-configuration-processor-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-configuration-processor</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

（4）新建启动类。注意与controller、service等层同级

```
@SpringBootApplication
public class FoodieApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoodieApplication.class,args);
    }
}
```

（5）新建测试类

```
@RestController
public class HelloController {
    @GetMapping("/hello")
    public Object hello(){
        return "Hello world";
    }
}
```



（6）父工程先clean、install，再执行启动类。访问http://localhost:8080/hello



5、spring-boot-starter-parent的作用

1、预定义了编码、java版本等诸多信息

2、通过dependencyManagement统一管理依赖及版本，预定义了一些常用依赖

3、ctrl可以点进去查看，若发现爆红，则是本地没有相关依赖，解决办法，放到<dependencies> </dependencies>标签里下载，下载好然后删掉即可（详见笔记问题：maven工程里依赖找不到依赖或插件爆红 plugin not foun）



6、spring-boot-configuration-processor的作用

给自定义的配置类生成元数据信息。

例如，在application.properties的（自定义）key上 ctrl+单击，可以跳转到对应的配置类的属性上（在springboot项目启动过一次才生效）



7、maven中dependencyManagement的作用

作用：锁定版本+子modlue不用写groupId和version。**可统一管理依赖的版本号，防止各项目对同一依赖声明多个版本，想切换到另一个版本时，只需要在父类容器里修改即可。**

（1）***划重点！***dependencyManagement里只是声明依赖，**并不实现引入**，因此**子项目需要显示的声明需要用的依赖。即并没有被导入项目，必须在子项目再次声明才会真正导入jar包。**

（2）如果不在子项目中声明依赖，是不会从父项目中继承下来的；只有在子项目中写了该依赖项,并且没有指定具体版本，才会从父项目中继承该项，并且version和scope都读取自父pom。

（3）如果子项目中指定了版本号，那么会使用子项目中指定的jar版本。

例如：父工程的申明如下

    <properties><tobato.version>1.26.5</tobato.version>　</properties><!-- 依赖声明 -->
        <dependencyManagement>
            <dependencies>
                <!-- FastDFS 分布式文件系统 -->
                <dependency>
                    <groupId>com.github.tobato</groupId>
                    <artifactId>fastdfs-client</artifactId>
                    <version>${tobato.version}</version>
                </dependency>                
            </dependencies>
        </dependencyManagement>


 子工程的pom声明如下
    	<dependencies>     
            <!-- FastDFS -->
            <dependency>
                <groupId>com.github.tobato</groupId>
                <artifactId>fastdfs-client</artifactId>
            </dependency>   
    	</dependencies>



8、数据库连接池druid与hikaricp对比

（1）功能角度考虑，Druid 功能更全面，除具备连接池基本功能外，还支持sql级监控、扩展、SQL防注入等。最新版甚至有集群监控

（2）单从性能角度考虑，从数据上确实HikariCP要强，但Druid有更多、更久的生产实践，它可靠。

（3）单从监控角度考虑，如果我们有像skywalking、prometheus等组件是可以将监控能力交给这些的 HikariCP 也可以将metrics暴露出去。

（4）springboot2.0官方默认是hikaricp数据源 

参考链接：https://developer.aliyun.com/article/893563



9、Mybatis整合Hikaricp数据源

（1）pom中引入mysql与mybatis依赖

```
<!--mysql-->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.22</version>
</dependency>

<!--Mybatis-->
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.2.2</version>
</dependency>
```

（2）在yml中配置数据源和mybatis

```
#spring数据源的相关配置
spring:
  datasource: #
    type: com.zaxxer.hikari.HikariDataSource # 数据源类型：HikariCP
    driver-class-name: com.mysql.cj.jdbc.Driver # mysql驱动
    url: jdbc:mysql://localhost:3306/foodie?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: 123456
    hikari:
    connection-timeout: 30000 # 等待连接池分配连接的最大时长（毫秒），超过这个时长还没可用的连接则发生SQ
    minimum-idle: 5 # 最小连接数
    maximum-pool-size: 20 # 最大连接数
    auto-commit: true # 自动提交
    idle-timeout: 600000 # 连接超时的最大时长（毫秒），超时则被释放（retired），默认:10分钟
    pool-name: DateSourceHikariCP # 连接池名字
    max-lifetime: 1800000 # 连接的生命时长（毫秒），超时而且没被使用则被释放（retired），默认:30分钟
    connection-test-query: SELECT 1

# mybatis配置
mybatis:
  type-aliases-package: com.icecocoeci.pojo # 所有POJO类所在包路径
  mapper-locations: classpath:mapper/*.xml # mapper映射文件
```
注：在pojo模块中新建pojo包，在mapper模块的resources下新建mapper包

（3）默认内置tomcat（非必选）

```
server:
  port: 8083
  tomcat:
    uri-encoding: UTF-8
  max-http-header-size: 80KB
```



逆向工程

注

可以在新工程进行逆向工程，再拷贝到需要的项目上

多次运行是追加，而不是覆盖



（1）添加逆向工程相关依赖

```
<!--mapper-->
<dependency>
    <groupId>tk.mybatis</groupId>
    <artifactId>mapper-spring-boot-starter</artifactId>
    <version>1.2.4</version>
</dependency>
<!--pagehelper-->
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper-spring-boot-starter</artifactId>
    <version>1.2.3</version>
</dependency>
<!-- mybatis 逆向生成工具  -->
<dependency>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator-core</artifactId>
    <version>1.3.2</version>
    <scope>compile</scope>
    <optional>true</optional>
</dependency>
```



（2）在父工程根目录下放generatorConfig.xml文件，内容如下

generatorConfig.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>
    <context id="MysqlContext" targetRuntime="MyBatis3Simple" defaultModelType="flat">
        <property name="beginningDelimiter" value="`"/>
        <property name="endingDelimiter" value="`"/>

        <!-- 修改1 通用mapper所在目录 一直填到类名 -->
        <plugin type="tk.mybatis.mapper.generator.MapperPlugin">
            <property name="mappers" value="com.icecocoeci.utils.MyMapper"/>
        </plugin>

        <!--修改2 驱动名、路径、用户名、密码-->
        <jdbcConnection driverClass="com.mysql.cj.jdbc.Driver"
                        connectionURL="jdbc:mysql://localhost:3306/foodie?useUnicode=true&amp;characterEncoding=utf8&amp;autoReconnect=true&amp;useSSL=false&amp;serverTimezone=Asia/Shanghai&amp;allowPublicKeyRetrieval=true"
                        userId="root"
                        password="123456">
        </jdbcConnection>

        <!-- 修改3 对应生成的pojo所在包 -->
        <javaModelGenerator targetPackage="com.icecocoeci.pojo" targetProject="src/main/java"/>

      <!-- 修改4 对应生成的mapper所在目录 -->
        <sqlMapGenerator targetPackage="mapper" targetProject="src/main/resources"/>

      <!-- 修改5 配置mapper对应的java映射 -->
        <javaClientGenerator targetPackage="com.icecocoeci.mapper" targetProject="src/main/java" type="XMLMAPPER"/>

        <!-- 修改6 数据库表 -->
      <table tableName="carousel"></table>
        <table tableName="category"></table>
        <table tableName="items"></table>
        <table tableName="items_comments"></table>
        <table tableName="items_img"></table>
        <table tableName="items_spec"></table>
        <table tableName="order_items"></table>
        <table tableName="order_status"></table>
        <table tableName="orders"></table>
        <table tableName="stu"></table>
        <table tableName="user_address"></table>
        <table tableName="users"></table>

    </context>
</generatorConfiguration>
```



（3）在某个子工程目录下新建utils包，新建MyMapper和GeneratorDisplay

（注意：父工程下测试失败，报错Failed to execute goal org.codehaus.mojo:exec-maven-plugin:1.6.0:exec (default-cli) ，暂未解决）

```
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
/**
 * 继承自己的MyMapper
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
```

```
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GeneratorDisplay {

   public void generator() throws Exception {

      List<String> warnings = new ArrayList<String>();
      boolean overwrite = true;
      //指定 逆向工程配置文件
      File configFile = new File("generatorConfig.xml");
      ConfigurationParser cp = new ConfigurationParser(warnings);
      Configuration config = cp.parseConfiguration(configFile);
      DefaultShellCallback callback = new DefaultShellCallback(overwrite);
      MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
            callback, warnings);
      myBatisGenerator.generate(null);
   } 
   
   public static void main(String[] args) throws Exception {
      try {
         GeneratorDisplay generatorSqlmap = new GeneratorDisplay();
         generatorSqlmap.generator();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
```

（4）执行GeneratorDisplay即可，会在父工程目录生成相应代码。（可选拷贝至子工程）



（5）项目保留逆向工具pom依赖mapper-spring-boot-starter，保留MyMapper接口类



（6）yml中引入通用mapper配置

```
# 通用 Mapper 配置
mapper:
  mappers: com.icecocoeci.utils.MyMapper
  not-empty: false
  identity: MYSQL
```



（7）标注相关注解以及启动类加上@MapperScan

1、补上相关注解，比如dao层（mapper层）要@Repository，service层要@Service#代码生成后的注意事项

2、启动类标注dao层的位置

```
@MapperScan(basePackages = "com.icecocoeci.mapper")
```

作用是扫描mybatis通用mapper所在的包

注意引入的包是import tk.mybatis.spring.annotation.MapperScan;



通用Mapper的作用

（1）详见[(35条消息) 通用Mapper常用方法__Gerald的博客-CSDN博客_mapper方法](https://blog.csdn.net/Mr_GaoYang/article/details/107933744)

（2）2-19 图文节-慕课网Java架构师技术专家-第一部分



Rest设计规范

第二列是Rest建议规范，建议用第三列这种弱规范，见名知义，减少前后端沟通成本

GET	->	/order/{id}	->	/getOrder?id=1001

POST	->	/order	->	/saveOrder

PUT	->	/order/{id}	->	/modifyOrder

DELETE	->	/order/{id}	->	/deleteOrder?id=1001



用户注册登录

（1）前端验证：一部分校验信息前端做，降低后端负载

（2）后端验证：防止用户绕过前端访问服务器



引入测试依赖

```
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
```

测试

```
package com.icecocoeci;

import com.icecocoeci.service.StuService;
import com.icecocoeci.service.TestTransService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodieApplication.class)
public class TransTest {

    @Autowired
    private StuService stuService;

    @Autowired
    private TestTransService testTransService;

    @Test
    public void myTest() {
//        stuService.testPropagationTrans();
        testTransService.testPropagationTrans();
    }

}
```





事务

```
@Transactional(propagation = Propagation.REQUIRED)
默认是Propagation.REQUIRED，常用于增删改
SUPPORTS常用于查询
    /**
     * 事务传播 - Propagation
     *      REQUIRED: 使用当前的事务，如果当前没有事务，则自己新建一个事务，子方法是必须运行在一个事务中的；
     *                如果当前存在事务，则加入这个事务，成为一个整体。
     *                举例：领导没饭吃，我有钱，我会自己买了自己吃；领导有的吃，会分给你一起吃。
     *      SUPPORTS: 如果当前有事务，则使用事务；如果当前没有事务，则不使用事务。跟着外层走。
     *                举例：领导没饭吃，我也没饭吃；领导有饭吃，我也有饭吃。
     *      MANDATORY: 该传播属性强制必须存在一个事务，如果不存在，则抛出异常
     *                 举例：领导必须管饭，不管饭没饭吃，我就不乐意了，就不干了（抛出异常）
     *      REQUIRES_NEW: 如果当前有事务，则挂起该事务，并且自己创建一个新的事务给自己使用；
     *                    如果当前没有事务，则同 REQUIRED
     *                    举例：领导有饭吃，我偏不要，我自己买了自己吃
     *      NOT_SUPPORTED: 如果当前有事务，则把事务挂起，自己不适用事务去运行数据库操作
     *                     举例：领导有饭吃，分一点给你，我太忙了，放一边，我不吃
     *      NEVER: 如果当前有事务存在，则抛出异常
     *             举例：领导有饭给你吃，我不想吃，我热爱工作，我抛出异常
     *      NESTED: 如果当前有事务，则开启子事务（嵌套事务），嵌套事务是独立提交或者回滚；
     *              如果当前没有事务，则同 REQUIRED。
     *              但是如果主事务提交，则会携带子事务一起提交。
     *              如果主事务回滚，则子事务会一起回滚。相反，子事务异常，则父事务可以回滚或不回滚。
     *              举例：领导决策不对，老板怪罪，领导带着小弟一同受罪。小弟出了差错，领导可以推卸责任。
     */
```





```

```