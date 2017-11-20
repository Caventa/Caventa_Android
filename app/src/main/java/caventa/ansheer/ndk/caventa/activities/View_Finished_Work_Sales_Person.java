package caventa.ansheer.ndk.caventa.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.List;

import caventa.ansheer.ndk.caventa.R;
import caventa.ansheer.ndk.caventa.adapters.Work_Advances_View_Adapter;
import caventa.ansheer.ndk.caventa.adapters.Work_Expense_View_Adapter;
import caventa.ansheer.ndk.caventa.constants.General_Data;
import caventa.ansheer.ndk.caventa.models.Work;
import caventa.ansheer.ndk.caventa.models.Work_Advance;
import caventa.ansheer.ndk.caventa.models.Work_Expense;
import ndk.prism.common_utils.Date_Utils;

public class View_Finished_Work_Sales_Person extends AppCompatActivity {

    private Context application_context;
    private View mProgressView;
    private View mLoginFormView;

    private Work_Advances_View_Adapter work_advances_adapter;

    private Work_Expense_View_Adapter work_expenses_adapter;

    static List<Work_Advance> work_advances;

    static List<Work_Expense> work_expenses;

    private RecyclerView work_expenses_recycler_view;
    private RecyclerView work_advances_recycler_view;

    private TextView txt_name, txt_address, txt_total_advance, txt_total_expense, txt_profit;
    Work selected_work;
    private TextView txt_commision;
    private TextView txt_net_Profit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_finished_work);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        application_context = getApplicationContext();

        selected_work = CachePot.getInstance().pop(Work.class);
        setTitle(selected_work.getWork_name());

        work_advances = new ArrayList<>();
        work_advances_adapter = new Work_Advances_View_Adapter(this, work_advances);

        work_advances_recycler_view = findViewById(R.id.recycler_view_advance);

        work_advances_recycler_view.setHasFixedSize(false);

        // use a linear layout manager
        LinearLayoutManager work_advances_recycler_view_layout_manager = new LinearLayoutManager(this);
        work_advances_recycler_view.setLayoutManager(work_advances_recycler_view_layout_manager);

        work_advances_recycler_view.setAdapter(work_advances_adapter);

        work_expenses = new ArrayList<>();
        work_expenses_adapter = new Work_Expense_View_Adapter(this, work_expenses);

        work_expenses_recycler_view = findViewById(R.id.recycler_view_expense);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        work_expenses_recycler_view.setHasFixedSize(false);

        // use a linear layout manager
        LinearLayoutManager work_expenses_recycler_view_layout_manager = new LinearLayoutManager(this);
        work_expenses_recycler_view.setLayoutManager(work_expenses_recycler_view_layout_manager);

        work_expenses_recycler_view.setAdapter(work_expenses_adapter);

        if (load_work_profit_task != null) {
            finish();
        }
        showProgress(true);
        load_work_profit_task = new Load_Work_Profit_Task();
        load_work_profit_task.execute((Void) null);

        TextView txt_date = findViewById(R.id.work_date);
        txt_date.setText(Date_Utils.normal_Date_Format_words.format(selected_work.getWork_date()));

        initView();

        txt_name.setText(selected_work.getWork_name());
        txt_address.setText(selected_work.getWork_address());
    }

    private void initView() {
        txt_name = findViewById(R.id.name);
        txt_address = findViewById(R.id.address);
        txt_total_advance = findViewById(R.id.total_advance);
        txt_total_expense = findViewById(R.id.total_expense);
        txt_profit = findViewById(R.id.total_profit);
        txt_commision = (TextView) findViewById(R.id.commision);
        txt_net_Profit = (TextView) findViewById(R.id.net_profit);
    }

    private Load_Work_Profit_Task load_work_profit_task = null;

    static double total_advance = 0;
    static double total_expense = 0;

    public class Load_Work_Profit_Task extends AsyncTask<Void, Void, String[]> {
        private ArrayList<NameValuePair> name_pair_value;

        private boolean work_advances_flag = true;


        Load_Work_Profit_Task() {
        }

        DefaultHttpClient http_client;
        HttpPost http_post;
        String network_action_response;

        @Override
        protected String[] doInBackground(Void... params) {
            try {
                http_client = new DefaultHttpClient();
                http_post = new HttpPost(General_Data.SERVER_IP_ADDRESS + "/android/get_work.php");

                name_pair_value = new ArrayList<NameValuePair>(1);
                name_pair_value.add(new BasicNameValuePair("work_id", selected_work.getId()));

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
            load_work_profit_task = null;

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


                    for (i = 0; i < json_array.length(); i++) {
                        if (json_array.getJSONObject(0).getString("status").equals("1")) {
                            Toast.makeText(application_context, "No Advances...", Toast.LENGTH_LONG).show();
                            work_advances_flag = false;
                        } else {
                            if (i != 0 && work_advances_flag) {

                                work_advances.add(new Work_Advance(json_array.getJSONObject(i).getDouble("amount"), json_array.getJSONObject(i).getString("advance_description")));
                                total_advance = total_advance + json_array.getJSONObject(i).getDouble("amount");

                            }
                        }

                    }


                } catch (JSONException e) {

                    Log.d(General_Data.TAG, String.valueOf(i));

                    if (e.getLocalizedMessage().contains("No value for amount")) {
                        try {
                            for (int j = i; j < json_array.length(); j++) {
                                if (json_array.getJSONObject(i).getString("status").equals("1")) {
                                    Toast.makeText(application_context, "No Expenses...", Toast.LENGTH_LONG).show();
                                    break;
                                } else {
                                    if (j != i) {
                                        work_expenses.add(new Work_Expense(json_array.getJSONObject(j).getDouble("amount"), json_array.getJSONObject(j).getString("expense_description")));
                                        total_expense = total_expense + json_array.getJSONObject(j).getDouble("amount");
                                    }
                                }
                            }

                            work_advances_adapter.notifyDataSetChanged();
                            txt_total_advance.setText("Advances : " + total_advance);

                            work_expenses_adapter.notifyDataSetChanged();
                            txt_total_expense.setText("Expenses : " + total_expense);

                            txt_profit.setText("Profit : " + (total_advance - total_expense));

                            txt_commision.setText("Commision : " + ((total_advance - total_expense)*0.6));

                            txt_net_Profit.setText("Net Profit : " + ((total_advance - total_expense)*0.4));

                        } catch (JSONException ex) {
                            Toast.makeText(application_context, "Error : " + ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            Log.d(General_Data.TAG, ex.getLocalizedMessage());
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(application_context, "Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        Log.d(General_Data.TAG, e.getLocalizedMessage());
//                        Log.d(General_Data.TAG, e.getCause().toString());
//                        Log.d(General_Data.TAG, e.getClass().toString());
                        e.printStackTrace();
                    }
                }


            }


        }

        @Override
        protected void onCancelled() {
            load_work_profit_task = null;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_work, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_item_cancel) {
            Intent i = new Intent(application_context, Sales_Person_Dashboard_Page.class);
            startActivity(i);
            finish();
            return true;
        }

//        if (id == R.id.menu_item_finish) {
//
//            AlertDialog.Builder after_time_dialog = new AlertDialog.Builder(this);
//            after_time_dialog.setMessage("Work is Finished, Is it?").setCancelable(false)
//                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//
//                            dialog.cancel();
//                        }
//                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//
//                    dialog.cancel();
//                }
//            });
//            AlertDialog alert = after_time_dialog.create();
//            alert.setTitle("Warning!");
//            alert.show();
//
////            Intent i = new Intent(application_context, Sales_Person_Dashboard_Page.class);
////            startActivity(i);
////            finish();
//            return true;
//        }
//
//        if (id == R.id.menu_item_work_cancel) {
////            Intent i = new Intent(application_context, Sales_Person_Dashboard_Page.class);
////            startActivity(i);
////            finish();
//
//            AlertDialog.Builder after_time_dialog = new AlertDialog.Builder(this);
//            after_time_dialog.setMessage("Work will be cancelled, OK?").setCancelable(false)
//                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//
//                            dialog.cancel();
//                        }
//                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//
//                    dialog.cancel();
//                }
//            });
//            AlertDialog alert = after_time_dialog.create();
//            alert.setTitle("Warning!");
//            alert.show();
//
//            return true;
//        }

//        if (id == R.id.menu_item_edit) {
//
//            Intent i = new Intent(application_context, Edit_Work.class);
//            CachePot.getInstance().push(selected_work);
//            CachePot.getInstance().push(work_advances);
//            CachePot.getInstance().push(work_expenses);
//            startActivity(i);
//            finish();
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(application_context, Sales_Person_Dashboard_Page.class);
        startActivity(i);
        finish();
    }
}
