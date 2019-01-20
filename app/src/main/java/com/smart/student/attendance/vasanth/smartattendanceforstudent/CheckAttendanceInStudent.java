package com.smart.student.attendance.vasanth.smartattendanceforstudent;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckAttendanceInStudent extends AppCompatActivity {
    long attendedClasses = 0L;
    TextView dbTV;

    DatabaseReference databaseReferenceEmail;
    String year,branch,section,userName;
    FirebaseAuth firebaseAuthSub;

    DatabaseReference dbForCheckingAttendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_attendance_in_student);


        dbTV = (TextView) findViewById(R.id.dbTV);
        firebaseAuthSub  = FirebaseAuth.getInstance();
        databaseReferenceEmail  = FirebaseDatabase.getInstance().getReference().child("emails").child(String.valueOf(firebaseAuthSub.getCurrentUser().getUid()));
        databaseReferenceEmail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userName = String.valueOf(dataSnapshot.child("Name").getValue());
                year = String.valueOf(dataSnapshot.child("Year").getValue());
                branch = String.valueOf(dataSnapshot.child("Branch").getValue());
                section = String.valueOf(dataSnapshot.child("Section").getValue());


                dbForCheckingAttendance = FirebaseDatabase.getInstance().getReference().child("attendance").child(year).child(branch).child(section);
                dbForCheckingAttendance.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                            final DatabaseReference dbForCheckingAttendance1 = dbForCheckingAttendance.child(ds.getKey());
                            dbForCheckingAttendance1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    DatabaseReference dbForCheckingAttendance2 = dbForCheckingAttendance1.child(userName);
                                    dbForCheckingAttendance2.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot ds1:dataSnapshot.getChildren())
                                                if(ds1.getValue(Long.class) != null)
                                                    attendedClasses += ds1.getValue(Long.class);
                                            dbTV.setText(String.valueOf(attendedClasses));
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
