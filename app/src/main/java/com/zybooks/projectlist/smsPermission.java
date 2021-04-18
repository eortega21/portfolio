package com.zybooks.projectlist;


import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
/*SMSPERMISSION will give permission to the user to use the SMS notification
* */
public class smsPermission extends AppCompatActivity {
    //variables
    static final int REQUEST_CODE = 123;
    Button btRequest, btCheck, goBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_permission);

        btRequest= findViewById(R.id.buttonGet);
        btCheck = findViewById(R.id.buttonCheck);
        goBack = findViewById(R.id.buttonBack);

        /*Button to request permission if it already does not have permission, provide it.
        * */
        btRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(ContextCompat.checkSelfPermission(smsPermission.this, Manifest.permission.SEND_SMS) != getPackageManager().PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(smsPermission.this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_CODE);
                }
            }
        });

        /*Button will check if we already have permission in our detail settings.
        * */
        btCheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",getPackageName(),null);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        /*go back bt will go back to the previous screen and finish this this screen.
        * */
        goBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                finish();
            }
        });
    }
}