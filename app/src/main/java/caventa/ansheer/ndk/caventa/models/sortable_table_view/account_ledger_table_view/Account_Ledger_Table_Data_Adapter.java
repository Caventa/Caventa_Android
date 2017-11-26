package caventa.ansheer.ndk.caventa.models.sortable_table_view.account_ledger_table_view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import caventa.ansheer.ndk.caventa.commons.Date_Utils;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

import static android.graphics.Color.BLACK;

public class Account_Ledger_Table_Data_Adapter extends LongPressAwareTableDataAdapter<Account_Ledger_Entry> {

    private static final int TEXT_SIZE = 14;

    public Account_Ledger_Table_Data_Adapter(final Context context, final List<Account_Ledger_Entry> data, final TableView<Account_Ledger_Entry> tableView) {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final Account_Ledger_Entry account_ledger_entry = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderString(Date_Utils.normal_date_time_short_year_format.format(account_ledger_entry.getInsertion_date()));
                break;
            case 1:
                renderedView = renderString(account_ledger_entry.getParticulars());
                break;
            case 2:
                renderedView = renderString(String.valueOf(account_ledger_entry.getDebit_amount()));
                break;
            case 3:
                renderedView = renderString(String.valueOf(account_ledger_entry.getCredit_amount()));
                break;
            case 4:
                renderedView = renderString(String.valueOf(account_ledger_entry.getBalance()));
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
