package caventa.ansheer.ndk.caventa.commons;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created on 03-12-2017 10:35 under Caventa_Android.
 */
public class Spinner_Utils {
    public static void attach_items_to_simple_spinner(Context context, Spinner spinner, ArrayList<String> items) {
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);
    }
}
