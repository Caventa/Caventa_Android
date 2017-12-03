package caventa.ansheer.ndk.caventa.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import caventa.ansheer.ndk.caventa.R;
import caventa.ansheer.ndk.caventa.adapters.Work_Overview_Adapter;
import caventa.ansheer.ndk.caventa.constants.General_Data;
import caventa.ansheer.ndk.caventa.models.Sales_Person;
import caventa.ansheer.ndk.caventa.models.Work_Overview;
import ndk.prism.common_utils.Date_Utils;
import ndk.prism.common_utils.Toast_Utils;

//TODO:Sales reports

public class Sales_Reports extends AppCompatActivity {

    private Context application_context;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private View mProgressView;
    private View mLoginFormView;
    private Sales_Person selected_sales_person;
    private Bundle extras;
    private ArrayList<String> spinner_list;
    Spinner spinner_Scheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_reports);
        selected_sales_person = CachePot.getInstance().pop(Sales_Person.class);
        application_context = getApplicationContext();

        Toolbar mToolbar = findViewById(R.id.toolbar);
        spinner_Scheme = findViewById(R.id.spinner_nav);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        extras = getIntent().getExtras();
        final boolean[] mSpinnerInitialized = new boolean[1];
        spinner_Scheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int item_position, long id) {
                //TODO : refresh on same scheme selection
                if (!mSpinnerInitialized[0]) {
                    mSpinnerInitialized[0] = true;
                    return;
                }

                if (isOnline()) {
                    load_sales_reports_page();
                } else {
                    Toast_Utils.longToast(getApplicationContext(), "Internet is unavailable");
                }


            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO : Your code here
            }
        });

        spinner_list = new ArrayList<String>();

        for (int i = 0; i < Accounts.sales_persons.size(); i++) {
            spinner_list.add(Accounts.sales_persons.get(i).getName());
        }
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(application_context, R.layout.spinner_item_actionbar, spinner_list);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Scheme.setAdapter(spinner_adapter);

        spinner_Scheme.setSelection(extras.getInt("position"));

        work_overviews = new ArrayList<>();

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mRecyclerView = findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // create an Object for Adapter
        mAdapter = new Work_Overview_Adapter(work_overviews);

        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);


        if (load_sales_person_sale_reports_task != null) {
            finish();
        }
        showProgress(true);
        load_sales_person_sale_reports_task = new Load_Sales_Person_Sale_Reports_Task();
        load_sales_person_sale_reports_task.execute((Void) null);



    }
    private List<Work_Overview> work_overviews;

    void start_activity(Class activity) {
        Intent intent = new Intent(getApplicationContext(), activity);
        startActivity(intent);
    }



    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void load_sales_reports_page() {
        Intent i = new Intent(application_context, Sales_Reports.class);
        CachePot.getInstance().push(Accounts.sales_persons.get(spinner_Scheme.getSelectedItemPosition()));
        i.putExtra("position", spinner_Scheme.getSelectedItemPosition());
        startActivity(i);
        finish();
    }

    private Load_Sales_Person_Sale_Reports_Task load_sales_person_sale_reports_task = null;

    public class Load_Sales_Person_Sale_Reports_Task extends AsyncTask<Void, Void, String[]> {
        Load_Sales_Person_Sale_Reports_Task() {
        }

        DefaultHttpClient http_client;
        HttpPost http_post;
        String network_action_response;
        ArrayList<NameValuePair> name_pair_value;

        @Override
        protected String[] doInBackground(Void... params) {
            try {
                http_client = new DefaultHttpClient();
                http_post = new HttpPost(General_Data.SERVER_IP_ADDRESS + "/android/get_work_overviews.php");

                name_pair_value = new ArrayList<>(1);
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
            load_sales_person_sale_reports_task = null;

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
                        Toast.makeText(application_context, "No Reports...", Toast.LENGTH_LONG).show();

                    } else {

                        for (i = 1; i < json_array.length(); i++) {

                            work_overviews.add(new Work_Overview(json_array.getJSONObject(i).getString("name"), json_array.getJSONObject(i).getString("address"),Date_Utils.normal_Date_Format_words.format(Date_Utils.mysql_Date_Format.parse(json_array.getJSONObject(i).getString("work_date"))),json_array.getJSONObject(i).getString("total_advance").equals("null")? 0 : Double.parseDouble(json_array.getJSONObject(i).getString("total_advance")),json_array.getJSONObject(i).getString("total_expense").equals("null")? 0 : Double.parseDouble(json_array.getJSONObject(i).getString("total_expense")) ));

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
            load_sales_person_sale_reports_task = null;
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
}
