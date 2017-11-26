package caventa.ansheer.ndk.caventa.models.sortable_table_view.account_ledger_table_view;

import java.util.Comparator;

/**
 * A collection of {@link Comparator}s for {@link Account_Ledger_Entry} objects.
 *
 * @author ISchwarz
 */
public final class Account_Ledger_Table_Comparators {

    private Account_Ledger_Table_Comparators() {
        //no instance
    }

    public static Comparator<Account_Ledger_Entry> get_Insertion_Date_Comparator() {
        return new Insertion_Date_Comparator();
    }

    public static Comparator<Account_Ledger_Entry> get_Particulars_Comparator() {
        return new Particulars_Comparator();
    }

    public static Comparator<Account_Ledger_Entry> get_Debit_Amount_Comparator() {
        return new Debit_Amount_Comparator();
    }

    public static Comparator<Account_Ledger_Entry> get_Credit_Amount_Comparator() {
        return new Credit_Amount_Comparator();
    }

    public static Comparator<Account_Ledger_Entry> get_Balance_Comparator() {
        return new Balance_Comparator();
    }

    private static class Insertion_Date_Comparator implements Comparator<Account_Ledger_Entry> {

        @Override
        public int compare(final Account_Ledger_Entry account_ledger_entry1, final Account_Ledger_Entry account_ledger_entry2) {
            if (account_ledger_entry1.getInsertion_date().before(account_ledger_entry2.getInsertion_date()))
                return -1;
            if (account_ledger_entry1.getInsertion_date().after(account_ledger_entry2.getInsertion_date()))
                return 1;
            return 0;
        }
    }


    private static class Debit_Amount_Comparator implements Comparator<Account_Ledger_Entry> {

        @Override
        public int compare(final Account_Ledger_Entry account_ledger_entry1, final Account_Ledger_Entry account_ledger_entry2) {
            if (account_ledger_entry1.getDebit_amount() < account_ledger_entry2.getDebit_amount())
                return -1;
            if (account_ledger_entry1.getDebit_amount() > account_ledger_entry2.getDebit_amount())
                return 1;
            return 0;
        }
    }

    private static class Credit_Amount_Comparator implements Comparator<Account_Ledger_Entry> {

        @Override
        public int compare(final Account_Ledger_Entry account_ledger_entry1, final Account_Ledger_Entry account_ledger_entry2) {
            if (account_ledger_entry1.getCredit_amount() < account_ledger_entry2.getCredit_amount())
                return -1;
            if (account_ledger_entry1.getCredit_amount() > account_ledger_entry2.getCredit_amount())
                return 1;
            return 0;
        }
    }

    private static class Balance_Comparator implements Comparator<Account_Ledger_Entry> {

        @Override
        public int compare(final Account_Ledger_Entry account_ledger_entry1, final Account_Ledger_Entry account_ledger_entry2) {
            if (account_ledger_entry1.getBalance() < account_ledger_entry2.getBalance())
                return -1;
            if (account_ledger_entry1.getBalance() > account_ledger_entry2.getBalance())
                return 1;
            return 0;
        }
    }

    private static class Particulars_Comparator implements Comparator<Account_Ledger_Entry> {

        @Override
        public int compare(final Account_Ledger_Entry account_ledger_entry1, final Account_Ledger_Entry account_ledger_entry2) {
            return account_ledger_entry1.getParticulars().compareTo(account_ledger_entry2.getParticulars());
        }
    }


}
