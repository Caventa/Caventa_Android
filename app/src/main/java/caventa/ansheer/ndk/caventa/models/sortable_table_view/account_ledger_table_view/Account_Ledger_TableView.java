package caventa.ansheer.ndk.caventa.models.sortable_table_view.account_ledger_table_view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import caventa.ansheer.ndk.caventa.R;
import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.SortStateViewProviders;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;


/**
 * An extension of the {@link SortableTableView} that handles {@link Account_Ledger_Entry}s.
 *
 * @author ISchwarz
 */
public class Account_Ledger_TableView extends SortableTableView<Account_Ledger_Entry> {

    public Account_Ledger_TableView(final Context context) {
        this(context, null);
    }

    public Account_Ledger_TableView(final Context context, final AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
    }

    public Account_Ledger_TableView(final Context context, final AttributeSet attributes, final int styleAttributes) {
        super(context, attributes, styleAttributes);

        final SimpleTableHeaderAdapter simpleTableHeaderAdapter = new SimpleTableHeaderAdapter(context, "#", "Par.", "Deb.", "Cre.", "Bal.");
        simpleTableHeaderAdapter.setTextColor(ContextCompat.getColor(context, R.color.table_header_text));
        setHeaderAdapter(simpleTableHeaderAdapter);

        final int rowColorEven = ContextCompat.getColor(context, R.color.table_data_row_even);
        final int rowColorOdd = ContextCompat.getColor(context, R.color.table_data_row_odd);
        setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(rowColorEven, rowColorOdd));
        setHeaderSortStateViewProvider(SortStateViewProviders.brightArrows());

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(5);
        tableColumnWeightModel.setColumnWeight(0, 3);
        tableColumnWeightModel.setColumnWeight(1, 3);
        tableColumnWeightModel.setColumnWeight(2, 2);
        tableColumnWeightModel.setColumnWeight(3, 2);
        tableColumnWeightModel.setColumnWeight(4, 2);
        setColumnModel(tableColumnWeightModel);

        setColumnComparator(0, Account_Ledger_Table_Comparators.get_Insertion_Date_Comparator());
        setColumnComparator(1, Account_Ledger_Table_Comparators.get_Particulars_Comparator());
        setColumnComparator(2, Account_Ledger_Table_Comparators.get_Debit_Amount_Comparator());
        setColumnComparator(3, Account_Ledger_Table_Comparators.get_Credit_Amount_Comparator());
        setColumnComparator(4, Account_Ledger_Table_Comparators.get_Balance_Comparator());
    }

}
