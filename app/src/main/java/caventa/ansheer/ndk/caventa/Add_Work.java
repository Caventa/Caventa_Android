package caventa.ansheer.ndk.caventa;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

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
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import caventa.ansheer.ndk.caventa.models.Work_Advance;
import caventa.ansheer.ndk.caventa.models.Work_Expense;
import ndk.prism.common_utils.Date_Picker_Utils;
import ndk.prism.common_utils.Date_Utils;
import ndk.prism.common_utils.Toast_Utils;

public class Add_Work extends AppCompatActivity {

    private Work_Advances_Adapter work_advances_adapter;
//    private Work_Advances_Sugar_Adapter work_advances_sugar_adapter;

    private Work_Expense_Adapter work_expenses_adapter;
//    private Work_Expenses_Sugar_Adapter work_expenses_sugar_adapter;

    private List<Work_Advance> work_advances;
//    List<Work_Advance_Sugar> work_advance_sugars;

    private List<Work_Expense> work_expenses;
//    List<Work_Expense_Sugar> work_expense_sugars;

    private RecyclerView work_expenses_recycler_view;
    private RecyclerView work_advances_recycler_view;

    TextView txt_total_advance, txt_total_expense, txt_total_profit;

    double total_advance = 0, total_expense = 0, total_profit = 0;
    private Calendar calendar;
    private SimpleDateFormat sdf;
    private EditText txt_name;
    private EditText txt_address;

    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_work);
        setTitle("New Work");
        application_context = getApplicationContext();
        settings = getApplicationContext().getSharedPreferences(General_Data.SHARED_PREFERENCE,
                Context.MODE_PRIVATE);
        txt_total_advance = findViewById(R.id.total_advance);
        txt_total_expense = findViewById(R.id.total_expense);
        txt_total_profit = findViewById(R.id.total_profit);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        work_advances = new ArrayList<>();
        work_advances_adapter = new Work_Advances_Adapter(this, work_advances);
//        work_advance_sugars =new ArrayList<>();
//        fill_work_advances_demo_Database();
//        work_advances_sugar_adapter = new Work_Advances_Sugar_Adapter(this, work_advance_sugars);

        work_advances_recycler_view = (RecyclerView) findViewById(R.id.recycler_view_advance);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        work_advances_recycler_view.setHasFixedSize(false);

        // use a linear layout manager
        LinearLayoutManager work_advances_recycler_view_layout_manager = new LinearLayoutManager(this);
        work_advances_recycler_view.setLayoutManager(work_advances_recycler_view_layout_manager);

//        fill_work_advances_demo_Data();

        // specify an work_advances_adapter (see also next example)

        work_advances_recycler_view.setAdapter(work_advances_adapter);
//        work_advances_recycler_view.setAdapter(work_advances_sugar_adapter);


        work_expenses = new ArrayList<>();
        work_expenses_adapter = new Work_Expense_Adapter(this, work_expenses);
//        work_expense_sugars =new ArrayList<>();
//        fill_work_expenses_demo_Database();
//        work_expenses_sugar_adapter = new Work_Expenses_Sugar_Adapter(this, work_expense_sugars);


        work_expenses_recycler_view = (RecyclerView) findViewById(R.id.recycler_view_expense);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        work_expenses_recycler_view.setHasFixedSize(false);

        // use a linear layout manager
        LinearLayoutManager work_expenses_recycler_view_layout_manager = new LinearLayoutManager(this);
        work_expenses_recycler_view.setLayoutManager(work_expenses_recycler_view_layout_manager);

//        fill_work_expenses_demo_Data();

        // specify an work_advances_adapter (see also next example)

        work_expenses_recycler_view.setAdapter(work_expenses_adapter);
//        work_expenses_recycler_view.setAdapter(work_expenses_sugar_adapter);

        ImageView pick_date = findViewById(R.id.show_calendar);
        final TextView txt_date = findViewById(R.id.work_date);

        calendar = Calendar.getInstance();
