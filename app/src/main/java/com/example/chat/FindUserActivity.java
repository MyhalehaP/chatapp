package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FindUserActivity extends AppCompatActivity {

    private RecyclerView mUserList;
    private RecyclerView.Adapter mUserListAdapter;
    private RecyclerView.LayoutManager mUserListLayoutManager;

    ArrayList<UserObject> userList, contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);

        contactList = new ArrayList<>();
        userList = new ArrayList<>();

        initializeRecyclerView();
        getContactList();

    }

    private void getContactList(){

        String ISOPrefix = getCountryISO();

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,
                null,null);

        while (phones.moveToNext()){
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            phone = phone.replace(" ","");
            phone = phone.replace("-","");
            phone = phone.replace("(","");
            phone = phone.replace(")","");

            if(!String.valueOf(phone.charAt(0)).equals("+"))
                phone = ISOPrefix + phone;

            UserObject mContact = new UserObject(name,phone);
            contactList.add(mContact);
            getUserDetails(mContact);
        }

    }

    private void getUserDetails(final UserObject mContact) {

        DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("user");
        Query query = mUserDB.orderByChild("phone").equalTo(mContact.getPhone());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String phone = "", name = "";
                    for(DataSnapshot childSnapshots : dataSnapshot.getChildren()){
                        if(childSnapshots.child("phone").getValue() != null){
                            phone = childSnapshots.child("phone").getValue().toString();
                        }
                        if(childSnapshots.child("name").getValue() != null){
                            name = childSnapshots.child("name").getValue().toString();
                        }

                        UserObject mUser = new UserObject(name,phone);
                        if(name.equals(phone)){
                            for(UserObject mContactIterator: contactList){
                                if(mContactIterator.getPhone().equals(mUser.getPhone())){
                                    mUser.setName(mContactIterator.getName());

                                }
                            }
                        }


                        userList.add(mUser);
                        mUserListAdapter.notifyDataSetChanged();
                        return;

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    }

    private String getCountryISO(){
        String iso = null;

        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        if(telephonyManager.getNetworkCountryIso() != null)
            if(!telephonyManager.getNetworkCountryIso().toString().equals(""))
                iso = telephonyManager.getNetworkCountryIso().toString();

        return CountryToPhonePrefix.getPhone(iso);
    }

    private void initializeRecyclerView() {
        mUserList = findViewById(R.id.userList);
        mUserList.setNestedScrollingEnabled(false);
        mUserList.setHasFixedSize(false);

        mUserListLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        mUserList.setLayoutManager(mUserListLayoutManager);

        mUserListAdapter = new UserListAdapter(userList);
        mUserList.setAdapter(mUserListAdapter);

    }


}
