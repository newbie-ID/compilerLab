import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public abstract class GETSYM {
    static void initSYM(HashMap<String, String> SYM) throws IOException {
        SYM.put("+","+");
        SYM.put(":=",":=");
        SYM.put(">=",">=");
        SYM.put("<=","<=");
        SYM.put("==","==");
        SYM.put("-","-");
        SYM.put("*","*");
        SYM.put("/","/");
        SYM.put("(","(");
        SYM.put(")",")");
        SYM.put("=","=");
        SYM.put(",",",");
        SYM.put(".",".");
        SYM.put("#","#");
        SYM.put(";",";");
        SYM.put("{","}");
        SYM.put("}","}");
        SYM.put("begin","beginsym");
        SYM.put("call","callsym");
        SYM.put("const","constsym");
        SYM.put("do","dosym");
        SYM.put("end","endsym");
        SYM.put("if","ifsym");
        SYM.put("procedure","proceduresym");
        SYM.put("odd","oddsym");
        SYM.put("var","varsym");
        SYM.put("while","whilesym");
        SYM.put("write","writesym");
        SYM.put("read","readsym");
        SYM.put("then","thensym");

        FileWriter fw = new FileWriter("output",false);
        //将这个普通的FileWriter对象传递给BufferedWriter构造方法即可
        BufferedWriter bw = new BufferedWriter(fw);
        //后面bw的时候和fw没有区别
        //关闭流
        bw.close();
    }

    static int type(char ch){
        if (ch == ' ') return 0;
        else if (ch - '0' >= 0 && ch - '0' <= 9) return 1;
        else if (ch - 'a' >= 0 && ch - 'z' <= 0) return 2;
        else if (ch - 'A' >= 0 && ch - 'Z' <= 0) return 2;
        else return 3;
    }

}
