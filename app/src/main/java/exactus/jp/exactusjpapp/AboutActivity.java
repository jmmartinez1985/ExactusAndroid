package exactus.jp.exactusjpapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class AboutActivity extends AppCompatActivity {

    //DrawerLayout drawerLayout;
    NavigationView navigationView;
    DrawerLayout Drawer;
    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //setupNavigationView();
        setupToolbar();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


   /* private void setupNavigationView(){

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    }
*/
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
        navigationView.getMenu().getItem(2).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                Drawer.closeDrawers();
                android.os.Handler h = new android.os.Handler();

                switch (menuItem.getItemId()) {

                    case R.id.inicio:
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(AboutActivity.this, LandingActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }, 250);

                        return true;

                    case R.id.pedido:
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent1 = new Intent(AboutActivity.this, MainActivity.class);
                                startActivity(intent1);
                                finish();
                            }
                        }, 250);
                        return true;

                    case R.id.info:

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
