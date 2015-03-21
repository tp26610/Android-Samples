package brian.com.main;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


public class ContactsListFragment extends Fragment {

    private ListView listView = null;
    private ContactsListAdapter adapter = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contacts_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView = (ListView) getActivity().findViewById(R.id.listView);

        adapter = new ContactsListAdapter(getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ContactsListAdapter.Item item =
                        (ContactsListAdapter.Item) adapter.getItem(position);

                String itemString = "id = " + item.getId() + "\r\n" +
                        "displayNamePrimary = " + item.getDisplayNamePrimary() + "\r\n" +
                        "photoThumbnailUri = " + item.getPhotoThumbnailUri();

                Toast.makeText(getActivity(), itemString, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        adapter.closeCursor();

    }
}
