package exactus.jp.exactusjpapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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

import exactus.jp.exactusjpapp.services.Connectivity;
import exactus.jp.exactusjpapp.services.ServiceCallBack;

/**
 * Created by JM on 30/9/15.
 */
public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        //Inicializa la cadena de conexion de los servicios.
        Common.RootServiceUrl = getString(R.string.base_service_url);
        Common.RootWebSiteUrl = getString(R.string.base_website_url);

        // Busca el nombre de usuario que se ha logueado con anterioridad a la aplicacion.
        final SharedPreferences prefs = LoginActivity.this.getSharedPreferences("exactus.jp.exactusjpapp.app", Context.MODE_PRIVATE);
        String previousUserName = prefs.getString("userName", "");
        ((EditText)findViewById(R.id.txtUserName)).setText(previousUserName);

        Common.setFontOnAllControls(getAssets(), (ViewGroup) findViewById(R.id.rootView), "fonts/Lato/Lato-Regular.ttf");

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


                } catch (Exception ex) {
                    Toast.makeText(getBaseContext(), ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
