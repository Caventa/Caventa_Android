package caventa.ansheer.ndk.caventa.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import caventa.ansheer.ndk.caventa.R;
import caventa.ansheer.ndk.caventa.adapters.Work_Advances_Adapter;
import caventa.ansheer.ndk.caventa.adapters.Work_Expense_Adapter;
import caventa.ansheer.ndk.caventa.commons.Activity_Utils;
import caventa.ansheer.ndk.caventa.commons.RecyclerTouchListener;
import caventa.ansheer.ndk.caventa.constants.General_Data;
import caventa.ansheer.ndk.caventa.models.Work_Advance;
import caventa.ansheer.ndk.caventa.models.Work_Expense;
import ndk.prism.common_utils.Date_Picker_Utils;
import ndk.prism.common_utils.Date_Utils;
import ndk.prism.common_utils.Toast_Utils;

import static caventa.ansheer.ndk.caventa.commons.Network_Utils.isOnline;
import static caventa.ansheer.ndk.caventa.commons.Network_Utils.perform_http_client_network_task;
import static caventa.ansheer.ndk.caventa.commons.Network_Utils.showProgress;
import static caventa.ansheer.ndk.caventa.commons.Validation_Utils.empty_check;
import static caventa.ansheer.ndk.caventa.commons.Validation_Utils.non_empty_check;
import static caventa.ansheer.ndk.caventa.commons.Validation_Utils.reset_errors;
import static caventa.ansheer.ndk.caventa.commons.Validation_Utils.zero_check;

public class Add_Work extends AppCompatActivity {

    private Work_Advances_Adapter work_advances_adapter;
    private Work_Expense_Adapter work_expenses_adapter;

    private List<Work_Advance> work_advances;
    private List<Work_Expense> work_expenses;

    TextView txt_total_advance, txt_total_expense, txt_total_profit;

    double total_advance = 0, total_expense = 0, total_profit = 0;
    private Calendar calendar;
    private EditText txt_name;
    private EditText txt_address;

