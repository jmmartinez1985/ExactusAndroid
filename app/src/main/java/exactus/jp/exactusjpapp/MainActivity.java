package exactus.jp.exactusjpapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.CountDownTimer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import exactus.jp.exactusjpapp.model.*;
import exactus.jp.exactusjpapp.services.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;




public class MainActivity extends ActionBarActivity {

    //Instancia del menu que hace slide a la izquierda.
    private SlidingMenu _menu;

    /// The pager adapter, which provides the pages to the view pager widget.
    private PagerAdapter mPagerAdapter;

    static boolean canjearInternal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
            setContentView(R.layout.activity_main);



            // Inicializa el image loader (debe hacerse antes de usarlo).
            // Esta clase sirve para descargar imagenes por http y cachearlas en disco.
            // https://github.com/nostra13/Android-Universal-Image-Loader
            initImageLoader();
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(this));

            // Configuracion del Action bar
            LayoutInflater mInflater = LayoutInflater.from(this);
            View mCustomView = mInflater.inflate(R.layout.custom_action_toolbar, null);
            ImageButton imageButton = (ImageButton) mCustomView.findViewById(R.id.imageButton);
            ActionBar mActionBar = getSupportActionBar();

            if (mActionBar != null) {
                mActionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_background)));
                mActionBar.setDisplayShowHomeEnabled(false);
                mActionBar.setDisplayShowTitleEnabled(false);

                ActionBar.LayoutParams l = new ActionBar.LayoutParams(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
                mActionBar.setCustomView(mCustomView, l);
                mActionBar.setDisplayShowCustomEnabled(true);
            }

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _menu.toggle();
                }
            });

            //Configuracion del menu lateral.
            setMainMenu();

            //Setea la fuente de todos los controles del menu.
            Common.setFontOnAllControls(getAssets(), (ViewGroup) _menu.findViewById(R.id.rootView), "fonts/Lato/Lato-Regular.ttf");
            // Almacena el comercio en una variable global.
            final DeviceAppApplication app = (DeviceAppApplication) getApplication();
            Devices device = app.getDevice();

            final ArrayList<ListViewItem> elementos = getSideMenuListItems();

            ImageListViewAdapter adapter = new ImageListViewAdapter(MainActivity.this, elementos);
            ListView list = (ListView) _menu.findViewById(R.id.lstMenu);
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {


                        Intent intent;


                        final ListViewItem item = elementos.get(position);
                        switch (item.text) {

                            case "Pedidos": //Mis pedidos
                                _menu.toggle();
                                intent = new Intent(MainActivity.this, PedidoActivity.class);
                                startActivity(intent);
                                break;

                            case "Cotizaciones": //Mis Cotizaciones
                                _menu.toggle();
                                intent = new Intent(MainActivity.this, PedidoActivity.class);
                                startActivity(intent);
                                break;

                            default: //Estadisticas de uso
                                break;
                        }
                    } catch (Exception ex) {
                        Log.d("Error", ex.getLocalizedMessage());
                        Toast.makeText(getBaseContext(), ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });



            ImageView imgBanner = (ImageView) findViewById(R.id.imgBanner);
            ImageView imgLogo = (ImageView) findViewById(R.id.imgLogo);
            ImageView imgLogoMenu = (ImageView) _menu.findViewById(R.id.imgLogo);

            TextView txtComercio =(TextView) findViewById(R.id.txtNombreComercio);
            TextView txtEmailComercio =(TextView) findViewById(R.id.txtEmailComercio);

            txtComercio.setText(device.empresaObject.NombreEmpresa);
            txtEmailComercio.setText(app.getUsuario());

            // Le cambia la fuente a todos los TextView de la pantalla.
            AssetManager assets = getAssets();
            Common.setFontOnAllControls(assets, (ViewGroup) findViewById(R.id.rootView), "fonts/Lato/Lato-Regular.ttf");
            Common.setFontOnAllControls(assets, (ViewGroup) _menu.findViewById(R.id.rootView), "fonts/Lato/Lato-Regular.ttf");

        } catch (Exception ex) {
            Log.d("Error", ex.getLocalizedMessage());
            Toast.makeText(getBaseContext(), ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }



    /// Obtiene la lista de items del menu contextual de la aplicación.
    private ArrayList<ListViewItem> getSideMenuListItems() {
        ArrayList<ListViewItem> lst = new ArrayList<ListViewItem>();

        final DeviceAppApplication app = (DeviceAppApplication) getApplication();
        List<Opciones> opciones = app.getDevice().opcionesList;
        if(opciones.size() > 0){
            for(Opciones opci : opciones){
                lst.add(new ListViewItem(R.drawable.ic_folder_shared_blue_48dp, opci.Opcion, "",0, opci.Id));
            }
        }

        //lst.add(new ListViewItem(R.drawable.ic_stars_blue_48, "Notificaciones", ""));

        //lst.add(new ListViewItem(R.drawable.ic_trending_up_black_48dp, "Estadisticas de Uso", ""));

        return lst;
    }

    /// Configura el menu principal de la aplicación.
    private void setMainMenu() {
        DisplayMetrics metrics = Common.getScreenSizeInPx(getApplicationContext());
        _menu = new SlidingMenu(this);
        _menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        _menu.setSlidingEnabled(false);
        _menu.setShadowWidth(50);
        _menu.setShadowDrawable(R.drawable.sliding_menu_shadow);
        _menu.setFadeDegree(0.0f);
        _menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        //Indica que el menu lateral debe expandirse hasta el 80% de la actividad.
        _menu.setBehindWidth(((Double) (metrics.widthPixels * 0.8)).intValue());
        _menu.setMenu(R.layout.side_menu);

        Account[] accounts = AccountManager.get(getBaseContext()).getAccountsByType("com.google");
        for (Account account : accounts) {
            int a = 0;
        }
    }

    /// Muestra la pantalla usada para activar una tarjeta.

    /// Inicializa el componente ImageLoader.
    /// https://github.com/nostra13/Android-Universal-Image-Loader/wiki/Useful-Info
    private void initImageLoader() {

        // Indica que se quiere cachear las imagenes en memoria y en disco.
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();

        ImageLoader.getInstance().init(config);
    }

    //Muestra errores centrados en el toast
    private void ShowToastError(Exception ex) {
        String message = ex.getLocalizedMessage();
        SpannableStringBuilder biggerText = new SpannableStringBuilder(message);
        biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, message.length(), 0);
        final Toast toast = Toast.makeText(getBaseContext(), biggerText, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        CountDownTimer timer =new CountDownTimer(3000, 100)
        {
            public void onTick(long millisUntilFinished)
            {
                toast.show();
            }
            public void onFinish()
            {
                toast.cancel();
            }
        }.start();
    }

}
