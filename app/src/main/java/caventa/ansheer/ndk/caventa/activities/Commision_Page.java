package caventa.ansheer.ndk.caventa.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.kimkevin.cachepot.CachePot;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import caventa.ansheer.ndk.caventa.R;
import caventa.ansheer.ndk.caventa.adapters.Commision_Adapter;
import caventa.ansheer.ndk.caventa.constants.General_Data;
import caventa.ansheer.ndk.caventa.models.Sales_Person;
import caventa.ansheer.ndk.caventa.models.Commision;
import ndk.utils.Date_Utils;
import ndk.utils.Toast_Utils;


//TODO:Action bar redesign
//TODO:Action bar combo box redesign
//TODO:Payout disable redesign

public class Commision_Page extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private View mProgressView;
    private View mLoginFormView;

    private Bundle extras;
    private ArrayList<String> spinner_list;
    Spinner spinner_Scheme;
    private Context application_context;
    private Sales_Person selected_sales_person;
    private Button button_payout;

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.commisions_page);
        selected_sales_person = CachePot.getInstance().pop(Sales_Person.class);

        application_context = getApplicationContext();

        final boolean[] mSpinnerInitialized = new boolean[1];

        Toolbar mToolbar = findViewById(R.id.toolbar);
        spinner_Scheme = findViewById(R.id.spinner_nav);

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
                    load_commision_page();
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

        commisions = new ArrayList<>();

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mRecyclerView = findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // create an Object for Adapter
        mAdapter = new Commision_Adapter(commisions);

        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);


        if (load_sales_person_commision_task != null) {
            finish();
        }
        showProgress(true);
        load_sales_person_commision_task = new Load_Sales_Person_Commision_Task();
        load_sales_person_commision_task.execute((Void) null);

        initView();

        button_payout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_uncancelled_yes_no_confirmation_dialogue_for_payout();
            }
        });
    }

    void show_uncancelled_yes_no_confirmation_dialogue_for_payout() {
        AlertDialog.Builder delete_confirmation_dialog = new AlertDialog.Builder(this);
        delete_confirmation_dialog.setMessage("Want to payout? ");
        delete_confirmation_dialog.setCancelable(false);
        delete_confirmation_dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                execute_payout_task();
            }
        });
        delete_confirmation_dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = delete_confirmation_dialog.create();
        alert.setTitle("Warning!");
        alert.show();
    }

    private void execute_payout_task() {
        if (payout_task != null) {
            payout_task.cancel(true);
            payout_task = null;
        }
        showProgress(true);
        payout_task = new Payout_Task();
        payout_task.execute((Void) null);
    }

    private void load_commision_page() {
        Intent i = new Intent(application_context, Commision_Page.class);
        CachePot.getInstance().push(Accounts.sales_persons.get(spinner_Scheme.getSelectedItemPosition() + 1));
        i.putExtra("position", spinner_Scheme.getSelectedItemPosition());
        startActivity(i);
        finish();
    }

    private Load_Sales_Person_Commision_Task load_sales_person_commision_task = null;
    private List<Commision> commisions;

    private void initView() {
        button_payout = (Button) findViewById(R.id.btnShow);
    }

    public class Load_Sales_Person_Commision_Task extends AsyncTask<Void, Void, String[]> {

        Load_Sales_Person_Commision_Task() {
        }

        DefaultHttpClient http_client;
        HttpPost http_post;
        String network_action_response;
        ArrayList<NameValuePair> name_pair_value;

        @Override
        protected String[] doInBackground(Void... params) {
            try {
                http_client = new DefaultHttpClient();
                http_post = new HttpPost(General_Data.SERVER_IP_ADDRESS + "/android/get_commision.php");

                name_pair_value = new ArrayList<NameValuePair>(1);
                name_pair_value.add(new BasicNameValuePair("sales_person_id", String.valueOf(selected_sales_person.getId())));

                http_post.setEntity(new UrlEncodedFormEntity(name_pair_value));

                ResponseHandler<String> response_handler = new BasicResponseHandler();
                network_action_response = http_client.execute(http_post, response_handler);
                return new String[]{"0", network_action_response};

            } catch (UnsupportedEncodingException e) {
                return new String[]{"1", "UnsupportedEncodingException : " + e.getLocalizedMessage()};
            } catch (ClientProtocolException e) {
                return new String[]{"1", "ClientProtocolException : " + e.getLocalizedMessage()};
            } catch (IOException e) {
                return new String[]{"1", "IOException : " + e.getLocalizedMessage()};
            }
        }


        @Override
        protected void onPostExecute(final String[] network_action_response_array) {
            load_sales_person_commision_task = null;

            showProgress(false);

            Log.d(General_Data.TAG, network_action_response_array[0]);
            Log.d(General_Data.TAG, network_action_response_array[1]);

            if (network_action_response_array[0].equals("1")) {
                Toast.makeText(application_context, "Error : " + network_action_response_array[1], Toast.LENGTH_LONG).show();
                Log.d(General_Data.TAG, network_action_response_array[1]);
            } else {

                JSONArray json_array;
                int i = 0;
                try {

                    json_array = new JSONArray(network_action_response_array[1]);
                    if (json_array.getJSONObject(0).getString("status").equals("1")) {
                        Toast.makeText(application_context, "No Commisions...", Toast.LENGTH_LONG).show();
                        button_payout.setEnabled(false);

                    } else {

                        commisions.add(new Commision(Date_Utils.normal_Date_Format.format(Date_Utils.mysql_Date_Format.parse(json_array.getJSONObject(1).getString("clear_date_time"))), ""));

                        for (i = 1; i < json_array.length(); i++) {

                            commisions.add(new Commision(json_array.getJSONObject(i).getString("name"), json_array.getJSONObject(i).getString("amount")));

                        }

                        mAdapter.notifyDataSetChanged();
                    }


                } catch (JSONException e) {


                    Toast.makeText(application_context, "Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Log.d(General_Data.TAG, e.getLocalizedMessage());
                    e.printStackTrace();

                } catch (ParseException e) {
                    Toast.makeText(application_context, "Date Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


            }


        }

        @Override
        protected void onCancelled() {
            load_sales_person_commision_task = null;
            showProgress(false);
        }
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    /* Keep track of the login task to ensure we can cancel it if requested. */
    private Payout_Task payout_task = null;

    public class Payout_Task extends AsyncTask<Void, Void, String[]> {

        Payout_Task() {

        }

        DefaultHttpClient http_client;
        HttpPost http_post;
        ArrayList<NameValuePair> name_pair_value;
        String network_action_response;

        @Override
        protected String[] doInBackground(Void... params) {
            try {
                http_client = new DefaultHttpClient();
                http_post = new HttpPost(General_Data.SERVER_IP_ADDRESS + "/android/payout.php");
                name_pair_value = new ArrayList<NameValuePair>(1);
                name_pair_value.add(new BasicNameValuePair("sales_person_id", selected_sales_person.getId()));

                http_post.setEntity(new UrlEncodedFormEntity(name_pair_value));
                ResponseHandler<String> response_handler = new BasicResponseHandler();
                network_action_response = http_client.execute(http_post, response_handler);
                return new String[]{"0", network_action_response};

            } catch (UnsupportedEncodingException e) {
                return new String[]{"1", "UnsupportedEncodingException : " + e.getLocalizedMessage()};
            } catch (ClientProtocolException e) {
                return new String[]{"1", "ClientProtocolException : " + e.getLocalizedMessage()};
            } catch (IOException e) {
                return new String[]{"1", "IOException : " + e.getLocalizedMessage()};
            }
        }


        @Override
        protected void onPostExecute(final String[] network_action_response_array) {
            payout_task = null;

            showProgress(false);

            Log.d(General_Data.TAG, network_action_response_array[0]);
            Log.d(General_Data.TAG, network_action_response_array[1]);

            if (network_action_response_array[0].equals("1")) {
                Toast.makeText(application_context, "Error : " + network_action_response_array[1], Toast.LENGTH_LONG).show();
                Log.d(General_Data.TAG, network_action_response_array[1]);
            } else {

                try {
                    JSONObject json = new JSONObject(network_action_response_array[1]);
                    String count = json.getString("status");
                    switch (count) {
                        case "0":
                            Toast.makeText(application_context, "OK", Toast.LENGTH_LONG).show();
                            load_commision_page();
                            break;
                        case "1":
                            Toast.makeText(application_context, "Error : " + json.getString("error"), Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(application_context, "Error : Check json", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(application_context, "Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Log.d(General_Data.TAG, e.getLocalizedMessage());
                }


            }


        }

        @Override
        protected void onCancelled() {
            payout_task = null;
            showProgress(false);
        }
    }

}

