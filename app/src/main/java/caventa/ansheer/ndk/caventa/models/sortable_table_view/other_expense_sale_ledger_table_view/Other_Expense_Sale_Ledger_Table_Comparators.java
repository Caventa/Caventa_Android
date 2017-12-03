package caventa.ansheer.ndk.caventa.models.sortable_table_view.other_expense_sale_ledger_table_view;

import java.util.Comparator;

/**
 * A collection of {@link Comparator}s for {@link Other_Expense_Sale_Ledger_Entry} objects.
 *
 * @author ISchwarz
 */
public final class Other_Expense_Sale_Ledger_Table_Comparators {

    private Other_Expense_Sale_Ledger_Table_Comparators() {
        //no instance
    }

    public static Comparator<Other_Expense_Sale_Ledger_Entry> get_Insertion_Date_Comparator() {
        return new Insertion_Date_Comparator();
    }

    public static Comparator<Other_Expense_Sale_Ledger_Entry> get_Particulars_Comparator() {
        return new Particulars_Comparator();
    }

    public static Comparator<Other_Expense_Sale_Ledger_Entry> get_Amount_Comparator() {
        return new Amount_Comparator();
    }


    private static class Insertion_Date_Comparator implements Comparator<Other_Expense_Sale_Ledger_Entry> {

        @Override
        public int compare(final Other_Expense_Sale_Ledger_Entry other_expense_Sale_ledger_entry1, final Other_Expense_Sale_Ledger_Entry other_expense_Sale_ledger_entry2) {
            if (other_expense_Sale_ledger_entry1.getInsertion_date().before(other_expense_Sale_ledger_entry2.getInsertion_date()))
                return -1;
            if (other_expense_Sale_ledger_entry1.getInsertion_date().after(other_expense_Sale_ledger_entry2.getInsertion_date()))
                return 1;
            return 0;
        }
    }


    private static class Amount_Comparator implements Comparator<Other_Expense_Sale_Ledger_Entry> {

        @Override
        public int compare(final Other_Expense_Sale_Ledger_Entry other_expense_Sale_ledger_entry1, final Other_Expense_Sale_Ledger_Entry other_expense_Sale_ledger_entry2) {
            if (other_expense_Sale_ledger_entry1.getAmount() < other_expense_Sale_ledger_entry2.getAmount())
                return -1;
            if (other_expense_Sale_ledger_entry1.getAmount() > other_expense_Sale_ledger_entry2.getAmount())
                return 1;
            return 0;
        }
    }


    private static class Particulars_Comparator implements Comparator<Other_Expense_Sale_Ledger_Entry> {

        @Override
        public int compare(final Other_Expense_Sale_Ledger_Entry other_expense_Sale_ledger_entry1, final Other_Expense_Sale_Ledger_Entry other_expense_Sale_ledger_entry2) {
            return other_expense_Sale_ledger_entry1.getParticulars().compareTo(other_expense_Sale_ledger_entry2.getParticulars());
        }
    }


}
