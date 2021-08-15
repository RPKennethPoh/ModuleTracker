package sg.edu.rp.c346.id20040654.moduletracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CagActivity extends AppCompatActivity {

    TextView tvCagSub;
    Button btnCagAdd;
    ListView lvCag;
    ArrayList<CAG> cagList;
    ArrayAdapter<CAG> aaCag;
    Module moduleIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cag);

        tvCagSub = findViewById(R.id.textViewCAGSubHead);
        btnCagAdd = findViewById(R.id.buttonCAGAdd);
        lvCag = findViewById(R.id.listViewCAG);

        Intent get = getIntent();
        moduleIntent = (Module) get.getSerializableExtra("module");
        tvCagSub.setText(String.format("%s - %s", moduleIntent.getCode(), moduleIntent.getName()));

        DBHelper dbh = new DBHelper(CagActivity.this);
        cagList = new ArrayList<CAG>();
        cagList.addAll(dbh.getAllCAG(moduleIntent));
        aaCag = new ArrayAdapter<CAG>(this, android.R.layout.simple_list_item_1, cagList);
        lvCag.setAdapter(aaCag);

        registerForContextMenu(lvCag);

        btnCagAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CagActivity.this, CalculateActivity.class);
                intent.putExtra("from", "cag");
                intent.putExtra("module", moduleIntent);
                startActivity(intent);
            }
        });

    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        Log.v("context", "Context Menu has been created");

        // inflate menu / add items into menu
        menu.add(0, 0, 1, "Remove");

    }

    public boolean onContextItemSelected(MenuItem item) {

        DBHelper dbh = new DBHelper(CagActivity.this);
        // idk, got code from google
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        // removes item used to open contextmenu from the arraylist
        CAG editCag = cagList.get(info.position);

            // Inflate the input.xml layout file
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            AlertDialog.Builder myBuilder = new AlertDialog.Builder(CagActivity.this);
            myBuilder.setTitle("Confirmation");
            myBuilder.setMessage("Are you sure you want to delete this CAG?");
            myBuilder.setCancelable(true);
            myBuilder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int result = dbh.deleteCAG(editCag.getCagId());
                    if(result == 1) {
                        Toast.makeText(CagActivity.this, "CAG deleted", Toast.LENGTH_SHORT).show();
                        cagList.clear();
                        cagList.addAll(dbh.getAllCAG(moduleIntent));
                        aaCag.notifyDataSetChanged();
                    } else {
                        Toast.makeText(CagActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            myBuilder.setNeutralButton("Cancel", null);
            AlertDialog myDialog = myBuilder.create();
            myDialog.show();

        return super.onContextItemSelected(item); //pass menu item to the superclass implementation
    }

}