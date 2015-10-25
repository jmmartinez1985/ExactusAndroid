package exactus.jp.exactusjpapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.*;
import com.bumptech.glide.Glide;
import android.widget.*;
import android.content.res.AssetManager;
import android.support.v7.widget.RecyclerView;

import  exactus.jp.exactusjpapp.Common;
import  exactus.jp.exactusjpapp.R;
import exactus.jp.exactusjpapp.viewItem.ListViewItem;

import java.util.ArrayList;

public class RVImageListAdapter extends RecyclerView.Adapter<RVImageListAdapter.ListHolder> {


    ArrayList<ListViewItem> listItem;
    Boolean _showRatingBar;
    Context _context;

    public  RVImageListAdapter (ArrayList<ListViewItem> items, Context context, Boolean showRating){
        this.listItem = items;
        _showRatingBar = showRating;
        _context = context;
    }


    @Override
    public ListHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.image_listview_item, viewGroup, false);
        return new ListHolder(view);
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

        ListHolder(View itemView) {
            super(itemView);

            cv = (CardView)itemView.findViewById(R.id.cvList);
            img = (ImageView)itemView.findViewById(R.id.img);
            txt = (TextView) itemView.findViewById(R.id.txt);
            txtSubText = (TextView) itemView.findViewById(R.id.txtSubText);
            txtSubTextInner = (TextView) itemView.findViewById(R.id.txtSubTextInner);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
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