//        sdf = Date_Utils.normal_Date_Format_words;
        txt_date.setText(Date_Utils.normal_Date_Format_words.format(calendar.getTime()));
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                txt_date.setText(Date_Utils.normal_Date_Format_words.format(calendar.getTime()));
            }
        };

        pick_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date_Picker_Utils.show_date_picker(Add_Work.this, date, calendar);
            }
        });

        txt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date_Picker_Utils.show_date_picker(Add_Work.this, date, calendar);
            }
        });

        ImageView add_advance_button = findViewById(R.id.add_advance);
        add_advance_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wrapInScrollView = false;
                new MaterialDialog.Builder(Add_Work.this)
                        .title("Advance+")
                        .customView(R.layout.add_advance, wrapInScrollView)
                        .positiveText("Submit")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                Work_Advance_Sugar work_advance_sugar = new Work_Advance_Sugar();
//                                work_advance_sugar.setId(2);
//                                work_advance_sugar.setAmount(100.0);
//                                work_advance_sugar.setDescription("Advance");
//                                work_advance_sugar.setWork_id(1);
//                                work_advance_sugar.setInsertion_date_time("2017-10-12 11:25:22");
//                                work_advance_sugar.save();
//
//                                work_advance_sugars = Select.from(Work_Advance_Sugar.class)
//                                        .where(Condition.prop("id").eq(1),Condition.prop("id").eq(2))
//                                        .list();
//                                work_advances_sugar_adapter.notifyDataSetChanged();
//                                work_advances_sugar_adapter = new Work_Advances_Sugar_Adapter(Add_Work.this, work_advance_sugars);
//                                work_advances_recycler_view.setAdapter(work_advances_sugar_adapter);

//                                String name = ((EditText)dialog.findViewById(R.id.address)).getText().toString();
//                                String number = ((EditText)dialog.findViewById(R.id.name)).getText().toString();

                                Work_Advance work_advance = new Work_Advance(Double.parseDouble(((EditText) dialog.findViewById(R.id.name)).getText().toString()), ((EditText) dialog.findViewById(R.id.address)).getText().toString());
                                work_advances.add(work_advance);

                                total_advance = total_advance + Double.parseDouble(((EditText) dialog.findViewById(R.id.name)).getText().toString());
                                txt_total_advance.setText("Advances : " + total_advance);

                                calculate_total_profit();

                                work_advances_adapter.notifyDataSetChanged();

                                Log.d(General_Data.TAG, String.valueOf(work_advances.size()));
                                Log.d(General_Data.TAG, Arrays.toString(work_advances.toArray()));
                            }

                        })
                        .show();
            }
        });
        ImageView add_expense_button = findViewById(R.id.add_expense);
        add_expense_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wrapInScrollView = false;
                new MaterialDialog.Builder(Add_Work.this)
                        .title("Expense+")
                        .customView(R.layout.add_advance, wrapInScrollView)
                        .positiveText("Submit")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                Work_Expense_Sugar work_expense_sugar = new Work_Expense_Sugar();
//                                work_expense_sugar.setId(2);
//                                work_expense_sugar.setAmount(100.0);
//                                work_expense_sugar.setDescription("Expense");
//                                work_expense_sugar.setWork_id(1);
//                                work_expense_sugar.setInsertion_date_time("2017-10-12 11:25:22");
//                                work_expense_sugar.save();
//
//                                work_expense_sugars = Select.from(Work_Expense_Sugar.class)
//                                        .where(Condition.prop("id").eq(1),Condition.prop("id").eq(2))
//                                        .list();
//
//                                work_expenses_sugar_adapter.notifyDataSetChanged();
//                                work_expenses_sugar_adapter = new Work_Expenses_Sugar_Adapter(Add_Work.this, work_expense_sugars);
//                                work_expenses_recycler_view.setAdapter(work_expenses_sugar_adapter);

                                Work_Expense work_expense = new Work_Expense(Double.parseDouble(((EditText) dialog.findViewById(R.id.name)).getText().toString()), ((EditText) dialog.findViewById(R.id.address)).getText().toString());
                                work_expenses.add(work_expense);

                                total_expense = total_expense + Double.parseDouble(((EditText) dialog.findViewById(R.id.name)).getText().toString());
                                txt_total_expense.setText("Expenses : " + total_expense);

                                calculate_total_profit();

                                work_expenses_adapter.notifyDataSetChanged();


                            }

                        })
                        .show();
            }
        });


        work_advances_recycler_view.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), work_advances_recycler_view, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.d(General_Data.TAG, String.valueOf(position));
                Log.d(General_Data.TAG, String.valueOf(work_advances.size()));
                Log.d(General_Data.TAG, Arrays.toString(work_advances.toArray()));

