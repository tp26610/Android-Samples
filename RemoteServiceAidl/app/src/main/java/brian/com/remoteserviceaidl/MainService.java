package brian.com.remoteserviceaidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class MainService extends Service {
    public MainService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new IMainServiceInterface.Stub() {
            @Override
            public String getStringFromRemoteService() throws RemoteException {
                return "Hello world From AIDL.";
            }
        };
    }

    @Override
    public void onCreate() {
        super.onCreate();

        trace("onCreate");

        
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        trace("onDestroy");
    }

    private void trace(String string) {
        Log.i("MainService", string);
    }
}
