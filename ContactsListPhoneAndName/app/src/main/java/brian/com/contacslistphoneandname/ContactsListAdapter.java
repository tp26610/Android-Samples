package brian.com.contacslistphoneandname;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Yeh on 2015/3/20.
 */
public class ContactsListAdapter extends BaseAdapter {

    private static final boolean DEBUG = true;

    private Cursor mCursor;
    private LayoutInflater mInflater;
    private ContentResolver mResolver;
    private int dataCount;

    private void trace(String message) {
        if(DEBUG)
            Log.d("ContactsListAdapter", message);
    }

    public ContactsListAdapter(Context context) {
        trace("ContactListAdapter");

        mResolver = context.getContentResolver();
        mInflater = LayoutInflater.from(context);

        long start = System.currentTimeMillis();
        mCursor = queryContacts();
        long spent = System.currentTimeMillis() - start;
        dataCount = mCursor.getCount();
        Log.i("ContactsListAdapter", "ContactsListAdapter <init> >> query spent " + spent + " ms");
        Log.i("ContactsListAdapter", "dataCount = " + dataCount);

    }

    @Override
    public int getCount() {
        trace("getCount");
        return dataCount;
    }

    @Override
    public Object getItem(int position) {

        trace("getItem");

        mCursor.moveToPosition(position);

        int idIndex = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID);
        int displayNameIndex = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int phoneNumberIndex = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        int photoThumbnailUriIndex = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI);
        int labelIndex = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL);
        int lookupKeyIndex = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY);


        long _id = mCursor.getLong(idIndex);
        String displayName = mCursor.getString(displayNameIndex);
        String phoneNumber = mCursor.getString(phoneNumberIndex);
        String photoThumbnailUri = mCursor.getString(photoThumbnailUriIndex);
        String label = mCursor.getString(labelIndex);
        String lookupKey = mCursor.getString(lookupKeyIndex);

        Item item = new Item();
        item.id = _id;
        item.displayName = displayName;
        item.phoneNumber = phoneNumber;
        item.photoThumbnailUri = photoThumbnailUri;
        item.label = label;
        item.lookupKey = lookupKey;

        trace(item.toString());

        return item;
    }

    @Override
    public long getItemId(int position) {
        trace("getItemId");

        mCursor.moveToPosition(position);
        return mCursor.getLong(0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        trace("getView");
        ViewHolder viewHolder;

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.contacts_list_item, null, false);
            viewHolder = new ViewHolder();
            viewHolder.tvDisplayName = (TextView) convertView.findViewById(R.id.textView_Name);
            viewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.imageView_thumbnail);
            viewHolder.tvPhoneNumber = (TextView) convertView.findViewById(R.id.textView_Phone);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Item item = (Item) getItem(position);

        viewHolder.tvDisplayName.setText(item.getDisplayName());
        viewHolder.tvPhoneNumber.setText(item.getPhoneNumber());

        String uriString = item.getPhotoThumbnailUri();
        if(uriString != null)
            viewHolder.ivPhoto.setImageURI(Uri.parse(uriString));
        else
            viewHolder.ivPhoto.setImageResource(R.drawable.ic_contact_picture_holo_light);

        return convertView;
    }

    public void closeCursor() {
        trace("closeCursor");
        mCursor.close();
    }

    private Cursor queryContacts() {
        trace("queryContacts");
        String[] projection = {
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI,
                ContactsContract.CommonDataKinds.Phone.LABEL,
                ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY
        };

        String selection = ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER + "=1";

        String sortOrder = ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY;

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        return mResolver.query(uri, projection, selection, null, sortOrder);
    }

    private class ViewHolder {
        TextView tvDisplayName;
        TextView tvPhoneNumber;
        ImageView ivPhoto;
    }

    public class Item {

        long id;
        String displayName;
        String phoneNumber;
        String photoThumbnailUri;
        String label;
        String lookupKey;

        public long getId() {
            trace("getId");
            return id;
        }

        public String getDisplayName() {
            trace("getDisplayName");
            return displayName;
        }

        public String getPhotoThumbnailUri() {
            trace("getPhotoThumbnailUri");
            return photoThumbnailUri;
        }

        public String getPhoneNumber() {
            trace("getPhoneNumber");
            return phoneNumber;
        }

        public String getLabel() {
            trace("getLabel");
            return label;
        }

        public String getLookupKey() {
            trace("getLookupKey");
            return lookupKey;
        }

        @Override
        public String toString() {
            trace("toString");
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("------------");
            stringBuffer.append("id = " + id + "\r\n");
            stringBuffer.append("displayName = " + displayName + "\r\n");
            stringBuffer.append("phoneNumber = " + phoneNumber + "\r\n");
            stringBuffer.append("photoThumbnailUri = " + photoThumbnailUri + "\r\n");
            stringBuffer.append("label = " + label + "\r\n");
            stringBuffer.append("lookupKey = " + lookupKey + "\r\n");
            stringBuffer.append("------------");


            return stringBuffer.toString();
        }
    }

}
