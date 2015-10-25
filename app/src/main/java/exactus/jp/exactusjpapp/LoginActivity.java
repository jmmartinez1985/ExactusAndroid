package exactus.jp.exactusjpapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;

import org.json.JSONObject;

import exactus.jp.exactusjpapp.model.Devices;
import exactus.jp.exactusjpapp.services.Connectivity;
import exactus.jp.exactusjpapp.services.Device;
import exactus.jp.exactusjpapp.services.Exactus;
import exactus.jp.exactusjpapp.services.ServiceCallBack;

/**
 * Created by JM on 30/9/15.
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        //Inicializa la cadena de conexion de los servicios.
        Common.RootServiceUrl = getString(R.string.base_service_url_licence);
        Common.RootWebSiteUrl = getString(R.string.base_website_url);

        setToolbar();

        // Busca el nombre de usuario que se ha logueado con anterioridad a la aplicacion.
        final SharedPreferences prefs = LoginActivity.this.getSharedPreferences("exactus.jp.exactusjpapp.app", Context.MODE_PRIVATE);
        String previousUserName = prefs.getString("userName", "");
        ((EditText)findViewById(R.id.txtUserName)).setText(previousUserName);

        EditText editText= (EditText) findViewById(R.id.txtPassword);
        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    ((Button) findViewById(R.id.btnLogin)).performClick();
                }
                return false;
            }
        });

        ((Button) findViewById(R.id.btnLogin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Obtiene la informaci�n del comercio luego del login.
                    final String usuario = ((EditText) findViewById(R.id.txtUserName)).getText().toString();
                    final String password = ((EditText) findViewById(R.id.txtPassword)).getText().toString();

                    if (usuario.trim().isEmpty()) {
                        Toast.makeText(getBaseContext(), "El correo electr\u00f3nico es requerido.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (password.trim().isEmpty()) {
                        Toast.makeText(getBaseContext(), "La contrase\u00f1a es requerida.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (!Connectivity.getInstance(getBaseContext()).isOnline()) {

                        String message = getString(R.string.connection_message);
                        SpannableStringBuilder biggerText = new SpannableStringBuilder(message);
                        biggerText.setSpan(new RelativeSizeSpan(1.50f), 0, message.length(), 0);
                        Toast toast = Toast.makeText(getBaseContext(), biggerText, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        return;
                    }

                    WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    WifiInfo info = manager.getConnectionInfo();
                    String address = info.getMacAddress();

                    Device.ObtenerDispositivo(
                            LoginActivity.this,
                            address,
                            new ServiceCallBack<Devices>() {
                                @Override
                                public void onPostExecute(Devices device) {
                                    // Almacena el device en una variable global.
                                    if (device == null) {
                                        Toast.makeText(getBaseContext(), "El usuario o la contrase\u00F1a es incorrecta.", Toast.LENGTH_LONG).show();
                                    } else {
                                        DeviceAppApplication app = (DeviceAppApplication) getApplication();
                                        app.setDevice(device);
                                        app.setUsuario(usuario);
                                        app.setPassword(password);

                                        //Inicializa la cadena de conexión de los servicios.
                                        //Common.RootServiceUrl = device.empresaObject.UrlApi;
                                        Common.RootServiceUrl = getString(R.string.base_service_url_core);

                                        // Guarda el nombre del usuario para usarlo como usuario por defecto cuando el usuario vuelva a abrir el app.
                                        SharedPreferences.Editor editor = prefs.edit();
                                        editor.putString("userName", usuario);
                                        editor.commit();

                                       /* Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();*/
                                        ValidaUsuario(usuario,password);

                                    }
                                }

                                @Override
                                public void onException(Exception ex) {
                                    Log.d("Error", ex.getLocalizedMessage());
                                    Toast.makeText(getBaseContext(), ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            });


                } catch (Exception ex) {
                    Toast.makeText(getBaseContext(), ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void ValidaUsuario(String usuario, String password){

        Exactus.ValidaUsuario(
                LoginActivity.this,
                usuario, password,
                new ServiceCallBack<JSONObject>() {
                    @Override
                    public void onPostExecute(JSONObject obj) {
                        try {
                            if (obj.getString("existe").equals("true")) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getBaseContext(), "El usuario o la contrase\u00F1a es incorrecta.", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception ex) {
                            ShowToastError(ex);
                        }
                    }

                    @Override
                    public void onException(Exception ex) {
                        Log.d("Error", ex.getLocalizedMessage());
                        Toast.makeText(getBaseContext(), ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });

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