//                Toast_Utils.longToast(getApplicationContext(),view.toString());
//                ImageView delete_icon=(ImageView)view.findViewById(R.id.delete_icon);
//                Toast_Utils.longToast(getApplicationContext(),delete_icon.getContentDescription().toString());
//                if (((ImageView) view.findViewById(R.id.delete_icon)).getContentDescription().toString().equals("delete_icon")) {
                work_advances.remove(position);

                String description_amount = ((TextView) view.findViewById(R.id.description_amount)).getText().toString();
                total_advance = total_advance - Double.parseDouble(description_amount.substring(description_amount.indexOf("-") + 1));
                txt_total_advance.setText("Advances : " + total_advance);

                calculate_total_profit();

                work_advances_adapter.notifyDataSetChanged();
//                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        work_expenses_recycler_view.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), work_advances_recycler_view, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
//                Toast_Utils.longToast(getApplicationContext(), view.toString());
//                ImageView delete_icon = (ImageView) view.findViewById(R.id.delete_icon);
//                Toast_Utils.longToast(getApplicationContext(), delete_icon.getContentDescription().toString());
//                if (((ImageView) view.findViewById(R.id.delete_icon)).getContentDescription().toString().equals("delete_icon")) {
                work_expenses.remove(position);


                String description_amount = ((TextView) view.findViewById(R.id.description_amount)).getText().toString();

                total_expense = total_expense - Double.parseDouble(description_amount.substring(description_amount.indexOf("-") + 1));

                txt_total_expense.setText("Expenses : " + total_expense);
                calculate_total_profit();

                work_expenses_adapter.notifyDataSetChanged();
//                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        initView();
    }

    private void calculate_total_profit() {
        total_profit = total_advance - total_expense;
        txt_total_profit.setText("Profit : " + total_profit);
    }

//    private void fill_work_expenses_demo_Database() {
//        Work_Expense_Sugar work_expense_sugar = new Work_Expense_Sugar();
//        work_expense_sugar.setId(1);
//        work_expense_sugar.setAmount(100.0);
//        work_expense_sugar.setDescription("Expense 1");
//        work_expense_sugar.setWork_id(1);
//        work_expense_sugar.setInsertion_date_time("2017-10-12 11:25:22");
//        work_expense_sugar.save();
//
//        work_expense_sugars = Select.from(Work_Expense_Sugar.class)
//                .where(Condition.prop("id").eq(1))
//                .list();
//    }
//
//    private void fill_work_advances_demo_Database() {
//
//        Work_Advance_Sugar work_advance_sugar = new Work_Advance_Sugar();
//        work_advance_sugar.setId(1);
//        work_advance_sugar.setAmount(100.0);
//        work_advance_sugar.setDescription("Advance 1");
//        work_advance_sugar.setWork_id(1);
//        work_advance_sugar.setInsertion_date_time("2017-10-12 11:25:22");
//        work_advance_sugar.save();
//
//        work_advance_sugars = Select.from(Work_Advance_Sugar.class)
//                .where(Condition.prop("id").eq(1))
//                .list();
//        Log.d(General_Data.TAG, String.valueOf(work_advance_sugars.size()));
//        work_advances_sugar_adapter.notifyDataSetChanged();
//    }

