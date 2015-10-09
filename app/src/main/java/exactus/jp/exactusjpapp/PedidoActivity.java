package exactus.jp.exactusjpapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import exactus.jp.exactusjpapp.model.*;
import exactus.jp.exactusjpapp.services.Exactus;
import exactus.jp.exactusjpapp.services.ServiceCallBack;

public class PedidoActivity extends ActionBarActivity {

    //Instancia del menu que hace slide a la izquierda.
    private SlidingMenu _menu;

    /// The pager adapter, which provides the pages to the view pager widget.
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
            setContentView(R.layout.activity_pedido);


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

            ImageListViewAdapter adapter = new ImageListViewAdapter(PedidoActivity.this, elementos);
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
                                //_menu.toggle();
                                //intent = new Intent(PedidoActivity.this, PedidoActivity.class);
                                //startActivity(intent);
                                break;

                            case "Cotizaciones": //Mis Cotizaciones
                                // _menu.toggle();
                                //intent = new Intent(MainActivity.this, PedidoActivity.class);
                                // startActivity(intent);
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



            Button imgBuscarBodega = (Button) findViewById(R.id.btnBodega);
            imgBuscarBodega.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Exactus.ObtenerBodega(
                            PedidoActivity.this,
                            app.getUsuario(), app.getPassword(),
                            new ServiceCallBack<JSONObject>() {
                                @Override
                                public void onPostExecute(JSONObject obj) {
                                    try {
                                        showBodegasDialog(obj);
                                    } catch (Exception ex) {
                                        ShowToastError(ex);
                                    }
                                }

                                @Override
                                public void onException(Exception ex) {
                                    Log.d("Error", ex.getLocalizedMessage());
                                    Toast.makeText(getBaseContext(), ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                }
            });
            Button btnCrearPedido = (Button) findViewById(R.id.btnCrearPedido);
            btnCrearPedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInner, int which) {

                            switch (which) {

                                case DialogInterface.BUTTON_POSITIVE:


                                    List<PedidoLineaParametros> lineas = new ArrayList<PedidoLineaParametros>();
                                    PedidoLineaParametros linea = new PedidoLineaParametros();
                                    linea.ARTICULO = "0101";
                                    linea.CANTIDAD = 10;
                                    linea.CREADOR_POR = "jm";
                                    linea.DESCUENTO = 0;
                                    linea.Linea = 1;
                                    linea.PRECIO_UNITARIO = 10.50;


                                    lineas.add(linea);
                                    linea.ARTICULO = "0102";
                                    linea.PRECIO_UNITARIO = 20.50;
                                    lineas.add(linea);

                                    PedidoParametros pedido = new PedidoParametros();
                                    pedido.BODEGA = "10";
                                    pedido.CLIENTE = "01";
                                    pedido.CONDICION_PAGO = 1;
                                    pedido.CODIGO_CONSECUTIVO = "01";
                                    pedido.NOMBRE_CUENTA = "PRUEBA";
                                    pedido.TARJETA_CREDITO = "";
                                    pedido.ORDEN_COMPRA = "";
                                    pedido.USUARIO_LOGIN = "JM";
                                    pedido.PEDIDODETALLE = lineas;
                                    String data = "";
                                    Gson gson = new Gson();
                                    data = gson.toJson(pedido);
                                    Exactus.GuardarPedido(
                                            PedidoActivity.this,
                                            app.getUsuario(),
                                            app.getPassword(),
                                            data,
                                            new ServiceCallBack<JSONObject>() {
                                                @Override
                                                public void onPostExecute(JSONObject data) {
                                                    String message = "Pedido creado satisfactoriamente";
                                                    SpannableStringBuilder biggerText = new SpannableStringBuilder(message);
                                                    biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, message.length(), 0);
                                                    Toast toast = Toast.makeText(PedidoActivity.this, biggerText, Toast.LENGTH_LONG);
                                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                                    toast.show();
                                                }

                                                @Override
                                                public void onException(Exception ex) {
                                                    Log.d("Error", ex.getLocalizedMessage());
                                                    ShowToastError(ex);
                                                }
                                            });


                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialogInner.dismiss();
                                    String message = "El usuario canceló operación";
                                    SpannableStringBuilder biggerText = new SpannableStringBuilder(message);
                                    biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, message.length(), 0);
                                    Toast toast = Toast.makeText(PedidoActivity.this, biggerText, Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(PedidoActivity.this);
                    builder.setMessage("Esta seguro de que quiere crear este pedido?").setPositiveButton("S\u00ED", dialogClickListener).
                            setNegativeButton("No", dialogClickListener).show();

                }
            });

            // Le cambia la fuente a todos los TextView de la pantalla.
            AssetManager assets = getAssets();
            Common.setFontOnAllControls(assets, (ViewGroup) findViewById(R.id.rootView), "fonts/Lato/Lato-Regular.ttf");
            Common.setFontOnAllControls(assets, (ViewGroup) _menu.findViewById(R.id.rootView), "fonts/Lato/Lato-Regular.ttf");

        } catch (Exception ex) {
            Log.d("Error", ex.getLocalizedMessage());
            ShowToastError(ex);
        }
    }

    /// Obtiene la lista de items del menu contextual de la aplicación.
    private ArrayList<ListViewItem> getSideMenuListItems() {
        ArrayList<ListViewItem> lst = new ArrayList<ListViewItem>();

        final DeviceAppApplication app = (DeviceAppApplication) getApplication();
        List<Opciones> opciones = app.getDevice().opcionesList;
        if (opciones.size() > 0) {
            for (Opciones opci : opciones) {
                lst.add(new ListViewItem(R.drawable.ic_folder_shared_blue_48dp, opci.Opcion, "", 0, opci.Id));
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
        CountDownTimer timer = new CountDownTimer(3000, 100) {
            public void onTick(long millisUntilFinished) {
                toast.show();
            }

            public void onFinish() {
                toast.cancel();
            }
        }.start();
    }

    private void showBodegasDialog(JSONObject obj) throws JSONException {


        final Dialog dialog = new Dialog(PedidoActivity.this);
        dialog.setContentView(R.layout.popup_bodegas);

        dialog.setCancelable(true);
        dialog.setTitle("BODEGA:: Escoga la bodega para el pedido");

        Type bodegasType = new TypeToken<ArrayList<Bodega>>() {
        }.getType();
        ArrayList<Bodega> bodegas = new Gson().fromJson(obj.getString("bodegas"), bodegasType);
        ListView lv = (ListView) dialog.findViewById(R.id.lvBodegas);
        final ArrayList<ListViewItem> bodegasData = getBodegas(bodegas);
        if (bodegas.size() > 0) {
            lv.setVisibility(View.VISIBLE);
        } else
            lv.setVisibility(View.GONE);

        ImageListViewAdapter adapter = new ImageListViewAdapter(PedidoActivity.this, bodegasData, false);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInner, int which) {

                        switch (which) {

                            case DialogInterface.BUTTON_POSITIVE:
                                //Find control
                                //final EditText txtCardNumber = (EditText) findViewById(R.id."CONTROL");
                                //final EditText txtCedula = (EditText) findViewById(R.id.txtCedula);
                                ListViewItem item = bodegasData.get(position);
                                //Set Item to Control
                                dialog.dismiss();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialogInner.dismiss();
                                String message = "El usuario canceló operación";
                                SpannableStringBuilder biggerText = new SpannableStringBuilder(message);
                                biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, message.length(), 0);
                                Toast toast = Toast.makeText(PedidoActivity.this, biggerText, Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(PedidoActivity.this);
                builder.setMessage("Desea utilizar esta bodega?").setPositiveButton("S\u00ED", dialogClickListener).
                        setNegativeButton("No", dialogClickListener).show();


            }
        });
        dialog.show();
    }

    private ArrayList<ListViewItem> getBodegas(ArrayList<Bodega> bodegas) {
        ArrayList<ListViewItem> lst = new ArrayList<ListViewItem>();
        for (Bodega bodega : bodegas) {
            ListViewItem item = new ListViewItem();
            item.text = bodega.Bodega;
            item.subText = bodega.Nombre;
            lst.add(item);
        }
        return lst;
    }
}