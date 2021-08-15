package sg.edu.rp.c346.id20040654.moduletracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import java.util.ArrayList;

public class ModuleActivity extends AppCompatActivity {

    Button btnModuleAdd;
    ListView lvModules;
    ArrayList<Module> moduleList;
    ArrayAdapter<Module> aaModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);

        btnModuleAdd = findViewById(R.id.buttonModuleAdd);
        lvModules = findViewById(R.id.listViewModule);
        moduleList = new ArrayList<Module>();
        DBHelper dbh = new DBHelper(ModuleActivity.this);
        moduleList.addAll(dbh.getAllModules());
        aaModule = new ArrayAdapter<Module>(this, android.R.layout.simple_list_item_1, moduleList);
        lvModules.setAdapter(aaModule);

        registerForContextMenu(lvModules);

        btnModuleAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflate the input.xml layout file
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewDialog = inflater.inflate(R.layout.add_module, null);

                // Obtain the UI component in the input.xml layout
                // It needs to be defined as "final", so that it can used in the onClick() method later
                final EditText etModuleCode = viewDialog.findViewById(R.id.editTextModuleCode);
                final EditText etModuleName = viewDialog.findViewById(R.id.editTextModuleName);
                final RadioGroup rgBreakdown = viewDialog.findViewById(R.id.radioGroupBreakdown);

                AlertDialog.Builder myBuilder = new AlertDialog.Builder(ModuleActivity.this);
                myBuilder.setView(viewDialog);  // Set the view of the dialog
                myBuilder.setTitle("Add Module");
                myBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!etModuleCode.getText().toString().trim().isEmpty() && !etModuleName.getText().toString().trim().isEmpty() && rgBreakdown.getCheckedRadioButtonId() != -1) {
                            String code = etModuleCode.getText().toString();
                            String name = etModuleName.getText().toString();
                            int radio = rgBreakdown.getCheckedRadioButtonId();
                            int breakdown = 50;
                            if(radio == R.id.radioButtonBreakdown60) {
                                breakdown = 60;
                            }
                            Module module = new Module(breakdown, code, name);
                            long result = dbh.insertModule(module);
                            if(result != -1) {
                                Toast.makeText(ModuleActivity.this, "Module added", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ModuleActivity.this, "Failed to add module", Toast.LENGTH_SHORT).show();
                            }
                            moduleList.clear();
                            moduleList.addAll(dbh.getAllModules());
                            aaModule.notifyDataSetChanged();
                        } else {
                            Toast.makeText(ModuleActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                myBuilder.setNeutralButton("Cancel", null);
                AlertDialog myDialog = myBuilder.create();
                myDialog.show();
            }
        });

        lvModules.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Module module = moduleList.get(position);
                Intent intent = new Intent(ModuleActivity.this, CagActivity.class);
                intent.putExtra("module", module);
                startActivity(intent);
            }
        });
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        Log.v("context", "Context Menu has been created");

        // inflate menu / add items into menu
        menu.add(0, 0, 0, "Edit");
        menu.add(0, 1, 1, "Remove");

    }

    public boolean onContextItemSelected(MenuItem item) {

        DBHelper dbh = new DBHelper(ModuleActivity.this);
        // idk, got code from google
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        // removes item used to open contextmenu from the arraylist
        Module editModule = moduleList.get(info.position);
        if(item.getItemId() == 0) {
            // Inflate the input.xml layout file
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewDialog = inflater.inflate(R.layout.add_module, null);

            // Obtain the UI component in the input.xml layout
            // It needs to be defined as "final", so that it can used in the onClick() method later
            final EditText etModuleCode = viewDialog.findViewById(R.id.editTextModuleCode);
            final EditText etModuleName = viewDialog.findViewById(R.id.editTextModuleName);
            final RadioGroup rgBreakdown = viewDialog.findViewById(R.id.radioGroupBreakdown);

            AlertDialog.Builder myBuilder = new AlertDialog.Builder(ModuleActivity.this);
            myBuilder.setView(viewDialog);  // Set the view of the dialog
            myBuilder.setTitle("Edit Module");
            etModuleCode.setText(editModule.getCode());
            etModuleName.setText(editModule.getName());
            if(editModule.getBreakdown() == 50) {
                rgBreakdown.check(R.id.radioButtonBreakdown50);
            } else {
                rgBreakdown.check(R.id.radioButtonBreakdown60);
            }
            myBuilder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (!etModuleCode.getText().toString().trim().isEmpty() && !etModuleName.getText().toString().trim().isEmpty() && rgBreakdown.getCheckedRadioButtonId() != -1) {
                        String code = etModuleCode.getText().toString();
                        String name = etModuleName.getText().toString();
                        int radio = rgBreakdown.getCheckedRadioButtonId();
                        int breakdown = 50;
                        if (radio == R.id.radioButtonBreakdown60) {
                            breakdown = 60;
                        }
                        editModule.setCode(code);
                        editModule.setName(name);
                        editModule.setBreakdown(breakdown);
                        int result = dbh.updateModule(editModule);
                        if (result != -1) {
                            Toast.makeText(ModuleActivity.this, "Module added", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ModuleActivity.this, "Failed to add module", Toast.LENGTH_SHORT).show();
                        }
                        moduleList.clear();
                        moduleList.addAll(dbh.getAllModules());
                        aaModule.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ModuleActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            myBuilder.setNeutralButton("Cancel", null);
            AlertDialog myDialog = myBuilder.create();
            myDialog.show();
        } else if (item.getItemId() == 1) {
            int[] results = dbh.deleteModule(editModule.getModuleId());
            if(results[0] >= 1 && results[1] >= 1) {
                Toast.makeText(ModuleActivity.this, "Successfully removed module", Toast.LENGTH_SHORT).show();
            } else {
                if(results[0] < 1) {
                    Toast.makeText(ModuleActivity.this, "Removing failed for CAGs", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ModuleActivity.this, "Removing failed for module", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return super.onContextItemSelected(item); //pass menu item to the superclass implementation
    }
}