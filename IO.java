import java.io.*;

/**
 * @Auther: XuZH
 * @Date: 2023/10/11 - 10 - 11 - 19:10
 * @Description: PACKAGE_NAME
 * @version: 1.0
 */
public class IO {
    static String read() throws Exception {
        //读取直到读到空行，并把空格删除返回没有换行的字符串
        String str = "";
        BufferedReader bf = new BufferedReader(new FileReader("code/in"));
        String temp = bf.readLine();
        while (temp != null && temp.length() != 0) {
            str = str.concat(temp + " ");
            temp = bf.readLine();
        }
        return str;
    }


    public static void writeToFile(String str) throws IOException {
        FileWriter fw = new FileWriter("code/compile", true);
        //将这个普通的FileWriter对象传递给BufferedWriter构造方法即可
        BufferedWriter bw = new BufferedWriter(fw);
        //后面bw的时候和fw没有区别
        bw.append(str);
        bw.newLine();//自动根据操作系统选择换行符
        //关闭流
        bw.close();
    }
}
