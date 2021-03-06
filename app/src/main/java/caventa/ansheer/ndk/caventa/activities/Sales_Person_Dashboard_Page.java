package caventa.ansheer.ndk.caventa.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import caventa.ansheer.ndk.caventa.adapters.Work_Adapter;
import caventa.ansheer.ndk.caventa.commons.Activity_Utils;
import caventa.ansheer.ndk.caventa.commons.DividerItemDecoration;
import caventa.ansheer.ndk.caventa.commons.Network_Utils;
import caventa.ansheer.ndk.caventa.commons.RecyclerTouchListener;
import caventa.ansheer.ndk.caventa.commons.Snackbar_Utils;
import caventa.ansheer.ndk.caventa.commons.Tab_Layout_Utils;
import caventa.ansheer.ndk.caventa.constants.General_Data;
import caventa.ansheer.ndk.caventa.models.Work;
import ndk.utils.Date_Utils;


//TODO:Work search

public class Sales_Person_Dashboard_Page extends AppCompatActivity {

    private static Context application_context;
    private static SharedPreferences settings;

    static FloatingActionButton fab;
    private Context activity_context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        application_context = getApplicationContext();
        activity_context = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fin_data_flag = 0;
                pen_data_flag = 0;
                up_data_flag = 0;

                finished_works_list = new ArrayList<>();
                pending_works_list = new ArrayList<>();
                upcoming_works_list = new ArrayList<>();

