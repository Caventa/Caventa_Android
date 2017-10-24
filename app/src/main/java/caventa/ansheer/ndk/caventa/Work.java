package caventa.ansheer.ndk.caventa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class Work extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);

        Bundle extras=getIntent().getExtras();
        setTitle(extras.getString("work"));

        ArrayList yourlist = new ArrayList();
        yourlist.add("25-10-2017 : 100");
        yourlist.add("25-10-2017 : 100");
        yourlist.add("25-10-2017 : 100");

        ListView lv = (ListView) findViewById(R.id.list_advance);
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, yourlist));

        yourlist = new ArrayList();
        yourlist.add("Item : 100");
        yourlist.add("Item : 100");
        yourlist.add("Item : 100");

        lv = (ListView) findViewById(R.id.list_expense);
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, yourlist));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_work, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_item_bank) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
