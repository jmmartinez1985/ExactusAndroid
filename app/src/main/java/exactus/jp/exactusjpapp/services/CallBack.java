package exactus.jp.exactusjpapp.services;

/**
 * Created by JM on 30/9/15.
 */
public interface CallBack {
    void onPostExecute(String result);
    void onException(Exception ex);
}