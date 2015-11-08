package exactus.jp.exactusjpapp.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import exactus.jp.exactusjpapp.DeviceAppApplication;
import exactus.jp.exactusjpapp.R;
import exactus.jp.exactusjpapp.adapter.ItemClickSupport;
import exactus.jp.exactusjpapp.adapter.RVImageListAdapter;
import exactus.jp.exactusjpapp.model.Articulo;
import exactus.jp.exactusjpapp.model.TopPedido;
import exactus.jp.exactusjpapp.services.Exactus;
import exactus.jp.exactusjpapp.services.ServiceCallBack;
import exactus.jp.exactusjpapp.viewItem.LineViewItem;
import exactus.jp.exactusjpapp.viewItem.ListViewItem;


public class ultimoPedidoFragment extends Fragment {
    private static List<LineViewItem> lineList;
    private static int counter = 1;
    private CoordinatorLayout coordinator;
    FragmentActivity fragment= null;


    void buscarArticulo()
    {

        final Dialog dialog = new Dialog(fragment);
        dialog.setContentView(R.layout.fragment_ultimo_pedido);
        dialog.setCancelable(true);
        fragment = getActivity();

        final DeviceAppApplication app =(DeviceAppApplication) getActivity().getApplicationContext();
        {
            Exactus.ObtenerUltimosPedidos(
                    fragment,
                    app.getUsuario(),
                    app.getPassword(),
                    new ServiceCallBack<JSONObject>() {
                        @Override
                        public void onPostExecute(JSONObject obj) {
                            try
                            {Type articuloType = new TypeToken<ArrayList<TopPedido>>() {}.getType();
                                final ArrayList<TopPedido> articulos = new Gson().fromJson(obj.getString("TopPedido"), articuloType);
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

    private ArrayList<ListViewItem> getClientes(ArrayList<TopPedido> articulos) {
        ArrayList<ListViewItem> lst = new ArrayList<ListViewItem>();
        for (TopPedido articulo : articulos) {
            ListViewItem item = new ListViewItem();
            item.text = articulo.PEDIDO;
            item.subText = articulo.FECHA;
            item.subTextInner = "CLIENTE: " + articulo.CLIENTE + " MONTO: " + articulo.MONTO;
            lst.add(item);
        }
        return lst;
    }





    public static ultimoPedidoFragment newInstance() {
        ultimoPedidoFragment fragment = new ultimoPedidoFragment();
        return fragment;
    }

    public ultimoPedidoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_ultimo_pedido, container, false);
        return  view;

    }

}






