# aw09

Please develop a reactive version of your latest MicroPoS system using the Spring WebFlux stack.

## 完成的一些改进

1. 结构重新编排，去除了很多无用的依赖，同时代码全部使用Kotlin改写（除去`Model`, `Mapper`因为插件无法改写），提高了代码的可读性和简洁程度。
2. 将所有的服务均使用`Reactive`风格改写。按照分层架构，我们主要在`Service`层实现响应式编程，`Repository`层并不是响应式的。我们将`Repository`层的操作均看作原子性的操作，
   并且将其在`Service`层经过`Mono`或`Flux`包装。
3. 将`pos-counter`集成进入`pos-cart`中，使得其他服务与`pos-counter`相互隔绝。
