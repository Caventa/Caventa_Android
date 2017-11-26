package caventa.ansheer.ndk.caventa.models.sortable_table_view.loan_ledger_table_view;

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
 * An extension of the {@link SortableTableView} that handles {@link Loan_Ledger_Entry}s.
 *
 * @author ISchwarz
 */
public class Loan_Ledger_TableView extends SortableTableView<Loan_Ledger_Entry> {

    public Loan_Ledger_TableView(final Context context) {
        this(context, null);
    }

    public Loan_Ledger_TableView(final Context context, final AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
    }

    public Loan_Ledger_TableView(final Context context, final AttributeSet attributes, final int styleAttributes) {
        super(context, attributes, styleAttributes);

        final SimpleTableHeaderAdapter simpleTableHeaderAdapter = new SimpleTableHeaderAdapter(context, "#", "Par.", "LAm.", "IAm.");
        simpleTableHeaderAdapter.setTextColor(ContextCompat.getColor(context, R.color.table_header_text));
        setHeaderAdapter(simpleTableHeaderAdapter);

        final int rowColorEven = ContextCompat.getColor(context, R.color.table_data_row_even);
        final int rowColorOdd = ContextCompat.getColor(context, R.color.table_data_row_odd);
        setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(rowColorEven, rowColorOdd));
        setHeaderSortStateViewProvider(SortStateViewProviders.brightArrows());

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(4);
        tableColumnWeightModel.setColumnWeight(0, 2);
        tableColumnWeightModel.setColumnWeight(1, 3);
        tableColumnWeightModel.setColumnWeight(2, 2);
        tableColumnWeightModel.setColumnWeight(3, 2);
        setColumnModel(tableColumnWeightModel);

        setColumnComparator(0, Loan_Ledger_Table_Comparators.get_Insertion_Date_Comparator());
        setColumnComparator(1, Loan_Ledger_Table_Comparators.get_Particulars_Comparator());
        setColumnComparator(2, Loan_Ledger_Table_Comparators.get_Loan_Amount_Comparator());
        setColumnComparator(3, Loan_Ledger_Table_Comparators.get_Installment_Amount_Comparator());
    }

}
