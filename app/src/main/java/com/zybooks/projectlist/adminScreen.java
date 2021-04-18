package com.zybooks.projectlist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;

public class adminScreen extends AppCompatActivity {

    DatabaseHelper db;
    ArrayList<item> arrayList;
    adapterList myAdapter;
    ListView approvalList;
    Button exitButton, refreshButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_screen);

        approvalList = (ListView)findViewById(R.id.listView_pending_approval);
        exitButton = (Button)findViewById(R.id.admin_exit_button);
        refreshButton = (Button)findViewById(R.id.admin_refresh_button);

        db = new DatabaseHelper(this);
        arrayList = new ArrayList<>();
        loadDataInListView();

        //REFRESH BUTTON
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDataInListView();
            }
        });

        //EXIT BUTTON
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //BROADCAST
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
               new IntentFilter("adapterList_to_adminScreen"));
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            //String ItemName = intent.getStringExtra("id");
            //Toast.makeText(MainActivity.this,"ID: " + ItemName,Toast.LENGTH_SHORT).show();
            loadDataInListView();
        }
    };


    public void loadDataInListView(){
        //TESTING getAll_Data(table_name)
        arrayList = db.getAll_Data("loginApproval");
        myAdapter = new adapterList(this, arrayList);
        approvalList.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
    }
}
