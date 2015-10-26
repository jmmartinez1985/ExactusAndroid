package exactus.jp.exactusjpapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import exactus.jp.exactusjpapp.R;
import exactus.jp.exactusjpapp.adapter.RVLineListAdapter;
import exactus.jp.exactusjpapp.viewItem.LineViewItem;



public class pedidoDetalleFragment extends Fragment {

    private static List<LineViewItem> lineList;
    private static int counter = 1;



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
    FloatingActionButton fabAgregarLinea;

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
        LineViewItem itemLine;
        itemLine = new LineViewItem(txtArticulo.getText().toString(), txtPrecioLinea.getText().toString(),
                txtCantidad.getText().toString(), txtDescuentoLinea.getText().toString(), counter);
        lineList.add(itemLine);
        counter++;
        llenarObjetoLineas(lineList);
    }

    private void llenarObjetoLineas(List<LineViewItem> item) {

        if (item.size() > 0) {
            rvLineas.setVisibility(View.VISIBLE);
        } else
            rvLineas.setVisibility(View.GONE);
        rvLineas.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        rvLineas.setLayoutManager(layoutManager);
        RVLineListAdapter adapter = new RVLineListAdapter(item, c);
        rvLineas.setAdapter(adapter);
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
