package caventa.ansheer.ndk.caventa.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import caventa.ansheer.ndk.caventa.R;
import caventa.ansheer.ndk.caventa.adapters.Sales_Person_Adapter;
import caventa.ansheer.ndk.caventa.commons.Activity_Utils;
import caventa.ansheer.ndk.caventa.commons.Network_Utils;
import caventa.ansheer.ndk.caventa.commons.RecyclerTouchListener;
import caventa.ansheer.ndk.caventa.constants.General_Data;
import caventa.ansheer.ndk.caventa.models.Sales_Person;
import ndk.utils.Toast_Utils;


//TODO:Add sales person with photo

public class List_Sales_Persons extends AppCompatActivity {

    private Sales_Person_Adapter sales_person_adapter;
    private List<Sales_Person> sales_persons;
    SharedPreferences settings;
    private View mProgressView;
    private View mLoginFormView;
    private Context activity_context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_persons);

        application_context = getApplicationContext();
        activity_context = this;

        settings = getApplicationContext().getSharedPreferences(General_Data.SHARED_PREFERENCE,Context.MODE_PRIVATE);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        application_context = getApplicationContext();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        sales_persons = new ArrayList<>();
        sales_person_adapter = new Sales_Person_Adapter(this, sales_persons);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(sales_person_adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                // Show a progress spinner, and kick off a background task to perform the user login attempt.
                if (Network_Utils.isOnline(application_context)) {
                    Sales_Person sales_person = sales_persons.get(position);
                    Toast.makeText(getApplicationContext(), sales_person.getName() + " is selected!", Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("sales_person_id", Integer.parseInt(sales_person.getId()));
                    editor.putString("sales_person", sales_person.getName());
                    editor.apply();

//                    Intent i = new Intent(application_context, Sales_Person_Dashboard_Page.class);
//                    startActivity(i);
                    Activity_Utils.start_activity_with_finish_and_tab_index(activity_context,Sales_Person_Dashboard_Page.class,0);

                } else {
                    Toast_Utils.longToast(getApplicationContext(), "Internet is unavailable");
                }


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with Add Sales Person action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

//                Intent i=new Intent(application_context,Sales_Persons.class);
//                startActivity(i);
            }
        });


        if (load_sales_persons_task != null) {
            load_sales_persons_task.cancel(true);
            load_sales_persons_task = null;
        }
        Network_Utils.showProgress(true,application_context,mProgressView,mLoginFormView);
        load_sales_persons_task = new Load_Sales_Persons_Task();
        load_sales_persons_task.execute((Void) null);
    }

    Context application_context;

    private Load_Sales_Persons_Task load_sales_persons_task = null;

    /* Represents an asynchronous login task used to authenticate the user. */
    public class Load_Sales_Persons_Task extends AsyncTask<Void, Void, String[]> {
        Load_Sales_Persons_Task() {
        }

        DefaultHttpClient http_client;
        HttpPost http_post;
        String network_action_response;

        @Override
        protected String[] doInBackground(Void... params) {
            try {
                http_client = new DefaultHttpClient();
                http_post = new HttpPost(General_Data.SERVER_IP_ADDRESS + "/android/get_sales_persons.php");
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
            load_sales_persons_task = null;

            Network_Utils.showProgress(false,application_context,mProgressView,mLoginFormView);

            Log.d(General_Data.TAG, network_action_response_array[0]);
            Log.d(General_Data.TAG, network_action_response_array[1]);

            if (network_action_response_array[0].equals("1")) {
                Toast.makeText(application_context, "Error : " + network_action_response_array[1], Toast.LENGTH_LONG).show();
                Log.d(General_Data.TAG, network_action_response_array[1]);
            } else {
                try {
                    JSONArray json_array = new JSONArray(network_action_response_array[1]);
                    if (json_array.getJSONObject(0).getString("status").equals("1")) {
                        Toast.makeText(application_context, "Error...", Toast.LENGTH_LONG).show();
                    } else if (json_array.getJSONObject(0).getString("status").equals("0")) {

                        for (int i = 1; i < json_array.length(); i++) {
                            sales_persons.add(new Sales_Person(json_array.getJSONObject(i).getString("name"), json_array.getJSONObject(i).getString("id")));
                        }
                        sales_person_adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    Toast.makeText(application_context, "Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Log.d(General_Data.TAG, e.getLocalizedMessage());
                }
            }
        }

        @Override
        protected void onCancelled() {
            load_sales_persons_task = null;
            Network_Utils.showProgress(false,application_context,mProgressView,mLoginFormView);
        }
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onBackPressed() {
        Activity_Utils.start_activity_with_finish_and_tab_index(this,Dashboard_Page.class,0);
    }
}
