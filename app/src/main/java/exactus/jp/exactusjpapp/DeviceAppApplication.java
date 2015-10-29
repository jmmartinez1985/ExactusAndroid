package exactus.jp.exactusjpapp;

import android.app.Application;

import java.util.List;

import exactus.jp.exactusjpapp.model.Devices;
import exactus.jp.exactusjpapp.viewItem.LineViewItem;

/**
 * Created by JM on 10/03/2015.
 */
public class DeviceAppApplication extends Application {

    private Devices _device;
    private String usuario;
    private String password;

    private List<LineViewItem> lineasShared;


    public Devices getDevice() {
        return _device;
    }

    public void setDevice(Devices _device) {
        this._device = _device;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public List<LineViewItem> getLineasShared() {
        return lineasShared;
    }

    public void setLineasShared(List<LineViewItem> lineasShared) {
        this.lineasShared = lineasShared;
    }
}
