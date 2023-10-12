## 题意参考网址

[编译原理 实验1 PL/0语言词法分析_odd编译原理-CSDN博客](https://blog.csdn.net/u011686226/article/details/40323679)

## 题意理解

 GETSYM要完成的任务：

1. 滤掉单词间的空格。

存在疑惑：如语句`const a = 5;`其中如果单纯去除所有空格，则成为`consta=5;`，对于`consta`一定是编译出错。这一条没有实现，后来考虑题意可能是`const a=5;`，但是因为已经处理好了，没有更改。

2. 识别关键字，用查关键字表的方法识别。当单词是关键字时，将对应的类别放在SYM中。如IF的类别为IFSYM，THEN的类别为THENSYM。

将关键字存入了`hashmap`，识别到关键字（保留字或标识符），将`key`转换为`value`并存入文件中。

3. 识别标识符，标识符的类别为IDENT，IDRNT放在SYM中，标识符本身的值放在ID中。关键字或标识符的最大长度是10。

“标识符本身的值放在ID中”该语句没有实现。实现思路：在识别到`const`,`var`,`procedure`关键字后，下一个读到的字符将往`hashmap`中存入键值对：(标识符,标识符：是一共标识符)

4. 拼数，将数的类别NUMBER放在SYM中，数本身的值放在NUM中。

待改进：识别NUM的数据类型，并将（NUM，类型）的键值对存入SYM

5. 拼由两个字符组成的运算符，如：>=、<=等等，识别后将类别存放在SYM中。

疑惑：拼接字符串应该提前存入SYM中吧。

6. 打印源程序，边读入字符边打印。

实际实现：全部读入拼接成一行字符串，再全部注入程序分析。



## TODO

1. 一行一读一处理
2. 存标识符的数据类型
3. 存入数据的数据类型