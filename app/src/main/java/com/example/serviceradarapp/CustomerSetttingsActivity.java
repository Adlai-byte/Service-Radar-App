package com.example.serviceradarapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CustomerSetttingsActivity extends AppCompatActivity {


    private EditText mNameField, mPhoneField;
    private Button mBack, mConfirm;

    //Adding Data to the database

    private FirebaseAuth mAuth;                         // 1.Create a Firebase instance
    private DatabaseReference mCustomerDatabase;        // 2.Create a Database Reference

    private String userID;
    private String mName;
    private String mPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_setttings);


        mNameField = (EditText) findViewById(R.id.name);
        mPhoneField =(EditText) findViewById(R.id.phone);

        mBack = (Button) findViewById(R.id.back);
        mConfirm= (Button) findViewById(R.id.confirm);


        mAuth = FirebaseAuth.getInstance();             // 3
        userID = mAuth.getCurrentUser().getUid();       // 4


        mCustomerDatabase= FirebaseDatabase.getInstance().getReference().child("Customers").child(userID); //5

        //11.Function call
        getUserInfo();

        //6.Create On Click Listener (Trigger)
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });


        //8. Back Button (Trigger)
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                return;

            }
        });

    }

    //9.  Get User Information
    private void getUserInfo(){
        mCustomerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //10. Add more details later
                if(snapshot.exists() && snapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    //Name
                    if(map.get("name")!=null){
                        mName = map.get("name").toString();
                        mNameField.setText(mName);
                    }
                    //Phone
                    if(map.get("phone")!=null){
                      mPhone = map.get("phone").toString();
                      mPhoneField.setText(mPhone);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    //7.Create a function for saving user information
    private void saveUserInformation() {
        // To get the most updated data
        mName = mNameField.getText().toString();
        mPhone = mPhoneField.getText().toString();

        Map userInfo = new HashMap();
        userInfo.put("name", mName);
        userInfo.put("phone", mPhone);
        mCustomerDatabase.updateChildren(userInfo);


        finish();
    }



}