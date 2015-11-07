package exactus.jp.exactusjpapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.CountDownTimer;
import android.support.v4.view.PagerAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import exactus.jp.exactusjpapp.adapter.RVImageListAdapter;
import exactus.jp.exactusjpapp.adapter.RVLineListAdapter;
import exactus.jp.exactusjpapp.adapter.SpinnerListAdapter;
import exactus.jp.exactusjpapp.viewItem.LineViewItem;
import exactus.jp.exactusjpapp.viewItem.ListViewItem;
import exactus.jp.exactusjpapp.model.*;
import exactus.jp.exactusjpapp.services.Connectivity;
import exactus.jp.exactusjpapp.services.Exactus;
import exactus.jp.exactusjpapp.services.ServiceCallBack;
import exactus.jp.exactusjpapp.viewItem.SpinnerItem;

import static exactus.jp.exactusjpapp.R.layout.popup_busqueda_articulos;
import static exactus.jp.exactusjpapp.R.layout.popup_linea;

public class PedidoActivity extends AppCompatActivity {


    /// The pager adapter, which provides the pages to the view pager widget.
    private PagerAdapter mPagerAdapter;

    private static List<LineViewItem> lineList;
    private static int counter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
            setContentView(R.layout.activity_pedido);
            Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(PedidoActivity.this));
            lineList = new ArrayList<>();

            // Inicializa el image loader (debe hacerse antes de usarlo).
            // Esta clase sirve para descargar imagenes por http y cachearlas en disco.
            // https://github.com/nostra13/Android-Universal-Image-Loader
            initImageLoader();
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(this));

            final DeviceAppApplication app = (DeviceAppApplication) getApplication();
            final Devices device = app.getDevice();

            final ArrayList<ListViewItem> elementos = getSideMenuListItems();


            Button imgBuscarBodega = (Button) findViewById(R.id.btnBodega);
            imgBuscarBodega.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!Connectivity.getInstance(getBaseContext()).isOnline()) {
                        String message = getString(R.string.connection_message);
                        SpannableStringBuilder biggerText = new SpannableStringBuilder(message);
                        biggerText.setSpan(new RelativeSizeSpan(1.50f), 0, message.length(), 0);
                        Toast toast = Toast.makeText(getBaseContext(), biggerText, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        return;
                    }

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









            Button imgBuscarcliente = (Button) findViewById(R.id.btnCliente);
            imgBuscarcliente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showClientesDialog();
                }
            });

            Button btnCrearPedido = (Button) findViewById(R.id.btnCrearPedido);
            btnCrearPedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!Connectivity.getInstance(getBaseContext()).isOnline()) {

                        String message = getString(R.string.connection_message);
                        SpannableStringBuilder biggerText = new SpannableStringBuilder(message);
                        biggerText.setSpan(new RelativeSizeSpan(1.50f), 0, message.length(), 0);
                        Toast toast = Toast.makeText(getBaseContext(), biggerText, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        return;
                    }

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInner, int which) {

                            switch (which) {

                                case DialogInterface.BUTTON_POSITIVE:

                                    List<PedidoLineaParametros> lineas = new ArrayList<PedidoLineaParametros>();

                                    int lineaVal = 1;

                                    if(lineList.size() == 0){
                                        String message = "Debe agregar los productos por línea en el pedido.";
                                        SpannableStringBuilder biggerText = new SpannableStringBuilder(message);
                                        biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, message.length(), 0);
                                        Toast toast = Toast.makeText(PedidoActivity.this, biggerText, Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                        return;
                                    }

                                    for(LineViewItem lineaLista: lineList){
                                        PedidoLineaParametros linea = new PedidoLineaParametros();
                                        linea.ARTICULO = lineaLista.articulo;
                                        linea.CANTIDAD = Double.valueOf(lineaLista.cantidad);
                                        linea.CREADOR_POR = app.getUsuario();
                                        linea.DESCUENTO = Double.valueOf(lineaLista.descuento);
                                        linea.Linea = lineaVal;
                                        linea.PRECIO_UNITARIO = Double.valueOf(lineaLista.precio_unitario);
                                        lineaVal++;
                                        lineas.add(linea);
                                    }
                                    PedidoParametros pedido = new PedidoParametros();
                                    TextView bodega = (TextView) findViewById(R.id.txtBodega);
                                    TextView cliente = (TextView) findViewById(R.id.txtCliente);
                                    TextView nombreCuenta = (TextView) findViewById(R.id.txtNombreCuenta);
                                    pedido.BODEGA = bodega.getText().toString();
                                    pedido.CLIENTE = cliente.getText().toString();
                                    pedido.CONDICION_PAGO = 1;
                                    pedido.CODIGO_CONSECUTIVO = "P03";
                                    pedido.NOMBRE_CUENTA = nombreCuenta.getText().toString();
                                    pedido.TARJETA_CREDITO = "10-10-10";
                                    pedido.ORDEN_COMPRA = "10-10-10";
                                    pedido.USUARIO_LOGIN = app.getUsuario();
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



            Button btnAgregarLinea = (Button) findViewById(R.id.btnAgregarLinea);
            btnAgregarLinea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialog = new Dialog(PedidoActivity.this);
                    dialog.setContentView(popup_linea);
                    dialog.setCancelable(true);
                    dialog.setTitle("LINEAS:: Detalle su pedido");
                    dialog.show();






                    Button btnBuscarArticulo = (Button) dialog.findViewById(R.id.btnBuscarArticulo);
                    btnBuscarArticulo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Dialog dialog1 = new Dialog(PedidoActivity.this);
                            dialog1.setContentView(popup_busqueda_articulos);
                            dialog1.setTitle("BUSQUEDA ARTICULOS:: Artículos para pedido");
                            dialog1.show();

//--------------------------------------



//--------------------------------------




                        }
                    });











                    Button btnCrearLinea = (Button) dialog.findViewById(R.id.btnCrearLinea);
                    btnCrearLinea.setOnClickListener(new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View view) {
                                                             LineViewItem itemLine;
                                                             TextView txtArticulo = (TextView) dialog.findViewById(R.id.txtArticuloLinea);
                                                             TextView txtPrecio = (TextView) dialog.findViewById(R.id.txtPrecioLinea);
                                                             TextView txtDescuento = (TextView) dialog.findViewById(R.id.txtDescuentoLinea);
                                                             TextView txtCantidad = (TextView) dialog.findViewById(R.id.txtCantidadLinea);
                                                             itemLine = new LineViewItem(txtArticulo.getText().toString(), txtPrecio.getText().toString(),
                                                                     txtCantidad.getText().toString(), txtDescuento.getText().toString(), counter);
                                                             lineList.add(itemLine);
                                                             counter++;
                                                             fillArrayAndListView(lineList);
                                                             dialog.dismiss();
                                                             //Oculto el teclado
                                                             InputMethodManager inputManager = (InputMethodManager) getSystemService(getBaseContext().INPUT_METHOD_SERVICE);
                                                             inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                                         }
                                                     }

                    );

                }
            });






            // Le cambia la fuente a todos los TextView de la pantalla.
            AssetManager assets = getAssets();
            Common.setFontOnAllControls(assets, (ViewGroup) findViewById(R.id.rootView), "fonts/Lato/Lato-Regular.ttf");


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
        return lst;
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


        RecyclerView recycler = (RecyclerView) dialog.findViewById(R.id.rv);
        final ArrayList<ListViewItem> bodegasData = getBodegas(bodegas);
        if (bodegas.size() > 0) {
            recycler.setVisibility(View.VISIBLE);
        } else
            recycler.setVisibility(View.GONE);
        recycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(PedidoActivity.this);
        recycler.setLayoutManager(layoutManager);
        final EditText txtBodega = (EditText) findViewById(R.id.txtBodega);
        RVImageListAdapter adapter = new RVImageListAdapter(bodegasData,PedidoActivity.this,false);
        recycler.setAdapter(adapter);
        dialog.show();
    }

    private void showClientesDialog() {

        final Dialog dialog = new Dialog(PedidoActivity.this);
        dialog.setContentView(R.layout.popup_clientes);
        dialog.setCancelable(true);
        final DeviceAppApplication app =(DeviceAppApplication) getApplication();
        Button btnBusquedaCliente = (Button) dialog.findViewById(R.id.btnBuscarCliente);
        btnBusquedaCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText NombreClienteBusqueda = (EditText) dialog.findViewById(R.id.txtBusquedaCliente);
                Exactus.ObtenerCliente(
                        PedidoActivity.this,
                        app.getUsuario(), app.getPassword(), NombreClienteBusqueda.getText().toString(),
                        new ServiceCallBack<JSONObject>() {
                            @Override
                            public void onPostExecute(JSONObject obj) {
                                try {
                                    Type clientesType = new TypeToken<ArrayList<Cliente>>() {
                                    }.getType();
                                    ArrayList<Cliente> clientes = new Gson().fromJson(obj.getString("clientes"), clientesType);

                                    RecyclerView recycler = (RecyclerView) dialog.findViewById(R.id.rv);
                                    final ArrayList<ListViewItem> clientesData = getClientes(clientes);
                                    if (clientesData.size() > 0) {
                                        recycler.setVisibility(View.VISIBLE);
                                    } else
                                        recycler.setVisibility(View.GONE);
                                    recycler.setHasFixedSize(true);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(PedidoActivity.this);
                                    recycler.setLayoutManager(layoutManager);
                                    final EditText txtCliente = (EditText) findViewById(R.id.txtCliente);
                                    final EditText txtNombreCuenta = (EditText) findViewById(R.id.txtNombreCuenta);
                                    RVImageListAdapter adapter = new RVImageListAdapter(clientesData,PedidoActivity.this,false);
                                    recycler.setAdapter(adapter);

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


                    dialog.setTitle("CLIENTE:: Seleccione Cliente");
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

    private ArrayList<ListViewItem> getClientes(ArrayList<Cliente> clientes) {
                    ArrayList<ListViewItem> lst = new ArrayList<ListViewItem>();
                    for (Cliente cliente : clientes) {
                        ListViewItem item = new ListViewItem();
                        item.text = cliente.NOMBRE;
                        item.subText = cliente.CLIENTE;
                        lst.add(item);
                    }
                    return lst;
                }

    private ArrayList<SpinnerItem> getClasificacion(ArrayList<Clasificacion> clasificacions) {
        ArrayList<SpinnerItem> lst = new ArrayList<SpinnerItem>();
        for (Clasificacion clasificacion  : clasificacions) {
            SpinnerItem item = new SpinnerItem();
            item.descripcion = clasificacion.DESCRIPCION;
            item.clasificacion = clasificacion.CLASIFICACION;
            lst.add(item);
        }
        return lst;
    }

    private void fillArrayAndListView(List<LineViewItem> item){
        RecyclerView recycler = (RecyclerView) findViewById(R.id.rvLineas);
        if (item.size() > 0) {
            recycler.setVisibility(View.VISIBLE);
        } else
            recycler.setVisibility(View.GONE);
        recycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(PedidoActivity.this);
        recycler.setLayoutManager(layoutManager);
        RVLineListAdapter adapter = new RVLineListAdapter(item,PedidoActivity.this);
        recycler.setAdapter(adapter);



      /*  ListView lv = (ListView) findViewById(R.id.lvLineas);
        LineListViewAdapter adapter = new LineListViewAdapter(PedidoActivity.this,(ArrayList<LineViewItem>) item);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {

                } catch (Exception ex) {
                    Log.d("Error", ex.getLocalizedMessage());
                    Toast.makeText(getBaseContext(), ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
*/
    }



}