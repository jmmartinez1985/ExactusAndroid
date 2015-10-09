package exactus.jp.exactusjpapp;

import exactus.jp.exactusjpapp.model.PedidoLineaParametros;

/**
 * Representa un item a agregar a un ListView.
 */
public class ListViewItem {
    public String text;
    public String subText;
    public String imgSrc;
    public int image;
    public int imageSize;
    public int starCount;
    public int ratingValue;
    public int identifier;

    public ListViewItem() {
    }

    public ListViewItem(PedidoLineaParametros detalle) {

    }

    public ListViewItem(Integer image, String text) {
        this.image = image;
        this.text = text;
    }

    public ListViewItem(Integer image, String text, String subText) {
        this(image, text);
        this.subText = subText;
    }

    public ListViewItem(Integer image, String text, String subText, Integer imageSize) {
        this(image, text, subText);
        this.imageSize = imageSize;
    }

    public ListViewItem(Integer image, String text, String subText, Integer imageSize, int identifier) {
        this(image, text, subText, imageSize);
        this.identifier = identifier;
    }
}
