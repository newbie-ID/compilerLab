import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: XuZH
 * @Date: 2023/10/25 - 10 - 25 - 17:08
 * @Description: PACKAGE_NAME
 * @version: 1.0
 */
public class block {
    public static int line = 1;
    public static String SYM = null;
    public static int cx = 0;
    public static String code[][] = new String[200][3];
    public static String table[][] = new String[200][6]; // 名字 类型 数值 所在层 地址 大小

    public static void gen(String str, String y, String z) {
        code[cx][0] = str;
        code[cx][1] = y;
        code[cx][2] = z;
        cx++;
    }

    public static void constDef(AtomicInteger ptx) throws Exception {
        String[] id = SYM.split(":");
        getLine(); // 等于
        getLine();
        ptx.set(ptx.get() + 1);
//        System.out.println(id + "=" + SYM);
        // type 1常量 2变量 3程序
        // todo 查找是不是已经定义
        // 0名字 1类型 2数值 3所在层 4地址 5大小
        table[ptx.get()][0] = id[0];
        table[ptx.get()][1] = String.valueOf(1);
        table[ptx.get()][2] = String.valueOf(SYM);
    }

    public static void varDef(AtomicInteger ptx, int lev, AtomicInteger pdx) {
        String[] id = SYM.split(":");
        ptx.set(ptx.get() + 1);
        // 0名字 1类型 2数值 3所在层 4地址 5大小
        table[ptx.get()][0] = id[0];
        table[ptx.get()][1] = String.valueOf(2);
        table[ptx.get()][3] = String.valueOf(lev);
        table[ptx.get()][4] = String.valueOf(pdx.get());
        pdx.set(pdx.get() + 1);
    }

    public static void proDef(AtomicInteger ptx, int lev) {
        // 0名字 1类型 2数值 3所在层 4地址 5大小
        ptx.set(ptx.get() + 1);
        String[] id = SYM.split(":");
        table[ptx.get()][0] = id[0];
        table[ptx.get()][1] = String.valueOf(3);
        table[ptx.get()][3] = String.valueOf(lev);
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

    public static void block(int lev, AtomicInteger tx) throws Exception {
        getLine();
        int i;
        AtomicInteger dx = new AtomicInteger();
        dx.set(3);/* 名字分配到的相对地址 前三行是规定的 从第四行开始写*/
        AtomicInteger tx0 = new AtomicInteger();
        tx0.set(tx.get());/* 保留初始tx */
        int cx0; /* 保留初始cx */

        // 0名字 1类型 2数值 3所在层 4地址 5大小
        table[tx.get()][4] = String.valueOf(cx);
        gen("JMP", "0", "0");

        do {
            // 常量
            if (SYM.equals("constsym")) {
                // 处理 id=num
                getLine();
                while (SYM.contains(":") && SYM.split(":")[1].equals("const")) {
                    constDef(tx);
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
                    varDef(tx, lev, dx);
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
                    proDef(tx, lev);
                    getLine(); // 处理分号
                }

                block(lev + 1, tx); // 处理 begin xxxxx end

                getLine(); //处理end后的分号

            } else {
                break;
            }
        } while (SYM != null && SYM.length() != 0);

        code[Integer.parseInt(table[tx0.get()][4])][2] = "" + cx;
        table[tx0.get()][4] = "" + cx;
        table[tx0.get()][5] = "" + dx.get();
        cx0 = cx;
        gen("inte", "0", "" + dx.get());
        // todo
        // 输出名字表
        statement(tx, lev);
        gen("opr", "0", "0");
//        listCode(cx0);
    }

    private static void listCode(int cx0) {
        for (int i = cx0; i < cx; i++) {
            System.out.println(i + " " + code[i][0] + " " + code[i][1] + " " + code[i][2]);
            // todo
            // 输入进文件
        }
    }

    public static void statement(AtomicInteger ptx, int lev) throws Exception {
        int i, cx1, cx2;
        if (SYM.contains(":")) {
            i = findTableIndex(ptx);
            getLine(); //取符号 :=
            doException(ptx, lev);
        } else if (SYM.equals("beginsym")) {
            getLine();
            statement(ptx, lev);
            getLine();
        } else if (SYM.equals("callsym")) {
            getLine(); // 程序名
            i = findTableIndex(ptx);
            // 0名字 1类型 2数值 3所在层 4地址 5大小
            gen("cal", String.valueOf(lev - Integer.parseInt(table[i][3])), table[i][4]); /* 生成call指令 */
            getLine(); // 分号
        } else if (SYM.equals("whilesym")) {
            cx1 = cx;
            doCondition(ptx, lev);
            cx2 = cx;
            gen("jpc", "0", "0");
            getLine(); // do
            statement(ptx, lev);
            gen("jmp", "0", String.valueOf(cx1));
            code[cx2][2] = String.valueOf(cx);
        } else if (SYM.equals("ifsym")) {
            doCondition(ptx, lev); // 条件
            getLine(); // then
            cx1 = cx;
            gen("jpc", "0", "0");
            statement(ptx, lev);
            code[cx1][2] = String.valueOf(cx);
        } else if (SYM.equals("writesym")) {
            getLine(); // 左括号
            doException(ptx, lev);
            gen("opr", "0", "14"); // 输出栈顶的值
            getLine(); // 下一个
        } else if (SYM.equals("readsym")) {
            getLine(); // 左括号
            getLine(); // 变量名
            i = findTableIndex(ptx);
            gen("opr", "0", "16"); // 读值到栈顶
            gen("sto", String.valueOf(lev - Integer.parseInt(table[i][3])), table[i][4]); // 读入
            getLine(); // 右括号
            getLine(); // 下一个
        }
    }

    private static void doCondition(AtomicInteger ptx, int lev) throws Exception{
        String key[] = {"beginsym", "callsym", "whilesym", "ifsym", "writesym", "readsym", "endsym", ")","thensym"};
        String str = "";
        do {
            getLine();
            str += SYM;
        } while (!Arrays.asList(key).contains(SYM));
        System.out.println(str);
    }

    private static void doException(AtomicInteger ptx, int lev) throws Exception {
        String key[] = {"beginsym", "callsym", "whilesym", "ifsym", "writesym", "readsym", "endsym", ")","thensym"};
        String str = "";
        do {
            getLine();
            str += SYM;
        } while (!Arrays.asList(key).contains(SYM));
        System.out.println(str);
    }

    private static int findTableIndex(AtomicInteger ptx) {
        String name = SYM.split(":")[0];
        int i = -1;
        for (int j = 1; j <= ptx.get(); j++) {
            if (name.equals(table[j][0])) {
                i = j;
                break;
            }
        }
        return i;
    }

    public static void main(String[] args) throws Exception {
        AtomicInteger tx = new AtomicInteger();
        tx.set(0);
        block(0, tx);

    }
}
