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

/**
 * Created by JM on 10/10/2015.
 */
public class LineListViewAdapter extends ArrayAdapter<String> {

    private Activity _context;
    private ArrayList<LineViewItem> _listItems;

    public LineListViewAdapter(Activity context, ArrayList<LineViewItem> items) {
        super(context, R.layout.image_listview_item);

        this._context = context;
        this._listItems = items;

        for(LineViewItem item : items) {
            super.add(item.articulo);
        }
    }


    public void resetList(ArrayList<LineViewItem> list) {
        _listItems.clear();
        _listItems.addAll(list);

        try {
            super.clear();
        }
        catch(Exception ex) {
            String a = ex.getLocalizedMessage();
            String b = a;
        }

        for (LineViewItem item : list) {
            try {
                super.add(item.articulo);
            } catch (Exception ex) {
                String a = ex.getLocalizedMessage();
                String b = a;
            }
        }
        notifyDataSetChanged();
    }


    public View getView(int position, View view, ViewGroup parent) {

        View rowView = null;
        final ViewHolder holder;
        try {
            LayoutInflater inflater = _context.getLayoutInflater();
            LineViewItem item = _listItems.get(position);

            if (view == null) {

                //Verifica que layout debe usar dependiendo su tiene o no un subText.
                rowView = inflater.inflate(R.layout.line_listview_item, null, true);


                AssetManager assets = getContext().getAssets();


                TextView lblArticulo = (TextView) rowView.findViewById(R.id.lblArticulo);
                TextView lblPrecio = (TextView) rowView.findViewById(R.id.lblPrecio);
                TextView lblDescuento = (TextView) rowView.findViewById(R.id.lblDescuento);
                TextView lblCantidad = (TextView) rowView.findViewById(R.id.lblCantidad);

                TextView txtArticulo = (TextView) rowView.findViewById(R.id.txtArticulo);
                TextView txtPrecio = (TextView) rowView.findViewById(R.id.txtPrecio);
                TextView txtDescuento = (TextView) rowView.findViewById(R.id.txtDescuento);
                TextView txtCantidad = (TextView) rowView.findViewById(R.id.txtCantidad);


                txtArticulo.setText(item.articulo);
                txtPrecio.setText(item.precio_unitario);
                txtCantidad.setText(item.cantidad);
                txtDescuento.setText(item.descuento);

                holder = new ViewHolder();
                holder.lblArticulo = lblArticulo;
                holder.lblCantidad = lblCantidad;
                holder.lblDescuento= lblDescuento;
                holder.lblPrecio = lblPrecio;

                holder.txtArticulo =  txtArticulo;
                holder.txtCantidad = txtCantidad;
                holder.txtDescuento = txtDescuento;
                holder.txtPrecio = txtPrecio;

                rowView.setTag(holder);

                Common.setFontOnView(assets, txtArticulo, "fonts/Arial/arial.ttf");
                Common.setFontOnView(assets, txtPrecio, "fonts/Arial/arial.ttf");
                Common.setFontOnView(assets, txtDescuento, "fonts/Arial/arial.ttf");
                Common.setFontOnView(assets, txtDescuento, "fonts/Arial/arial.ttf");

            }
            else
            {
                holder = (ViewHolder) view.getTag();
                if(holder == null){
                    getView( position,  view,  parent);
                }
                rowView = view;
            }

        } catch (Exception ex) {
            Log.e("LineListViewAdapter", ex.getLocalizedMessage());
        }

        return rowView;
    }

    private static class ViewHolder {
        public TextView lblArticulo;
        public TextView txtArticulo;

        public TextView lblPrecio;
        public TextView txtPrecio;

        public TextView lblCantidad;
        public TextView txtCantidad;

        public TextView lblDescuento;
        public TextView txtDescuento;

    }

}
