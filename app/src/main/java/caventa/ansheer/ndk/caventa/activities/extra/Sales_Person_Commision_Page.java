package caventa.ansheer.ndk.caventa.activities.extra;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;

import caventa.ansheer.ndk.caventa.R;
import caventa.ansheer.ndk.caventa.adapters.extra.Commision_Adapter;
import caventa.ansheer.ndk.caventa.models.extra.Commision;
import caventa.ansheer.ndk.caventa.constants.General_Data;


public class Sales_Person_Commision_Page extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private View mProgressView;
    private View mLoginFormView;

    private List<Commision> commisions;
    private static Context application_context;
    private static SharedPreferences settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_person_commisions_page);


        commisions = new ArrayList<>();
        settings = getApplicationContext().getSharedPreferences(General_Data.SHARED_PREFERENCE,
                Context.MODE_PRIVATE);
        application_context = getApplicationContext();

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


    }

    private Load_Sales_Person_Commision_Task load_sales_person_commision_task = null;

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
                name_pair_value.add(new BasicNameValuePair("sales_person_id", String.valueOf(settings.getInt("sales_person_id", 0))));

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

                JSONArray json_array = new JSONArray();
                int i = 0;
                try {

                    json_array = new JSONArray(network_action_response_array[1]);
                    if (json_array.getJSONObject(0).getString("status").equals("1")) {
                        Toast.makeText(application_context, "No Commisions...", Toast.LENGTH_LONG).show();

                    } else {
                        for (i = 1; i < json_array.length(); i++) {

                            commisions.add(new Commision(json_array.getJSONObject(i).getString("name"), json_array.getJSONObject(i).getString("amount")));


                        }

                        mAdapter.notifyDataSetChanged();
                    }


                } catch (JSONException e) {


                    Toast.makeText(application_context, "Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Log.d(General_Data.TAG, e.getLocalizedMessage());
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


}