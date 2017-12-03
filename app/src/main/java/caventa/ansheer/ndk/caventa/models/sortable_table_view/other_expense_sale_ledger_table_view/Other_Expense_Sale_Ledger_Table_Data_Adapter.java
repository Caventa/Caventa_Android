package caventa.ansheer.ndk.caventa.models.sortable_table_view.other_expense_sale_ledger_table_view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import caventa.ansheer.ndk.caventa.commons.Date_Utils;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

import static android.graphics.Color.BLACK;

public class Other_Expense_Sale_Ledger_Table_Data_Adapter extends LongPressAwareTableDataAdapter<Other_Expense_Sale_Ledger_Entry> {

    private static final int TEXT_SIZE = 14;

    public Other_Expense_Sale_Ledger_Table_Data_Adapter(final Context context, final List<Other_Expense_Sale_Ledger_Entry> data, final TableView<Other_Expense_Sale_Ledger_Entry> tableView) {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final Other_Expense_Sale_Ledger_Entry other_expense_Sale_ledger_entry = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderString(Date_Utils.normal_date_short_year_format.format(other_expense_Sale_ledger_entry.getInsertion_date()));
                break;
            case 1:
                renderedView = renderString(other_expense_Sale_ledger_entry.getParticulars());
                break;
            case 2:
                renderedView = renderString(String.valueOf(other_expense_Sale_ledger_entry.getAmount()));
                break;

        }

        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {

        View renderedView = getDefaultCellView(rowIndex, columnIndex, parentView);
        return renderedView;
    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setTextColor(BLACK);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

}
