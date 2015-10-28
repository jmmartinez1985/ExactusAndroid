package exactus.jp.exactusjpapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.widget.Toast;
import android.os.CountDownTimer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import exactus.jp.exactusjpapp.adapter.PedidoFragmentPagerAdapter;
import exactus.jp.exactusjpapp.model.*;


public class MainActivity extends AppCompatActivity {


    /// The pager adapter, which provides the pages to the view pager widget.
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
            setContentView(R.layout.activity_main);

            // Inicializa el image loader (debe hacerse antes de usarlo).
            // Esta clase sirve para descargar imagenes por http y cachearlas en disco.
            // https://github.com/nostra13/Android-Universal-Image-Loader
            initImageLoader();
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(this));

            setToolbar();

            final DeviceAppApplication app = (DeviceAppApplication) getApplication();
            Devices device = app.getDevice();

            //Tabs + ViewPager

            //Establecer el PageAdapter del componente ViewPager
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            //viewPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager()));
            viewPager.setAdapter(new PedidoFragmentPagerAdapter(getSupportFragmentManager()));

            TabLayout tabLayout = (TabLayout) findViewById(R.id.appbartabs);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            tabLayout.setupWithViewPager(viewPager);

        } catch (Exception ex) {
            Log.d("Error", ex.getLocalizedMessage());
            Toast.makeText(getBaseContext(), ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
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
        CountDownTimer timer =new CountDownTimer(3000, 100)
        {
            public void onTick(long millisUntilFinished)
            {
                toast.show();
            }
            public void onFinish()
            {
                toast.cancel();
            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    private void setToolbar() {
        // Añadir la Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("TRANSCT Mobile");
        setSupportActionBar(toolbar);

    }

}
