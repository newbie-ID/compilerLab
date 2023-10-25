import java.io.BufferedReader;
import java.io.FileReader;

/**
 * @Auther: XuZH
 * @Date: 2023/10/25 - 10 - 25 - 17:08
 * @Description: PACKAGE_NAME
 * @version: 1.0
 */
public class block {
    public static Integer type(String str) {
        // 判断是不是数字
        if (str.matches("-?\\d+(\\.\\d+)?")) return 2;
        // 判断是不是标识符
        if (str.contains(":")) {
            if (str.length() >= 5) return 1; // 这是一个标识符
            else return 0; // 这是一个符号
        }
        // 判断是不是符号
        if (str.contains(",") || str.contains(";")
                || str.contains("=") || str.contains(":")
                || str.contains("(") || str.contains(")")
                || str.contains("<") || str.contains(">")
                || str.contains(".")) return 0;
        // 其他就是保留字
        return -1;
    }

    public static void handle(String str) {
        if (str == null) return;
        else if (str.equals("constsym") || str.equals("varsym") || str.equals("proceduresym")) {
            System.out.println();
            return;
        }
        System.out.println(type(str));
    }

    public static void main(String[] args) throws Exception {
        BufferedReader bf = new BufferedReader(new FileReader("code/compile"));
        String str;
        do {
            str = bf.readLine();
            handle(str);
        } while (str != null && str.length() != 0);

    }

}
