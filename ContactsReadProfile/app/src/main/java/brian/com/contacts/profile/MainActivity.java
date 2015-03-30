package brian.com.contacts.profile;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        textView.setText("Start Logging.\r\n");


        String[] mProjection = new String[] {
          ContactsContract.Profile._ID,
          ContactsContract.Profile.DISPLAY_NAME,
          ContactsContract.Profile.LOOKUP_KEY,
          ContactsContract.Profile.PHOTO_THUMBNAIL_URI
        };

        Cursor mProfileCursor = getContentResolver().query(
                ContactsContract.Profile.CONTENT_URI,
                mProjection,
                null,
                null,
                null
        );


        boolean isNotEmpty = mProfileCursor.moveToFirst();

        if(!isNotEmpty) {
            finish();
            Toast.makeText(this, "No data. finish this activity.", Toast.LENGTH_SHORT).show();
        }

        do {
            long id = mProfileCursor.getLong(0);
            String displayName = mProfileCursor.getString(1);
            String lookupKey = mProfileCursor.getString(2);
            String photo = mProfileCursor.getString(3);


            printARow(id, displayName, lookupKey, photo);

        } while(mProfileCursor.moveToNext());

        mProfileCursor.close();
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

    private void trace(String log) {
        Log.i("MainActivity", log);
        textView.append(log + "\r\n");
    }

    private void printARow(long id, String displayName, String lookupKey, String photoUri) {
        String row = "id=" + id + "\r\n displayName=" + displayName + "\r\n lookupKey=" + lookupKey +
                "\r\n photoUri=" + photoUri;
        trace(row);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
