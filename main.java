import java.util.HashMap;

public class main {
    public static void main(String[] args) throws Exception {

        HashMap<String, String> symTable = new HashMap<String, String>();
        GETSYM.initSYM(symTable);

        String str = IO.read();
        System.out.println(str);
        System.out.println();
        Boolean isNum = false;
        Boolean isSpace = false;
        Boolean isChar = false;
        Boolean isSym = false;
        Boolean isNew = false;//标识符

        String num = "";
        String sym = "";
        for (int i = 0; i < str.length(); i++) {
            int state = GETSYM.type(str.charAt(i));
            System.out.println(state);
            if (state == 0) {
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
                    isSym = false;
                    isSpace = true;
                } else if (isChar) {
                    System.out.println(sym);
                    if (isNew && symTable.get(sym) != null) {
                        System.out.println("保留字不能作为标识符");
                        System.exit(1);
                    } else if (isNew && symTable.get(sym) == null) {
                        IO.writeToFile(sym + ":标识符");
                        symTable.put(sym, sym + ":标识符");
                        isNew = false;
                    } else if (!isNew && symTable.get(sym) != null) {
                        IO.writeToFile(symTable.get(sym));
                    }
                    if (sym.equals("const") || sym.equals("var") || sym.equals("procedure")) isNew = true;
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
                        System.out.println(symTable.get(sym));
                    }
                } else if (isChar) {
                    System.out.println(sym);
                    if (isNew && symTable.get(sym) != null) {
                        System.out.println("保留字不能作为标识符");
                        System.exit(1);
                    } else if (isNew && symTable.get(sym) == null) {
                        IO.writeToFile(sym + ":标识符");
                        symTable.put(sym, sym + ":标识符");
                        if (str.charAt(i) != ',') isNew = false;
                    } else if (!isNew && symTable.get(sym) != null) {
                        IO.writeToFile(symTable.get(sym));
                    }
                    if (sym.equals("const") || sym.equals("var") || sym.equals("procedure")) isNew = true;
                    sym = "" + str.charAt(i);
                    isChar = false;
                    isSym = true;
                } else {
                    //没有这种情况
                }
            }
        }
        System.out.println(num);
    }
}
