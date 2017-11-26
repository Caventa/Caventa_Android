package caventa.ansheer.ndk.caventa.models.sortable_table_view.loan_installments_ledger_table_view;

import java.util.Comparator;

/**
 * A collection of {@link Comparator}s for {@link Loan_Installments_Ledger_Entry} objects.
 *
 * @author ISchwarz
 */
public final class Loan_Installments_Ledger_Table_Comparators {

    private Loan_Installments_Ledger_Table_Comparators() {
        //no instance
    }

    public static Comparator<Loan_Installments_Ledger_Entry> get_Insertion_Date_Comparator() {
        return new Insertion_Date_Comparator();
    }

    public static Comparator<Loan_Installments_Ledger_Entry> get_Receipt_Number_Comparator() {
        return new Receipt_Number_Comparator();
    }

    public static Comparator<Loan_Installments_Ledger_Entry> get_Payed_Amount_Comparator() {
        return new Payed_Amount_Comparator();
    }

    public static Comparator<Loan_Installments_Ledger_Entry> get_Principle_Amount_Comparator() {
        return new Principle_Amount_Comparator();
    }

    public static Comparator<Loan_Installments_Ledger_Entry> get_Interest_Amount_Comparator() {
        return new Interest_Amount_Comparator();
    }

    public static Comparator<Loan_Installments_Ledger_Entry> get_Remarks_Comparator() {
        return new Remarks_Comparator();
    }

    private static class Insertion_Date_Comparator implements Comparator<Loan_Installments_Ledger_Entry> {

        @Override
        public int compare(final Loan_Installments_Ledger_Entry loan_installments_ledger_entry1, final Loan_Installments_Ledger_Entry loan_installments_ledger_entry2) {
            if (loan_installments_ledger_entry1.getInsertion_date().before(loan_installments_ledger_entry2.getInsertion_date()))
                return -1;
            if (loan_installments_ledger_entry1.getInsertion_date().after(loan_installments_ledger_entry2.getInsertion_date()))
                return 1;
            return 0;
        }
    }


    private static class Payed_Amount_Comparator implements Comparator<Loan_Installments_Ledger_Entry> {

        @Override
        public int compare(final Loan_Installments_Ledger_Entry loan_installments_ledger_entry1, final Loan_Installments_Ledger_Entry loan_installments_ledger_entry2) {
            if (loan_installments_ledger_entry1.getPayed_amount() < loan_installments_ledger_entry2.getPayed_amount())
                return -1;
            if (loan_installments_ledger_entry1.getPayed_amount()> loan_installments_ledger_entry2.getPayed_amount())
                return 1;
            return 0;
        }
    }

    private static class Principle_Amount_Comparator implements Comparator<Loan_Installments_Ledger_Entry> {

        @Override
        public int compare(final Loan_Installments_Ledger_Entry loan_installments_ledger_entry1, final Loan_Installments_Ledger_Entry loan_installments_ledger_entry2) {
            if (loan_installments_ledger_entry1.getPrinciple_amount() < loan_installments_ledger_entry2.getPrinciple_amount())
                return -1;
            if (loan_installments_ledger_entry1.getPrinciple_amount() > loan_installments_ledger_entry2.getPrinciple_amount())
                return 1;
            return 0;
        }
    }


    private static class Receipt_Number_Comparator implements Comparator<Loan_Installments_Ledger_Entry> {

        @Override
        public int compare(final Loan_Installments_Ledger_Entry loan_installments_ledger_entry1, final Loan_Installments_Ledger_Entry loan_installments_ledger_entry2) {
            return loan_installments_ledger_entry1.getReceipt_number().compareTo(loan_installments_ledger_entry2.getReceipt_number());
        }
    }

    private static class Interest_Amount_Comparator implements Comparator<Loan_Installments_Ledger_Entry> {

        @Override
        public int compare(final Loan_Installments_Ledger_Entry loan_installments_ledger_entry1, final Loan_Installments_Ledger_Entry loan_installments_ledger_entry2) {
            if (loan_installments_ledger_entry1.getInterest_amount() < loan_installments_ledger_entry2.getInterest_amount())
                return -1;
            if (loan_installments_ledger_entry1.getInterest_amount() > loan_installments_ledger_entry2.getInterest_amount())
                return 1;
            return 0;
        }
    }


    private static class Remarks_Comparator implements Comparator<Loan_Installments_Ledger_Entry> {

        @Override
        public int compare(final Loan_Installments_Ledger_Entry loan_installments_ledger_entry1, final Loan_Installments_Ledger_Entry loan_installments_ledger_entry2) {
            return loan_installments_ledger_entry1.getRemarks().compareTo(loan_installments_ledger_entry2.getRemarks());
        }
    }


}
