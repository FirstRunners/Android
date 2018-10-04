package com.android.firstlearners.learners.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;

import com.android.firstlearners.learners.R;
import com.android.firstlearners.learners.etc.Address;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InviteActivity extends AppCompatActivity{
    private static final int PERMISSION_REQUEST_CODE = 1;

    @BindView(R.id.listOfPhoneNumber)
    RecyclerView recyclerView;
    @BindView(R.id.selected_item)
    GridView gridView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private List<Address> addressList;
    private List<Address> selectedItems;
    private List<Boolean> flag;
    private InviteViewAdapter inviteViewAdapter;
    private GridViewAdapter gridViewAdapter;

    private View.OnClickListener inviteViewListener;
    private View.OnClickListener gridViewListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        addressList = new ArrayList<>();
        selectedItems = new ArrayList<>();
        flag = new ArrayList<>();
        if(checkPermission()){
            getPhoneNumberList();
        }
        else{
            requestPermission();
        }

        inviteViewAdapter = new InviteViewAdapter(addressList);
        inviteViewAdapter.addItem(selectedItems);
        recyclerView.setAdapter(inviteViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        gridViewAdapter = new GridViewAdapter(selectedItems);
        gridView.setAdapter(gridViewAdapter);

        inviteViewListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = (int)v.getTag();
                boolean temp = !flag.get(position);

                if(temp){
                    Address address = addressList.get(position);
                    selectedItems.add(address);
                }else{
                    for(Address address : selectedItems){
                        if(address.position == position){
                            selectedItems.remove(address);
                            break;
                        }
                    }
                }
                flag.set(position,temp);

                inviteViewAdapter.addItem(selectedItems);
                gridViewAdapter.addItem(selectedItems);

                inviteViewAdapter.notifyDataSetChanged();
                gridViewAdapter.notifyDataSetChanged();
            }
        };

        inviteViewAdapter.setInviteViewListener(inviteViewListener);

        gridViewListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = (int)v.getTag();

                Address address = selectedItems.get(position);

                flag.set(address.position, false);

                selectedItems.remove(position);

                inviteViewAdapter.addItem(selectedItems);
                gridViewAdapter.addItem(selectedItems);

                gridViewAdapter.notifyDataSetChanged();
                inviteViewAdapter.notifyDataSetChanged();
            }
        };

        gridViewAdapter.setGridViewListener(gridViewListener);
    }

    private void getPhoneNumberList(){
        int count = 0;
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                },null,null,null);

        if(cursor != null && cursor.moveToFirst()){
            do{
                String name = cursor.getString(0);
                String phoneNumber = cursor.getString(1);
                Address address = new Address(name, phoneNumber);
                address.position = count++;
                addressList.add(address);
                flag.add(false);
            }while(cursor.moveToNext());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPhoneNumberList();
                    inviteViewAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private boolean checkPermission(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},PERMISSION_REQUEST_CODE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
