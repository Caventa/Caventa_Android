package caventa.ansheer.ndk.caventa.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import caventa.ansheer.ndk.caventa.R;
import caventa.ansheer.ndk.caventa.commons.Activity_Utils;
import caventa.ansheer.ndk.caventa.commons.Network_Utils;
import caventa.ansheer.ndk.caventa.commons.Validation_Utils;
import caventa.ansheer.ndk.caventa.constants.General_Data;
import ndk.prism.common_utils.Date_Picker_Utils;
import ndk.prism.common_utils.Date_Utils;
import ndk.prism.common_utils.Toast_Utils;

import static caventa.ansheer.ndk.caventa.commons.Network_Utils.perform_http_client_network_task;
import static caventa.ansheer.ndk.caventa.commons.Network_Utils.showProgress;
import static caventa.ansheer.ndk.caventa.commons.Validation_Utils.non_empty_check;

public class Add_Investment extends AppCompatActivity {

    private Calendar calendar = Calendar.getInstance();
    private EditText txt_description;
    private EditText txt_amount;

    private View mProgressView;
    private View mLoginFormView;

    private Button button_submit;

    Context application_context, activity_context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_investment);

        application_context = getApplicationContext();

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        ImageView pick_date = findViewById(R.id.show_calendar);
        final TextView txt_date = findViewById(R.id.work_date);

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
                Date_Picker_Utils.show_date_picker(Add_Investment.this, date, calendar);
            }
        });

        txt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date_Picker_Utils.show_date_picker(Add_Investment.this, date, calendar);
            }
        });

        initView();

//        Spinner spinner_category = findViewById(R.id.spinner_category);
//        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int item_position, long id) {
//                category = item_position;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//
//
//        });
//
//        Spinner_Utils.attach_items_to_simple_spinner(application_context, spinner_category, new ArrayList<>(Arrays.asList("Other Expenses", "Other Sales")));

    }

    private void initView() {
        txt_description = findViewById(R.id.description);
        txt_amount = findViewById(R.id.amount);
        button_submit = findViewById(R.id.button_submit);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_work, menu);
        return true;
    }

    static MenuItem save_menu_item;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_item_save) {
            save_menu_item = item;
            attempt_investment_save();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void attempt_investment_save() {
        if (investment_save_task != null) {
            investment_save_task.cancel(true);
            investment_save_task = null;
        }

        Validation_Utils.reset_errors(new EditText[]{txt_description, txt_amount});

        Pair<Boolean, EditText> empty_check_result = Validation_Utils.empty_check(new Pair[]{new Pair(txt_description, "Please Enter Investment Name..."),
                new Pair(txt_amount, "Please Enter Investment Amount...")});

        if (empty_check_result.first) {
            // There was an error; don't attempt login and focus the first form field with an error.
            empty_check_result.second.requestFocus();
        } else {

            Pair<Boolean, EditText> zero_check_result = Validation_Utils.zero_check(new Pair[]{new Pair(txt_amount, "Please Enter Valid Investment Amount...")});
            if (zero_check_result.first) {
                zero_check_result.second.requestFocus();
            } else {
                execute_other_expense_sale_save_task();
            }
        }
    }

    private void execute_other_expense_sale_save_task() {
        if (Network_Utils.isOnline(application_context)) {
            showProgress(true, application_context, mProgressView, mLoginFormView);
            investment_save_task = new Investment_Save_Task(txt_description.getText().toString(), Double.parseDouble(txt_amount.getText().toString()), this);
            save_menu_item.setEnabled(false);
            investment_save_task.execute((Void) null);
        } else {
            Toast_Utils.longToast(getApplicationContext(), "Internet is unavailable");
        }
    }

    /* Keep track of the login task to ensure we can cancel it if requested. */
    private Investment_Save_Task investment_save_task = null;

    /* Represents an asynchronous login task used to authenticate the user. */
    public class Investment_Save_Task extends AsyncTask<Void, Void, String[]> {

        String task_investment_description;
        Double task_investment_amount;
        AppCompatActivity current_activity;

        Investment_Save_Task(String investment_description, Double investment_amount, AppCompatActivity current_activity) {
            task_investment_description = investment_description;
            task_investment_amount = investment_amount;
            this.current_activity = current_activity;
        }


        @Override
        protected String[] doInBackground(Void... params) {
            Log.d(General_Data.TAG, task_investment_description + "," + task_investment_amount);

            return perform_http_client_network_task(General_Data.SERVER_IP_ADDRESS + "/android/add_investment.php", new Pair[]{new Pair("description", task_investment_description), new Pair("amount", String.valueOf(task_investment_amount))});
        }

        @Override
        protected void onPostExecute(final String[] network_action_response_array) {
            investment_save_task = null;

            showProgress(false, application_context, mProgressView, mLoginFormView);

            handle_json_insertion_response_and_switch_with_finish(network_action_response_array, current_activity, application_context, txt_description);

        }

        @Override
        protected void onCancelled() {
            investment_save_task = null;
            showProgress(false, application_context, mProgressView, mLoginFormView);
            save_menu_item.setEnabled(true);
        }
    }

    public static void handle_json_insertion_response_and_switch_with_finish(String[] network_action_response_array, AppCompatActivity current_activity, Context context, View view_to_focus_on_error) {
        Log.d(General_Data.TAG, network_action_response_array[0]);
        Log.d(General_Data.TAG, network_action_response_array[1]);

        if (network_action_response_array[0].equals("1")) {
            Toast.makeText(context, "Error : " + network_action_response_array[1], Toast.LENGTH_LONG).show();
            Log.d(General_Data.TAG, network_action_response_array[1]);
        } else {

            try {
                JSONObject json = new JSONObject(network_action_response_array[1]);
                String response_code = json.getString("status");
                switch (response_code) {
                    case "0":
                        Toast.makeText(context, "OK", Toast.LENGTH_LONG).show();
                        Activity_Utils.start_activity_with_finish(current_activity, Investment_Ledger.class);
                        break;
                    case "1":
                        Toast.makeText(context, "Error : " + json.getString("error"), Toast.LENGTH_LONG).show();
                        view_to_focus_on_error.requestFocus();
                        break;
                    default:
                        Toast.makeText(context, "Error : Check json", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                Toast.makeText(context, "Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.d(General_Data.TAG, e.getLocalizedMessage());
            }
        }
        save_menu_item.setEnabled(true);
    }


    @Override
    public void onBackPressed() {
        if (form_check()) {
            show_uncancelled_yes_no_confirmation_for_unsaved_data_and_finish_on_yes(this);
        } else {
            Activity_Utils.start_activity_with_finish(this, Investment_Ledger.class);
        }

    }

    public boolean form_check() {
        return non_empty_check(new EditText[]{txt_description, txt_amount});
    }

    public void show_uncancelled_yes_no_confirmation_for_unsaved_data_and_finish_on_yes(final Context context) {
        AlertDialog.Builder delete_confirmation_dialog = new AlertDialog.Builder(context);
        delete_confirmation_dialog.setMessage("Unsaved data will be lost! Continue? ");
        delete_confirmation_dialog.setCancelable(false);
        delete_confirmation_dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                Activity_Utils.start_activity_with_finish(activity_context, Investment_Ledger.class);
            }
        });
        delete_confirmation_dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = delete_confirmation_dialog.create();
        alert.setTitle("Warning!");
        alert.show();
    }

}
