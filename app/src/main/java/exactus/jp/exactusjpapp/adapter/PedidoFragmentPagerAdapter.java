package exactus.jp.exactusjpapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import exactus.jp.exactusjpapp.fragment.pedidoDetalleFragment;
import exactus.jp.exactusjpapp.fragment.pedidoFragment;
import exactus.jp.exactusjpapp.fragment.ultimoMovimientoFragment;
import exactus.jp.exactusjpapp.fragment.ultimoPedidoFragment;


public class PedidoFragmentPagerAdapter extends FragmentPagerAdapter {


    final int PAGE_COUNT = 2;
    private String tabTitles[] =
            new String[] { "Generales de Pedido","Detalle del Pedido"};


    public PedidoFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;

        switch(position) {
            case 0:
                f = pedidoFragment.newInstance();
            break;
            case 1:
                f = pedidoDetalleFragment.newInstance();
                break;

        }
        return f;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
