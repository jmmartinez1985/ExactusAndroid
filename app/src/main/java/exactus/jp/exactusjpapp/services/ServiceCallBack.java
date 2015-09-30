
package exactus.jp.exactusjpapp.services;
/**
 * Created by JM on 30/9/15.
 */
public interface ServiceCallBack<T> {
    void onPostExecute(T result);
    void onException(Exception ex);
}
