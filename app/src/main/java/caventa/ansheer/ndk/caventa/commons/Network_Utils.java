package caventa.ansheer.ndk.caventa.commons;

import android.support.design.widget.Snackbar;
import android.view.View;

import static android.graphics.Color.RED;

public class Network_Utils {
    public static void display_Long_no_FAB_no_network_bottom_SnackBar(View view, View.OnClickListener network_function) {
        Snackbar snackbar = Snackbar
                .make(view, "Internet unavailable!", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", network_function);
        snackbar.getView().setBackgroundColor(RED);
        snackbar.show();
    }
}