    private View mProgressView;
    private View mLoginFormView;
    private SharedPreferences settings;
    static Context activity_context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_work);
        setTitle("New Work");
        application_context = getApplicationContext();
        activity_context = this;
        settings = getApplicationContext().getSharedPreferences(General_Data.SHARED_PREFERENCE,Context.MODE_PRIVATE);
        txt_total_advance = findViewById(R.id.total_advance);
        txt_total_expense = findViewById(R.id.total_expense);
        txt_total_profit = findViewById(R.id.total_profit);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        work_advances = new ArrayList<>();
        work_advances_adapter = new Work_Advances_Adapter(this, work_advances);

        RecyclerView work_advances_recycler_view = findViewById(R.id.recycler_view_advance);

        work_advances_recycler_view.setHasFixedSize(false);

        // use a linear layout manager
        final LinearLayoutManager work_advances_recycler_view_layout_manager = new LinearLayoutManager(this);
        work_advances_recycler_view.setLayoutManager(work_advances_recycler_view_layout_manager);

        work_advances_recycler_view.setAdapter(work_advances_adapter);

        work_expenses = new ArrayList<>();
        work_expenses_adapter = new Work_Expense_Adapter(this, work_expenses);

        RecyclerView work_expenses_recycler_view = findViewById(R.id.recycler_view_expense);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        work_expenses_recycler_view.setHasFixedSize(false);

        // use a linear layout manager
        LinearLayoutManager work_expenses_recycler_view_layout_manager = new LinearLayoutManager(this);
        work_expenses_recycler_view.setLayoutManager(work_expenses_recycler_view_layout_manager);

        work_expenses_recycler_view.setAdapter(work_expenses_adapter);

        ImageView pick_date = findViewById(R.id.show_calendar);
        final TextView txt_date = findViewById(R.id.work_date);

        calendar = Calendar.getInstance();
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

                                Pair<Boolean, EditText> empty_check_result = empty_check(new Pair[]{new Pair((EditText) dialog.findViewById(R.id.name), "Please Enter Advance Description..."),
                                        new Pair((EditText) dialog.findViewById(R.id.name), "Please Enter Advance Amount...")});

                                if (empty_check_result.first) {
                                    // There was an error; don't attempt login and focus the first form field with an error.
                                    empty_check_result.second.requestFocus();
                                } else {


                                    Work_Advance work_advance = new Work_Advance(Double.parseDouble(((EditText) dialog.findViewById(R.id.name)).getText().toString()), ((EditText) dialog.findViewById(R.id.address)).getText().toString());
                                    work_advances.add(work_advance);

                                    total_advance = total_advance + Double.parseDouble(((EditText) dialog.findViewById(R.id.name)).getText().toString());
                                    txt_total_advance.setText("Advances : " + total_advance);

                                    recalculate_total_profit();

                                    work_advances_adapter.notifyDataSetChanged();
                                }

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
                                Pair<Boolean, EditText> empty_check_result = empty_check(new Pair[]{new Pair((EditText) dialog.findViewById(R.id.name), "Please Enter Advance Description..."),
                                        new Pair((EditText) dialog.findViewById(R.id.name), "Please Enter Advance Amount...")});

                                if (empty_check_result.first) {
                                    // There was an error; don't attempt login and focus the first form field with an error.
                                    empty_check_result.second.requestFocus();
                                } else {

                                    Work_Expense work_expense = new Work_Expense(Double.parseDouble(((EditText) dialog.findViewById(R.id.name)).getText().toString()), ((EditText) dialog.findViewById(R.id.address)).getText().toString());
                                    work_expenses.add(work_expense);

                                    total_expense = total_expense + Double.parseDouble(((EditText) dialog.findViewById(R.id.name)).getText().toString());
                                    txt_total_expense.setText("Expenses : " + total_expense);

                                    recalculate_total_profit();

                                    work_expenses_adapter.notifyDataSetChanged();
                                }
                            }

                        })
                        .show();
            }
        });


        work_advances_recycler_view.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), work_advances_recycler_view, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                show_uncancelled_yes_no_confirmation_dialogue_for_advance("Do You Want to Delete Advance - " + work_advances.get(position).getDescription() + " : " + work_advances.get(position).getAmount(), "Warning!", position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        work_expenses_recycler_view.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), work_advances_recycler_view, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                show_uncancelled_yes_no_confirmation_dialogue_for_expense("Do You Want to Delete Expense - " + work_expenses.get(position).getDescription() + " : " + work_expenses.get(position).getAmount(), "Warning!", position);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        initView();
    }


    void show_uncancelled_yes_no_confirmation_dialogue_for_expense(String message, String title, final int position) {
        AlertDialog.Builder delete_confirmation_dialog = new AlertDialog.Builder(this);
        delete_confirmation_dialog.setMessage(message);
        delete_confirmation_dialog.setCancelable(false);
        delete_confirmation_dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                total_expense = total_expense - work_expenses.get(position).getAmount();
                redraw_total_expense();
                work_expenses.remove(position);
                work_expenses_adapter.notifyDataSetChanged();
            }
        });
        delete_confirmation_dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = delete_confirmation_dialog.create();
        alert.setTitle(title);
        alert.show();
    }

    void show_uncancelled_yes_no_confirmation_dialogue_for_advance(String message, String title, final int position) {
        AlertDialog.Builder delete_confirmation_dialog = new AlertDialog.Builder(this);
        delete_confirmation_dialog.setMessage(message);
        delete_confirmation_dialog.setCancelable(false);
        delete_confirmation_dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                total_advance = total_advance - work_advances.get(position).getAmount();
                redraw_total_advance();
                work_advances.remove(position);
                work_advances_adapter.notifyDataSetChanged();
            }
        });
        delete_confirmation_dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = delete_confirmation_dialog.create();
        alert.setTitle(title);
        alert.show();
    }

    void redraw_total_expense() {
        txt_total_expense.setText("Expenses : " + total_expense);
        recalculate_total_profit();
    }

    void redraw_total_advance() {
        txt_total_advance.setText("Advances : " + total_advance);
        recalculate_total_profit();
    }

    void recalculate_total_profit() {
        total_profit = total_advance - total_expense;
        txt_total_profit.setText("Profit : " + total_profit);
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
            save_menu_item=item;
            attempt_work_save();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void show_uncancelled_yes_no_confirmation_for_unsaved_data_and_finish_on_yes(final Context context) {
        AlertDialog.Builder delete_confirmation_dialog = new AlertDialog.Builder(context);
        delete_confirmation_dialog.setMessage("Unsaved data will be lost! Continue? ");
        delete_confirmation_dialog.setCancelable(false);
        delete_confirmation_dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                Activity_Utils.start_activity_with_finish_and_tab_index(activity_context,Sales_Person_Dashboard_Page.class,0);
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

    @Override
    public void onBackPressed() {
        if (form_check()) {
            show_uncancelled_yes_no_confirmation_for_unsaved_data_and_finish_on_yes(this);
        } else {
            Activity_Utils.start_activity_with_finish_and_tab_index(activity_context,Sales_Person_Dashboard_Page.class,0);
        }
    }


    void show_uncancelled_yes_no_confirmation_dialogue_for_no_advances() {
        AlertDialog.Builder delete_confirmation_dialog = new AlertDialog.Builder(this);
        delete_confirmation_dialog.setMessage("There is no advances! Continue? ");
        delete_confirmation_dialog.setCancelable(false);
        delete_confirmation_dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                execute_work_save_task();

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

    void show_uncancelled_yes_no_confirmation_dialogue_for_unprofitable() {
        AlertDialog.Builder delete_confirmation_dialog = new AlertDialog.Builder(this);
        delete_confirmation_dialog.setMessage("Greater expense! Continue? ");
        delete_confirmation_dialog.setCancelable(false);
        delete_confirmation_dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                execute_work_save_task();
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

    private void execute_work_save_task() {
        // Show a progress spinner, and kick off a background task to perform the user login attempt.
        if (isOnline(application_context)) {
            showProgress(true,application_context,mProgressView,mLoginFormView);
            mAuthTask = new Work_Save_Task(txt_name.getText().toString(), txt_address.getText().toString(), generate_advances_json(), generate_expenses_json(), calendar.getTime(), settings.getInt("sales_person_id", 0), this);
            save_menu_item.setEnabled(false);
            mAuthTask.execute((Void) null);
        } else {
            Toast_Utils.longToast(getApplicationContext(), "Internet is unavailable");
        }
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
        AppCompatActivity current_activity;

        Work_Save_Task(String work_name, String work_address, String advances_json, String expenses_json, Date work_date, int sales_person_id, AppCompatActivity current_activity) {
            task_work_name = work_name;
            task_work_address = work_address;
            task_advances_json = advances_json;
            task_expenses_json = expenses_json;
            task_work_date = work_date;
            task_sales_person_id = sales_person_id;
            this.current_activity = current_activity;
        }


        @Override
        protected String[] doInBackground(Void... params) {
            Log.d(General_Data.TAG,task_work_name+","+task_work_address+","+ caventa.ansheer.ndk.caventa.commons.Date_Utils.mysql_date_time_format.format(task_work_date)+","+task_sales_person_id+","+task_advances_json+","+task_expenses_json);
            return perform_http_client_network_task(General_Data.SERVER_IP_ADDRESS + "/android/add_work.php", new Pair[]{new Pair("work_name", task_work_name),
                    new Pair("work_address", task_work_address), new Pair("work_date", Date_Utils.mysql_Date_Format.format(task_work_date)), new Pair("sales_person_id", String.valueOf(task_sales_person_id)),
                    new Pair("advances_json", task_advances_json), new Pair("expenses_json", task_expenses_json)});
        }


        @Override
        protected void onPostExecute(final String[] network_action_response_array) {
            mAuthTask = null;

            showProgress(false,application_context,mProgressView,mLoginFormView);

            handle_json_insertion_response_and_switch_with_finish(network_action_response_array, current_activity, Sales_Person_Dashboard_Page.class, application_context, txt_name);

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false,application_context,mProgressView,mLoginFormView);
            save_menu_item.setEnabled(true);
        }
    }

    public void handle_json_insertion_response_and_switch_with_finish(String[] network_action_response_array, AppCompatActivity current_activity, Class to_switch_activity, Context context, View view_to_focus_on_error) {
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
                        Activity_Utils.start_activity_with_finish_and_tab_index(current_activity,to_switch_activity,0);
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

    private void attempt_work_save() {
        if (mAuthTask != null) {
            mAuthTask.cancel(true);
            mAuthTask = null;
        }

        reset_errors(new EditText[]{txt_name, txt_address});

        Pair<Boolean, EditText> empty_check_result = empty_check(new Pair[]{new Pair(txt_name, "Please Enter Work Name..."),
                new Pair(txt_address, "Please Enter Work Address...")});

        if (empty_check_result.first) {
            // There was an error; don't attempt login and focus the first form field with an error.
            empty_check_result.second.requestFocus();
        } else {

            if (zero_check(new Double[]{total_advance})) {
                show_uncancelled_yes_no_confirmation_dialogue_for_no_advances();
            } else if (total_expense > total_advance) {
                show_uncancelled_yes_no_confirmation_dialogue_for_unprofitable();
            } else {
                execute_work_save_task();
            }
        }
    }

    private String generate_expenses_json() {
        JSONArray mJSONArray = new JSONArray();
        for (int i = 0; i < work_expenses.size(); i++) {
            JSONObject json_obj = new JSONObject();
            try {
                json_obj.put("description", work_expenses.get(i).getDescription());
                json_obj.put("amount", work_expenses.get(i).getAmount());
                mJSONArray.put(json_obj);
            } catch (JSONException e) {
                Toast.makeText(application_context, "Error : Check json", Toast.LENGTH_LONG).show();
            }
        }
        Log.d(General_Data.TAG, mJSONArray.toString());
        return mJSONArray.toString();

    }

    private String generate_advances_json() {
        JSONArray mJSONArray = new JSONArray();
        for (int i = 0; i < work_advances.size(); i++) {
            JSONObject json_obj = new JSONObject();
            try {
                json_obj.put("description", work_advances.get(i).getDescription());
                json_obj.put("amount", work_advances.get(i).getAmount());
                mJSONArray.put(json_obj);
            } catch (JSONException e) {
                Toast.makeText(application_context, "Error : Check json", Toast.LENGTH_LONG).show();
            }
        }
        Log.d(General_Data.TAG, mJSONArray.toString());
        return mJSONArray.toString();
    }

    Context application_context;

    public boolean form_check() {
        return non_empty_check(new EditText[]{txt_name, txt_address}) && zero_check(new Double[]{total_advance, total_expense});
    }

}
