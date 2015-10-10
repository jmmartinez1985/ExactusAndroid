package exactus.jp.exactusjpapp.logic;

/**
 * Created by JM on 10/10/2015.
 */
public class InterfaceHolder {

    private static OpenDialog openDialog;


    public static OpenDialog get() {
        return openDialog;
    }

    public static void set(OpenDialog openDialog) {
        openDialog = openDialog;
    }
}
