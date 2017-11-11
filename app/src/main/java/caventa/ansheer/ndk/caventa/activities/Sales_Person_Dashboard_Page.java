package caventa.ansheer.ndk.caventa.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import caventa.ansheer.ndk.caventa.R;
import caventa.ansheer.ndk.caventa.adapters.MoviesAdapter;
import caventa.ansheer.ndk.caventa.adapters.Work_Adapter;
import caventa.ansheer.ndk.caventa.commons.DividerItemDecoration;
import caventa.ansheer.ndk.caventa.commons.RecyclerTouchListener;
import caventa.ansheer.ndk.caventa.constants.General_Data;
import caventa.ansheer.ndk.caventa.models.Movie;
import caventa.ansheer.ndk.caventa.models.Work;
import ndk.prism.common_utils.Date_Utils;
import ndk.prism.common_utils.Toast_Utils;

public class Sales_Person_Dashboard_Page extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
//    private static ArrayList<Work> work_List;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private static Context application_context;
    private static SharedPreferences settings;
//    static ArrayList<Work> work_List_Blank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        application_context = getApplicationContext();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

//                Intent i=new Intent(Home.this,Sales_Persons.class);
//                startActivity(i);
            }
        });

        settings = getApplicationContext().getSharedPreferences(General_Data.SHARED_PREFERENCE,
                Context.MODE_PRIVATE);

        setTitle(settings.getString("sales_person", "Unknown"));

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);

        // Associate searchable configuration with the SearchView
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));

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
            Intent i = new Intent(Sales_Person_Dashboard_Page.this, Accounts.class);
            startActivity(i);
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
            pending_works_adaptor = new Work_Adapter(pending_works_list);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
//            recyclerView.setAdapter(mAdapter);
            recyclerView.setAdapter(pending_works_adaptor);

            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
