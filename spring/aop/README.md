## 切面方法
<nav>
<a href="./xml-format/README.md">xml格式</a><br/>
<a href="#annotation格式">annotation格式</a><br/>
<a href="#差异性">两者差异性</a><br/>
<a href="#通用方法">通用方法说明</a><br/>
<a href="#切面表达式">切面表达式</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#基本格式">1、基本格式</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#任意公共方法">2、任意公共方法</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#任意以 ? 开头的方法">3、任意以 ? 开头的方法</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#指定接口任意方法：">4、指定接口任意方法</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#定义在 service 包里任意方法的执行">5、定义在 service 包里任意方法的执行</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#定义在 service 包或者子包里任意方法的执行">6、定义在 service 包或者子包里任意方法的执行</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#在 service 包里的任意连接点（仅在Spring AOP中执行）">7、在 service 包里的任意连接点（仅在Spring AOP中执行）</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#在 service 包或者子包里的任意连接点（仅在Spring AOP中执行）">8、在 service 包或者子包里的任意连接点（仅在Spring AOP中执行）</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#实现了 `AccountService` 接口的代理对象的任意连接点（仅在Spring AOP中执行）：">9、实现了 `AccountService` 接口的代理对象的任意连接点（仅在Spring AOP中执行）</a><br/>
</nav>



## 差异性
> **xml格式** ：主要为基本的xml风格配置aop
>**annotation格式**：除了注解化配置之外，还以同样切面执行顺序来进行对比





## 通用方法
> 两者，其实都是针对5种常用切面方法的介绍。也是属于日常较为常用的方法，联系对应官方文档有助于理解





## 切面表达式

> 如需直接官方跳转：`https://docs.spring.io/spring/docs/5.1.7.RELEASE/spring-framework-reference/core.html#aop-pointcuts`



###### 基本格式

> 此处借用官方格式的代码

```java
execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?name-pattern(param-pattern)
            throws-pattern?)
```



```shell
除了返回类型模式，名字模式和参数模式以外，所有的部分都是可选的。

1、`*` 代表了匹配任意的返回类型。

2、 `()` 匹配一个不接受任何参数的方法， `(..)` 匹配一个接受任意数量参数的方法（零或者更多）。


3、`(*)` 匹配一个接受任意类型的参数的方法。 模式 `(*,String)` 匹配了一个接受两个参数的方法，第一个可以是任意类型，第二个则必须是 String 类型。
```



###### 任意公共方法

```java
 execution(public * *(..))
```

###### 任意以 ? 开头的方法

> 任一以 `set` 开头的方法

  ```java
  execution(* set*(..))
  ```

###### 指定接口任意方法

> `AccountService` 接口上任意方法的执行：

```java
execution(* com.coffeeandice.service.AccountService.*(..))
```



###### 定义在 service 包里任意方法的执行

```java
execution(* com.coffeeandice.service.*.*(..))
```



###### 定义在 service 包或者子包里任意方法的执行

```java
execution(* com.coffeeandice.service..*.*(..))
```



###### 在 service 包里的任意连接点（仅在Spring AOP中执行）

```java
within(com.coffeeandice.service.*)
```



###### 在 service 包或者子包里的任意连接点（仅在Spring AOP中执行） 

```
within(com.coffeeandice.service..*)
```



###### 实现了 `AccountService` 接口的代理对象的任意连接点（仅在Spring AOP中执行）

```
this(com.coffeeandice.service.AccountService)
```
