import java.util.HashMap;

public class compile {
    public static void main(String[] args) throws Exception {

        HashMap<String, String> symTable = new HashMap<String, String>();
        GETSYM.initSYM(symTable);
        GETSYM.getSym(symTable);

    }
}
