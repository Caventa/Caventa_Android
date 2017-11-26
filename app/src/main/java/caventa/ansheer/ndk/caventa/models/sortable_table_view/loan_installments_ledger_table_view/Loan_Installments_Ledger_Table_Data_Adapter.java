package caventa.ansheer.ndk.caventa.models.sortable_table_view.loan_installments_ledger_table_view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import caventa.ansheer.ndk.caventa.commons.Date_Utils;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

import static android.graphics.Color.BLACK;

public class Loan_Installments_Ledger_Table_Data_Adapter extends LongPressAwareTableDataAdapter<Loan_Installments_Ledger_Entry> {

    private static final int TEXT_SIZE = 14;

    public Loan_Installments_Ledger_Table_Data_Adapter(final Context context, final List<Loan_Installments_Ledger_Entry> data, final TableView<Loan_Installments_Ledger_Entry> tableView) {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final Loan_Installments_Ledger_Entry loan_installments_ledger_entry = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderString(Date_Utils.normal_date_short_year_format.format(loan_installments_ledger_entry.getInsertion_date()));
                break;
            case 1:
                renderedView = renderString(loan_installments_ledger_entry.getReceipt_number());
                break;
            case 2:
                renderedView = renderString(String.valueOf(loan_installments_ledger_entry.getPayed_amount()));
                break;
            case 3:
                renderedView = renderString(String.valueOf(loan_installments_ledger_entry.getPrinciple_amount()));
                break;
            case 4:
                renderedView = renderString(String.valueOf(loan_installments_ledger_entry.getInterest_amount()));
                break;
            case 5:
                renderedView = renderString(String.valueOf(loan_installments_ledger_entry.getRemarks()));
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
