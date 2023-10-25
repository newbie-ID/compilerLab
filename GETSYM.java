import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public abstract class GETSYM {
    static void initSYM(HashMap<String, String> SYM) throws IOException {
        SYM.put("+", "+");
        SYM.put(":=", ":=");
        SYM.put(">=", ">=");
        SYM.put("<=", "<=");
        SYM.put("==", "==");
        SYM.put("-", "-");
        SYM.put("*", "*");
        SYM.put("/", "/");
        SYM.put("(", "(");
        SYM.put(")", ")");
        SYM.put("=", "=");
        SYM.put(",", ",");
        SYM.put(".", ".");
        SYM.put("#", "#");
        SYM.put(";", ";");
        SYM.put("{", "}");
        SYM.put("}", "}");
        SYM.put("begin", "beginsym");
        SYM.put("call", "callsym");
        SYM.put("const", "constsym");
        SYM.put("do", "dosym");
        SYM.put("end", "endsym");
        SYM.put("if", "ifsym");
        SYM.put("procedure", "proceduresym");
        SYM.put("odd", "oddsym");
        SYM.put("var", "varsym");
        SYM.put("while", "whilesym");
        SYM.put("write", "writesym");
        SYM.put("read", "readsym");
        SYM.put("then", "thensym");

        FileWriter fw = new FileWriter("./code/output", false);
        //将这个普通的FileWriter对象传递给BufferedWriter构造方法即可
        BufferedWriter bw = new BufferedWriter(fw);
        //后面bw的时候和fw没有区别
        //关闭流
        bw.close();
    }

    static int type(char ch) {
        if (ch == ' ') return 0;
        else if (ch - '0' >= 0 && ch - '0' <= 9) return 1;
        else if (ch - 'a' >= 0 && ch - 'z' <= 0) return 2;
        else if (ch - 'A' >= 0 && ch - 'Z' <= 0) return 2;
        else return 3;
    }

    static void getSym(HashMap<String, String> symTable) throws Exception {
        String str = IO.read();

        Boolean isNum = false;
        Boolean isSpace = false;
        Boolean isChar = false;
        Boolean isSym = false;
        Boolean isNew = false;//标识符
        Boolean isConst = false;
        Boolean isVar = false;
        Boolean isPro = false;

        String num = "";
        String sym = "";

        // 相当于GETCH
        for (int i = 0; i < str.length(); i++) {
            int state = GETSYM.type(str.charAt(i));

            if (state == 0) {
                //读到了空格
                if (isNum) {
                    //数字结尾
                    IO.writeToFile(num);
                    num = "";
                    isSpace = true;
                    isNum = false;
                } else if (isSpace) {
                    //无事发生
                } else if (isSym) {
                    //符号
                    IO.writeToFile(symTable.get(sym));
                    if (sym.equals(";")) {
                        isNew = false;
                        isConst = false;
                        isVar = false;
                        isPro = false;
                    }
                    isSym = false;
                    isSpace = true;
                } else if (isChar) {
                    if (isNew && symTable.get(sym) != null) {
                        // error
                        System.out.println("保留字不能作为标识符1");
                        System.exit(1);
                    } else if (isNew && symTable.get(sym) == null) {
                        // 定义新标识符了
                        if (isConst) {
                            IO.writeToFile(sym + ":标识符const");
                            symTable.put(sym, sym + ":标识符const");
                        } else if (isVar) {
                            IO.writeToFile(sym + ":标识符var");
                            symTable.put(sym, sym + ":标识符var");
                        } else if (isPro) {
                            IO.writeToFile(sym + ":标识符procedure");
                            symTable.put(sym, sym + ":标识符procedure");
                        }
                        if (sym.equals(";")) {
                            isNew = false;
                            isConst = false;
                            isVar = false;
                            isPro = false;
                        }
                    } else if (!isNew && symTable.get(sym) != null) {
                        IO.writeToFile(symTable.get(sym));
                    }
                    if (sym.equals("const") || sym.equals("var") || sym.equals("procedure")) {
                        isNew = true;
                        if (sym.equals("const")) isConst = true;
                        else if (sym.equals("var")) isVar = true;
                        else if (sym.equals("procedure")) isPro = true;
                    }
                    isChar = false;
                    isSpace = true;
                } else {
                    isSpace = true;
                }

            } else if (state == 1) {
                if (isNum) {
                    //如果前面的字符是数字
                    num += str.charAt(i);
                } else if (isSpace) {
                    num = "" + str.charAt(i);
                    isNum = true;
                    isSpace = false;
                } else if (isSym) {
                    //保留字符转换
                    IO.writeToFile(symTable.get(sym));
                    num = "" + str.charAt(i);
                    isSym = false;
                    isNum = true;
                } else if (isChar) {
                    //自定义标识符 字母数字混合，这里数字相当于字母
                    sym += str.charAt(i);
                } else {
                    //数字开头
                    isNum = true;
                    num += str.charAt(i);
                }

            } else if (state == 2) {
                //如果是字母
                if (isNum) {
                    // 不可能
                } else if (isSpace) {
                    sym = "" + str.charAt(i);
                    isSpace = false;
                    isChar = true;
                } else if (isSym) {
                    IO.writeToFile(symTable.get(sym));
                    sym = "" + str.charAt(i);
                    isSym = false;
                    isChar = true;
                } else if (isChar) {
                    sym += str.charAt(i);
                } else {
                    i--;
                    isSpace = true;
                }
            } else if (state == 3) {
                //如果是符号
                if (isNum) {
                    IO.writeToFile(num);
                    sym = "" + str.charAt(i);
                    isNum = false;
                    isSym = true;
                } else if (isSpace) {
                    sym = "" + str.charAt(i);
                    isSpace = false;
                    isSym = true;
                } else if (isSym) {
                    if (str.charAt(i) == ';') {
                        IO.writeToFile(symTable.get(sym));
                        sym = "" + str.charAt(i);
                    } else {
                        sym += str.charAt(i);
                    }
                } else if (isChar) {
                    if (isNew && symTable.get(sym) != null) {
                        System.out.println("保留字不能作为标识符2");
                        System.exit(1);
                    } else if (isNew && symTable.get(sym) == null) {
                        if (isConst) {
                            IO.writeToFile(sym + ":标识符const");
                            symTable.put(sym, sym + ":标识符const");
                        } else if (isVar) {
                            IO.writeToFile(sym + ":标识符var");
                            symTable.put(sym, sym + ":标识符var");
                        } else if (isPro) {
                            IO.writeToFile(sym + ":标识符procedure");
                            symTable.put(sym, sym + ":标识符procedure");
                        }
                        if (str.charAt(i) == ';') {
                            isNew = false;
                            isConst = false;
                            isVar = false;
                            isPro = false;
                        }
                    } else if (!isNew && symTable.get(sym) != null) {
                        IO.writeToFile(symTable.get(sym));
                    }
                    if (sym.equals("const") || sym.equals("var") || sym.equals("procedure")) {
                        isNew = true;
                        if (sym.equals("const")) isConst = true;
                        else if (sym.equals("var")) isVar = true;
                        else if (sym.equals("procedure")) isPro = true;
                    }
                    sym = "" + str.charAt(i);
                    isChar = false;
                    isSym = true;
                } else {
                    //没有这种情况
                }
            }
        }
    }
}
