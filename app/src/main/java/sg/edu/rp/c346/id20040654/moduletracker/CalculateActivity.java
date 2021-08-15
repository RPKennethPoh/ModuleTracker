package sg.edu.rp.c346.id20040654.moduletracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CalculateActivity extends AppCompatActivity {

    Spinner spinModule;
    EditText etAks, etSdl, etCol;
    Button btnCalculate;
    ArrayList<Module> moduleList;
    ArrayAdapter<Module> aaModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);

        spinModule = findViewById(R.id.spinnerCalc);
        etAks = findViewById(R.id.editTextCalcAks);
        etSdl = findViewById(R.id.editTextCalcSdl);
        etCol = findViewById(R.id.editTextCalcCol);
        btnCalculate = findViewById(R.id.buttonCalculate);

        DBHelper dbh = new DBHelper(CalculateActivity.this);
        moduleList = new ArrayList<Module>();
        moduleList.addAll(dbh.getAllModules());
        aaModule = new ArrayAdapter<Module>(this, android.R.layout.simple_list_item_1, moduleList);
        spinModule.setAdapter(aaModule);


        Intent get = getIntent();
        String from = get.getStringExtra("from");
        Module module;
        if(from.equalsIgnoreCase("cag")) {
            module = (Module) get.getSerializableExtra("module");
            for(int i = 0 ; i < moduleList.size(); i++) {
                if(module.getCode().equals(moduleList.get(i).getCode())) {
                    spinModule.setSelection(i);
                    break;
                }
            }
        }

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float aks, sdl, col;
                double score;
                String grade;
                Module selected = moduleList.get(spinModule.getSelectedItemPosition());
                int breakdown = selected.getBreakdown();
                if(!etAks.getText().toString().isEmpty() && !etSdl.getText().toString().isEmpty() && !etCol.getText().toString().isEmpty()) {
                    aks = Float.parseFloat(etAks.getText().toString());
                    sdl = Float.parseFloat(etSdl.getText().toString());
                    col = Float.parseFloat(etCol.getText().toString());
                    if(breakdown == 50) {
                        score = (aks/4 * 0.5 * 100) + (sdl/4 * 0.25 * 100) + (col/4 * 0.25 * 100);
                    } else {
                        score = (aks/4 * 0.6 * 100) + (sdl/4 * 0.2 * 100) + (col/4 * 0.2 * 100);
                    }

                    if(score >= 80 && score <= 100) {
                        grade = "A";
                    } else if (score >= 70 && score < 80) {
                        grade = "B";
                    } else if (score >= 60 && score < 70) {
                        grade = "C";
                    } else if (score >= 50 && score < 60) {
                        grade = "D";
                    } else {
                        grade = "F";
                    }

                    // Inflate the input.xml layout file
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View viewDialog = inflater.inflate(R.layout.add_cag, null);

                    // Obtain the UI component in the input.xml layout
                    // It needs to be defined as "final", so that it can used in the onClick() method later
                    final TextView tvAks = viewDialog.findViewById(R.id.textViewAks);
                    final TextView tvSdl = viewDialog.findViewById(R.id.textViewSdl);
                    final TextView tvCol = viewDialog.findViewById(R.id.textViewCol);
                    final TextView tvScore = viewDialog.findViewById(R.id.textViewScore);
                    final TextView tvGrade = viewDialog.findViewById(R.id.textViewGrade);

                    tvAks.setText(String.format("Aks: %.0f/4", aks));
                    tvSdl.setText(String.format("Sdl: %.0f/4", sdl));
                    tvCol.setText(String.format("Col: %.0f/4", col));
                    tvScore.setText(String.format("Score: %.2f%%", score));
                    tvGrade.setText(String.format("Grade: %s", grade));

                    AlertDialog.Builder myBuilder = new AlertDialog.Builder(CalculateActivity.this);
                    myBuilder.setView(viewDialog);  // Set the view of the dialog
                    myBuilder.setTitle("Calculate Results");
                    myBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // int moduleId, int breakdown, String code, String name, int aks, int sdl, int col) {
                            //        super(moduleId, breakdown, code, name
                            CAG save = new CAG(selected.getModuleId(), selected.getBreakdown(), selected.getCode(), selected.getName(), aks, sdl, col);
                            long result = dbh.insertCag(save);
                            if(result != -1) {
                                Toast.makeText(CalculateActivity.this, "CAG added", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CalculateActivity.this, "Failed to add CAG", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    myBuilder.setNeutralButton("Close", null);
                    AlertDialog myDialog = myBuilder.create();
                    myDialog.show();
                } else {
                    Toast.makeText(CalculateActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}