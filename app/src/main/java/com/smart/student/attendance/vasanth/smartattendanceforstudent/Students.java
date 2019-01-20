package com.smart.student.attendance.vasanth.smartattendanceforstudent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Students extends AppCompatActivity {

    DatabaseReference databaseReferenceInStudents;

    ArrayList<String> keysArrayListInStudents;
    ArrayList<String> namesForReferenceInStudents;
    ArrayList<CardClass> membersArrayListInStudents;
    static ArrayList<String> referenceForKeyArrayListInStudents;
    static ArrayList<String> referenceForNamesArrayListInStudents;

    DatabaseReference databaseReferenceEmail;
    String year,branch,section,userName;
    FirebaseAuth firebaseAuthSub;
    ProgressBar pb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        firebaseAuthSub  = FirebaseAuth.getInstance();
        pb = findViewById(R.id.pBarInStudents);

        TextView studentsTV = (TextView)findViewById(R.id.studentsTV);
        studentsTV.append(" "+ AdapterClassForSubjects.subjectName);

        databaseReferenceEmail  = FirebaseDatabase.getInstance().getReference().child("emails").child(String.valueOf(firebaseAuthSub.getCurrentUser().getUid()));
        databaseReferenceEmail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName = String.valueOf(dataSnapshot.child("Name").getValue());
                year = String.valueOf(dataSnapshot.child("Year").getValue());
                branch = String.valueOf(dataSnapshot.child("Branch").getValue());
                section = String.valueOf(dataSnapshot.child("Section").getValue());
                getStudentList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void getStudentList(){
        databaseReferenceInStudents = FirebaseDatabase.getInstance().getReference().child("addedStudents").child(year).child(branch).child(section).child(AdapterClassForSubjects.subjectName);
        ListView listViewInStudents = (ListView) findViewById(R.id.listViewForMemberInStudentActivity);

        membersArrayListInStudents = new ArrayList<>();
        keysArrayListInStudents = new ArrayList<>();
        namesForReferenceInStudents = new ArrayList<>();
        referenceForKeyArrayListInStudents = new ArrayList<>();
        referenceForNamesArrayListInStudents= new ArrayList<>();

        final AdapterClassForStudents adapterForMemberInStudents = new AdapterClassForStudents(this, R.layout.card_design_for_students, membersArrayListInStudents);
        listViewInStudents.setAdapter(adapterForMemberInStudents);

        databaseReferenceInStudents.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String addedMember = dataSnapshot.getValue(String.class);
                membersArrayListInStudents.add(new CardClass(1, addedMember));
                namesForReferenceInStudents.add(addedMember);
                String addedkey = dataSnapshot.getKey();
                keysArrayListInStudents.add(addedkey);
                adapterForMemberInStudents.notifyDataSetChanged();



                referenceForKeyArrayListInStudents = keysArrayListInStudents;
                referenceForNamesArrayListInStudents = namesForReferenceInStudents;

                if (pb != null) {
                    pb.setVisibility(View.GONE);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s){

            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void markAttendance(View v){
        if(namesForReferenceInStudents.contains(userName))
            startActivity(new Intent(Students.this, AttendanceInStudent.class));
        else
            Toast.makeText(Students.this,"You are not a student of this subject yet. Contact the admin for further details.",Toast.LENGTH_LONG).show();
    }


}
