package brian.com.contacslistphoneandname;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;


public class ContactsListActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ContactsListFragment())
                .commit();

    }

}
