package exactus.jp.exactusjpapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import exactus.jp.exactusjpapp.Common;
import exactus.jp.exactusjpapp.R;
import exactus.jp.exactusjpapp.viewItem.LineViewItem;
import exactus.jp.exactusjpapp.viewItem.ListViewItem;

/**
 * Created by Logic on 10/18/2015.
 */
public class RVLineListAdapter  extends RecyclerView.Adapter<RVLineListAdapter.ViewHolder> {


    static Context _context;
    List<LineViewItem> _listItems;

    public  RVLineListAdapter (List<LineViewItem> items, Context context){
        this._listItems = items;
        _context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.line_listview_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        LineViewItem item = _listItems.get(i);
        AssetManager assets = _context.getAssets();

        viewHolder.txtArticulo.setText(item.articulo);
        viewHolder.txtPrecio.setText(item.precio_unitario);
        viewHolder.txtCantidad.setText(item.cantidad);
        viewHolder.txtDescuento.setText(item.descuento);

        Common.setFontOnView(assets, viewHolder.txtArticulo, "fonts/Arial/arial.ttf");
        Common.setFontOnView(assets, viewHolder.txtPrecio, "fonts/Arial/arial.ttf");
        Common.setFontOnView(assets, viewHolder.txtDescuento, "fonts/Arial/arial.ttf");
        Common.setFontOnView(assets, viewHolder.txtDescuento, "fonts/Arial/arial.ttf");

    }

    @Override
    public int getItemCount() {
        return _listItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView lblArticulo;
        public TextView txtArticulo;

        public TextView lblPrecio;
        public TextView txtPrecio;

        public TextView lblCantidad;
        public TextView txtCantidad;

        public TextView lblDescuento;
        public TextView txtDescuento;

        ViewHolder(View itemView) {
            super(itemView);

            lblArticulo = (TextView) itemView.findViewById(R.id.lblArticulo);
            lblPrecio = (TextView) itemView.findViewById(R.id.lblPrecio);
            lblDescuento = (TextView) itemView.findViewById(R.id.lblDescuento);
            lblCantidad = (TextView) itemView.findViewById(R.id.lblCantidad);

             txtArticulo = (TextView) itemView.findViewById(R.id.txtArticulo);
             txtPrecio = (TextView) itemView.findViewById(R.id.txtPrecio);
             txtDescuento = (TextView) itemView.findViewById(R.id.txtDescuento);
             txtCantidad = (TextView) itemView.findViewById(R.id.txtCantidad);

        }

    }

}
