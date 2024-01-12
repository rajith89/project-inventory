# project-inventory


# Local Set up
In order to set up this projects, you need to perform some steps:
* A favorite text editor or IDE
* <a href="http://www.oracle.com/technetwork/java/javase/downloads/index.html">JDK 1.8</a> or later
* <a>MySql 8.0</a>
* <a href="http://www.gradle.org/downloads">Gradle 2.3+</a> or <a href="https://maven.apache.org/download.cgi">Maven 3.0+</a>
* You can also import the code straight into your IDE:
     * <a href="/guides/gs/sts">Spring Tool Suite (STS)</a>
     * <a href="/guides/gs/intellij-idea/">IntelliJ IDEA</a>
```
Don't Forget to insert roles to the users before to continue in the next step. [Build&Deploy](#build&Deploy)
```SQL
INSERT INTO `role` VALUES (1,'ADMIN');
INSERT INTO `role` VALUES (2,'USER');
```

## Build and Deploy
___
