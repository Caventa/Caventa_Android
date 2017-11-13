package caventa.ansheer.ndk.caventa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import caventa.ansheer.ndk.caventa.R;

public class Sales_Person_Accounts extends AppCompatActivity {

    private Button button_sales;
    private Button button_commision;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_person_accounts);
        initView();

        button_commision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CardViewActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        button_sales = (Button) findViewById(R.id.button_comb);
        button_commision = (Button) findViewById(R.id.button_a);
    }
}
