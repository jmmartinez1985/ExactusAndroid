package exactus.jp.exactusjpapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import exactus.jp.exactusjpapp.Common;

import exactus.jp.exactusjpapp.R;
import exactus.jp.exactusjpapp.viewItem.ListViewItem;


import java.util.ArrayList;


public class RVImageListToolBarAdapter extends RecyclerView.Adapter<RVImageListToolBarAdapter.ListHolder> {


    ArrayList<ListViewItem> listItem;
    Boolean _showRatingBar;
    Context _context;
    static ArrayList<ListViewItem> listItemHolder;
    static Context _contextHolder;

    public RVImageListToolBarAdapter(ArrayList<ListViewItem> items, Context context, Boolean showRating){
        this.listItem = items;
        this.listItemHolder = items;
        _showRatingBar = showRating;
        _context = context;
        _contextHolder = context;
    }


    @Override
    public ListHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        final View view = inflater.inflate(R.layout.card_toolbar, viewGroup, false);
        return new ListHolder(view);
    }

    private static void showSnackBar(View view,String msg) {
        Snackbar
                .make(view.findViewById(R.id.cvList), msg, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onBindViewHolder(ListHolder listHolder, int i) {

        ListViewItem item = listItem.get(i);
        AssetManager assets = _context.getAssets();
        if (item.image != 0) {
            listHolder.img.setImageResource(item.image);
        } else if (item.imgSrc != null) {
            String src;

            if (item.imgSrc.contains("~")) {
                src = item.imgSrc.replace("~/", Common.RootWebSiteUrl);
            } else {
                src = item.imgSrc;
            }

            Glide.with(_context).load(src).into(listHolder.img);
        }
        if(item.imgSrc == null){
            if(item.image == 0)
                listHolder.img.setVisibility(View.GONE);
        }

        if (!_showRatingBar) {
            listHolder.ratingBar.setVisibility(View.GONE);
        } else {
            //Setea el StarRating
            if (listHolder.ratingBar != null) {
                listHolder.ratingBar.setNumStars(item.starCount);
                if (item.ratingValue <= item.starCount) {
                    listHolder.ratingBar.setRating(item.ratingValue);
                }
            }
        }

        //Setea los textos y la fuente.
        listHolder.txt.setText(item.text);
        Common.setFontOnView(assets, listHolder.txt, "fonts/Arial/arial.ttf");

        if (!item.subText.isEmpty()) {
            listHolder.txtSubText.setText(item.subText);
            Common.setFontOnView(assets, listHolder.txtSubText, "fonts/Arial/arial.ttf");
        }

        if (item.subTextInner != null) {
            listHolder.txtSubTextInner.setText(item.subTextInner);
            Common.setFontOnView(assets, listHolder.txtSubTextInner, "fonts/Arial/arial.ttf");
        }

        //Le cambia el tama�o a la imagen, en el caso de que haya sido especificado.
        if (item.imageSize != 0) {
            int px = Common.dpToPx(item.imageSize, _context);
            listHolder.img.getLayoutParams().height = px;
            listHolder.img.getLayoutParams().width = px;
        }

        listHolder.toolBar.setTitle(item.text);


    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public static class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        ImageView img;
        TextView txt;
        TextView txtSubText;
        RatingBar ratingBar;
        TextView txtSubTextInner;
        Toolbar toolBar;

        ListHolder(final View itemView) {
            super(itemView);

            cv = (CardView)itemView.findViewById(R.id.cvList);
            img = (ImageView)itemView.findViewById(R.id.img);
            txt = (TextView) itemView.findViewById(R.id.txt);
            txtSubText = (TextView) itemView.findViewById(R.id.txtSubText);
            txtSubTextInner = (TextView) itemView.findViewById(R.id.txtSubTextInner);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            toolBar = (Toolbar) itemView.findViewById(R.id.TbCard);
            txt.setOnClickListener(this);
            txtSubText.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getPosition();
            //final ListViewItem itemData=  listItem.get(position);


        }
    }
}
