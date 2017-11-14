package caventa.ansheer.ndk.caventa.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import caventa.ansheer.ndk.caventa.R;
import ndk.prism.common_utils.Toast_Utils;


public class Commision_Page extends AppCompatActivity {


    private Bundle extras;
    private ArrayList<String> spinner_list;
    Spinner spinner_Scheme;
    private Context application_context;

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.commisions_page);


        application_context = getApplicationContext();

        final boolean[] mSpinnerInitialized = new boolean[1];

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        spinner_Scheme = (Spinner) findViewById(R.id.spinner_nav);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        extras = getIntent().getExtras();

        spinner_Scheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int item_position, long id) {
                //TODO : refresh on same scheme selection
                if (!mSpinnerInitialized[0]) {
                    mSpinnerInitialized[0] = true;
                    return;
                }

                if (isOnline()) {
                    Intent i = new Intent(application_context, Commision_Page.class);
                    i.putExtra("sales_person", spinner_Scheme.getSelectedItem().toString());
                    i.putExtra("position", spinner_Scheme.getSelectedItemPosition());
                    startActivity(i);
                    finish();
                } else {
                    Toast_Utils.longToast(getApplicationContext(), "Internet is unavailable");
                }


            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO : Your code here
            }
        });

        spinner_list = new ArrayList<String>();

        for (int i = 1; i < Accounts.sales_persons.size(); i++) {
            spinner_list.add(Accounts.sales_persons.get(i).getName());
        }
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(application_context, R.layout.spinner_item_actionbar, spinner_list);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Scheme.setAdapter(spinner_adapter);

        spinner_Scheme.setSelection(extras.getInt("position"));

    }

}

