package caventa.ansheer.ndk.caventa;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
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

import caventa.ansheer.ndk.caventa.models.Sales_Person;

public class Sales_Persons extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Sales_Person_Adapter sales_person_adapter;
    private List<Sales_Person> sales_persons;
    Context application_context;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_persons);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        initCollapsingToolbar();

        final SharedPreferences settings = getApplicationContext().getSharedPreferences(General_Data.SHARED_PREFERENCE,
                Context.MODE_PRIVATE);

        mProgressView = findViewById(R.id.login_progress);
        application_context = getApplicationContext();

        recyclerView = findViewById(R.id.recycler_view);

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
                Sales_Person sales_person = sales_persons.get(position);
                Toast.makeText(getApplicationContext(), sales_person.getName() + " is selected!", Toast.LENGTH_SHORT).show();


                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("sales_person_id", Integer.parseInt(sales_person.getId()));
                editor.apply();

                Intent i=new Intent(application_context,Add_Work.class);
//                i.putExtra("sales_person",sales_person.getName());
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


//        prepareAlbums();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

//                Intent i=new Intent(application_context,Sales_Persons.class);
//                startActivity(i);
            }
        });

//        try {
//            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        if (load_sales_persons_task != null) {
            return;
        }
        load_sales_persons_task = new Load_Sales_Persons();
        load_sales_persons_task.execute((Void) null);
    }
    private Load_Sales_Persons load_sales_persons_task = null;
    /* Represents an asynchronous login task used to authenticate the user. */
    public class Load_Sales_Persons extends AsyncTask<Void, Void, String[]> {

//        String task_work_name, task_work_address, task_advances_json, task_expenses_json;
//        Date task_work_date;
//        int task_sales_person_id;

        Load_Sales_Persons()
//                (String work_name, String work_address, String advances_json, String expenses_json, Date work_date, int sales_person_id)
        {
//            task_work_name = work_name;
//            task_work_address = work_address;
//            task_advances_json = advances_json;
//            task_expenses_json = expenses_json;
//            task_work_date = work_date;
//            task_sales_person_id = sales_person_id;
        }

        DefaultHttpClient http_client;
        HttpPost http_post;
        //        ArrayList<NameValuePair> name_pair_value;
        String network_action_response;

        @Override
        protected String[] doInBackground(Void... params) {
            try {
                http_client = new DefaultHttpClient();
                http_post = new HttpPost("http://" + General_Data.SERVER_IP_ADDRESS + "/android/get_sales_persons.php");
//                name_pair_value = new ArrayList<NameValuePair>(6);
//                name_pair_value.add(new BasicNameValuePair("work_name", task_work_name));
//                name_pair_value.add(new BasicNameValuePair("work_address", task_work_address));
//                name_pair_value.add(new BasicNameValuePair("work_date", Date_Utils.mysql_Date_Format.format(task_work_date)));
//                name_pair_value.add(new BasicNameValuePair("sales_person_id", String.valueOf(task_sales_person_id)));
//                name_pair_value.add(new BasicNameValuePair("advances_json", task_advances_json));
//                name_pair_value.add(new BasicNameValuePair("expenses_json", task_expenses_json));
//
//                http_post.setEntity(new UrlEncodedFormEntity(name_pair_value));
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
            mAuthTask = null;

            showProgress(false);

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

//                                spinner_list.add(json_array.getJSONObject(i).getString("name") + " " + json_array.getJSONObject(i).getString("place") + "[" + json_array.getJSONObject(i).getString("username") + "]");
//                                pum.getMenu().add(json_array.getJSONObject(i).getString("name") + " " + json_array.getJSONObject(i).getString("place") + "[" + json_array.getJSONObject(i).getString("username") + "]");
sales_persons.add(new Sales_Person(json_array.getJSONObject(i).getString("name"),json_array.getJSONObject(i).getString("id")));


                        }

                        sales_person_adapter.notifyDataSetChanged();


                    }


//                    JSONObject json = new JSONObject(network_action_response_array[1]);
//                    String count = json.getString("status");
//                    switch (count) {
//                        case "0":
//                            Toast.makeText(application_context, "OK", Toast.LENGTH_LONG).show();
////                            Intent i = new Intent(Agent_Addition.this, Agent_Addition.class);
////                            startActivity(i);
////                            finish();
//                            break;
//                        case "1":
//                            Toast.makeText(application_context, "Error : " + json.getString("error"), Toast.LENGTH_LONG).show();
////                            txt_name.requestFocus();
//                            finish();
//                            break;
//                        default:
//                            Toast.makeText(application_context, "Error : Check json", Toast.LENGTH_LONG).show();
//                    }

                } catch (JSONException e) {
                    Toast.makeText(application_context, "Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Log.d(General_Data.TAG, e.getLocalizedMessage());
                }


            }


        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    /* Keep track of the login task to ensure we can cancel it if requested. */
    private Load_Sales_Persons mAuthTask = null;

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

//        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            }
//        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
//    private void initCollapsingToolbar() {
//        final CollapsingToolbarLayout collapsingToolbar =
//                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbar.setTitle(" ");
//        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
//        appBarLayout.setExpanded(true);
//
//        // hiding & showing the title when toolbar expanded & collapsed
//        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            boolean isShow = false;
//            int scrollRange = -1;
//
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (scrollRange == -1) {
//                    scrollRange = appBarLayout.getTotalScrollRange();
//                }
//                if (scrollRange + verticalOffset == 0) {
//                    collapsingToolbar.setTitle(getString(R.string.app_name));
//                    isShow = true;
//                } else if (isShow) {
//                    collapsingToolbar.setTitle(" ");
//                    isShow = false;
//                }
//            }
//        });
//    }

    /**
     * Adding few albums for testing
     */
//    private void prepareAlbums() {
//        int[] covers = new int[]{
//                R.drawable.album1,
//                R.drawable.album2,
//                R.drawable.album3,
//                R.drawable.album4,
//                R.drawable.album5,
//                R.drawable.album6,
//                R.drawable.album7,
//                R.drawable.album8,
//                R.drawable.album9,
//                R.drawable.album10,
//                R.drawable.album11};
//
//        Album a = new Album("True Romance", 13, covers[0]);
//        sales_persons.add(a);
//
//        a = new Album("Xscpae", 8, covers[1]);
//        sales_persons.add(a);
//
//        a = new Album("Maroon 5", 11, covers[2]);
//        sales_persons.add(a);
//
//        a = new Album("Born to Die", 12, covers[3]);
//        sales_persons.add(a);
//
//        a = new Album("Honeymoon", 14, covers[4]);
//        sales_persons.add(a);
//
//        a = new Album("I Need a Doctor", 1, covers[5]);
//        sales_persons.add(a);
//
//        a = new Album("Loud", 11, covers[6]);
//        sales_persons.add(a);
//
//        a = new Album("Legend", 14, covers[7]);
//        sales_persons.add(a);
//
//        a = new Album("Hello", 11, covers[8]);
//        sales_persons.add(a);
//
//        a = new Album("Greatest Hits", 17, covers[9]);
//        sales_persons.add(a);
//
//        sales_person_adapter.notifyDataSetChanged();
//    }

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
}
