package exactus.jp.exactusjpapp.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.res.AssetManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import exactus.jp.exactusjpapp.Common;
import exactus.jp.exactusjpapp.PedidoActivity;
import exactus.jp.exactusjpapp.R;
import exactus.jp.exactusjpapp.logic.AlertDialogListView;
import exactus.jp.exactusjpapp.viewItem.ListViewItem;

/**
 * Created by Logic on 10/18/2015.
 */
public class RVImageListAdapter extends RecyclerView.Adapter<RVImageListAdapter.ListHolder> {


    static List<ListViewItem> listItem;
    Boolean _showRatingBar;
    static Context _context;
    static Dialog _dialog;
    static TextView _txtFocus;
    static TextView _txtsecondFocus;
    static AlertDialogListView _messageDialog;

    public  RVImageListAdapter (List<ListViewItem> items, Context context, Boolean showRating){
        this.listItem = items;
        _showRatingBar = showRating;
        _context = context;
        _txtsecondFocus = null;
    }

    public  RVImageListAdapter (List<ListViewItem> items, Context context, Boolean showRating, TextView txtFocus, Dialog dialog,AlertDialogListView message){
        this.listItem = items;
        _showRatingBar = showRating;
        _context = context;
        _dialog = dialog;
        _txtFocus = txtFocus;
        _messageDialog = message;
         _txtsecondFocus = null;
    }
    public  RVImageListAdapter (List<ListViewItem> items, Context context, Boolean showRating, TextView txtFocus,TextView txtSecondFocus, Dialog dialog,AlertDialogListView message){
        this.listItem = items;
        _showRatingBar = showRating;
        _context = context;
        _dialog = dialog;
        _txtFocus = txtFocus;
        _messageDialog = message;
        _txtsecondFocus = txtSecondFocus;
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
            //En el caso de que sea web, utiliza el ImageLoader para cachearla.
            ImageLoader imageLoader = ImageLoader.getInstance();
            String src;

            if (item.imgSrc.contains("~")) {
                src = item.imgSrc.replace("~/", Common.RootWebSiteUrl);
            } else {
                src = item.imgSrc;
            }

            imageLoader.displayImage(src, listHolder.img);
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

        //Le cambia el tamaño a la imagen, en el caso de que haya sido especificado.
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

        ListHolder(View itemView) {
            super(itemView);

            cv = (CardView)itemView.findViewById(R.id.cv);
            img = (ImageView)itemView.findViewById(R.id.img);
            txt = (TextView) itemView.findViewById(R.id.txt);
            txtSubText = (TextView) itemView.findViewById(R.id.txtSubText);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            txt.setOnClickListener(this);
            txtSubText.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getPosition();
            final ListViewItem itemData=  listItem.get(position);

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInner, int which) {

                    switch (which) {

                        case DialogInterface.BUTTON_POSITIVE:
                            if(_txtFocus != null){
                                _txtFocus.setText(itemData.text);

                                if(_txtsecondFocus != null){
                                    _txtsecondFocus.setText(itemData.subText);
                                }
                                if(_dialog != null){
                                    _dialog.dismiss();
                                }
                            }
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            dialogInner.dismiss();
                            String message = "El usuario canceló operación";
                            SpannableStringBuilder biggerText = new SpannableStringBuilder(message);
                            biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, message.length(), 0);
                            Toast toast = Toast.makeText(_context, biggerText, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(_context);
            builder.setMessage(_messageDialog.getMessage()).setPositiveButton("S\u00ED", dialogClickListener).
                    setNegativeButton("No", dialogClickListener).show();
        }
    }
}


