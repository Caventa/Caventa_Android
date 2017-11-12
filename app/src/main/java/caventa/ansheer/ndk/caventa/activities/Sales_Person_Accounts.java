package caventa.ansheer.ndk.caventa.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import caventa.ansheer.ndk.caventa.R;

public class Sales_Person_Accounts extends AppCompatActivity {

    private Button buttonComb;
    private Button buttonA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_person_accounts);
        initView();
    }

    private void initView() {
        buttonComb = (Button) findViewById(R.id.button_comb);
        buttonA = (Button) findViewById(R.id.button_a);
    }
}
