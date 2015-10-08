package exactus.jp.exactusjpapp.services;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;

import exactus.jp.exactusjpapp.Common;
import exactus.jp.exactusjpapp.model.Devices;
import exactus.jp.exactusjpapp.model.Empresa;
import exactus.jp.exactusjpapp.model.Opciones;

/**
 * Created by Logic on 10/07/2015.
 */
public class Exactus {

    public static void ValidaUsuario(
            Context context,
            String usuario,
            String password,
            final ServiceCallBack<JSONObject> callback) {

        HttpAsyncTask task = new HttpAsyncTask(context, new CallBack() {

            @Override
            public void onPostExecute(String json) {
                try {

                    callback.onPostExecute(new JSONObject(json));
                }
                catch (Exception ex) {
                    callback.onException(ex);
                }
            }

            @Override
            public void onException(Exception ex) {
                callback.onException(ex);
            }
        });

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("usuario", usuario);
        parameters.put("password", password);
        String url = Common.RootServiceUrl + "/api/Exactus/ValidaUsuario";
        task.execute(new HttpAsyncTaskParameters(url, "GET", parameters));
    }
}
