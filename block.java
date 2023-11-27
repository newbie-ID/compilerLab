import java.io.BufferedReader;
import java.io.FileReader;

/**
 * @Auther: XuZH
 * @Date: 2023/10/25 - 10 - 25 - 17:08
 * @Description: PACKAGE_NAME
 * @version: 1.0
 */
public class block {
    public static int line = 1;
    public static String SYM = null;
    public static int dx, level = 0, cx = 1;
    public static int genIndex = 0, constIndex = 0, varIndex = 0;
    public static String gen[] = new String[4];
    public static String constStore[] = new String[4];
    public static String varStore[] = new String[4];

    public static void constDef() throws Exception {
        String id = SYM;
        getLine(); // 等于
        getLine();
        System.out.println(id + "=" + SYM);
    }

    public static void varDef() {
        System.out.println(SYM);
    }

    public static void proDef() {
        System.out.println(SYM);
    }

    public static void getLine() throws Exception {
        BufferedReader bf = new BufferedReader(new FileReader("code/compile"));
        String str;
        int i = 0;
        while (true) {
            i++;
            str = bf.readLine();
            if (i == line) {
                line++;
                SYM = str;
                return;
            }
        }
    }

    public static void block() throws Exception {

        int savedDx, savedTx;
        int savedCx = cx; // 保存下一条指令的地址
        dx = 3;
//        System.out.println(savedCx + "gen JMP");
//        gen(JMP,0,0);

        do {
            // 常量
            if (SYM.equals("constsym")) {
                // 处理 id=num
                getLine();
                while (SYM.contains(":") && SYM.split(":")[1].equals("const")) {
                    constDef();
                    getLine(); // 读一个符号
                    if (SYM.equals(",")) getLine();
                    else if (SYM.equals(";")) {
                        getLine();
                        break;
                    }
                }
            }
            // 变量
            else if (SYM.equals("varsym")) {
                getLine();
                while (SYM.contains(":") && SYM.split(":")[1].equals("var")) {
                    varDef();
                    getLine(); // 读一个符号
                    if (SYM.equals(",")) getLine();
                    else if (SYM.equals(";")) {
                        getLine();
                        break;
                    }
                }
            }
            // 程序
            else if (SYM.equals("proceduresym")) {
                getLine();
                if (SYM.contains(":") && SYM.split(":")[1].equals("procedure")) {
                    proDef();
                    getLine(); // 处理分号
                }


                level++;
                block(); // 处理 begin xxxxx end
                level--;

//                while (!SYM.equals("endsym")){
//                    getLine();
//                    System.out.println(SYM);
//                }

                getLine(); //处理end后的分号

            } else {
                break;
            }
        } while (SYM != null && SYM.length() != 0);

        // 修改gen(JMP,0,x); 修改x指向 cx
//        System.out.println(savedCx + "JMP to" + cx);

//        gen(INT,0,dx)
        statement();
//        gen(OPR,0,OPR_RET)
    }

    public static void statement() {
        if (SYM.contains(":")) {

        } else if (SYM.equals("beginsym")) {
        } else if (SYM.equals("callsym")) {
        } else if (SYM.equals("whilesym")) {
        } else if (SYM.equals("ifsym")) {
        } else if (SYM.equals("writesym")) {
        } else if (SYM.equals("readsym")) {
        }
    }

    public static void main(String[] args) throws Exception {
        getLine();
        block();

    }
}
