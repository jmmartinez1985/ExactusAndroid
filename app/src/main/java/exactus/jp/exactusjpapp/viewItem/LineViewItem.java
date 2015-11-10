package exactus.jp.exactusjpapp.viewItem;

/**
 * Created by JM on 10/10/2015.
 */
public class LineViewItem {

    public String articulo;
    public String precio_unitario;
    public String cantidad;
    public String descuento;
    public String descripcion;
    public int identifier;

    public LineViewItem(String articulo, String precioUnitario,String cantidad,String descuento, String descripcion,int identifier) {
        this.articulo = articulo;
        this.precio_unitario = precioUnitario;
        this.cantidad = cantidad;
        this.descuento = descuento;
        this.identifier = identifier;
        this.descripcion = descripcion;
    }

    public LineViewItem() {
    }
}
