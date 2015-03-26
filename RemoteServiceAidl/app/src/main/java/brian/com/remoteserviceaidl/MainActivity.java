package brian.com.remoteserviceaidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {


    public static final String TEXT_SERVICE_CONNECTED = "serviceConnection >> onServiceConnected";
    public static final String TEXT_SERVICE_DISCONNECTED = "serviceConnection >> onServiceDisconnected";

    private TextView tvLog;

    private IMainServiceInterface aidl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tvLog = (TextView) findViewById(R.id.tvLog);
        trace("Start unit test.");

        findViewById(R.id.btnStartService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButtonStartService();
            }
        });
        findViewById(R.id.btnStopService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButtonStopService();
            }
        });
        findViewById(R.id.btnBindService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButtonBindService();
            }
        });
        findViewById(R.id.btnUnbindService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButtonUnbindService();
            }
        });
        findViewById(R.id.btnGetStringFromService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButtonGetStringFromService();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onClickButtonStartService() {
        try {
            startService(new Intent(this, MainService.class));
        } catch(Throwable e) {
            trace("StartService catch exception. message = " + e.getMessage());
            e.printStackTrace();
        }

    }

    private void onClickButtonStopService() {
        try {
            stopService(new Intent(this, MainService.class));
        } catch(Throwable e) {
            trace("StopService catch exception. message = " + e.getMessage());
        }

    }

    private void onClickButtonBindService() {
        try {
            boolean binded = bindService(
                    new Intent(getApplicationContext(), MainService.class),
                    serviceConnection,
                    Context.BIND_AUTO_CREATE
            );
            trace("bindSuccessful = " + binded);
        } catch(Throwable e) {
            trace("Bind Service catch exception. message = " + e.getMessage());
        }

    }

    private void onClickButtonUnbindService() {
        try {
            unbindService(serviceConnection);
        } catch(Throwable e) {
            trace("Bind Service catch exception. message = " + e.getMessage());
        }
    }

    private void onClickButtonGetStringFromService() {
        try {
            String string = aidl.getStringFromRemoteService();
            trace(string);
        } catch(Throwable e) {
            trace("GetStringFromService catch exception. message = " + e.getMessage());
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            trace(TEXT_SERVICE_CONNECTED);
            aidl = IMainServiceInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            trace(TEXT_SERVICE_DISCONNECTED);
        }
    };

    private void trace(String string) {
        Log.i("MainActivity", string);
        tvLog.append(string + "\r\n");
    }
}
