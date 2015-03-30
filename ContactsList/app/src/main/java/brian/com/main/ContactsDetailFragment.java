package brian.com.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Yeh on 2015/3/21.
 */
public class ContactsDetailFragment extends Fragment {


    public static final String EXTRA_DETAIL_STRING = "DetailString";

    public static Fragment newInstance(String detailString) {
        Fragment fragment = new ContactsDetailFragment();

        Bundle args = new Bundle();
        args.putString(EXTRA_DETAIL_STRING, detailString);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.contacts_detail_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView textView = (TextView) getActivity().findViewById(R.id.textView);

        Bundle args = getArguments();

        String detail = args.getString(EXTRA_DETAIL_STRING);

        textView.setText(detail);

    }
}
