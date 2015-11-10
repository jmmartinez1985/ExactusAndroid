package exactus.jp.exactusjpapp.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
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
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import exactus.jp.exactusjpapp.DeviceAppApplication;
import exactus.jp.exactusjpapp.R;
import exactus.jp.exactusjpapp.adapter.ItemClickSupport;
import exactus.jp.exactusjpapp.adapter.RVImageListAdapter;
import exactus.jp.exactusjpapp.adapter.RVLineListAdapter;
import exactus.jp.exactusjpapp.adapter.WrappingLinearLayoutManager;
import exactus.jp.exactusjpapp.model.Articulo;
import exactus.jp.exactusjpapp.model.Cliente;
import exactus.jp.exactusjpapp.services.Exactus;
import exactus.jp.exactusjpapp.services.ServiceCallBack;
import exactus.jp.exactusjpapp.viewItem.LineViewItem;
import exactus.jp.exactusjpapp.viewItem.ListViewItem;


public class pedidoDetalleFragment extends Fragment {

    private static List<LineViewItem> lineList;
    private static int counter = 1;
    private CoordinatorLayout coordinator;
    FragmentActivity fragment= null;



    @Bind(R.id.txtArticuloLinea)
    EditText txtArticulo;
    @Bind(R.id.input_layout_articulo)
    TextInputLayout inputLayoutArticulo;

    @Bind(R.id.txtArticuloDescripcion)
    EditText txtArticuloDescripcion;
    @Bind(R.id.input_layout_descripcion)
    TextInputLayout inputLayoutArticuloDescripcion;

    @Bind(R.id.txtPrecioLinea)
    EditText txtPrecioLinea;
    @Bind(R.id.input_layout_precio)
    TextInputLayout inputLayoutPrecio;

    @Bind(R.id.txtCantidadLinea)
    EditText txtCantidad;
    @Bind(R.id.input_layout_cantidad)
    TextInputLayout inputLayoutCantidad;

    @Bind(R.id.txtDescuentoLinea)
    EditText txtDescuentoLinea;
    @Bind(R.id.input_layout_descuento)
    TextInputLayout inputLayoutDescuento;

    static FragmentActivity c = null;

    @Bind(R.id.rvLineas)
    RecyclerView rvLineas;

    @Bind(R.id.fabAddLinea)
    Button fabAgregarLinea;

    @Bind(R.id.fabArticulo)
    Button fabArticulo;

    @Bind(R.id.fabPrecio)
    Button fabPrecioArticulo;


    public static pedidoDetalleFragment newInstance() {
        pedidoDetalleFragment fragment = new pedidoDetalleFragment();
        return fragment;
    }

