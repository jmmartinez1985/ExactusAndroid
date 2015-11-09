package exactus.jp.exactusjpapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.os.CountDownTimer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import exactus.jp.exactusjpapp.adapter.PedidoFragmentPagerAdapter;
import exactus.jp.exactusjpapp.model.*;


public class MainActivity extends AppCompatActivity {


    //DrawerLayout drawerLayout;
    NavigationView navigationView;
    DrawerLayout Drawer;
    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
            setContentView(R.layout.activity_main);
            //setupNavigationView();
            setupToolbar();


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

    /*private void setupNavigationView(){

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    }*/

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("TRANSCT Mobile");
        setSupportActionBar(toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null)
            setSupportActionBar(toolbar);

        // Show menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_view_list_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.getMenu().getItem(1).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                Drawer.closeDrawers();
                Handler h = new Handler();

                switch (menuItem.getItemId()) {

                    case R.id.inicio:
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MainActivity.this, LandingActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }, 250);

                        return true;

                    case R.id.pedido:

                        return true;

                    case R.id.info:
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent1 = new Intent(MainActivity.this, AboutActivity.class);
                                startActivity(intent1);
                                finish();
                            }
                        }, 250);

                        return true;

                    default:
                        return true;

                }

            }


        });



        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);

        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }


            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }

        };
        Drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }


}
