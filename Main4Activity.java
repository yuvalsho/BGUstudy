package com.example.bgustudy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import java.util.Iterator;

public class Main4Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText date;
    EditText time;
    EditText call;
    EditText room;
    DatabaseReference mRootRef;
    DatabaseReference mFaculties;
    DatabaseReference mCourses;
    String username;
    ArrayList<String> first;
    String[] firstConverter;
    ArrayList<String> second;
    Spinner course;
    Button create;
    String toPass="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        username = getIntent().getStringExtra("EXTRA_SESSION_ID_1");
        first = getIntent().getStringArrayListExtra("EXTRA_SESSION_ID_2");

        Button myRooms=(Button) findViewById(R.id.myRooms);

        myRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatabaseReference mRootRef=FirebaseDatabase.getInstance().getReference().child("rooms");
                mRootRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        final DatabaseReference faculties=mRootRef.child(dataSnapshot.getKey());

                        faculties.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                final DatabaseReference courses=faculties.child(dataSnapshot.getKey());

                                courses.orderByChild(username).equalTo("Hello").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                        toPass=toPass+"room: " + dataSnapshot.getKey().toString()+"\n";
                                        for (DataSnapshot datas : dataSnapshot.getChildren()) {
                                            String keys = datas.getKey();
                                            String values = datas.getValue().toString();
                                            toPass=toPass+keys + " " + values+"\n";
                                        }
                                        Intent intent = new Intent(Main4Activity.this, Main2Activity.class);
                                        intent.putExtra("EXTRA_SESSION_ID_1", username);
                                        intent.putExtra("EXTRA_SESSION_ID_2", toPass);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                    }

                                    @Override
                                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                    }

                                    @Override
                                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }
        });


        create =(Button) findViewById(R.id.Create) ;
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mFaculties = mRootRef.child("rooms");
        firstConverter=new String[first.size()+1];
        Iterator<String> iter=first.iterator();
        firstConverter[0]="";
        int i=1;
        while(iter.hasNext()){
           firstConverter[i]=iter.next();
           i=i+1;
        }
        Spinner department=(Spinner) findViewById(R.id.Department);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, first.toArray());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        department.setOnItemSelectedListener(this);
        department.setAdapter(arrayAdapter);



    }
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

        final String chosen = arg0.getItemAtPosition(position).toString();
        if(!chosen.equals("")) {
            if (chosen.equals("הנדסת חשמל") | chosen.equals("מדעי המחשב") | chosen.equals("מתמטיקה")) {
                second = new ArrayList<>();
                second.add("");
                mCourses = mFaculties.child(chosen);
                mCourses.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        second.add(dataSnapshot.getKey());
                        ArrayAdapter arrayAdapter2 = new ArrayAdapter(Main4Activity.this, android.R.layout.simple_spinner_item, second.toArray());
                        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        course = (Spinner) findViewById(R.id.Course);
                        course.setVisibility(View.GONE);
                        course.setOnItemSelectedListener(Main4Activity.this);
                        course.setAdapter(arrayAdapter2);
                        course.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            } else {
                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        time = (EditText) findViewById(R.id.Time);
                        call = (EditText) findViewById(R.id.Call);
                        room = (EditText) findViewById(R.id.enterRoom);
                        date=(EditText) findViewById(R.id.date);
                        String theInput=""+username+"_"+date.getText()+"_"+time.getText().toString()+"_"+room.getText().toString()+"_"+call.getText().toString();
                        DatabaseReference mCreate = mCourses.child(chosen);
                        System.out.println(mCreate.getKey());
                        DatabaseReference mCreate2 = mCreate.child(theInput);
                        DatabaseReference finallyDone = mCreate2.child(username);
                        finallyDone.setValue("Hello");

                    }
                });

            }
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
}

