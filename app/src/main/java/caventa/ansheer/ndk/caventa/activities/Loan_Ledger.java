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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.kimkevin.cachepot.CachePot;

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
import caventa.ansheer.ndk.caventa.commons.Activity_Utils;
import caventa.ansheer.ndk.caventa.constants.General_Data;
import caventa.ansheer.ndk.caventa.models.sortable_table_view.loan_ledger_table_view.Loan_Ledger_Entry;
import caventa.ansheer.ndk.caventa.models.sortable_table_view.loan_ledger_table_view.Loan_Ledger_TableView;
import caventa.ansheer.ndk.caventa.models.sortable_table_view.loan_ledger_table_view.Loan_Ledger_Table_Data_Adapter;
import de.codecrafters.tableview.listeners.TableDataClickListener;

import static caventa.ansheer.ndk.caventa.commons.Date_Utils.mysql_date_time_format;

//TODO:Loans

public class Loan_Ledger extends AppCompatActivity {


    private Context application_context;
    static List<Loan_Ledger_Entry> loan_ledger_entries;
    private ProgressBar mProgressView;
    private Loan_Ledger_TableView loan_ledger_tableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loan_ledger);
        initView();

        application_context = getApplicationContext();

        if (load_account_ledger_task != null) {
            load_account_ledger_task.cancel(true);
            load_account_ledger_task = null;
        }
        showProgress(true);
        load_account_ledger_task = new Load_Account_Ledger_Task();
        load_account_ledger_task.execute((Void) null);

        loan_ledger_tableView.addDataClickListener(new Loan_Entry_Click_Listener());
    }

    private class Loan_Entry_Click_Listener implements TableDataClickListener<Loan_Ledger_Entry> {
        @Override
        public void onDataClicked(int rowIndex, Loan_Ledger_Entry clicked_loan_ledger_entry) {
            String clicked_loan_ledger_entry_string = clicked_loan_ledger_entry.getParticulars() + " " + clicked_loan_ledger_entry.getLoan_amount();
            Toast.makeText(application_context, clicked_loan_ledger_entry_string, Toast.LENGTH_SHORT).show();

            CachePot.getInstance().push(clicked_loan_ledger_entry);
            start_activity(Loan_Installments_Ledger.class);
        }
    }

    void start_activity(Class activity) {
        Intent intent = new Intent(getApplicationContext(), activity);
        startActivity(intent);
    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private Load_Account_Ledger_Task load_account_ledger_task = null;

    private void initView() {
        mProgressView = findViewById(R.id.login_progress);
        loan_ledger_tableView = findViewById(R.id.tableView);

    }

    public class Load_Account_Ledger_Task extends AsyncTask<Void, Void, String[]> {
        Load_Account_Ledger_Task() {
        }

        DefaultHttpClient http_client;
        HttpPost http_post;
        String network_action_response;

        @Override
        protected String[] doInBackground(Void... params) {
            try {
                http_client = new DefaultHttpClient();
                http_post = new HttpPost(General_Data.SERVER_IP_ADDRESS + "/android/get_loans_ledger.php");
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
            load_account_ledger_task = null;

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
                        Toast.makeText(application_context, "No Entries...", Toast.LENGTH_LONG).show();
                    } else if (json_array.getJSONObject(0).getString("status").equals("0")) {

                        loan_ledger_entries = new ArrayList<>();

                        for (int i = 1; i < json_array.length(); i++) {
                            loan_ledger_entries.add(new Loan_Ledger_Entry(Integer.parseInt(json_array.getJSONObject(i).getString("id")), mysql_date_time_format.parse(json_array.getJSONObject(i).getString("insertion_date_time")), json_array.getJSONObject(i).getString("description"), Double.parseDouble(json_array.getJSONObject(i).getString("loan_amount")), Double.parseDouble(json_array.getJSONObject(i).getString("installment_amount"))));

                        }

                        if (loan_ledger_tableView != null) {
                            final Loan_Ledger_Table_Data_Adapter loan_ledger_table_data_adapter = new Loan_Ledger_Table_Data_Adapter(getApplicationContext(), loan_ledger_entries, loan_ledger_tableView);
                            loan_ledger_tableView.setDataAdapter(loan_ledger_table_data_adapter);

                        }
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
            load_account_ledger_task = null;
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

        loan_ledger_tableView.setVisibility(show ? View.GONE : View.VISIBLE);
        loan_ledger_tableView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                loan_ledger_tableView.setVisibility(show ? View.GONE : View.VISIBLE);
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
        getMenuInflater().inflate(R.menu.other_expense_ledger, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.menu_item_cancel) {
//            finish();
//            return true;
//        }

        if (id == R.id.menu_item_add) {
            Activity_Utils.start_activity_with_finish(this,Add_Loan.class);
//            TODO_Utils.display_TODO_no_FAB_SnackBar(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
