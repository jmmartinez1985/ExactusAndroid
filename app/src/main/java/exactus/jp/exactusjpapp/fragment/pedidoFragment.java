package exactus.jp.exactusjpapp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import exactus.jp.exactusjpapp.DeviceAppApplication;
import exactus.jp.exactusjpapp.R;
import exactus.jp.exactusjpapp.adapter.ItemClickSupport;
import exactus.jp.exactusjpapp.adapter.RVImageListAdapter;
import exactus.jp.exactusjpapp.model.Bodega;
import exactus.jp.exactusjpapp.model.Cliente;
import exactus.jp.exactusjpapp.model.PedidoLineaParametros;
import exactus.jp.exactusjpapp.model.PedidoParametros;
import exactus.jp.exactusjpapp.services.Connectivity;
import exactus.jp.exactusjpapp.services.Exactus;
import exactus.jp.exactusjpapp.services.ServiceCallBack;
import exactus.jp.exactusjpapp.viewItem.LineViewItem;
import exactus.jp.exactusjpapp.viewItem.ListViewItem;


public class pedidoFragment extends Fragment {


    EditText txtCliente = null,txtNombreCuenta= null, txtBodega = null, txtObservacion=null, txtOdc=null;
    private CoordinatorLayout coordinator;

    private TextInputLayout inputLayoutCliente,inputLayoutNombreCuenta, inputLayoutBodega;
    private com.melnykov.fab.FloatingActionButton fabGuardarPedido, fabBuscarCliente, fabBuscarBodega;
    FragmentActivity fragment= null;

    private static int counter = 1;

    public static pedidoFragment newInstance() {
        pedidoFragment fragment = new pedidoFragment();
        return fragment;
    }

