package exactus.jp.exactusjpapp.services;

import java.util.HashMap;

/**
 * Created by JM on 30/9/15.
 */
public class HttpAsyncTaskParameters {
    public String Url;
    public String Method;
    public HashMap<String, String> Params;

    public HttpAsyncTaskParameters(String url, String method) {
        this.Url = url;
        this.Method = method;
        this.Params = new HashMap<>();
    }

    public HttpAsyncTaskParameters(String url, String method, HashMap<String, String> params) {
        this.Url = url;
        this.Method = method;
        this.Params = params;
    }
}
