# **USL —— Universal Script Language** (通用脚本语言)

<p style="text-align:center">
<img src="https://img.shields.io/badge/JDK-21+-brightgreen" alt="">
<img src="https://img.shields.io/badge/Aviator-5.4.1-brightgreen" alt="">
<img src="https://jitpack.io/v/com.gitee.yixi-dlmu/usl.svg" alt="">
<a href='https://gitee.com/yixi-dlmu/usl/stargazers'><img src='https://gitee.com/yixi-dlmu/usl/badge/star.svg?theme=dark' alt='star'></a>
</p>

<br/>

![IntelliJ IDEA logo](https://resources.jetbrains.com/storage/products/company/brand/logos/IntelliJ_IDEA_icon.svg)
> 特别感谢：`JetBrains`提供的开源项目免费许可证支持

## 一、简介

> `USL`是一个通用脚本语言开发框架，核心实现基于`Aviator`脚本引擎，并在其基础上做了大量的封装与扩展，
> 以满足更多更复杂的实际业务场景。所有的内置插件与机制都支持动态删除或替换，主要特性包括但不限于：

1. **更灵活的函数定义**：可以为任意类或方法(静态或非静态)创建自定义函数，且无需实现`AviatorFunction`接口
2. **更丰富的功能插件**，在函数执行前、执行后、执行成功时、执行失败时、执行完成时，提供功能扩展
3. **更完整的调用堆栈**，对于每一次函数调用，保存调用时的子表达式内容、参数信息、异常信息、调用结果
4. **更强大的参数校验**，对函数的参数类型、参数个数、参数大小、返回值类型等常见场景进行校验
5. **更实用的性能优化**，例如使用`Caffeine`替代原有的`LRU-Map`缓存，提供脚本引擎执行全生命周期监听器等
6. **更方便的交互选项**，可以使用内嵌模式，也可以使用`CLI`命令行模式，也可以使用`WEB`模式交互

## 二、整体架构

![图标](img/framework.png)

## 三、内置功能与插件

- 缓存插件、异步插件、日志插件、监控插件、参数绑定插件、参数校验插件、限流插件、权限插件、重试插件等
- 网络函数、系统函数、数据库函数、文本处理函数、数学函数、加解密函数、日期函数、逻辑函数、集合函数等

## 四、代码合并【新增特性 或 修复缺陷】

1. 拉取远程`master`主分支，更新本地分支，确保本地获取的是最新版代码
2. 从本地`master`分支切出一个新分支并重命名，通常与新增特性或缺陷内容相关
3. 在本地新分支完成开发并测试
4. 再次更新本地`master`分支，并将本地`master`分支合并到新分支上，确保无代码冲突
5. 发起`pull request`，将本地新分支合并到远程`master`主分支上，并等待审核

## 五、使用方法

### 1.执行脚本/表达式

```java
class Test {
    void test() {
        USLRunner runner = new USLRunner();
        // USL初始化
        runner.start();

        // 构建执行参数
        Param param = new Param();
        param.setScript("str.isEmpty('test')");

        // 运行脚本/表达式
        Result result = runner.run(param);

        // 打印输出执行结果的状态码、异常信息以及执行结果
        System.out.println(result.getCode());
        System.out.println(result.getMessage());
        System.out.println(result.getData());
    }
}
```

> 可以看出，相比于`Aviator`原本的调用方式，仅多了一个`USL`初始化的逻辑，以及添加了更具体的执行结果信息。在初始化逻辑中，`USL`
>
将会根据传入的配置项，初始化缓存、线程池、函数库等等，而状态码和异常信息可以帮助调用者更快速地排查当前执行的具体错误原因。`USL`
> 支持为不同的应用场景配置独立的执行器实例，每一个实例之间的配置项相互隔离，以实现更复杂的功能需求。

### 2.如何定义一个函数

```java

@FunctionGroup
class StringFunction {

    @Function("str.isEmpty")
    public boolean strIsEmpty(String str) {
        return isEmpty(str);
    }

    @Function("str.isBlank")
    public boolean strIsBlank(String str) {
        return isBlank(str);
    }

    @Function("str.emptyToDefault")
    public String strEmptyToDefault(String str, String defaultStr) {
        return emptyToDefault(str, defaultStr);
    }
}
```

> 以上例子中，定义了三个函数，它们的函数名称由`@Func`注解中声明，支持为同一个函数配置多个函数别名，同时，建议将多个功能类似的函数放在同一个类中，
> 以便于更好地维护它们。如果想使用`Aviator`提供的函数声明方式，`USL`同样支持，例如：

```java
class NativeStringFunction extends AbstractFunction {

    @Override
    public String getName() {
        return "str.isEmpty";
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        return AviatorBoolean.valueOf(isEmpty(String.valueOf(arg1.getValue(env))));
    }
}
```

### 3.如何批量注册函数

在第二节示例中，仅仅是通过两种方式编写了函数的定义，但是还没有注册到`USL`实例中，因此`USL`无法主动感知到它们，需要手动
在配置项中进行注册，`USL`提供了多种注册方式，如包扫描注册、类扫描注册等，例如：

```java
class RegisterTest {
    void test() {
        Configuration configuration = USLRunner.defaultConfiguration()
                .configEngine()
                .scan(StringFunctionTest.class)
                .finish();

        USLRunner runner = new USLRunner(configuration);
    }
}
```

> 上述例子中，首先获取了一个新的默认配置项，`defaultConfiguration()`，然后在默认配置项的基础上，获取执行引擎配置项，并设置
> 扫描以`StringFunctionTest.class`类路径为基础的包及其子包下的函数。当且仅当类上拥有`@Func`注解时，才会被注册。

### 4.如何开发一个插件

插件系统时`USL`的核心特性之一，插件可以在函数（基于@Func注解的函数以及实现了`AviatorFunction`接口的原生函数）执行前、执行成功、
执行失败以及执行完成后，进行功能的动态扩展，`USL`也内置了很多插件供调用者自由组合，插件的核心接口是`Plugin`，共有四种子插件，
`BeginPlugin`、`SuccessPlugin`、`FailurePlugin`以及`FinallyPlugin`，具体的插件均需要实现其中一个或多个子接口。以日志插件为例：

```java
class LoggerPlugin implements BeginPlugin, SuccessPlugin, FailurePlugin {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onBegin(FunctionSession session) {
        String name = session.definition().name();
        Supplier<Object[]> supplier = () -> new Object[]{name, format(session.env(), session.objects())};
        EnabledLogger.info(logger, "USL function execute params - [{}] : [{}]", supplier);
    }

    @Override
    public void onFailure(FunctionSession session) {
        String name = session.definition().name();
        Supplier<Object[]> supplier = () -> new Object[]{name, session.exception().getMessage()};
        EnabledLogger.warn(logger, "USL function execute errors - [{}] : [{}]", supplier);
    }

    @Override
    public void onSuccess(FunctionSession session) {
        String name = session.definition().name();
        Supplier<Object[]> supplier = () -> new Object[]{name, session.result()};
        EnabledLogger.info(logger, "USL function execute return - [{}] : [{}]", supplier);
    }

    /**
     * 格式化参数
     *
     * @param env     上下文环境
     * @param objects 原始参数
     * @return 参数描述
     */
    protected String format(Env env, AviatorObject[] objects) {
        return Arrays.stream(objects)
                .map(item -> String.valueOf(item.getValue(env)))
                .collect(Collectors.joining(CharPool.COMMA + CharSequenceUtil.SPACE));
    }
}
```

> 日志插件实现了执行前、执行成功时以及执行失败时的接口，分别日志记录参数信息、执行成功的返回值信息以及执行失败的异常信息。插件的核心在于
> `FunctionSession`函数调用会话，在会话中存储了当前调用的函数定义信息、调用信息以及异常信息。完成的插件编制逻辑如下：

```java
interface FunctionPluggable {
    /**
     * 实际的处理逻辑
     *
     * @param session 函数调用会话
     * @return 执行结果
     */
    Object handle(final FunctionSession session);

    /**
     * 获取插件集合
     *
     * @return 插件集合
     */
    Plugins plugins();

    /**
     * 将插件逻辑编织到实际处理逻辑前、成功、失败、后
     *
     * @param session 本次函数调用会话
     * @return 最终返回值
     */
    default AviatorObject withPlugin(final FunctionSession session) {
        try {
            // 执行前置回调插件
            this.plugins().execute(BeginPlugin.class, plugin -> plugin.onBegin(session));

            // 正常来说执行结果还没有被初始化，这里应该为空
            // 但如果不为空，说明前置插件已经设置了本次调用返回值
            // 那么就直接将前置插件的返回值作为最终结果
            if (session.result() != null) {
                // 如果前置插件设置了返回值，则会被视为执行成功
                // 因此同样会执行成功回调插件
                this.plugins().execute(SuccessPlugin.class, plugin -> plugin.onSuccess(session));

                // 统一包装返回值
                return FunctionUtils.wrapReturn(session.result());
            }

            // 调用实际处理逻辑
            Object result = this.handle(session);

            // 设置当前调用的返回值
            session.setResult(result);

            // 执行成功回调插件
            this.plugins().execute(SuccessPlugin.class, plugin -> plugin.onSuccess(session));

            // 统一包装返回值
            // 这里的返回值取的是调用会话中的返回值
            // 也就意味着执行成功回调插件可以改变返回值
            return FunctionUtils.wrapReturn(session.result());
        } catch (Exception e) {

            // 设置当前调用的异常
            session.setException(e);

            // 设置失败回调插件
            this.plugins().execute(FailurePlugin.class, plugin -> plugin.onFailure(session));

            // 正常来说当前调用异常一定不为空
            // 但是如果为空说明失败回调插件清空了当前调用异常
            // 那么就直接返回调用会话中的返回值
            Optional.ofNullable(session.exception()).ifPresent(error -> {
                // 如果调用异常不为空，则将调用异常统一包装为 USL-Execute 异常
                // 这样做是为了更好的区分整个脚本执行周期中的异常来源
                if (error instanceof UslExecuteException) {
                    throw (UslExecuteException) error;
                } else {
                    throw new UslExecuteException(error);
                }
            });

            // 返回并包装调用会话中的返回值
            return FunctionUtils.wrapReturn(session.result());
        } finally {

            // 执行最终回调插件
            this.plugins().execute(FinallyPlugin.class, plugin -> plugin.onFinally(session));
        }
    }
}
```

### 5.如何批量注册插件

与函数注册逻辑类似，插件的开发也分为插件声明和插件注册两部分，一个标准的插件注册逻辑如下：

```java
class LoggerEnhancer extends AbstractFunctionEnhancer {
    private final LoggerPlugin singletonPlugin = new LoggerPlugin();

    @Override
    protected void enhanceNativeFunction(NativeFunction nf) {
        nf.plugins().install(singletonPlugin);
    }

    @Override
    protected void enhanceAnnotatedFunction(AnnotatedFunction af) {
        af.plugins().install(singletonPlugin);
    }
}
```

> 插件注册依赖于`FunctionEnhancer`函数增强接口，该接口在所有函数都被初始化并注册成功后执行，用以实现特定逻辑下的函数功能增强。
> 在上述示例中，`LoggerEnhancer`类为基于注解（基于@Func注解的函数以及实现了`AviatorFunction`接口的原生函数）安装了日志插件，
> 插件生效的顺序为`Integer.MAX_VALUE - 10`
> ，该数值越大则生效期越晚，多个有序插件会形成一条插件链。`@AutoService(FunctionEnhancer.class)`
> 注解可以在编译器自动生成`SPI`服务配置文件，即所有的函数增强器均由`SPI`机制进行注册。

### 6.服务注册与发现

在前几节中，除了通过配置项声明的固定参数以外，大部分的扩展机制都是可以动态替换的，核心原理基于`SPI`
机制实现，即在`META-INF/services`中，
声明接口的全类名，以及在配置文件中，声明实现类的全类名。`USL`在`JDK`内置的`SPI`机制基础上，做了进一步封装，支持为同一个接口的多个服务实现类
进行排序，优先级越高的实现类越先被执行，优先级的声明依赖于`@Order`注解。与此同时，`USL`也提供了自定义服务发现接口，以供扩展，例如：

```java
class SpringServiceFinder implements ServiceFinder, ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext context) {
        SpringServiceFiner.context = context;
    }

    @Override
    public <T> T search(Class<T> serviceType) {
        return context.getBean(serviceType);
    }
}
```

> 上述例子中，提供了基于`Spring`容器的服务发现扩展机制，支持从容器中获取服务实现类。当然，也可以将`SPI`机制与容器机制结合使用。

### 7.`USL`交互模式

默认情况下，`USL`作为通用脚本语言开发框架供开发者进行扩展调用，但`USL`还额外提供了两种交互模式，`WEB`页面管理模式和`CLI`
命令行模式，
`CLI`命令行模式下提供了与`WEB`模式类似的功能，支持使用命令进行批处理或调用运行脚本。在`WEB`模式下，会开启一个`HTTP`本地服务，
并在基于`Layui-Admin`二次开发的页面上提供各类管理功能，包括但不限于：

- 查看`USL`实例信息
- 查看系统运行信息
- 管理已注册的函数
- 管理所有初始化器、函数增强器
- 管理本机所有`USL`脚本以及提供快速运行入口
  ![WEB模式预览图1](img/web-init.jpg)
  ![WEB模式预览图2](img/web-script.jpg)
  ![WEB模式预览图2](img/web-login.jpg)

### 8.`USL`功能模块

由于`USL`提供了大量函数定义，但往往业务逻辑并不需要用到全部的功能，冗余的函数定义可能会对性能造成一定影响，因此`USL`
项目被划分为若干个子模块。

1. `USL-Core`模块：核心功能实现，所有子模块必须引入，负责处理脚本编译、缓存管理、函数注册、插件管理等。
2. `USL-Logger`模块：对接`Slf4j`日志功能实现，如您的项目已经引入其它日志实现，如`Logback`，则无需引入此模块，对功能无任何影响。
3. `USL-Plugin`模块：核心插件实现，建议所有子模块引入，实现了常用插件，如参数绑定插件、参数校验插件、异步插件、重试插件、日志插件等。
4. `USL-Resource`模块：`WEB`交互模式实现，包含了前端页面与后端接口，如无需开启`WEB`交互，则不用引入此模块。
5. `USL-Function-Base`模块：基础函数类库，建议引入，实现了一些基础函数功能，如`get()`函数、`set()`函数、`logger.info()`函数等。
6. `USL-Function-XXX`模块：具体函数类库，建议按需引入，每个子模块集中实现了某个功能相关的函数，如`USL-Function-Date`
   实现日期时间相关函数。

### 9.语法高亮与自定义函数提示
一个好的代码编辑器对日常开发和学习理解十分重要，遗憾的是`Aviator`作为一门较为小众的脚本语言，并没有获得主流代码编辑器的适配，如`IDEA`，
其配套的`IDEA-Plugin`也很久没有维护，导致无法在高版本的`IDEA`上使用。然而专门开发一门代码编辑器其本身的投入远比维护`USL`所需时间精力
大得多，因此，本人选择了一个折衷方案 —— 将`USL`即`Aviator`的基础语法结构对`JavaScript`保持兼容，即：

1. 支持使用`let`和`var`关键字来定义变量
2. 支持使用`function`和`fn`关键字来定义函数
3. 支持使用`===`和`==`运算符表示相等运算
4. 支持使用`//`和`##`来表示注释符
5. 支持`for`循环语句定义`()`左右括号结构，原生的结构如下：`for xxx in array`，支持`()`内变量前加`let`或者`var`关键字；
```javascript
for (var xxx in array) {
    do_something();
}
```
6. 由于`JavaScript`脚本最后一句不建议为`return`，因此提供`await`关键字，两者语义完全等效；
7. 对于所有的自定义函数，提供`JavaScript`函数库示例文件生成器，该生成器会生成一组配置文件，在配置文件中的函数支持语法高亮和动态跳转
```java
static class Test {
    void test() {
        LibraryGenerator.newBuilder()
                // 是否为`USL`内置自定义函数生成示例文件
                .all()
                // 示例文件的输出目录，默认为当前项目根目录下
                .output("path")
                // 需要生成示例文件的函数包名，允许配置多个
                .packageName("packageName")
                // 构建生成器
                .build()
                // 开始构建示例文件
                .generate();
    }
}
```

如果自定义函数存在描述性注解，则生成的文件中也会存在对应的描述注解，示例文件的生成结果参考：
```ts
declare function server_start(a: any): any;

declare function server_stop(a: any): any;

declare function server_filter(a: any, b: any, c: any): any;

declare function server(a: any, b: number): any;

declare function server_resource(a: any, b: any, c: string): any;

declare function server_route(a: any, b: any, c: string, d: any): any;

declare function server_route_script(a: any, b: any, c: string, d: any): any;

declare function logger_error(a: string, b: any): any;

declare function console_error(a: string, b: any): any;

declare function logger_debug(a: string, b: any): any;
```

> 使用示例文件生成器成功生成以后，还需要将生成的目录配置为动态链接库，不同代码编辑器的配置方法不同，此处以`IDEA`为例：

![动态链接库配置方式](img/usl@types.jpg)

> 配置完以后，新建的`js`文件就支持`Aviator`以及`USL`所有的语法规则，并且支持`USL`自定义函数语法高亮和动态跳转