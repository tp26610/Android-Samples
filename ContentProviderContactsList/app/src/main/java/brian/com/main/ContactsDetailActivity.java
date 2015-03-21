package brian.com.main;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

/**
 * Created by Yeh on 2015/3/21.
 */
public class ContactsDetailActivity extends ActionBarActivity {

    public static final String EXTRA_CONTACT_ID = "contactId";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        long contactId = getIntent().getLongExtra(EXTRA_CONTACT_ID, -1);

        Cursor c = queryContactsDetail(contactId);
        String contactDetailString = getContactsDetailString(c);
        c.close();

        openContactsDetailFragment(contactDetailString);
    }

    private void openContactsDetailFragment(String contactDetailString) {
        Fragment fragment = ContactsDetailFragment.newInstance(contactDetailString);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private String getContactsDetailString(Cursor c) {
        String contactDetailString = null;
        StringBuffer contactStringBuffer = new StringBuffer();
        c.moveToFirst();
        do {
            int idIndex = c.getColumnIndex(ContactsContract.Data._ID);
            int phoneNumberIndex = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int phoneTypeIndex = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
            int phoneLabelIndex = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL);

            long id = c.getLong(idIndex);
            String phoneNumber = c.getString(phoneNumberIndex);
            int phoneType = c.getInt(phoneTypeIndex);
            String phoneLabel = c.getString(phoneLabelIndex);


            contactStringBuffer.append("id = " + id + "\r\n" +
                    "phoneNumber = " + phoneNumber + "\r\n" +
                    "phoneType = " + phoneType + "\r\n" +
                    "phoneLabel = " + phoneLabel + "\r\n"
            );
            contactStringBuffer.append("\r\n");


        } while(c.moveToNext());
        contactDetailString = contactStringBuffer.toString();
        return contactDetailString;
    }

    private Cursor queryContactsDetail(long contactId) {

        String[] projection = new String[] {
                ContactsContract.Data._ID,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.TYPE,
                ContactsContract.CommonDataKinds.Phone.LABEL
        };

        String selection = ContactsContract.Data.CONTACT_ID + "=?" + " AND "
                + ContactsContract.Data.MIMETYPE + "='"
                    + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' AND "
                + ContactsContract.CommonDataKinds.Phone.NUMBER + " <> '' ";

        String[] selectionArgs = new String[] { String.valueOf(contactId) };


        Cursor c = getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);

        return c;
    }


}
