package com.android.firstlearners.learners.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.android.firstlearners.learners.R;

public class SignUpActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;

    @BindView(R.id.phoneNumber)
    EditText editPhoneNumber;

    @BindView(R.id.btn_complete)
    TextView btn_complete;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        editPhoneNumber.setText(getDevicePhoneNumber());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick(R.id.btn_complete)
    public void OnClick(){
        String phoneNumber = editPhoneNumber.getText().toString();
        if(phoneNumber.length() == 11){
            Intent intent = new Intent();
            intent.putExtra("phoneNumber",phoneNumber);
            setResult(RESULT_OK,intent);
            finish();
        }
        else{
            Toast.makeText(this,"휴대전화 번호를 다시 확인해주세요.",Toast.LENGTH_LONG).show();
        }
    }


    private String getDevicePhoneNumber(){
        TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if(checkPermission()){

            String phoneNumber = manager.getLine1Number();
            if(phoneNumber.startsWith("+82")){
                phoneNumber = phoneNumber.replace("+82", "0");
            }
            return phoneNumber;
        }

        requestPermission();
        return "";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    editPhoneNumber.setText(getDevicePhoneNumber());
                }
                break;
        }
    }

    private boolean checkPermission(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},PERMISSION_REQUEST_CODE);
    }
}
