package brian.com.main;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactsListAdapter extends CursorAdapter
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final boolean DEBUG = true;

    private LayoutInflater mInflater;
    private Context mContext;


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
            trace("getLookupKey");
            return lookupKey;
        }

        public long getId() {
            trace("getId");
            return id;
        }

        public String getDisplayNamePrimary() {
            trace("getDisplayNamePrimary");
            return displayNamePrimary;
        }

        public String getPhotoThumbnailUri() {
            trace("getPhotoThumbnailUri");
            return photoThumbnailUri;
        }

        public String getSortKeyPrimary() {
            trace("getSortKeyPrimary");
            return sortKeyPrimary;
        }


    }

    private void trace(String log) {
        if(DEBUG) Log.d("ContactsListAdapter", log);
    }

    private Item cursorToItem(Cursor cursor) {

        trace("cursorToItem");

        long _id = cursor.getLong(0);
        String lookupKey = cursor.getString(1);
        String displayNamePrimary = cursor.getString(2);
        String photoThumbnailUri = cursor.getString(3);
        String sortKeyPrimary = cursor.getString(4);

        Item item = new Item();
        item.id = _id;
        item.lookupKey = lookupKey;
        item.displayNamePrimary = displayNamePrimary;
        item.photoThumbnailUri = photoThumbnailUri;
        item.sortKeyPrimary = sortKeyPrimary;

        return item;
    }

    public ContactsListAdapter(Context context) {
        super(context, null, 0);


        trace("ContactsListAdapter");
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        trace("newView dataCount = " + cursor.getCount() + " position = " + cursor.getPosition());

        View itemView;
        ViewHolder viewHolder;

        itemView = mInflater.inflate(R.layout.contacts_list_item, null, false);
        viewHolder = new ViewHolder();
        viewHolder.tvDisplayName = (TextView) itemView.findViewById(R.id.textView);
        viewHolder.ivPhoto = (ImageView) itemView.findViewById(R.id.imageView_thumbnail);
        itemView.setTag(viewHolder);


        return itemView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        trace("bindView  dataCount = " + cursor.getCount() + " position = " + cursor.getPosition());

        final ViewHolder viewHolder = (ViewHolder) view.getTag();

        Item item = cursorToItem(cursor);

        refreshUI(viewHolder,
                item.getDisplayNamePrimary(),
                item.getPhotoThumbnailUri());
    }

    private void refreshUI(ViewHolder viewHolder, String displayName, String photoThumbnailUri) {
        trace("refreshUI");
        viewHolder.tvDisplayName.setText(displayName);
        if(photoThumbnailUri != null) {
            viewHolder.ivPhoto.setImageURI(Uri.parse(photoThumbnailUri));
        } else {
            viewHolder.ivPhoto.setImageResource(R.drawable.ic_contact_picture_holo_light);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        trace("onCreateLoader");
        String[] projection = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.LOOKUP_KEY,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
                ContactsContract.Contacts.SORT_KEY_PRIMARY
        };

        String selection = ContactsContract.Contacts.DISPLAY_NAME +
                "<>''" + " AND " +
                ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1";

        String sortOrder = ContactsContract.Contacts.SORT_KEY_PRIMARY;


        Uri uri = ContactsContract.Contacts.CONTENT_URI;

        return new CursorLoader(
                mContext,
                uri,
                projection,
                selection,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        trace("onLoadFinished");
        this.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        trace("onLoaderReset");
        this.swapCursor(null);
    }

    @Override
    public Object getItem(int position) {
        trace("getItem");
        Cursor cursor = (Cursor) super.getItem(position);
        Item item = cursorToItem(cursor);
        return item;
    }

}
