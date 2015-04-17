package brian.com.contacslistphonefilter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


public class ContactsListFragment extends Fragment {

    private ListView listView = null;
    private ContactsListAdapter adapter = null;
    private EditText editText = null;


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
                        "displayNamePrimary = " + item.getDisplayName() + "\r\n" +
                        "photoThumbnailUri = " + item.getPhotoThumbnailUri();

                Toast.makeText(getActivity(), itemString, Toast.LENGTH_SHORT).show();
            }
        });

        editText = (EditText) getActivity().findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.filter(s.toString());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

//        adapter.closeCursor();

    }
}