//                    Movie movie = movieList.get(position);
//                    Toast.makeText(getContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(getContext(), Work_Page.class);
//                    i.putExtra("work", movie.getTitle());
//                    startActivity(i);
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));

            if (pen_data_flag == 0) {
//                prepareMovieData();


                if (load_sales_person_works_task != null) {
                    load_sales_person_works_task.cancel(true);
                    load_sales_person_works_task = null;
                }

                load_sales_person_works_task = new Load_Sales_Person_Works_Task();
                load_sales_person_works_task.execute((Void) null);

            }

            return rootView;
        }

        private List<Movie> movieList = new ArrayList<>();
        private static List<Work> pending_works_list = new ArrayList<>();

        private RecyclerView recyclerView;
        private MoviesAdapter mAdapter;
        static Work_Adapter pending_works_adaptor;

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


        public static class Load_Sales_Person_Works_Task extends AsyncTask<Void, Void, String[]> {


            Load_Sales_Person_Works_Task() {
            }

            DefaultHttpClient http_client;
            HttpPost http_post;
            ArrayList<NameValuePair> name_pair_value;
            String network_action_response;

            @Override
            protected String[] doInBackground(Void... params) {
                try {
                    http_client = new DefaultHttpClient();
                    http_post = new HttpPost("http://" + General_Data.SERVER_IP_ADDRESS + "/android/get_sales_person_pending_works.php");
                    name_pair_value = new ArrayList<>(1);
                    Log.d(General_Data.TAG, "Sales Man : " + String.valueOf(settings.getInt("sales_person_id", 0)));
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
                load_sales_person_works_task = null;

//                showProgress(false);

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


                                pending_works_list.add(new Work(json_array.getJSONObject(i).getString("name"),
                                        json_array.getJSONObject(i).getString("address"),
                                        json_array.getJSONObject(i).getString("id"),
                                        Date_Utils.mysql_Date_Format.parse(json_array.getJSONObject(i).getString("work_date")),
                                        Integer.parseInt(json_array.getJSONObject(i).getString("sales_person_id"))));


                            }
                            pending_works_adaptor.notifyDataSetChanged();
                            pen_data_flag = 1;

                        }


                    } catch (JSONException e) {
                        Toast.makeText(application_context, "Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        Log.d(General_Data.TAG, e.getLocalizedMessage());
                    } catch (ParseException e) {
                        Toast.makeText(application_context, "Date Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        Log.d(General_Data.TAG, e.getLocalizedMessage());
                    }


                }


            }

            @Override
            protected void onCancelled() {
                load_sales_person_works_task = null;
//                showProgress(false);
            }
        }

        private static Load_Sales_Person_Works_Task load_sales_person_works_task = null;

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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class Upcoming_Works_Fragment extends Fragment {

        public Upcoming_Works_Fragment() {
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

            mAdapter = new MoviesAdapter(movieList);
//            wAdapter = new Work_Adapter(work_List_Blank);
            wAdapter = new Work_Adapter(List_Sales_Persons.work_List);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

//            recyclerView.setAdapter(mAdapter);
            recyclerView.setAdapter(wAdapter);

            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
//                    Movie movie = movieList.get(position);
//                    Toast.makeText(getContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));


//            if (up_data_flag == 0) {
//                prepareWorkData();
//                prepareMovieData();
//            }

            return rootView;
        }


        private List<Movie> movieList = new ArrayList<>();
        List<Work> work_List_Blank = new ArrayList<>();

        private RecyclerView recyclerView;

        private MoviesAdapter mAdapter;
        private Work_Adapter wAdapter;

        private void prepareMovieData() {

            for (int i = 0; i < List_Sales_Persons.work_List.size(); i++) {
                Toast_Utils.longToast(application_context, List_Sales_Persons.work_List.get(i).getWork_name());

//                work_List_Blank.add(new Work(Sales_Persons.work_List.get(i).getWork_name(), Sales_Persons.work_List.get(i).getWork_address(), "", "", Sales_Persons.work_List.get(i).getId(), Sales_Persons.work_List.get(i).getWork_date(), Sales_Persons.work_List.get(i).getSales_person_id()));

                movieList.add(new Movie(List_Sales_Persons.work_List.get(i).getWork_name(), Date_Utils.normal_Date_Format_words.format(List_Sales_Persons.work_List.get(i).getWork_date()), "1995"));
            }


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

        private void prepareWorkData() {

            for (int i = 0; i < List_Sales_Persons.work_List.size(); i++) {
                Toast_Utils.longToast(application_context, List_Sales_Persons.work_List.get(i).getWork_name());

                work_List_Blank.add(new Work(List_Sales_Persons.work_List.get(i).getWork_name(), List_Sales_Persons.work_List.get(i).getWork_address(), List_Sales_Persons.work_List.get(i).getId(), List_Sales_Persons.work_List.get(i).getWork_date(), List_Sales_Persons.work_List.get(i).getSales_person_id()));
            }

//            Work work = new Work("Chicken Run", "Animation", "1", Calendar.getInstance().getTime(), 0);
//            work_List_Blank.add(work);
//            work = new Work("Chicken Run", "Animation",  "1", Calendar.getInstance().getTime(), 0);
//            work_List_Blank.add(work);
//            work = new Work("Chicken Run", "Animation", "1", Calendar.getInstance().getTime(), 0);
//            work_List_Blank.add(work);
//            work = new Work("Chicken Run", "Animation", "1", Calendar.getInstance().getTime(), 0);
//            work_List_Blank.add(work);

            wAdapter.notifyDataSetChanged();

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
                case 0:
                    return Upcoming_Works_Fragment.newInstance();


                case 1:
                    return Pending_Works_Fragment.newInstance();


                default:
                    return Finished_Works_Fragment.newInstance();


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
                case 0:
                    return "Up";
                case 1:
                    return "Pen";
                case 2:
                    return "Fin";
            }
            return null;
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
