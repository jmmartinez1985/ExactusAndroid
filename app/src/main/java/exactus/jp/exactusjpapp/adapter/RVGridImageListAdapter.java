package exactus.jp.exactusjpapp.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import  exactus.jp.exactusjpapp.Common;
import  exactus.jp.exactusjpapp.R;
import exactus.jp.exactusjpapp.viewItem.GridViewItem;

import java.util.ArrayList;


public class RVGridImageListAdapter extends RecyclerView.Adapter<RVGridImageListAdapter.ViewHolder> {

    Context _context;
    private ArrayList<GridViewItem> _grivItems;
    int _resource;

    public RVGridImageListAdapter(Context context, ArrayList<GridViewItem> items,int resource){
        _grivItems = items;
        _context = context;
        _resource = resource;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(_resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        GridViewItem item = _grivItems.get(position);
        AssetManager assets = _context.getAssets();
        //holder.txt.setText(item.text);

        //Verifica si la imagen a mostrar en el ImageView es un resource o una imagen de la web.
        if (item.image != 0) {
            holder.img.setImageResource(item.image);
        } else if (item.imgSrc != null) {
            //En el caso de que sea web, utiliza el ImageLoader para cachearla.
            //ImageLoader imageLoader = ImageLoader.getInstance();
            String src;

            if (item.imgSrc.contains("~")) {
                src = item.imgSrc.replace("~/", Common.RootWebSiteUrl);
            } else {
                src = item.imgSrc;
            }
            Glide.with(_context).load(src).into(holder.img);

        }
        Common.setFontOnView(assets, holder.txt, "fonts/Arial/arial.ttf");
    }

    @Override
    public int getItemCount() {
        return _grivItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cv;
        public ImageView img;
        public TextView txt;

        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cvGrid);
            img = (ImageView) itemView.findViewById(R.id.img);
            txt = (TextView) itemView.findViewById(R.id.txt);
        }
    }
}
