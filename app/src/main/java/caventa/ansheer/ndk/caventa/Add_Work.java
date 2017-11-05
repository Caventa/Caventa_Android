package caventa.ansheer.ndk.caventa;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_work);
        setTitle("New Work");

        txt_total_advance = findViewById(R.id.total_advance);
        txt_total_expense = findViewById(R.id.total_expense);
        txt_total_profit = findViewById(R.id.total_profit);



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
        sdf = Date_Utils.normal_Date_Format_words;
        txt_date.setText(sdf.format(calendar.getTime()));
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                txt_date.setText(sdf.format(calendar.getTime()));
            }
        };

        pick_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date_Picker_Utils.show_date_picker(Add_Work.this,date,calendar);
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

                                Work_Advance work_advance = new Work_Advance(2, 1, Double.parseDouble(((EditText) dialog.findViewById(R.id.name)).getText().toString()), ((EditText) dialog.findViewById(R.id.address)).getText().toString(), "2017-10-05 05:20:23");
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

                                Work_Expense work_expense = new Work_Expense(2, 1, Double.parseDouble(((EditText) dialog.findViewById(R.id.name)).getText().toString()), ((EditText) dialog.findViewById(R.id.address)).getText().toString(), "2017-10-05 05:20:23");
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
                if (((ImageView) view.findViewById(R.id.delete_icon)).getContentDescription().toString().equals("delete_icon")) {
                    work_advances.remove(position);

                    String description_amount=((TextView)view.findViewById(R.id.description_amount)).getText().toString();
                    total_advance = total_advance - Double.parseDouble(description_amount.substring(description_amount.indexOf("-")+1));
                    txt_total_advance.setText("Advances : " + total_advance);

                    calculate_total_profit();

                    work_advances_adapter.notifyDataSetChanged();
                }
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
                if (((ImageView) view.findViewById(R.id.delete_icon)).getContentDescription().toString().equals("delete_icon")) {
                    work_expenses.remove(position);


                    String description_amount=((TextView)view.findViewById(R.id.description_amount)).getText().toString();

                    total_expense = total_expense - Double.parseDouble(description_amount.substring(description_amount.indexOf("-")+1));

                    txt_total_expense.setText("Expenses : " + total_expense);
                    calculate_total_profit();

                    work_expenses_adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
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

    private void fill_work_expenses_demo_Data() {
        Work_Expense work_expense = new Work_Expense(1, 1, 10.5, "Test Expense", "2017-10-05 05:20:23");
        work_expenses.add(work_expense);
        work_expense = new Work_Expense(2, 1, 12.5, "Test Expense 2", "2017-10-05 05:22:23");
        work_expenses.add(work_expense);

        work_expenses_adapter.notifyDataSetChanged();
    }

    private void fill_work_advances_demo_Data() {
        Work_Advance work_advance = new Work_Advance(1, 1, 10.5, "Test Advance", "2017-10-05 05:20:23");
        work_advances.add(work_advance);

        work_advance = new Work_Advance(2, 1, 15.5, "Test Advance 2", "2017-10-05 05:22:23");
        work_advances.add(work_advance);

        work_advances_adapter.notifyDataSetChanged();
    }

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
            Toast_Utils.longToast(getApplicationContext(), "Success...");
            this.finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

}
