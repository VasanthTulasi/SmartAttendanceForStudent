package com.smart.student.attendance.vasanth.smartattendanceforstudent;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    EditText firstNameTV;
    EditText lastNameTV;
    EditText emailForRegTV;
    EditText passwordForRegTV;
    EditText confirmPasswordTV;

    FirebaseAuth fAuthReg;

    Spinner yearSpi;
    Spinner branchSpi;
    Spinner secSpi;

    String year="",branch="",section="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstNameTV = findViewById(R.id.firstNameTV);
        lastNameTV = findViewById(R.id.lastNameTV);
        emailForRegTV = findViewById(R.id.emailForRegTV);
        passwordForRegTV = findViewById(R.id.passwordForRegTV);
        confirmPasswordTV = findViewById(R.id.confirmPasswordTV);

        fAuthReg = FirebaseAuth.getInstance();


        //Spinner for Year
        String[] arraySpinnerForYear = {"Year 1", "Year 2", "Year 3", "Year 4"};
        yearSpi = (Spinner) findViewById(R.id.yearSpi);
        ArrayAdapter<String> adapterForYear = new ArrayAdapter<String>(this, R.layout.spinner_item, arraySpinnerForYear);
        adapterForYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpi.setAdapter(adapterForYear);
        yearSpi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(yearSpi.getItemAtPosition(i).equals("Year 1"))
                        year = "1";
                    else if(yearSpi.getItemAtPosition(i).equals("Year 2"))
                        year = "2";
                    else if(yearSpi.getItemAtPosition(i).equals("Year 3"))
                        year = "3";
                    else if(yearSpi.getItemAtPosition(i).equals("Year 4"))
                        year = "4";



                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


        //Spinner for Branch
        String[] arraySpinnerForBranch = {"CSE", "IT", "ECE", "EEE","Mechanical","Civil","Pharma","Chemical"};
        branchSpi = (Spinner) findViewById(R.id.branchSpi);
        ArrayAdapter<String> adapterForBranch = new ArrayAdapter<String>(this, R.layout.spinner_item, arraySpinnerForBranch);
        adapterForBranch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branchSpi.setAdapter(adapterForBranch);
        branchSpi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                branch = (String) branchSpi.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //Spinner for Section
        String[] arraySpinnerForSection = {"Section A", "Section B", "Section C", "Section D"};
        secSpi = (Spinner) findViewById(R.id.secSpi);
        ArrayAdapter<String> adapterForSec = new ArrayAdapter<String>(this, R.layout.spinner_item, arraySpinnerForSection);
        adapterForSec.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        secSpi.setAdapter(adapterForSec);
        secSpi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(secSpi.getItemAtPosition(i).equals("Section A"))
                    section = "A";
                else if(secSpi.getItemAtPosition(i).equals("Section B"))
                    section = "B";
                else if(secSpi.getItemAtPosition(i).equals("Section C"))
                    section = "C";
                else if(secSpi.getItemAtPosition(i).equals("Section D"))
                    section = "D";

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }



    public void register(View v){

        String fName="",lName="",email="",pass="",conPass="";
        fName=firstNameTV.getText().toString();
        lName=lastNameTV.getText().toString();
        email=emailForRegTV.getText().toString();
        pass=passwordForRegTV.getText().toString();
        conPass=confirmPasswordTV.getText().toString();

        if(!TextUtils.isEmpty(fName) && !TextUtils.isEmpty(lName) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(conPass)) {
            if (pass.equals(conPass)) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Please confirm the details:\nYou belong to Year "+year+" - " +branch+" - "+section+".")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                createUserOnCloud();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
            } else {
                Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(Register.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();

        }

    }

    public void createUserOnCloud(){

                fAuthReg.createUserWithEmailAndPassword(emailForRegTV.getText().toString(), passwordForRegTV.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference emailRegdb = FirebaseDatabase.getInstance().getReference().child("emails").child(String.valueOf(fAuthReg.getCurrentUser().getUid()));
                            emailRegdb.child("Year").setValue(year);
                            emailRegdb.child("Branch").setValue(branch);
                            emailRegdb.child("Section").setValue(section);
                            emailRegdb.child("Name").setValue(firstNameTV.getText().toString() + " " + lastNameTV.getText().toString());

                            Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        }
                        if (!task.isSuccessful()) {
                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                            Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}
