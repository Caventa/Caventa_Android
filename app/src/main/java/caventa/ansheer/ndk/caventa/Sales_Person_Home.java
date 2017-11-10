package caventa.ansheer.ndk.caventa;

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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

public class Sales_Person_Home extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private Context application_context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        Bundle extras = getIntent().getExtras();
        setTitle(extras.getString("sales_person"));

        application_context = getApplicationContext();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                Intent i = new Intent(Sales_Person_Home.this, Add_Work.class);
                startActivity(i);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sales, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_item_bank) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static int fin_data_flag = 0, pen_data_flag = 0, up_data_flag = 0;

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class Pending_Works_Fragment extends Fragment {


        public Pending_Works_Fragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Pending_Works_Fragment newInstance() {
            Pending_Works_Fragment fragment = new Pending_Works_Fragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_pending_works, container, false);

            recyclerView = rootView.findViewById(R.id.recycler_view);

            mAdapter = new MoviesAdapter(movieList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(mAdapter);

            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Movie movie = movieList.get(position);
                    Toast.makeText(getContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getContext(), Work.class);
                    i.putExtra("work", movie.getTitle());
                    startActivity(i);
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));

            if (pen_data_flag == 0) {
                prepareMovieData();
            }

            return rootView;
        }

        private List<Movie> movieList = new ArrayList<>();
        private RecyclerView recyclerView;
        private MoviesAdapter mAdapter;

        private void prepareMovieData() {
            Movie movie = new Movie("Mad Max: Fury Road", "Action & Adventure", "2015");
            movieList.add(movie);

            movie = new Movie("Inside Out", "Animation, Kids & Family", "2015");
            movieList.add(movie);

            movie = new Movie("Star Wars: Episode VII - The Force Awakens", "Action", "2015");
            movieList.add(movie);

            movie = new Movie("Shaun the Sheep", "Animation", "2015");
            movieList.add(movie);

            movie = new Movie("The Martian", "Science Fiction & Fantasy", "2015");
            movieList.add(movie);

            movie = new Movie("Mission: Impossible Rogue Nation", "Action", "2015");
            movieList.add(movie);

            mAdapter.notifyDataSetChanged();

            pen_data_flag = 1;
        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class Finished_Works_Fragment extends Fragment {

        public Finished_Works_Fragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Finished_Works_Fragment newInstance() {
            Finished_Works_Fragment fragment = new Finished_Works_Fragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_finished_works, container, false);

            recyclerView = rootView.findViewById(R.id.recycler_view);

            mAdapter = new MoviesAdapter(movieList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(mAdapter);

            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Movie movie = movieList.get(position);
                    Toast.makeText(getContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));

            if (fin_data_flag == 0) {
                prepareMovieData();
            }

            return rootView;
        }

        private List<Movie> movieList = new ArrayList<>();
        private RecyclerView recyclerView;
        private MoviesAdapter mAdapter;

        private void prepareMovieData() {


            Movie movie = new Movie("Up", "Animation", "2009");
            movieList.add(movie);

            movie = new Movie("Star Trek", "Science Fiction", "2009");
            movieList.add(movie);

            movie = new Movie("The LEGO Movie", "Animation", "2014");
            movieList.add(movie);

            movie = new Movie("Iron Man", "Action & Adventure", "2008");
            movieList.add(movie);

            movie = new Movie("Aliens", "Science Fiction", "1986");
            movieList.add(movie);


            mAdapter.notifyDataSetChanged();

            fin_data_flag = 1;
        }
    }

    private Sales_Persons.Load_Sales_Persons load_sales_persons_task = null;

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
                http_post = new HttpPost("http://" + General_Data.SERVER_IP_ADDRESS + "/android/get_sales_person_works.php");
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
                            sales_persons.add(new Sales_Person(json_array.getJSONObject(i).getString("name"), json_array.getJSONObject(i).getString("id")));


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
    private Sales_Persons.Load_Sales_Persons mAuthTask = null;

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
     * A placeholder fragment containing a simple view.
     */
    public static class Upcoming_Works_Fragment extends Fragment {

        public Upcoming_Works_Fragment() {

            if (load_sales_persons_task != null) {
                return;
            }
            showProgress(true);
            load_sales_persons_task = new Load_Sales_Persons();
            load_sales_persons_task.execute((Void) null);
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Upcoming_Works_Fragment newInstance() {
            Upcoming_Works_Fragment fragment = new Upcoming_Works_Fragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_upcoming_works, container, false);

            recyclerView = rootView.findViewById(R.id.recycler_view);

            mAdapter = new MoviesAdapter_Color(movieList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(mAdapter);

            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Movie movie = movieList.get(position);
                    Toast.makeText(getContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));

            if (up_data_flag == 0) {
                prepareMovieData();
            }

            return rootView;
        }

        private List<Movie> movieList = new ArrayList<>();
        private RecyclerView recyclerView;
        private MoviesAdapter_Color mAdapter;

        private void prepareMovieData() {


            Movie movie = new Movie("Chicken Run", "Animation", "2000");
            movieList.add(movie);

            movie = new Movie("Back to the Future", "Science Fiction", "1985");
            movieList.add(movie);

            movie = new Movie("Raiders of the Lost Ark", "Action & Adventure", "1981");
            movieList.add(movie);

            movie = new Movie("Goldfinger", "Action & Adventure", "1965");
            movieList.add(movie);

            movie = new Movie("Guardians of the Galaxy", "Science Fiction & Fantasy", "2014");
            movieList.add(movie);

            mAdapter.notifyDataSetChanged();

            up_data_flag = 1;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a Pending_Works_Fragment (defined as a static inner class below).
            switch (position) {
                case 1:
                    return Pending_Works_Fragment.newInstance();

                default:
                    return Finished_Works_Fragment.newInstance();

                case 0:
                    return Upcoming_Works_Fragment.newInstance();


            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                default:
                    return "Fin";
                case 1:
                    return "Pen";
                case 0:
                    return "Up";
            }
        }
    }

    @Override
    public void onBackPressed() {
        fin_data_flag = 0;
        pen_data_flag = 0;
        up_data_flag = 0;
        super.onBackPressed();
    }
}
