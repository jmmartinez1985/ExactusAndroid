package exactus.jp.exactusjpapp.adapter;

import android.app.Activity;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import exactus.jp.exactusjpapp.R;
import exactus.jp.exactusjpapp.viewItem.SpinnerItem;

/**
 * Created by Logic on 10/14/2015.
 */
public class SpinnerListAdapter extends ArrayAdapter<String> {


    private Activity _context;
    private ArrayList<SpinnerItem> _listItems;

    public SpinnerListAdapter(Activity context, ArrayList<SpinnerItem> items) {
        super(context, R.layout.image_listview_item);

        this._context = context;
        this._listItems = items;

        for(SpinnerItem item : items) {
            super.add(item.clasificacion);
        }
    }

    public void resetList(ArrayList<SpinnerItem> list) {
        _listItems.clear();
        _listItems.addAll(list);

        try {
            super.clear();
        }
        catch(Exception ex) {
            String a = ex.getLocalizedMessage();
            String b = a;
        }

        for (SpinnerItem item : list) {
            try {
                super.add(item.clasificacion);
            } catch (Exception ex) {
                String a = ex.getLocalizedMessage();
                String b = a;
            }
        }
        notifyDataSetChanged();
    }

    public View getView(int position, View view, ViewGroup parent) {

        View rowView = null;

        try {
            LayoutInflater inflater = _context.getLayoutInflater();
            SpinnerItem item = _listItems.get(position);

                //Verifica que layout debe usar dependiendo su tiene o no un subText.
                rowView = inflater.inflate(R.layout.spinner_item, null, true);
                AssetManager assets = getContext().getAssets();

            TextView lblClasificacion = (TextView) rowView.findViewById(R.id.lblClasificacion);
            TextView lblDescClasificacion = (TextView) rowView.findViewById(R.id.lblDescClasificacion);


            lblClasificacion.setText(item.clasificacion);
            lblDescClasificacion.setText(item.descripcion);


            lblClasificacion.setVisibility(View.GONE);

        } catch (Exception ex) {
            Log.e("LineListViewAdapter", ex.getLocalizedMessage());
        }

        return rowView;
    }
}
