package caventa.ansheer.ndk.caventa.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import caventa.ansheer.ndk.caventa.commons.Activity_Utils;
import caventa.ansheer.ndk.caventa.commons.Network_Utils;
import caventa.ansheer.ndk.caventa.constants.General_Data;
import caventa.ansheer.ndk.caventa.models.Sales_Person;
import ndk.prism.common_utils.Toast_Utils;

//TODO:Other expenses and sales
//TODO:Loans
//TODO:Investments

public class Accounts extends AppCompatActivity {

    private Button button_Commissions;
    private static Context application_context;
    static List<Sales_Person> sales_persons;
    private static LinearLayout mLoginFormView;
    private static ProgressBar mProgressView;
    private Button button_Other_Expenses;
    private Button button_sales;
    private Button button_loans;
    private Button button_investments;
    private Button button_Ledger;
    static Context activity_context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accounts);
        initView();
        application_context = getApplicationContext();
        activity_context = this;
        button_Commissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (caventa.ansheer.ndk.caventa.commons.Network_Utils.isOnline(application_context)) {
                    if (load_sales_persons_commission_task != null) {
                        load_sales_persons_commission_task.cancel(true);
                        load_sales_persons_commission_task = null;
                    }
                    caventa.ansheer.ndk.caventa.commons.Network_Utils.showProgress(true, application_context, mProgressView, mLoginFormView);
                    load_sales_persons_commission_task = new Load_Sales_Persons_Commission_Task("Commisions", activity_context);
                    load_sales_persons_commission_task.execute((Void) null);
                } else {
                    Toast_Utils.longToast(getApplicationContext(), "Internet is unavailable");
                }
            }
        });

        button_Other_Expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (caventa.ansheer.ndk.caventa.commons.Network_Utils.isOnline(application_context)) {
                    Activity_Utils.start_activity_with_integer_extras(activity_context, Other_Expense_Sale_Ledger.class, new Pair[]{new Pair("position", 0)});
                } else {
                    Toast_Utils.longToast(getApplicationContext(), "Internet is unavailable");
                }
            }
        });

        button_Other_Expenses.setVisibility(View.INVISIBLE);

        button_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (caventa.ansheer.ndk.caventa.commons.Network_Utils.isOnline(application_context)) {
                    if (load_sales_persons_commission_task != null) {
                        load_sales_persons_commission_task.cancel(true);
                        load_sales_persons_commission_task = null;
                    }
                    caventa.ansheer.ndk.caventa.commons.Network_Utils.showProgress(true, application_context, mProgressView, mLoginFormView);
                    load_sales_persons_commission_task = new Load_Sales_Persons_Commission_Task("Sales Reports", activity_context);
                    load_sales_persons_commission_task.execute((Void) null);
                } else {
                    Toast_Utils.longToast(getApplicationContext(), "Internet is unavailable");
                }
            }
        });

        button_loans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (caventa.ansheer.ndk.caventa.commons.Network_Utils.isOnline(application_context)) {
                    Activity_Utils.start_activity(activity_context, Loan_Ledger.class);
                } else {
                    Toast_Utils.longToast(getApplicationContext(), "Internet is unavailable");
                }

            }
        });

        button_loans.setVisibility(View.INVISIBLE);

        button_investments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (caventa.ansheer.ndk.caventa.commons.Network_Utils.isOnline(application_context)) {
                    Activity_Utils.start_activity(activity_context, Investment_Ledger.class);
                } else {
                    Toast_Utils.longToast(getApplicationContext(), "Internet is unavailable");
                }

            }
        });

        button_investments.setVisibility(View.INVISIBLE);

        button_Ledger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (caventa.ansheer.ndk.caventa.commons.Network_Utils.isOnline(application_context)) {
                    Activity_Utils.start_activity(activity_context, Account_Ledger.class);
                } else {
                    Toast_Utils.longToast(getApplicationContext(), "Internet is unavailable");
                }

            }
        });
    }

    private void initView() {
        button_Commissions = findViewById(R.id.button_commisions);
        mLoginFormView = findViewById(R.id.email_login_form);
        mProgressView = findViewById(R.id.login_progress);
        button_Other_Expenses = findViewById(R.id.button_other_expenses);
        button_sales = findViewById(R.id.button_sales);
        button_loans = findViewById(R.id.button_loans);
        button_investments = findViewById(R.id.button_investments);
        button_Ledger = findViewById(R.id.button_ledger);
    }

    private static Load_Sales_Persons_Commission_Task load_sales_persons_commission_task = null;

    public static class Load_Sales_Persons_Commission_Task extends AsyncTask<Void, Void, String[]> {
        String task_origin;
        Context current_activity;

        Load_Sales_Persons_Commission_Task(String origin, Context current_activity) {
            this.task_origin = origin;
            this.current_activity = current_activity;
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
            load_sales_persons_commission_task = null;

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
                        Toast.makeText(application_context, "Error...", Toast.LENGTH_LONG).show();
                    } else if (json_array.getJSONObject(0).getString("status").equals("0")) {

                        sales_persons = new ArrayList<>();
                        for (int i = 1; i < json_array.length(); i++) {
                            sales_persons.add(new Sales_Person(json_array.getJSONObject(i).getString("name"), json_array.getJSONObject(i).getString("id")));


                        }
                        if (task_origin.equals("Commisions")) {

                            Activity_Utils.start_activity_with_object_push_and_integer_extras(current_activity, Commision_Page.class, new Pair[]{new Pair("position", 0)}, sales_persons.get(1));

                        } else if (task_origin.equals("Sales Reports")) {

                            Activity_Utils.start_activity_with_object_push_and_integer_extras(current_activity, Sales_Ledger.class, new Pair[]{new Pair("position", 0)}, sales_persons.get(0));
//                            Activity_Utils.start_activity(current_activity,Sales_Ledger.class);
                        }
                    }


                } catch (JSONException e) {
                    Toast.makeText(application_context, "Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Log.d(General_Data.TAG, e.getLocalizedMessage());
                }


            }


        }

        @Override
        protected void onCancelled() {
            load_sales_persons_commission_task = null;
            Network_Utils.showProgress(false, application_context, mProgressView, mLoginFormView);
        }
    }

    @Override
    public void onBackPressed() {
        Activity_Utils.start_activity_with_finish_and_tab_index(this, Dashboard_Page.class, 0);
    }
}
