package exactus.jp.exactusjpapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import exactus.jp.exactusjpapp.Common;
import exactus.jp.exactusjpapp.R;
import exactus.jp.exactusjpapp.viewItem.LineViewItem;
import exactus.jp.exactusjpapp.viewItem.ListViewItem;

public class RVLineListAdapter  extends RecyclerView.Adapter<RVLineListAdapter.ViewHolder> {


    static Context _context;
    List<LineViewItem> _listItems;
    static View _viewContainer;

    public  RVLineListAdapter (List<LineViewItem> items, Context context, View viewContainer){
        this._listItems = items;
        _context = context;
        _viewContainer = viewContainer;
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
        viewHolder.txtArticuloDesc.setText(item.descripcion);

        Common.setFontOnView(assets, viewHolder.txtArticulo, "fonts/Arial/arial.ttf");
        Common.setFontOnView(assets, viewHolder.txtPrecio, "fonts/Arial/arial.ttf");
        Common.setFontOnView(assets, viewHolder.txtDescuento, "fonts/Arial/arial.ttf");
        Common.setFontOnView(assets, viewHolder.txtDescuento, "fonts/Arial/arial.ttf");

    }

    @Override
    public int getItemCount() {
        return _listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        public TextView lblArticulo;
        public TextView txtArticulo;

        //public TextView lblArticuloDesc;
        public TextView txtArticuloDesc;

        public TextView lblPrecio;
        public TextView txtPrecio;

        public TextView lblCantidad;
        public TextView txtCantidad;

        public TextView lblDescuento;
        public TextView txtDescuento;

        public ImageView imgEdit;
        public ImageView imgDelete;

        AdapterView.OnItemClickListener mItemClickListener;

        ViewHolder(View itemView) {
            super(itemView);

            lblArticulo = (TextView) itemView.findViewById(R.id.lblArticulo);
            lblPrecio = (TextView) itemView.findViewById(R.id.lblPrecio);
            lblDescuento = (TextView) itemView.findViewById(R.id.lblDescuento);
            lblCantidad = (TextView) itemView.findViewById(R.id.lblCantidad);
            //lblArticuloDesc = (TextView) itemView.findViewById(R.id.lblArticuloDesc);

             txtArticulo = (TextView) itemView.findViewById(R.id.txtArticulo);
             txtPrecio = (TextView) itemView.findViewById(R.id.txtPrecio);
             txtDescuento = (TextView) itemView.findViewById(R.id.txtDescuento);
             txtCantidad = (TextView) itemView.findViewById(R.id.txtCantidad);
            txtArticuloDesc = (TextView) itemView.findViewById(R.id.txtArticuloDesc);

            imgEdit = (ImageView) itemView.findViewById(R.id.imgEdit);
            imgDelete = (ImageView) itemView.findViewById(R.id.imgDelete);

            imgEdit.setOnClickListener(this);
            imgDelete.setOnClickListener(this);
        }

        public void setOnItemClickListener(final AdapterView.OnItemClickListener mItemClickListener) {
            this.mItemClickListener = mItemClickListener;
        }


        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();
            if(v.equals(imgEdit)){
                LineViewItem item = _listItems.get(position);
                SetContainer(_viewContainer, item);
                if (position != RecyclerView.NO_POSITION) {
                    removeAt(position);
                }
                updateList(_listItems);
            }
            else if(v.equals(imgDelete)){
                Toast.makeText(_context,
                        "Linea de artículo eliminada", Toast.LENGTH_SHORT).show();
                if (position != RecyclerView.NO_POSITION) {
                    removeAt(position);
                }
            }

        }

        public void removeAt(int position) {
            _listItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(getAdapterPosition(), _listItems.size());
        }


        private void SetContainer(View container, LineViewItem item){

            EditText txtArticuloLinea = (EditText) _viewContainer.findViewById(R.id.txtArticuloLinea);
            txtArticuloLinea.setText(item.articulo);


            EditText txtArticuloDescripcion = (EditText) _viewContainer.findViewById(R.id.txtArticuloDescripcion);
            txtArticuloDescripcion.setText(item.descripcion);

            EditText txtPrecioLinea = (EditText) _viewContainer.findViewById(R.id.txtPrecioLinea);
            txtPrecioLinea.setText(item.precio_unitario);

            EditText txtCantidadLinea = (EditText) _viewContainer.findViewById(R.id.txtCantidadLinea);
            txtCantidadLinea.setText(item.cantidad);

            EditText txtDescuentoLinea = (EditText) _viewContainer.findViewById(R.id.txtDescuentoLinea);
            txtDescuentoLinea.setText(item.descuento);



        }

        public void updateList(List<LineViewItem> data) {
            _listItems = data;
            notifyDataSetChanged();
        }
    }

}
