package brian.com.main;

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

    private Cursor mCursor;
    private LayoutInflater mInflater;
    private ContentResolver mResolver;


    public ContactsListAdapter(Context context) {
        mResolver = context.getContentResolver();
        mInflater = LayoutInflater.from(context);
        mCursor = queryContacts();
    }

    @Override
    public int getCount() {
        int count = mCursor.getCount();
        Log.d("ContactsListAdapter", "getCount() = " + count);
        return count;
    }

    @Override
    public Object getItem(int position) {

        mCursor.moveToPosition(position);

        long _id = mCursor.getLong(0);
        String lookupKey = mCursor.getString(1);
        String displayNamePrimary = mCursor.getString(2);
        String photoThumbnailUri = mCursor.getString(3);
        String sortKeyPrimary = mCursor.getString(4);

        Item item = new Item();
        item.id = _id;
        item.lookupKey = lookupKey;
        item.displayNamePrimary = displayNamePrimary;
        item.photoThumbnailUri = photoThumbnailUri;
        item.sortKeyPrimary = sortKeyPrimary;

        return item;
    }

    @Override
    public long getItemId(int position) {
        mCursor.moveToPosition(position);
        return mCursor.getLong(0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.contacts_list_item, null, false);
            viewHolder = new ViewHolder();
            viewHolder.tvDisplayName = (TextView) convertView.findViewById(R.id.textView);
            viewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.imageView_thumbnail);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Item item = (Item) getItem(position);

        viewHolder.tvDisplayName.setText(item.getDisplayNamePrimary());
        String uriString = item.getPhotoThumbnailUri();

        if(uriString != null)
            viewHolder.ivPhoto.setImageURI(Uri.parse(uriString));
        else
            viewHolder.ivPhoto.setImageResource(R.drawable.ic_contact_picture_holo_light);

        return convertView;
    }

    public void closeCursor() {
        mCursor.close();
    }

    private Cursor queryContacts() {

        String[] projection = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.LOOKUP_KEY,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
                ContactsContract.Contacts.SORT_KEY_PRIMARY
        };

        String selection = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY +
                "<>''" + " AND " + ContactsContract.Contacts.IN_VISIBLE_GROUP + "=1 AND " +
                ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1";

        String sortOrder = ContactsContract.Contacts.SORT_KEY_PRIMARY;


        Uri uri = ContactsContract.Contacts.CONTENT_URI;

        return mResolver.query(uri, projection, selection, null, sortOrder);
    }

    private class ViewHolder {
        TextView tvDisplayName;
        ImageView ivPhoto;
    }

    public class Item {

        long id;
        String lookupKey;
        String displayNamePrimary;
        String photoThumbnailUri;
        String sortKeyPrimary;

        public String getLookupKey() {
            return lookupKey;
        }

        public long getId() {
            return id;
        }

        public String getDisplayNamePrimary() {
            return displayNamePrimary;
        }

        public String getPhotoThumbnailUri() {
            return photoThumbnailUri;
        }

        public String getSortKeyPrimary() {
            return sortKeyPrimary;
        }


    }

}
