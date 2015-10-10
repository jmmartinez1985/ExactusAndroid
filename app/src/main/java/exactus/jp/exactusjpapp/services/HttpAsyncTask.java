package exactus.jp.exactusjpapp.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import exactus.jp.exactusjpapp.Common;
import exactus.jp.exactusjpapp.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by JM on 30/9/15.
 */
public class HttpAsyncTask extends AsyncTask<HttpAsyncTaskParameters, Void, String> {

    private CallBack _callback;
    private Context _context;
    private boolean _exceptionThrown;
    private ProgressDialog _progress;

    public HttpAsyncTask(Context context, CallBack callback) {
        this._callback = callback;
        this._context = context;
    }

    @Override
    protected void onPreExecute() {
        _progress = ProgressDialog.show(_context, _context.getString(R.string.progress_bar_title), _context.getString(R.string.progress_bar_message), true);
    }

    @Override
    protected String doInBackground(HttpAsyncTaskParameters... params) {
        try {
            return request(params[0]);
        } catch (Exception ex) {
            _exceptionThrown = true;
            return ex.getLocalizedMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {

        if(_progress != null){
            _progress.dismiss();
        }

        if(_exceptionThrown) {
            _callback.onException(new Exception(result));
        }
        else {
            _callback.onPostExecute(result);
        }
    }

    private String request(HttpAsyncTaskParameters httpParam) throws Exception {

        StringBuffer response = new StringBuffer("");
        String urlValue;

        if(httpParam.Method.toUpperCase().equals("GET") && httpParam.Params.size() > 0) {
            urlValue = httpParam.Url + "?" + getQuery(httpParam.Params);
        }
        else {
            urlValue = httpParam.Url;
        }

        URL url = new URL(urlValue);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", "");
        connection.setRequestMethod(httpParam.Method);
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);

        if (httpParam.Method.toUpperCase().equals("GET")) {
            connection.setDoInput(true);
        }
        else {
            connection.setDoInput(true);
            connection.setDoOutput(true);
        }

        if (httpParam.Method.toUpperCase().equals("POST")) {
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(httpParam.Params));
            writer.flush();
            writer.close();
            os.close();
        }

        connection.connect();

        try {
            InputStream inputStream = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }

            return response.toString();
        }
        catch (java.net.SocketTimeoutException e){
            _progress.dismiss();
            String msg = "La comunicaci\u00f3n con el servidor esta demorando mucho, por favor verifique su conexi\u00f3n a internet.";
            throw new Exception(msg);
        }
        catch(Exception ex) {
            InputStream inputStream = connection.getErrorStream();
            String msg = Common.parseServiceException(inputStream);
            throw new Exception(msg);
        }
    }

    private String getQuery(HashMap<String, String> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> pair : params.entrySet())
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue().replace('+', '_').replace('/', '-'), "UTF-8"));
        }

        return result.toString();
    }
}
