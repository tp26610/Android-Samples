package com.brian.install.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class MainActivity extends Activity {

    private static final String APP_FILE_NAME = "out_app.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity", "onCreate");

        copyAppFileToInternalStorage(APP_FILE_NAME);
        installApp(APP_FILE_NAME);
    }

    private void installApp(String appFileName) {
        Log.d("MainActivity", "installApp");
        File appFile = getFileStreamPath(appFileName);
//        File appFile = new File("/sdcard/" + appFileName);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(appFile), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void copyAppFileToInternalStorage(String fileName) {
        Log.d("MainActivity", "copyAppFileToInternalStorage");
        // copy app to internal storage that only can be used by this app.
        InputStream in = null;
        OutputStream out = null;
        try {
            out = openFileOutput(fileName, Context.MODE_WORLD_READABLE);
//            out = new FileOutputStream("/sdcard/" + fileName);
            in = getResources().openRawResource(R.raw.app);
            int size = in.available();
            Log.d("MainActivity", "size = " + size);
            byte[] buffer = new byte[size];

            int readLen = 0;
            while((readLen = in.read(buffer)) > 0) {
                out.write(buffer, 0, readLen);
            }
        } catch(Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(in);
            closeIO(out);
        }
    }

    private void closeIO(Closeable c) {
        Log.d("MainActivity", "closeIO");
        if(c != null) {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
