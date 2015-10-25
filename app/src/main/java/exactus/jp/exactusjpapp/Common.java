package exactus.jp.exactusjpapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Asset;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Expone metodos auxiliares.
 */
/**
 * Created by JM on 30/9/15.
 */
public class Common {

    /// Almacena la url base de los servicios, terminando antes de /api/.
    /// Por ejemplo, http://192.168.1.101/LogicStudio.MiCluPyme.Service.PublicApp
    /// Se asigna en el onCreate de la MainActivity.
    public static String RootServiceUrl;
    public static String RootWebSiteUrl;

    /// Obtiene un objeto conteniendo el alto y ancho de la pantalla en pixeles.
    public static DisplayMetrics getScreenSizeInPx(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics;
    }

    /// Convierte dp a px.
    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    /// Convierte px a dp.
    public static int pxToDp(int px, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    /// Ajusta el alto de un listview al total del alto de sus hijos, para lograr que no sea scrolleable.
    /// Debe llamarse despues de setear el adapter al listview.
    public static void adjustListViewHeight(ListView listView) {
        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }

    /// Inicializa el GoogleApiClient de geolocalizacion para una actividad.
    public static synchronized GoogleApiClient buildGoogleApiClient(Activity activity) {
        return new GoogleApiClient.Builder(activity)
            .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks)activity)
            .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener)activity)
            .addApi(LocationServices.API)
            .build();
    }

    public static void setFontOnView(AssetManager assets, TextView textView, String fontPath) {
        Typeface quicksandBold = Typeface.createFromAsset(assets, fontPath);
        textView.setTypeface(quicksandBold);
    }

    public static void setFontOnAllControls(AssetManager assets, ViewGroup group, String fontPath) {
        Typeface font = Typeface.createFromAsset(assets, fontPath);
        setFontOnAllControls(font, group);
    }



    private static void setFontOnAllControls(Typeface font, ViewGroup group) {
        int count = group.getChildCount();
        for (int i = 0; i < count; i++) {
            View v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof Button || v instanceof EditText /*etc.*/)
                ((TextView) v).setTypeface(font);
            else if (v instanceof ViewGroup)
                setFontOnAllControls(font, (ViewGroup) v);
        }
    }

    /// Hace que un dialogo ocupe el 90% de la pantalla del telefono.
    public static View makeDialogLarge(Activity parentActivity, Dialog dialog, int layoutId) {
        Rect displayRectangle = new Rect();
        Window window = parentActivity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        LayoutInflater inflater = (LayoutInflater)parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(layoutId, null);
        layout.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
        layout.setMinimumHeight((int) (displayRectangle.height() * 0.9f));
        dialog.setContentView(layout);
        return layout;
    }

    public static View makeDialogLargeFragment(Activity parentActivity, Dialog dialog, int layoutId) {
        Rect displayRectangle = new Rect();
        Window window = parentActivity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        LayoutInflater inflater = (LayoutInflater)parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(layoutId, null);
        layout.setMinimumWidth((int) (displayRectangle.width() * 0.5f));
        layout.setMinimumHeight((int) (displayRectangle.height() * 0.2f));
        dialog.setContentView(layout);
        return layout;
    }

    public static String parseServiceException(InputStream inputStream) throws Exception {
        BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer response = new StringBuffer("");
        String line = "";
        while ((line = rd.readLine()) != null) {
            response.append(line);
        }

        try {
            JSONObject obj = new JSONObject(response.toString());
            return obj.getString("Message");
        }
        catch(Exception ex) {
            return response.toString();
        }
    }

    /// Convierte una cadena de fecha con el formato yyyy-MM-ddTH:mm:ss a dd/MM/yyyy.
    public static String ChangeDateDotNetFormat(String date) {
        if(date.isEmpty()) {
            return "";
        }
        try {

            Date dt = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse(date.replace("T", " "));
            return new SimpleDateFormat("dd/MM/yyyy").format(dt);
        }
        catch(Exception ex) {
            return date;
        }
    }

    /// Crea una fecha en String a partir de dia, mes, anio.
    public static String padDate(int day, int month, int year) {
        return String.format("%02d", day) + "/" +
                String.format("%02d", month) + "/" +
                String.valueOf(year);
    }

    /// Convierte una imagen a base 64.
    public static String BitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    /// Reduce el tamano de una imagen a un ancho de 500.
    public static Bitmap getImageThumbnail(Bitmap input) {
        int desiredWidth = 500;
        Float width = new Float(input.getWidth());
        Float height = new Float(input.getHeight());
        Float ratio = width/height;
        return Bitmap.createScaledBitmap(input, (int)(desiredWidth * ratio), desiredWidth, false);
    }

    private class CustomOnClickListener implements View.OnClickListener {
        Activity _rootActivity;

        public CustomOnClickListener(Activity act) {
            this._rootActivity = act;
        }

        @Override
        public void onClick(View v) {
            _rootActivity.finish();
        }
    }
}
