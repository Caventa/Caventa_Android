package caventa.ansheer.ndk.caventa.models.sortable_table_view.loan_ledger_table_view;

import java.util.Comparator;

/**
 * A collection of {@link Comparator}s for {@link Loan_Ledger_Entry} objects.
 *
 * @author ISchwarz
 */
public final class Loan_Ledger_Table_Comparators {

    private Loan_Ledger_Table_Comparators() {
        //no instance
    }

    public static Comparator<Loan_Ledger_Entry> get_Insertion_Date_Comparator() {
        return new Insertion_Date_Comparator();
    }

    public static Comparator<Loan_Ledger_Entry> get_Particulars_Comparator() {
        return new Particulars_Comparator();
    }

    public static Comparator<Loan_Ledger_Entry> get_Loan_Amount_Comparator() {
        return new Loan_Amount_Comparator();
    }

    public static Comparator<Loan_Ledger_Entry> get_Installment_Amount_Comparator() {
        return new Installment_Amount_Comparator();
    }

    private static class Insertion_Date_Comparator implements Comparator<Loan_Ledger_Entry> {

        @Override
        public int compare(final Loan_Ledger_Entry loan_ledger_entry1, final Loan_Ledger_Entry loan_ledger_entry2) {
            if (loan_ledger_entry1.getInsertion_date().before(loan_ledger_entry2.getInsertion_date()))
                return -1;
            if (loan_ledger_entry1.getInsertion_date().after(loan_ledger_entry2.getInsertion_date()))
                return 1;
            return 0;
        }
    }


    private static class Loan_Amount_Comparator implements Comparator<Loan_Ledger_Entry> {

        @Override
        public int compare(final Loan_Ledger_Entry loan_ledger_entry1, final Loan_Ledger_Entry loan_ledger_entry2) {
            if (loan_ledger_entry1.getLoan_amount() < loan_ledger_entry2.getLoan_amount())
                return -1;
            if (loan_ledger_entry1.getLoan_amount()> loan_ledger_entry2.getLoan_amount())
                return 1;
            return 0;
        }
    }

    private static class Installment_Amount_Comparator implements Comparator<Loan_Ledger_Entry> {

        @Override
        public int compare(final Loan_Ledger_Entry loan_ledger_entry1, final Loan_Ledger_Entry loan_ledger_entry2) {
            if (loan_ledger_entry1.getInstallment_amount() < loan_ledger_entry2.getInstallment_amount())
                return -1;
            if (loan_ledger_entry1.getInstallment_amount() > loan_ledger_entry2.getInstallment_amount())
                return 1;
            return 0;
        }
    }


    private static class Particulars_Comparator implements Comparator<Loan_Ledger_Entry> {

        @Override
        public int compare(final Loan_Ledger_Entry loan_ledger_entry1, final Loan_Ledger_Entry loan_ledger_entry2) {
            return loan_ledger_entry1.getParticulars().compareTo(loan_ledger_entry2.getParticulars());
        }
    }


}
