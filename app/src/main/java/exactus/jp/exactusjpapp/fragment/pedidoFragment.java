package exactus.jp.exactusjpapp.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import exactus.jp.exactusjpapp.R;


public class pedidoFragment extends Fragment {


    private EditText txtCliente,txtNombreCuenta, txtBodega;
    private TextInputLayout inputLayoutCliente,inputLayoutNombreCuenta, inputLayoutBodega;
    private com.melnykov.fab.FloatingActionButton fabGuardarPedido;


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
        //Cliente
        inputLayoutCliente = (TextInputLayout) view.findViewById(R.id.input_layout_cliente);
        txtCliente = (EditText) view.findViewById(R.id.txtCliente);
        txtCliente.addTextChangedListener(new MyTextWatcher(txtCliente));
        //Nombre del Cliente
        inputLayoutNombreCuenta = (TextInputLayout) view.findViewById(R.id.input_layout_nombreCuenta);
        txtNombreCuenta = (EditText) view.findViewById(R.id.txtNombreCuenta);
        txtNombreCuenta.addTextChangedListener(new MyTextWatcher(txtNombreCuenta));

        fabGuardarPedido = (com.melnykov.fab.FloatingActionButton) view.findViewById(R.id.fabGuardarPedido);
        fabGuardarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnviaPedido();
            }
        });
        return  view;

    }

    private void EnviaPedido(){
        if (!validateTextField(txtCliente, inputLayoutCliente, getString(R.string.cliente_error))) {
            return;
        }
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



}