    public pedidoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_pedido, container, false);
        fragment = getActivity();
        coordinator = (CoordinatorLayout)view.findViewById(R.id.coordinator);

        //Cliente
        inputLayoutCliente = (TextInputLayout) view.findViewById(R.id.input_layout_cliente);
        txtCliente = (EditText) view.findViewById(R.id.txtCliente);
        txtCliente.addTextChangedListener(new MyTextWatcher(txtCliente));
        //Nombre del Cliente
        inputLayoutNombreCuenta = (TextInputLayout) view.findViewById(R.id.input_layout_nombreCuenta);
        txtNombreCuenta = (EditText) view.findViewById(R.id.txtNombreCuenta);
        txtNombreCuenta.addTextChangedListener(new MyTextWatcher(txtNombreCuenta));

        //Bodega
        txtBodega =(EditText) view.findViewById(R.id.txtBodega);
        inputLayoutBodega = (TextInputLayout) view.findViewById(R.id.input_layout_bodega);
        txtBodega.addTextChangedListener(new MyTextWatcher(txtBodega));

        //Observacion
        txtObservacion =(EditText) view.findViewById(R.id.txtObservacion);

        //ODC
        txtOdc =(EditText) view.findViewById(R.id.txtOrdenDeCompras);

        fabGuardarPedido = (com.melnykov.fab.FloatingActionButton) view.findViewById(R.id.fabGuardarPedido);
        fabGuardarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnviaPedido();
            }
        });

        fabBuscarCliente = (com.melnykov.fab.FloatingActionButton) view.findViewById(R.id.fabCliente);
        fabBuscarCliente.setType(com.melnykov.fab.FloatingActionButton.TYPE_MINI);
        fabBuscarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showClientesDialog();
            }
        });

        fabBuscarBodega = (com.melnykov.fab.FloatingActionButton) view.findViewById(R.id.fabBodega);
        fabBuscarBodega.setType(com.melnykov.fab.FloatingActionButton.TYPE_MINI);
        fabBuscarBodega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarBodega();
            }
        });

        return  view;

    }

    private void EnviaPedido(){

        final DeviceAppApplication app =(DeviceAppApplication) getActivity().getApplicationContext();
        if (!validateTextField(txtCliente, inputLayoutCliente, getString(R.string.cliente_error))) {
            return;
        }
        if (!validateTextField(txtBodega, inputLayoutBodega, getString(R.string.bodega_error))) {
            return;
        }

        final List<LineViewItem> lineList = app.getLineasShared();
        if(lineList==null){
            String message = "Debe agregar los productos por línea en el pedido.";
            SpannableStringBuilder biggerText = new SpannableStringBuilder(message);
            biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, message.length(), 0);
            Toast toast = Toast.makeText(fragment, biggerText, Toast.LENGTH_LONG);
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
                            Toast toast = Toast.makeText(fragment, biggerText, Toast.LENGTH_LONG);
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
                        pedido.BODEGA = txtBodega.getText().toString();
                        pedido.CLIENTE = txtCliente.getText().toString();
                        pedido.CONDICION_PAGO = 1;
                        pedido.CODIGO_CONSECUTIVO = "P03";
                        pedido.NOMBRE_CUENTA = txtNombreCuenta.getText().toString();
                        pedido.TARJETA_CREDITO = "10-10-10";
                        pedido.ORDEN_COMPRA = "10-10-10";
                        pedido.USUARIO_LOGIN = app.getUsuario();
                        pedido.PEDIDODETALLE = lineas;
                        String data = "";
                        Gson gson = new Gson();
                        data = gson.toJson(pedido);
                        Exactus.GuardarPedido(
                                fragment,
                                app.getUsuario(),
                                app.getPassword(),
                                data,
                                new ServiceCallBack<JSONObject>() {
                                    @Override
                                    public void onPostExecute(JSONObject data) {
                                        String message = "Pedido creado satisfactoriamente";
                                        SpannableStringBuilder biggerText = new SpannableStringBuilder(message);
                                        biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, message.length(), 0);
                                        Toast toast = Toast.makeText(fragment, biggerText, Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                        app.setLineasShared(null);
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
                        Toast toast = Toast.makeText(fragment, biggerText, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment);
        builder.setMessage("Esta seguro de que quiere crear este pedido?").setPositiveButton("S\u00ED", dialogClickListener).
                setNegativeButton("No", dialogClickListener).show();
        //Copiar logica del pedido activity, una vez realizada, se debe eliminar activity.

    }


    private boolean validateTextField(EditText input, TextInputLayout layout, String errorMessage ) {
        if (input.getText().toString().trim().isEmpty()) {
            layout.setError(errorMessage);
            requestFocus(input);
            return false;
        } else {
            layout.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }



    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.txtCliente:
                    validateTextField(txtCliente,inputLayoutCliente,getString(R.string.cliente_error));
                    break;
                case R.id.txtBodega:
                    validateTextField(txtCliente,inputLayoutCliente,getString(R.string.cliente_error));
                    break;
            }
        }
    }

    private void buscarBodega(){

        final DeviceAppApplication app =(DeviceAppApplication) getActivity().getApplicationContext();
        if (!Connectivity.getInstance(getActivity()).isOnline()) {
            String message = getString(R.string.connection_message);
            SpannableStringBuilder biggerText = new SpannableStringBuilder(message);
            biggerText.setSpan(new RelativeSizeSpan(1.50f), 0, message.length(), 0);
            Toast toast = Toast.makeText(getActivity(), biggerText, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }

        Exactus.ObtenerBodega(
                getActivity(),
                app.getUsuario(), app.getPassword(),
                new ServiceCallBack<JSONObject>() {
                    @Override
                    public void onPostExecute(JSONObject obj) {
                        try {
                            showBodegasDialog(obj);
                        } catch (Exception ex) {
                            Snackbar.make(coordinator,ex.getLocalizedMessage(),Snackbar.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onException(Exception ex) {
                        Log.d("Error", ex.getLocalizedMessage());
                        Snackbar.make(coordinator, ex.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    private void showBodegasDialog(JSONObject obj) throws JSONException {

        final Dialog dialog = new Dialog(fragment);
        dialog.setContentView(R.layout.popup_bodegas);
        dialog.setCancelable(true);
        dialog.setTitle("BODEGA:: Escoga la bodega para el pedido");
        Type bodegasType = new TypeToken<ArrayList<Bodega>>() {
        }.getType();
        final ArrayList<Bodega> bodegas = new Gson().fromJson(obj.getString("bodegas"), bodegasType);
        RecyclerView recycler = (RecyclerView) dialog.findViewById(R.id.rv);
        final ArrayList<ListViewItem> bodegasData = getBodegas(bodegas);
        if (bodegas.size() > 0) {
            recycler.setVisibility(View.VISIBLE);
        } else
            recycler.setVisibility(View.GONE);
        recycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(fragment);
        recycler.setLayoutManager(layoutManager);
        RVImageListAdapter adapter = new RVImageListAdapter(bodegasData,fragment,false);
        recycler.setAdapter(adapter);
        ItemClickSupport.addTo(recycler).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Bodega  b = bodegas.get(position);
                txtBodega.setText(b.Bodega);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showClientesDialog() {

        final Dialog dialog = new Dialog(fragment);
        dialog.setContentView(R.layout.popup_clientes);
        dialog.setCancelable(true);
        final DeviceAppApplication app =(DeviceAppApplication) getActivity().getApplicationContext();
        Button btnBusquedaCliente = (Button) dialog.findViewById(R.id.btnBuscarCliente);
        btnBusquedaCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText NombreClienteBusqueda = (EditText) dialog.findViewById(R.id.txtBusquedaCliente);
                Exactus.ObtenerCliente(fragment,
                        app.getUsuario(), app.getPassword(), NombreClienteBusqueda.getText().toString(),
                        new ServiceCallBack<JSONObject>() {
                            @Override
                            public void onPostExecute(JSONObject obj) {
                                try {
                                    Type clientesType = new TypeToken<ArrayList<Cliente>>() {
                                    }.getType();
                                    final ArrayList<Cliente> clientes = new Gson().fromJson(obj.getString("clientes"), clientesType);

                                    RecyclerView recycler = (RecyclerView) dialog.findViewById(R.id.rv);
                                    final ArrayList<ListViewItem> clientesData = getClientes(clientes);
                                    if (clientesData.size() > 0) {
                                        recycler.setVisibility(View.VISIBLE);
                                    } else
                                        recycler.setVisibility(View.GONE);
                                    recycler.setHasFixedSize(true);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(fragment);
                                    recycler.setLayoutManager(layoutManager);
                                    RVImageListAdapter adapter = new RVImageListAdapter(clientesData, fragment, false);
                                    recycler.setAdapter(adapter);
                                    ItemClickSupport.addTo(recycler).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                                        @Override
                                        public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                            Cliente c = clientes.get(position);
                                            txtCliente.setText(c.CLIENTE);
                                            txtNombreCuenta.setText(c.NOMBRE);
                                            dialog.dismiss();
                                        }
                                    });

                                } catch (Exception ex) {
                                    Snackbar.make(coordinator, ex.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onException(Exception ex) {
                                Log.d("Error", ex.getLocalizedMessage());
                                Toast.makeText(getActivity(), ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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

    private void ShowToastError(Exception ex) {
        String message = ex.getLocalizedMessage();
        SpannableStringBuilder biggerText = new SpannableStringBuilder(message);
        biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, message.length(), 0);
        final Toast toast = Toast.makeText(fragment, biggerText, Toast.LENGTH_LONG);
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


}
