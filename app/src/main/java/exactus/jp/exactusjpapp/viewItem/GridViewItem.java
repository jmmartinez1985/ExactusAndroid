package exactus.jp.exactusjpapp.viewItem;

public class GridViewItem {

    public String text;
    public String imgSrc;
    public int image;
    public int id;

    public GridViewItem() {
    }
    public GridViewItem(Integer image, String text) {
        this.image = image;
        this.text = text;
    }
}
