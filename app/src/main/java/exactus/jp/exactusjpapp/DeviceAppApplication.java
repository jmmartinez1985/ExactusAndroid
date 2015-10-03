package exactus.jp.exactusjpapp;

import android.app.Application;

import exactus.jp.exactusjpapp.model.Devices;

/**
 * Created by JM on 10/03/2015.
 */
public class DeviceAppApplication extends Application {

    private Devices _device;

    public Devices getDevice() {
        return _device;
    }

    public void setDevice(Devices _device) {
        this._device = _device;
    }
}