    public pedidoDetalleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lineList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_pedido_detalle, container, false);
        ButterKnife.bind(this, view);
        c = getActivity();
        fragment = getActivity();
        txtArticulo.addTextChangedListener(new MyTextWatcher(txtArticulo));
        txtArticuloDescripcion.addTextChangedListener(new MyTextWatcher(txtArticuloDescripcion));
        txtCantidad.addTextChangedListener(new MyTextWatcher(txtCantidad));
        txtPrecioLinea.addTextChangedListener(new MyTextWatcher(txtPrecioLinea));
        txtDescuentoLinea.addTextChangedListener(new MyTextWatcher(txtDescuentoLinea));
        return  view;
   }

    @OnClick(R.id.fabAddLinea)
    void agregarLinea(){
        if (!validateTextField(txtArticulo, inputLayoutArticulo, getString(R.string.articulo))) {
            return;
        }
        if (!validateTextField(txtArticuloDescripcion, inputLayoutArticuloDescripcion, getString(R.string.detalle_articulo))) {
            return;
        }
        if (!validateTextField(txtPrecioLinea, inputLayoutPrecio, getString(R.string.articulo_precio))) {
            return;
        }
        if (!validateTextField(txtCantidad, inputLayoutCantidad, getString(R.string.articulo_cantidad))) {
            return;
        }
        if (!validateTextField(txtDescuentoLinea, inputLayoutDescuento, getString(R.string.articulo_descuento))) {
            return;
        }
        LineViewItem itemLine;
        itemLine = new LineViewItem(txtArticulo.getText().toString(), txtPrecioLinea.getText().toString(),
                txtCantidad.getText().toString(), txtDescuentoLinea.getText().toString(),txtArticuloDescripcion.getText().toString(), counter);
        lineList.add(itemLine);
        counter++;
        llenarObjetoLineas(lineList);
        txtArticulo.setText("");
        txtArticuloDescripcion.setText("");
        txtCantidad.setText("");
        txtPrecioLinea.setText("");
        txtDescuentoLinea.setText("");

        inputLayoutArticulo.setErrorEnabled(false);
        inputLayoutArticuloDescripcion.setErrorEnabled(false);
        inputLayoutCantidad.setErrorEnabled(false);
        inputLayoutPrecio.setErrorEnabled(false);
        inputLayoutDescuento.setErrorEnabled(false);
        Snackbar.make(rvLineas,"Linea de artículo agregada satisfactoriamente.",Snackbar.LENGTH_LONG).show();
        txtArticulo.requestFocus();
    }




    @OnClick(R.id.fabArticulo)
     void desplegarBusquedaArticulo()
    {

        final Dialog dialog = new Dialog(fragment);
        dialog.setContentView(R.layout.popup_busqueda_articulos);
        dialog.setCancelable(true);

        Button btnBuscarArticulospopup = (Button) dialog.findViewById(R.id.btnBuscarArticulospopup);
        btnBuscarArticulospopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarArticulo(dialog);
            }
        });

        dialog.show();


    }

    void buscarArticulo(final Dialog dialog){

        final DeviceAppApplication app =(DeviceAppApplication) getActivity().getApplicationContext();
        EditText ArticuloBusqueda = (EditText) dialog.findViewById(R.id.txtingresebusqueda);
        RadioButton radioCodigo = (RadioButton) dialog.findViewById(R.id.radioCodigo);
        if (radioCodigo.isChecked() == true)
        {
            Exactus.ObtenerArticulos(fragment,
                    app.getUsuario(),
                    app.getPassword(),
                    "B-01",
                    ArticuloBusqueda.getText().toString(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "00008022",
                    new ServiceCallBack<JSONObject>() {
                        @Override
                        public void onPostExecute(JSONObject obj) {
                            try {
                                Type articuloType = new TypeToken<ArrayList<Articulo>>() {
                                }.getType();
                                final ArrayList<Articulo> articulos = new Gson().fromJson(obj.getString("articulos"), articuloType);

                                RecyclerView recycler = (RecyclerView) dialog.findViewById(R.id.rvarticulos);
                                final ArrayList<ListViewItem> clientesData = getClientes(articulos);
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
                                        Articulo c = articulos.get(position);
                                        txtArticulo.setText(c.ARTICULO);
                                        txtArticuloDescripcion.setText(c.DESCRIPCION);
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
        else
        {
            Exactus.ObtenerArticulos(fragment,
                    app.getUsuario(),
                    app.getPassword(),
                    "B-01",
                    "",
                    ArticuloBusqueda.getText().toString(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "00008022",
                    new ServiceCallBack<JSONObject>() {
                        @Override
                        public void onPostExecute(JSONObject obj) {
                            try {
                                Type clientesType = new TypeToken<ArrayList<Articulo>>() {
                                }.getType();
                                final ArrayList<Articulo> articulos = new Gson().fromJson(obj.getString("articulos"), clientesType);

                                RecyclerView recycler = (RecyclerView) dialog.findViewById(R.id.rvarticulos);
                                final ArrayList<ListViewItem> clientesData = getClientes(articulos);
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
                                        Articulo c = articulos.get(position);
                                        txtArticulo.setText(c.ARTICULO);
                                        txtArticuloDescripcion.setText(c.DESCRIPCION);
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

    }


    private ArrayList<ListViewItem> getClientes(ArrayList<Articulo> articulos) {
        ArrayList<ListViewItem> lst = new ArrayList<ListViewItem>();
        for (Articulo articulo : articulos) {
            ListViewItem item = new ListViewItem();
            item.text = articulo.ARTICULO;
            item.subText = articulo.DESCRIPCION;
            item.subTextInner = "PRECIO: " + articulo.PRECIO + " DISPONIBLE: " + articulo.DISPONIBLE;
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





    @OnClick(R.id.fabPrecio)
    void buscarPrecio()
    {

    }

    private void llenarObjetoLineas(List<LineViewItem> item) {

        if (item.size() > 0) {
            rvLineas.setVisibility(View.VISIBLE);
        } else
            rvLineas.setVisibility(View.GONE);
        rvLineas.setNestedScrollingEnabled(false);
        rvLineas.setHasFixedSize(false);
        rvLineas.setLayoutManager(new WrappingLinearLayoutManager(getContext()));
        //LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        //rvLineas.setLayoutManager(layoutManager);
        RVLineListAdapter adapter = new RVLineListAdapter(item, c);
        rvLineas.setAdapter(adapter);
        rvLineas.setItemAnimator(new DefaultItemAnimator());
        final DeviceAppApplication app =(DeviceAppApplication) getActivity().getApplicationContext();
        app.setLineasShared(lineList);
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
                case R.id.txtArticuloLinea:
                    validateTextField(txtArticulo,inputLayoutArticulo,getString(R.string.articulo));
                    break;
                case R.id.txtArticuloDescripcion:
                    validateTextField(txtArticuloDescripcion,inputLayoutArticuloDescripcion,getString(R.string.detalle_articulo));
                    break;
                case R.id.txtPrecioLinea:
                    validateTextField(txtPrecioLinea,inputLayoutPrecio,getString(R.string.articulo_precio));
                    break;
                case R.id.txtCantidadLinea:
                    validateTextField(txtCantidad,inputLayoutCantidad,getString(R.string.articulo_cantidad));
                    break;
                case R.id.txtDescuentoLinea:
                    validateTextField(txtDescuentoLinea,inputLayoutDescuento,getString(R.string.articulo_descuento));
                    break;
            }
        }
    }
}
