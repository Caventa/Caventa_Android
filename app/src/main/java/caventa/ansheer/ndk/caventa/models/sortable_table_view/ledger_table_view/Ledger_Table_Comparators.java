package caventa.ansheer.ndk.caventa.models.sortable_table_view.ledger_table_view;

import java.util.Comparator;

/**
 * A collection of {@link Comparator}s for {@link Ledger_Entry} objects.
 *
 * @author ISchwarz
 */
public final class Ledger_Table_Comparators {

    private Ledger_Table_Comparators() {
        //no instance
    }

    public static Comparator<Ledger_Entry> get_Insertion_Date_Comparator() {
        return new Insertion_Date_Comparator();
    }

    public static Comparator<Ledger_Entry> get_Particulars_Comparator() {
        return new Particulars_Comparator();
    }

    public static Comparator<Ledger_Entry> get_Debit_Amount_Comparator() {
        return new Debit_Amount_Comparator();
    }

    public static Comparator<Ledger_Entry> get_Credit_Amount_Comparator() {
        return new Credit_Amount_Comparator();
    }

    public static Comparator<Ledger_Entry> get_Balance_Comparator() {
        return new Balance_Comparator();
    }

    private static class Insertion_Date_Comparator implements Comparator<Ledger_Entry> {

        @Override
        public int compare(final Ledger_Entry ledger_entry1, final Ledger_Entry ledger_entry2) {
            if (ledger_entry1.getInsertion_date().before(ledger_entry2.getInsertion_date()))
                return -1;
            if (ledger_entry1.getInsertion_date().after(ledger_entry2.getInsertion_date()))
                return 1;
            return 0;
        }
    }


    private static class Debit_Amount_Comparator implements Comparator<Ledger_Entry> {

        @Override
        public int compare(final Ledger_Entry ledger_entry1, final Ledger_Entry ledger_entry2) {
            if (ledger_entry1.getDebit_amount() < ledger_entry2.getDebit_amount())
                return -1;
            if (ledger_entry1.getDebit_amount() > ledger_entry2.getDebit_amount())
                return 1;
            return 0;
        }
    }

    private static class Credit_Amount_Comparator implements Comparator<Ledger_Entry> {

        @Override
        public int compare(final Ledger_Entry ledger_entry1, final Ledger_Entry ledger_entry2) {
            if (ledger_entry1.getCredit_amount() < ledger_entry2.getCredit_amount())
                return -1;
            if (ledger_entry1.getCredit_amount() > ledger_entry2.getCredit_amount())
                return 1;
            return 0;
        }
    }

    private static class Balance_Comparator implements Comparator<Ledger_Entry> {

        @Override
        public int compare(final Ledger_Entry ledger_entry1, final Ledger_Entry ledger_entry2) {
            if (ledger_entry1.getBalance() < ledger_entry2.getBalance())
                return -1;
            if (ledger_entry1.getBalance() > ledger_entry2.getBalance())
                return 1;
            return 0;
        }
    }

    private static class Particulars_Comparator implements Comparator<Ledger_Entry> {

        @Override
        public int compare(final Ledger_Entry ledger_entry1, final Ledger_Entry ledger_entry2) {
            return ledger_entry1.getParticulars().compareTo(ledger_entry2.getParticulars());
        }
    }


}
