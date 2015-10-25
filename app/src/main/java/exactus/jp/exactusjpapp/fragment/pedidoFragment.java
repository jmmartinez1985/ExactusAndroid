package exactus.jp.exactusjpapp.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import exactus.jp.exactusjpapp.R;


public class pedidoFragment extends Fragment {

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
        return  view;

    }



}
