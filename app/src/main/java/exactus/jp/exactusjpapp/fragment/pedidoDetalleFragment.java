package exactus.jp.exactusjpapp.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import exactus.jp.exactusjpapp.R;


public class pedidoDetalleFragment extends Fragment {
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_pedido_detalle, container, false);
        return  view;

    }
}
