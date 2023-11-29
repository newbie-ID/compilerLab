import java.util.Scanner;

/**
 * @Auther: XuZH
 * @Date: 2023/11/28 - 11 - 28 - 17:18
 * @Description: 模拟虚拟机进行栈运算
 * @version: 1.0
 */
public class machine {
    private static int value(String[] str) {
        return Integer.parseInt(str[2]);
    }

    private static int level(String[] str) {
        return Integer.parseInt(str[1]);
    }

    public static void run(String[][] code) {
        int p = 0, b = 0, t = 0; //指令指针，指令基址，栈顶指针
        String[] i; // 存放当前指令 相当于指令寄存器
        int stack[] = new int[200]; // 栈

        do {
            i = code[p++]; //取指令
            switch (i[0]) {
                case "lit"://取值放到栈顶
                    stack[t] = value(i);
                    t++;
                    break;
                case "opr":
                    switch (value(i)) {
                        case 0:
                            t = b;
                            p = stack[t + 2];
                            b = stack[t + 1];
                            break;
                        case 1:
                            stack[t - 1] = -stack[t - 1];
                            break;
                        case 2:
                            t--;
                            stack[t - 1] = stack[t - 1] + stack[t];
                            break;
                        case 3:
                            t--;
                            stack[t - 1] = stack[t - 1] - stack[t];
                            break;
                        case 4:
                            t--;
                            stack[t - 1] = stack[t - 1] * stack[t];
                            break;
                        case 5:
                            t--;
                            stack[t - 1] = stack[t - 1] / stack[t];
                            break;
                        case 6:
                            stack[t - 1] = stack[t - 1] % 2;
                            break;
                        case 7:
                            break;
                        case 8:
                            t--;
                            if (stack[t - 1] == stack[t]) stack[t - 1] = 1;
                            else stack[t - 1] = 0;
                            break;
                        case 9:
                            t--;
                            if (stack[t - 1] != stack[t]) stack[t - 1] = 1;
                            else stack[t - 1] = 0;
                            break;
                        case 10:
                            t--;
                            if (stack[t - 1] < stack[t]) stack[t - 1] = 1;
                            else stack[t - 1] = 0;
                            break;
                        case 11:
                            t--;
                            if (stack[t - 1] >= stack[t]) stack[t - 1] = 1;
                            else stack[t - 1] = 0;
                            break;
                        case 12:
                            t--;
                            if (stack[t - 1] > stack[t]) stack[t - 1] = 1;
                            else stack[t - 1] = 0;
                            break;
                        case 13:
                            t--;
                            if (stack[t - 1] <= stack[t]) stack[t - 1] = 1;
                            else stack[t - 1] = 0;
                            break;
                        case 14:
                            System.out.println(stack[t - 1]);
                            t--;
                            break;
                        case 16:
                            System.out.print("请输入：");
                            Scanner sc = new Scanner(System.in);
                            int k = sc.nextInt();
                            stack[t++] = k;
                            break;
                    }
                    break;
                case "lod"://取相对当前过程的数据基地址为a的内存的值到栈顶
                    stack[t] = stack[base(level(i), stack, b) + value(i)];
                    t++;
                    break;
                case "sto":
                    //栈顶的值存到相对当前过程的数据基地址为a的内存
                    t--;
                    int temp = base(level(i), stack, b) + value(i);
                    stack[temp] = stack[t];
                    break;
                case "cal":
                    // 调用子进程
                    stack[t] = base(level(i), stack, b);
                    stack[t + 1] = b;
                    stack[t + 2] = p;
                    b = t;
                    p = value(i);
                    break;
                case "int":
                    t += value(i);
                    break;
                case "jmp":
                    p = value(i);
                    break;
                case "jpc":
                    t--;
                    if (stack[t] == 0) p = value(i);
                    break;
                default:
                    System.out.println("出错");
                    break;
            }
        } while (p != 0);
    }

    //求level层基址
    private static int base(int level, int[] stack, int b) {
        int b1 = b;
        while (level > 0) {
            b1 = stack[b1];
            level--;
        }
        return b1;
    }
}
