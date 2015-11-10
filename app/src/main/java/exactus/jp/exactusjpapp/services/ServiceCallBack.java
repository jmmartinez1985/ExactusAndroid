
package exactus.jp.exactusjpapp.services;

import org.json.JSONException;

/**
 * Created by JM on 30/9/15.
 */
public interface ServiceCallBack<T> {
    void onPostExecute(T result) throws JSONException;
    void onException(Exception ex);
}
