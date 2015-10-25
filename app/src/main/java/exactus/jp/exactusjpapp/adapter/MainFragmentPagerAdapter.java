package exactus.jp.exactusjpapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import exactus.jp.exactusjpapp.fragment.*;


public class MainFragmentPagerAdapter extends FragmentPagerAdapter {


    final int PAGE_COUNT = 2;
    private String tabTitles[] =
            new String[] { "Ultimos Pedidos","Ultimo Movimientos"};


    public MainFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;

        switch(position) {
            case 0:
                f = ultimoPedidoFragment.newInstance();
            break;
            case 1:
                f =ultimoMovimientoFragment.newInstance();
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
