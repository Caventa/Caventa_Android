package caventa.ansheer.ndk.caventa.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import caventa.ansheer.ndk.caventa.R;
import caventa.ansheer.ndk.caventa.adapters.Work_Adapter;
import caventa.ansheer.ndk.caventa.commons.DividerItemDecoration;
import caventa.ansheer.ndk.caventa.commons.RecyclerTouchListener;
import caventa.ansheer.ndk.caventa.constants.General_Data;
import caventa.ansheer.ndk.caventa.models.Work;
import ndk.prism.common_utils.Date_Utils;

public class Accounts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accounts);
    }

//
//    /**
//     * A placeholder fragment containing a simple view.
//     */
//    public static class Pending_Works_Fragment extends Fragment {
//
//        public Pending_Works_Fragment() {
//        }
//
//        public static Pending_Works_Fragment newInstance() {
//            Pending_Works_Fragment fragment = new Pending_Works_Fragment();
//            return fragment;
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_pending_works, container, false);
//
//            recyclerView = rootView.findViewById(R.id.recycler_view);
//            mLoginFormView = rootView.findViewById(R.id.login_form);
//            mProgressView = rootView.findViewById(R.id.login_progress);
//
//            finished_works_adaptor = new Work_Adapter(finished_works_list);
//
//            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
//            recyclerView.setLayoutManager(mLayoutManager);
//            recyclerView.setItemAnimator(new DefaultItemAnimator());
//            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
//            recyclerView.setAdapter(finished_works_adaptor);
//
//            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
//                @Override
//                public void onClick(View view, int position) {
////                    Movie movie = movieList.get(position);
////                    Toast.makeText(getContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
////                    Toast.makeText(getContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
////                    Intent i = new Intent(getContext(), Work_Page.class);
////                    i.putExtra("work", movie.getTitle());
////                    startActivity(i);
//                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                }
//
//                @Override
//                public void onLongClick(View view, int position) {
//
//                }
//            }));
//
//            if (pen_data_flag == 0) {
//
//                if (load_finished_works_task != null) {
//                    load_finished_works_task.cancel(true);
//                    load_finished_works_task = null;
//                }
//
//                showProgress(true);
//                load_finished_works_task = new Load_Finished_Works_Task();
//                load_finished_works_task.execute((Void) null);
//
//            }
//
//            return rootView;
//        }
//
//        private RecyclerView recyclerView;
//        static View mLoginFormView;
//        static View mProgressView;
//
//        private static List<Work> finished_works_list = new ArrayList<>();
//        static Work_Adapter finished_works_adaptor;
//
//        public static class Load_Finished_Works_Task extends AsyncTask<Void, Void, String[]> {
//
//            Load_Finished_Works_Task() {
//            }
//
//            DefaultHttpClient http_client;
//            HttpPost http_post;
//            String network_action_response;
//
//            @Override
//            protected String[] doInBackground(Void... params) {
//                try {
//                    http_client = new DefaultHttpClient();
//                    http_post = new HttpPost("http://" + General_Data.SERVER_IP_ADDRESS + "/android/get_pending_works.php");
//                    ResponseHandler<String> response_handler = new BasicResponseHandler();
//                    network_action_response = http_client.execute(http_post, response_handler);
//                    return new String[]{"0", network_action_response};
//
//                } catch (UnsupportedEncodingException e) {
//                    return new String[]{"1", "UnsupportedEncodingException : " + e.getLocalizedMessage()};
//                } catch (ClientProtocolException e) {
//                    return new String[]{"1", "ClientProtocolException : " + e.getLocalizedMessage()};
//                } catch (IOException e) {
//                    return new String[]{"1", "IOException : " + e.getLocalizedMessage()};
//                }
//            }
//
//
//            @Override
//            protected void onPostExecute(final String[] network_action_response_array) {
//                load_finished_works_task = null;
//
//                showProgress(false);
//
//                Log.d(General_Data.TAG, network_action_response_array[0]);
//                Log.d(General_Data.TAG, network_action_response_array[1]);
//
//                if (network_action_response_array[0].equals("1")) {
//                    Toast.makeText(application_context, "Error : " + network_action_response_array[1], Toast.LENGTH_LONG).show();
//                    Log.d(General_Data.TAG, network_action_response_array[1]);
//                } else {
//
//                    try {
//
//                        JSONArray json_array = new JSONArray(network_action_response_array[1]);
//                        if (json_array.getJSONObject(0).getString("status").equals("1")) {
//                            Toast.makeText(application_context, "Error...", Toast.LENGTH_LONG).show();
//                        } else if (json_array.getJSONObject(0).getString("status").equals("0")) {
//
//                            for (int i = 1; i < json_array.length(); i++) {
//
//                                finished_works_list.add(new Work(json_array.getJSONObject(i).getString("name"),
//                                        json_array.getJSONObject(i).getString("address"),
//                                        json_array.getJSONObject(i).getString("id"),
//                                        Date_Utils.mysql_Date_Format.parse(json_array.getJSONObject(i).getString("work_date")),
//                                        Integer.parseInt(json_array.getJSONObject(i).getString("sales_person_id"))));
//
//                            }
//                            finished_works_adaptor.notifyDataSetChanged();
//                            pen_data_flag = 1;
//                        }
//
//                    } catch (JSONException e) {
//                        Toast.makeText(application_context, "Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
//                        Log.d(General_Data.TAG, e.getLocalizedMessage());
//                    } catch (ParseException e) {
//                        Toast.makeText(application_context, "Date Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
//                        Log.d(General_Data.TAG, e.getLocalizedMessage());
//                    }
//                }
//            }
//
//            @Override
//            protected void onCancelled() {
//                load_finished_works_task = null;
//                showProgress(false);
//            }
//        }
//
//        private static Load_Finished_Works_Task load_finished_works_task = null;
//
//        /**
//         * Shows the progress UI and hides the login form.
//         */
//        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//        private static void showProgress(final boolean show) {
//            // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
//            // for very easy animations. If available, use these APIs to fade-in
//            // the progress spinner.
//
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });
//
//
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mProgressView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//                }
//            });
//        }
//
//    }
}
