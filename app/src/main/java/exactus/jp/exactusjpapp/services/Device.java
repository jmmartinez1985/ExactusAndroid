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
 * Created by JM on 10/03/2015.
 */
public class Device {

    public static void ObtenerDispositivo(
            Context context,
            String mac,
            final ServiceCallBack<Devices> callback) {

        HttpAsyncTask task = new HttpAsyncTask(context, new CallBack() {

            @Override
            public void onPostExecute(String json) {
                try {
                    JSONObject root = new JSONObject(json);
                    JSONObject comercioJson = root.getJSONObject("dispositivo");
                    JSONObject empresaJson = root.getJSONObject("empresa");
                    JSONArray opcionesListJson = root.getJSONArray("opciones");

                    Type typeDevice = new TypeToken<Devices>() {
                    }.getType();
                    Type typeEmpresa = new TypeToken<Empresa>() {
                    }.getType();
                    Type typeOpcion = new TypeToken<Opciones>() {
                    }.getType();

                    Devices deviceEntity = new Gson().fromJson(comercioJson.toString(), typeDevice);

                    Empresa empresaEntity = new Gson().fromJson(empresaJson.toString(), typeEmpresa);
                    deviceEntity.empresaObject = empresaEntity;

                    for (int i = 0; i < opcionesListJson.length(); i++) {
                        JSONObject item = opcionesListJson.getJSONObject(i);
                        Opciones opcion = new Gson().fromJson(item.toString(), typeOpcion);
                        deviceEntity.opcionesList.add(opcion);
                    }

                    callback.onPostExecute(deviceEntity);
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
        parameters.put("mac", mac);
        String url = Common.RootServiceUrl + "/api/Dispositivos/ObtenerDispositivo";
        task.execute(new HttpAsyncTaskParameters(url, "GET", parameters));
    }
}