                Activity_Utils.start_activity_with_finish(activity_context, Add_Work.class);
            }
        });

        settings = getApplicationContext().getSharedPreferences(General_Data.SHARED_PREFERENCE, Context.MODE_PRIVATE);

        setTitle(settings.getString("sales_person", "Unknown"));

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        /*
      The {@link android.support.v4.view.PagerAdapter} that will provide
      fragments for each of the sections. We use a
      {@link FragmentPagerAdapter} derivative, which will keep every
      loaded fragment in memory. If this becomes too memory intensive, it
      may be best to switch to a
      {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        /*
      The {@link ViewPager} that will host the section contents.
     */
        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Tab_Layout_Utils.select_Tab(tabLayout, getIntent().getIntExtra("tab_index", 0));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sales_person_home, menu);

        // Associate searchable configuration with the SearchView
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));

        return true;
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
            return new Pending_Works_Fragment();
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_pending_works, container, false);

            recyclerView = rootView.findViewById(R.id.recycler_view);
            mLoginFormView = rootView.findViewById(R.id.login_form);
            mProgressView = rootView.findViewById(R.id.login_progress);

            pending_works_adaptor = new Work_Adapter(pending_works_list);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

            recyclerView.setAdapter(pending_works_adaptor);

            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {

                    Log.d(General_Data.TAG, "Work ID : " + pending_works_list.get(position).getId());

                    Activity_Utils.start_activity_with_object_push_and_finish_and_origin(getActivity(), View_Work.class, clear_static_variables_and_return_current_work(1, position), "Sales_Person_Pen");

//                    start_activity_with_object_push_and_origin(getActivity(), View_Work.class, pending_works_list.get(position), "Sales_Person_Pen");
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));

            if (pen_data_flag == 0) {

                if (load_pending_works_task != null) {
                    load_pending_works_task.cancel(true);
                    load_pending_works_task = null;
                }
                Network_Utils.showProgress(true, application_context, mProgressView, mLoginFormView);
                load_pending_works_task = new Load_Pending_Works_Task(getActivity());
                load_pending_works_task.execute((Void) null);
            }
            return rootView;
        }


        static View mLoginFormView;
        static View mProgressView;

        private RecyclerView recyclerView;


        public static class Load_Pending_Works_Task extends AsyncTask<Void, Void, String[]> {

            FragmentActivity current_activity;

            Load_Pending_Works_Task(FragmentActivity current_activity) {
                this.current_activity = current_activity;
            }

            DefaultHttpClient http_client;
            HttpPost http_post;
            ArrayList<NameValuePair> name_pair_value;
            String network_action_response;

            @Override
            protected String[] doInBackground(Void... params) {
                try {
                    http_client = new DefaultHttpClient();
                    http_post = new HttpPost(General_Data.SERVER_IP_ADDRESS + "/android/get_sales_person_pending_works.php");
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
                load_pending_works_task = null;

                Network_Utils.showProgress(false, application_context, mProgressView, mLoginFormView);

                Log.d(General_Data.TAG, network_action_response_array[0]);
                Log.d(General_Data.TAG, network_action_response_array[1]);

                if (network_action_response_array[0].equals("1")) {
                    Toast.makeText(application_context, "Error : " + network_action_response_array[1], Toast.LENGTH_LONG).show();
                    Log.d(General_Data.TAG, network_action_response_array[1]);
                } else {
                    try {
                        JSONArray json_array = new JSONArray(network_action_response_array[1]);
                        if (json_array.getJSONObject(0).getString("status").equals("1")) {
                            Snackbar_Utils.display_Short_FAB_success_bottom_SnackBar(current_activity, "No Pending Works...", fab);

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
                load_pending_works_task = null;
                Network_Utils.showProgress(false, application_context, mProgressView, mLoginFormView);
            }
        }

        private static Load_Pending_Works_Task load_pending_works_task = null;
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class Finished_Works_Fragment extends Fragment {

        public Finished_Works_Fragment() {
        }

        public static Finished_Works_Fragment newInstance() {
            return new Finished_Works_Fragment();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_finished_works, container, false);

            recyclerView = rootView.findViewById(R.id.recycler_view);
            mLoginFormView = rootView.findViewById(R.id.login_form);
            mProgressView = rootView.findViewById(R.id.login_progress);

            finished_works_adaptor = new Work_Adapter(finished_works_list);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(finished_works_adaptor);

            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Log.d(General_Data.TAG, "Work ID : " + finished_works_list.get(position).getId());

                    Activity_Utils.start_activity_with_object_push_and_finish_and_origin(getActivity(), View_Work.class, clear_static_variables_and_return_current_work(2, position), "Sales_Person_Fin");

//                    start_activity_with_object_push_and_origin(getActivity(), View_Work.class, finished_works_list.get(position),"Sales_Person_Fin");
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));

            if (fin_data_flag == 0) {
                if (load_finished_works_task != null) {
                    load_finished_works_task.cancel(true);
                    load_finished_works_task = null;
                }

                Network_Utils.showProgress(true, application_context, mProgressView, mLoginFormView);
                load_finished_works_task = new Load_Finished_Works_Task();
                load_finished_works_task.execute((Void) null);
            }

            return rootView;
        }

        private RecyclerView recyclerView;

        static View mLoginFormView;
        static View mProgressView;


        public static class Load_Finished_Works_Task extends AsyncTask<Void, Void, String[]> {

            Load_Finished_Works_Task() {
            }

            DefaultHttpClient http_client;
            HttpPost http_post;
            String network_action_response;
            ArrayList<NameValuePair> name_pair_value;

            @Override
            protected String[] doInBackground(Void... params) {
                try {
                    http_client = new DefaultHttpClient();
                    http_post = new HttpPost(General_Data.SERVER_IP_ADDRESS + "/android/get_sales_person_finished_works.php");
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
                load_finished_works_task = null;

                Network_Utils.showProgress(false, application_context, mProgressView, mLoginFormView);

                Log.d(General_Data.TAG, network_action_response_array[0]);
                Log.d(General_Data.TAG, network_action_response_array[1]);

                if (network_action_response_array[0].equals("1")) {
                    Toast.makeText(application_context, "Error : " + network_action_response_array[1], Toast.LENGTH_LONG).show();
                    Log.d(General_Data.TAG, network_action_response_array[1]);
                } else {
                    try {
                        JSONArray json_array = new JSONArray(network_action_response_array[1]);
                        if (json_array.getJSONObject(0).getString("status").equals("1")) {
                            Toast.makeText(application_context, "No Finished Works...", Toast.LENGTH_LONG).show();
                        } else if (json_array.getJSONObject(0).getString("status").equals("0")) {

                            for (int i = 1; i < json_array.length(); i++) {

                                finished_works_list.add(new Work(json_array.getJSONObject(i).getString("name"),
                                        json_array.getJSONObject(i).getString("address"),
                                        json_array.getJSONObject(i).getString("id"),
                                        Date_Utils.mysql_Date_Format.parse(json_array.getJSONObject(i).getString("work_date")),
                                        Integer.parseInt(json_array.getJSONObject(i).getString("sales_person_id"))));

                            }
                            finished_works_adaptor.notifyDataSetChanged();
                            fin_data_flag = 1;
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
                load_finished_works_task = null;
                Network_Utils.showProgress(false, application_context, mProgressView, mLoginFormView);
            }
        }

        private static Load_Finished_Works_Task load_finished_works_task = null;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class Upcoming_Works_Fragment extends Fragment {

        public Upcoming_Works_Fragment() {
        }

        public static Upcoming_Works_Fragment newInstance() {
            return new Upcoming_Works_Fragment();
        }

        @Override
        public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_upcoming_works, container, false);

            recyclerView = rootView.findViewById(R.id.recycler_view);
            mLoginFormView = rootView.findViewById(R.id.login_form);
            mProgressView = rootView.findViewById(R.id.login_progress);

            upcoming_works_adaptor = new Work_Adapter(upcoming_works_list);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

            recyclerView.setAdapter(upcoming_works_adaptor);

            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Log.d(General_Data.TAG, "Work ID : " + upcoming_works_list.get(position).getId());

                    Activity_Utils.start_activity_with_object_push_and_finish_and_origin(getActivity(), View_Work.class, clear_static_variables_and_return_current_work(0, position), "Sales_Person_Up");

//                    start_activity_with_object_push_and_finish_and_origin(getActivity(), View_Work.class, upcoming_works_list.get(position), "Sales_Person_Up");


                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));


            if (up_data_flag == 0) {

                if (load_upcoming_works_task != null) {
                    load_upcoming_works_task.cancel(true);
                    load_upcoming_works_task = null;
                }

                Network_Utils.showProgress(true, application_context, mProgressView, mLoginFormView);
                load_upcoming_works_task = new Load_Upcoming_Works_Task();
                load_upcoming_works_task.execute((Void) null);
            }

            return rootView;
        }

        private RecyclerView recyclerView;

        static View mLoginFormView;
        static View mProgressView;

        public static class Load_Upcoming_Works_Task extends AsyncTask<Void, Void, String[]> {

            Load_Upcoming_Works_Task() {
            }

            DefaultHttpClient http_client;
            HttpPost http_post;
            String network_action_response;
            ArrayList<NameValuePair> name_pair_value;

            @Override
            protected String[] doInBackground(Void... params) {
                try {
                    http_client = new DefaultHttpClient();
                    http_post = new HttpPost(General_Data.SERVER_IP_ADDRESS + "/android/get_sales_person_upcoming_works.php");
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
                load_upcoming_works_task = null;

                Network_Utils.showProgress(false, application_context, mProgressView, mLoginFormView);

                Log.d(General_Data.TAG, network_action_response_array[0]);
                Log.d(General_Data.TAG, network_action_response_array[1]);

                if (network_action_response_array[0].equals("1")) {
                    Toast.makeText(application_context, "Error : " + network_action_response_array[1], Toast.LENGTH_LONG).show();
                    Log.d(General_Data.TAG, network_action_response_array[1]);
                } else {
                    try {
                        JSONArray json_array = new JSONArray(network_action_response_array[1]);
                        if (json_array.getJSONObject(0).getString("status").equals("1")) {
                            Toast.makeText(application_context, "No Upcoming Works...", Toast.LENGTH_LONG).show();
                        } else if (json_array.getJSONObject(0).getString("status").equals("0")) {

                            for (int i = 1; i < json_array.length(); i++) {

                                upcoming_works_list.add(new Work(json_array.getJSONObject(i).getString("name"),
                                        json_array.getJSONObject(i).getString("address"),
                                        json_array.getJSONObject(i).getString("id"),
                                        Date_Utils.mysql_Date_Format.parse(json_array.getJSONObject(i).getString("work_date")),
                                        Integer.parseInt(json_array.getJSONObject(i).getString("sales_person_id"))));

                            }
                            upcoming_works_adaptor.notifyDataSetChanged();
                            up_data_flag = 1;
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
                load_upcoming_works_task = null;
                Network_Utils.showProgress(false, application_context, mProgressView, mLoginFormView);
            }
        }

        private static Load_Upcoming_Works_Task load_upcoming_works_task = null;
    }

    static List<Work> finished_works_list = new ArrayList<>();
    static Work_Adapter finished_works_adaptor;

    static List<Work> pending_works_list = new ArrayList<>();
    static Work_Adapter pending_works_adaptor;

    static List<Work> upcoming_works_list = new ArrayList<>();
    static Work_Adapter upcoming_works_adaptor;

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

    static Work clear_static_variables_and_return_current_work(int tab_index, int position) {
        up_data_flag = 0;
        fin_data_flag = 0;
        pen_data_flag = 0;

        Work current_work;

        switch (tab_index) {
            case 0:
                current_work = upcoming_works_list.get(position);
                break;

            case 1:
                current_work = pending_works_list.get(position);
                break;

            default:
                current_work = finished_works_list.get(position);

        }

        upcoming_works_list = new ArrayList<>();
        pending_works_list = new ArrayList<>();
        finished_works_list = new ArrayList<>();

        return current_work;
    }

    @Override
    public void onBackPressed() {
        fin_data_flag = 0;
        pen_data_flag = 0;
        up_data_flag = 0;

        finished_works_list = new ArrayList<>();
        pending_works_list = new ArrayList<>();
        upcoming_works_list = new ArrayList<>();

        Activity_Utils.start_activity_with_finish(activity_context, List_Sales_Persons.class);
    }

}
