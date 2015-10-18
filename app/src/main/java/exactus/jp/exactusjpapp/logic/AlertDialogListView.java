package exactus.jp.exactusjpapp.logic;

/**
 * Created by Logic on 10/18/2015.
 */
public enum AlertDialogListView {

    Bodega("Desea utilizar esta bodega?"),
    Cliente("Desea utilizar este cliente?");



    AlertDialogListView(String id) {
        this.setMessage(id);
    }

    public String getMessage() {
        return value;
    }

    public void setMessage(String value) {
        this.value = value;
    }

    private String value;

}