//    private void fill_work_expenses_demo_Data() {
//        Work_Expense work_expense = new Work_Expense(1, 1, 10.5, "Test Expense", "2017-10-05 05:20:23");
//        work_expenses.add(work_expense);
//        work_expense = new Work_Expense(2, 1, 12.5, "Test Expense 2", "2017-10-05 05:22:23");
//        work_expenses.add(work_expense);
//
//        work_expenses_adapter.notifyDataSetChanged();
//    }
//
//    private void fill_work_advances_demo_Data() {
//        Work_Advance work_advance = new Work_Advance(1, 1, 10.5, "Test Advance", "2017-10-05 05:20:23");
//        work_advances.add(work_advance);
//
//        work_advance = new Work_Advance(2, 1, 15.5, "Test Advance 2", "2017-10-05 05:22:23");
//        work_advances.add(work_advance);
//
//        work_advances_adapter.notifyDataSetChanged();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_work, menu);
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
            this.finish();
            return true;
        }

        if (id == R.id.menu_item_save) {
//            this.finish();
//            Toast_Utils.longToast(getApplicationContext(), "Success...");
            attempt_work_save();
//            this.finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    /* Keep track of the login task to ensure we can cancel it if requested. */
    private Work_Save_Task mAuthTask = null;

    private void initView() {
        txt_name = findViewById(R.id.name);
        txt_address = findViewById(R.id.address);
    }

    /* Represents an asynchronous login task used to authenticate the user. */
    public class Work_Save_Task extends AsyncTask<Void, Void, String[]> {

        String task_work_name, task_work_address, task_advances_json, task_expenses_json;
        Date task_work_date;
        int task_sales_person_id;

        Work_Save_Task(String work_name, String work_address, String advances_json, String expenses_json,
                       Date work_date,
                       int sales_person_id
        ) {
            task_work_name = work_name;
            task_work_address = work_address;
            task_advances_json = advances_json;
            task_expenses_json = expenses_json;
            task_work_date = work_date;
            task_sales_person_id = sales_person_id;
        }

        DefaultHttpClient http_client;
        HttpPost http_post;
        ArrayList<NameValuePair> name_pair_value;
        String network_action_response;

        @Override
        protected String[] doInBackground(Void... params) {
            try {
                http_client = new DefaultHttpClient();
                http_post = new HttpPost("http://" + General_Data.SERVER_IP_ADDRESS + "/android/add_work.php");
                name_pair_value = new ArrayList<NameValuePair>(6);
                name_pair_value.add(new BasicNameValuePair("work_name", task_work_name));
                name_pair_value.add(new BasicNameValuePair("work_address", task_work_address));
                name_pair_value.add(new BasicNameValuePair("work_date", Date_Utils.mysql_Date_Format.format(task_work_date)));
                name_pair_value.add(new BasicNameValuePair("sales_person_id", String.valueOf(task_sales_person_id)));
                name_pair_value.add(new BasicNameValuePair("advances_json", task_advances_json));
                name_pair_value.add(new BasicNameValuePair("expenses_json", task_expenses_json));

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
            mAuthTask = null;

            showProgress(false);

            Log.d(General_Data.TAG, network_action_response_array[0]);
            Log.d(General_Data.TAG, network_action_response_array[1]);

            if (network_action_response_array[0].equals("1")) {
                Toast.makeText(Add_Work.this, "Error : " + network_action_response_array[1], Toast.LENGTH_LONG).show();
                Log.d(General_Data.TAG, network_action_response_array[1]);
            } else {

                try {
                    JSONObject json = new JSONObject(network_action_response_array[1]);
                    String count = json.getString("status");
                    switch (count) {
                        case "0":
                            Toast.makeText(application_context, "OK", Toast.LENGTH_LONG).show();
//                            Intent i = new Intent(Agent_Addition.this, Agent_Addition.class);
//                            startActivity(i);
//                            finish();
                            break;
                        case "1":
                            Toast.makeText(application_context, "Error : " + json.getString("error"), Toast.LENGTH_LONG).show();
                            txt_name.requestFocus();
                            break;
                        default:
                            Toast.makeText(application_context, "Error : Check json", Toast.LENGTH_LONG).show();
                    }

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


    private void attempt_work_save() {
        if (mAuthTask != null) {
            return;
        }

        reset_errors(new EditText[]{txt_name, txt_address});

        // Reset errors.
//        username.setError(null);
//        passcode.setError(null);

        // Store values at the time of the login attempt.
//        String entered_username = username.getText().toString();
//        String entered_passcode = passcode.getText().toString();

//        Pair<Integer, String> simplePair = new Pair<>(42, "Second");
//        empty_check(new Pair<>(txt_name, "Please Enter Work Name..."));
//        empty_check(new Pair[] {    new Pair(txt_name, "Please Enter Work Name..."),
//                                    new Pair(txt_address, "Please Enter Work Address...")});

//        boolean cancel = false;
//        View error_focus_View = null;
//
//        // Check for a valid password, if the user entered one.
//        if (TextUtils.isEmpty(entered_passcode)) {
//            passcode.setError("Please enter passcode");
//            error_focus_View = passcode;
//            cancel = true;
//        }
//
//        // Check for a valid email address.
//        if (TextUtils.isEmpty(entered_username)) {
//            username.setError("Please enter username");
//            error_focus_View = username;
//            cancel = true;
//        }
        Pair<Boolean, EditText> empty_check_result = empty_check(new Pair[]{new Pair(txt_name, "Please Enter Work Name..."),
                new Pair(txt_address, "Please Enter Work Address...")});

        if (empty_check_result.first) {
            // There was an error; don't attempt login and focus the first form field with an error.
            empty_check_result.second.requestFocus();
        } else {

//            InputMethodManager inputManager =
//                    (InputMethodManager) getApplicationContext().
//                            getSystemService(Context.INPUT_METHOD_SERVICE);
//            inputManager.hideSoftInputFromWindow(
//                    this.getCurrentFocus().getWindowToken(),
//                    InputMethodManager.HIDE_NOT_ALWAYS);

            // Show a progress spinner, and kick off a background task to perform the user login attempt.
            if (isOnline()) {
                showProgress(true);
                mAuthTask = new Work_Save_Task(txt_name.getText().toString(), txt_address.getText().toString(), generate_advances_json(), generate_expenses_json(), calendar.getTime(), settings.getInt("sales_person", 0));
                mAuthTask.execute((Void) null);
            } else {
                Toast_Utils.longToast(getApplicationContext(), "Internet is unavailable");
            }
        }
    }

    private String generate_expenses_json() {

        JSONArray mJSONArray = new JSONArray();
        for (int i = 0; i < work_expenses.size(); i++) {
//            TableRow rw = (TableRow) table_tickets.getChildAt(i);
//
//            if (((ColorDrawable) rw.getBackground()).getColor() == GREEN) {
            JSONObject json_obj = new JSONObject();
            try {
                json_obj.put("description", work_expenses.get(i).getDescription());
//                    String cnt = ((TextView) rw.getChildAt(1)).getText().toString();
//                    json_obj.put("serial", cnt.substring(0, cnt.indexOf("-")).trim());
//                    json_obj.put("count", cnt.substring(cnt.indexOf("-") + 1).trim());
                json_obj.put("amount", work_expenses.get(i).getAmount());
                mJSONArray.put(json_obj);
            } catch (JSONException e) {
                Toast.makeText(application_context, "Error : Check json", Toast.LENGTH_LONG).show();
            }
//            }
        }
        Log.d(General_Data.TAG, mJSONArray.toString());
        return mJSONArray.toString();

    }

    private String generate_advances_json() {


        JSONArray mJSONArray = new JSONArray();
        for (int i = 0; i < work_advances.size(); i++) {
//            TableRow rw = (TableRow) table_tickets.getChildAt(i);
//
//            if (((ColorDrawable) rw.getBackground()).getColor() == GREEN) {
            JSONObject json_obj = new JSONObject();
            try {
                json_obj.put("description", work_advances.get(i).getDescription());
//                    String cnt = ((TextView) rw.getChildAt(1)).getText().toString();
//                    json_obj.put("serial", cnt.substring(0, cnt.indexOf("-")).trim());
//                    json_obj.put("count", cnt.substring(cnt.indexOf("-") + 1).trim());
                json_obj.put("amount", work_advances.get(i).getAmount());
                mJSONArray.put(json_obj);
            } catch (JSONException e) {
                Toast.makeText(application_context, "Error : Check json", Toast.LENGTH_LONG).show();
            }
//            }
        }
        Log.d(General_Data.TAG, mJSONArray.toString());
        return mJSONArray.toString();
    }

    Context application_context;
    private SharedPreferences settings;

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


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private Pair<Boolean, EditText> empty_check(Pair[] editText_Error_Pairs) {
        for (Pair<EditText, String> editText_Error_Pair : editText_Error_Pairs) {
            if (TextUtils.isEmpty(editText_Error_Pair.first.getText().toString())) {
                editText_Error_Pair.first.setError(editText_Error_Pair.second);
                return new Pair<>(true, editText_Error_Pair.first);
            }
        }
        return new Pair<>(false, null);
    }

    private void reset_errors(EditText[] edit_texts) {
        for (EditText edit_text : edit_texts) {
            edit_text.setError(null);
        }
    }

}
