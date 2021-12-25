# Java训练营代码

## Maven 编译和运行

* 安装 JDK 8以上
* 安装 Apache Maven 3.6 以上
* 项目使用 `mvn compile` 编译
* 使用 `mvn exec:java -Dexec.mainClass="geektime.nio.CharSetDemo"` 执行测试
* `Dexec.args='XXX'` 提供运行参数

## 介绍
极客时间Java提薪营

### 并发
1. Java并发和多线程
2. 并发集合
3. 线程并发竞赛
 - 程序中用单线程完成了生成随机数成绩、和排序取前十名，这样的业务
 - 另外简单实现了使用多个线程并发生成随机数成绩（采用加锁），并使用多个线程进行排序计算（采用线程安全集合）
 - 希望重新设计程序，运用高效能集合和算法，更快的处理业务逻辑

### NIO
1. Java NIO
2. Netty
