package brian.com.contacslistphonefilter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ContactsListAdapter extends BaseAdapter {

    private static final boolean DEBUG = false;

    private LayoutInflater mInflater;
    private ContentResolver mResolver;
    private List<Item> mapArray = new ArrayList<>();

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

    private void trace(String message) {
        if(DEBUG)
            Log.d("ContactsListAdapter", message);
    }

    public ContactsListAdapter(Context context) {
        trace("ContactListAdapter");

        mResolver = context.getContentResolver();
        mInflater = LayoutInflater.from(context);

        new PutMapTask().execute();
    }

    private List<Item> cursorToMapList(Cursor mCursor) {
        List<Item> mapArray = new ArrayList<>();
        String preLookupKey = "";
        boolean hasData = mCursor.moveToFirst();

        if(hasData == false) {
            return mapArray;
        }

        do {
            Item item = toItem(mCursor);
            if(preLookupKey.equals(item.getLookupKey())){
                continue;
            }
            preLookupKey = item.getLookupKey();
            mapArray.add(item);
        } while(mCursor.moveToNext());
        return mapArray;
    }

    @Override
    public int getCount() {
        trace("getCount");
        return mapArray.size();
    }

    @Override
    public Object getItem(int position) {
        trace("getItem");

        return mapArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        trace("getItemId");
        return mapArray.get(position).getId();
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

//    public void closeCursor() {
//        trace("closeCursor");
//        mCursor.close();
//    }

    private Cursor queryContacts(String filterPhoneOrName) {
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
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY;
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        if(filterPhoneOrName != null && filterPhoneOrName.length() > 0) {
            selection += " AND ( "
                    + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE ? "
                    + " OR " + ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE ? "
                    + " ) ";
            selectionArgs = new String[]{"%" + filterPhoneOrName + "%", "%" + filterPhoneOrName + "%"};
            trace("selection = " + selection);
        }


        return mResolver.query(uri, projection, selection, selectionArgs, sortOrder);
    }

    private class ViewHolder {
        TextView tvDisplayName;
        TextView tvPhoneNumber;
        ImageView ivPhoto;
    }

    private Item toItem(Cursor cursor) {
        trace("toItem");

        int idIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID);
        int displayNameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int phoneNumberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        int photoThumbnailUriIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI);
        int labelIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL);
        int lookupKeyIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY);

        long _id = cursor.getLong(idIndex);
        String displayName = cursor.getString(displayNameIndex);
        String phoneNumber = cursor.getString(phoneNumberIndex);
        String photoThumbnailUri = cursor.getString(photoThumbnailUriIndex);
        String label = cursor.getString(labelIndex);
        String lookupKey = cursor.getString(lookupKeyIndex);

        Item item = new Item();
        item.id = _id;
        item.displayName = displayName;
        item.phoneNumber = phoneNumber;
        item.photoThumbnailUri = photoThumbnailUri;
        item.label = label;
        item.lookupKey = lookupKey;

        return item;
    }

    public void filter(String filterPhoneOrName) {
        trace("filterPhoneOrName");
        new PutMapTask().execute(filterPhoneOrName);
    }

    private class PutMapTask extends AsyncTask<String, Item, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mapArray.clear();
            notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(String... params) {

            String filter = null;
            if(params != null && params.length > 0) {
                filter = params[0];
            }
            Cursor cursor = queryContacts(filter);


            boolean hasData = cursor.moveToFirst();

            if(hasData == false) {
                return null;
            }

            String preLookupKey = "";
            do {
                Item item = toItem(cursor);
                if(preLookupKey.equals(item.getLookupKey())){
                    continue;
                }
                preLookupKey = item.getLookupKey();
                publishProgress(item);
            } while(cursor.moveToNext());



            return null;
        }

        @Override
        protected void onProgressUpdate(Item... values) {
            if(values != null) {
                for(Item item : values) {
                    mapArray.add(item);
                }
            }
            notifyDataSetChanged();
        }
    }
}
