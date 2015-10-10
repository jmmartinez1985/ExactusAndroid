package exactus.jp.exactusjpapp;

import android.app.Activity;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Iterator;

public class ImageListViewAdapter extends ArrayAdapter<String> {

    private Activity _context;
    private ArrayList<ListViewItem> _listItems;
    private boolean _showRatingBar = true;

    public ImageListViewAdapter(Activity context, ArrayList<ListViewItem> items) {
        super(context, R.layout.image_listview_item);

        this._context = context;
        this._listItems = items;

        for(ListViewItem item : items) {
            super.add(item.text);
        }
    }

    public ImageListViewAdapter(Activity context, ArrayList<ListViewItem> items, boolean showRatingBar) {
        super(context, R.layout.image_listview_item);

        this._context = context;
        this._listItems = items;
        this._showRatingBar = showRatingBar;

        for(ListViewItem item : items) {
            super.add(item.text);
        }
    }

    public void resetList(ArrayList<ListViewItem> list) {
        _listItems.clear();
        _listItems.addAll(list);

        try {
            super.clear();
        }
        catch(Exception ex) {
            String a = ex.getLocalizedMessage();
            String b = a;
        }

        for (ListViewItem item : list) {
            try {
                super.add(item.text);
            } catch (Exception ex) {
                String a = ex.getLocalizedMessage();
                String b = a;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View rowView = null;

        try {
            LayoutInflater inflater = _context.getLayoutInflater();
            ListViewItem item = _listItems.get(position);

            //Verifica que layout debe usar dependiendo su tiene o no un subText.
            if (item.subText.isEmpty()) {
                rowView = inflater.inflate(R.layout.image_listview_item_no_subtext, null, true);
            } else {
                rowView = inflater.inflate(R.layout.image_listview_item, null, true);
            }

            AssetManager assets = getContext().getAssets();
            ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);

            //Verifica si la imagen a mostrar en el ImageView es un resource o una imagen de la web.
            if (item.image != 0) {
                imageView.setImageResource(item.image);
            } else if (item.imgSrc != null) {
                //En el caso de que sea web, utiliza el ImageLoader para cachearla.
                ImageLoader imageLoader = ImageLoader.getInstance();
                String src;

                if (item.imgSrc.contains("~")) {
                    src = item.imgSrc.replace("~/", Common.RootWebSiteUrl);
                } else {
                    src = item.imgSrc;
                }

                imageLoader.displayImage(src, imageView);
            }
            if(item.imgSrc == null){
                if(item.image == 0)
                    imageView.setVisibility(View.GONE);
            }

            // Muestra u oculta la barra de rating.
            RatingBar ratingBar = (RatingBar) rowView.findViewById(R.id.ratingBar);
            if (!_showRatingBar) {
                ratingBar.setVisibility(View.GONE);
            } else {
                //Setea el StarRating
                if (ratingBar != null) {
                    ratingBar.setNumStars(item.starCount);
                    if (item.ratingValue <= item.starCount) {
                        ratingBar.setRating(item.ratingValue);
                    }
                }
            }

            //Setea los textos y la fuente.
            txtTitle.setText(item.text);
            Common.setFontOnView(assets, txtTitle, "fonts/Arial/arial.ttf");

            if (!item.subText.isEmpty()) {
                TextView txtSubtitle = (TextView) rowView.findViewById(R.id.txtSubText);
                txtSubtitle.setText(item.subText);
                Common.setFontOnView(assets, txtSubtitle, "fonts/Arial/arial.ttf");
            }

            //Le cambia el tamaño a la imagen, en el caso de que haya sido especificado.
            if (item.imageSize != 0) {
                int px = Common.dpToPx(item.imageSize, _context);
                imageView.getLayoutParams().height = px;
                imageView.getLayoutParams().width = px;
            }
        } catch (Exception ex) {
            Log.e("ImageListViewAdapter", ex.getLocalizedMessage());
        }

        return rowView;
    }

    /// Obtiene la cantidad de items a mostrar en este ListView (utilizado por el contructor del adapter).
    private static String[] getItemTexts(ArrayList<ListViewItem> items) {
        String[] s = new String[items.size()];
        for (Integer i = 0; i < items.size(); i++) {
            s[i] = items.get(i).text;
        }
        return s;
    }


}