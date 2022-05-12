# 1.Socket 

#### 1.不同的构造方法

- 直接使用无参构造提供的是一个空的Socket对象，后续需要使用`Socket.connect(SocketAddress)`进行连接
- 若直接传入一个SocketAddress或者host与端口号，默认就会自己调用connect连接

<img src="Java IO源码解析/image-20220510204552383.png" alt="image-20220510204552383" style="zoom: 67%;" />

#### 2.SocketAddress

该类是一个抽象接口，然后实现类最常用的就是`InetSocketAddress`，一般来说都是直接使用该类保存socket的信息，方便socket关闭之后继续使用该信息重新创建连接

<img src="Java IO源码解析/image-20220510205100007.png" alt="image-20220510205100007" style="zoom:67%;" />

# 2.ServerSocket

#### 1.构造方法

<img src="Java IO源码解析/image-20220511111544645.png" alt="image-20220511111544645" style="zoom:80%;" />

使用bind函数绑定一个端口