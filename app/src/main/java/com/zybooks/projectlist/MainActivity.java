package com.zybooks.projectlist;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/*MAIN ACTIVITY SCREEN - this will contain the search button for
either ID or by name. if no search id or search name provided, it will search thu
all in the list. if we are unable to find the person in the list. we can add them
to the list.
* */
public class MainActivity extends AppCompatActivity {

    //VARIABLES
    private Menu mMenu;
    EditText editTxt_studentName, editTxt_studentID;
    DatabaseHelper db;
    ListView l1;
    ArrayList<item> arrayList;
    MyAdapter myAdapter;
    Button alert, adminAdd,btnSearch,btnAdd;


/*on create will manage the variables and list.  It will also listen to any
changes.
* */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTxt_studentName = (EditText)findViewById(R.id.editText_item_name);
        editTxt_studentID = (EditText)findViewById(R.id.editText_qty);

        btnSearch = (Button)findViewById(R.id.btn_search);
        adminAdd = (Button)findViewById(R.id.bt_admin);

        l1 = (ListView)findViewById(R.id.listView);
        db = new DatabaseHelper(this);
        arrayList  = new ArrayList<item>();
        loadDataInListView();



        //BROADCAST
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));
        //USING BOTH INTENT AND BROADCAST, SOME SIMILARITIES.
        //INTENT
        Bundle bundle = getIntent().getExtras();
        String current_user = bundle.getString("user_Name");

        //admin will be the only one able to see button to approve user registering.
        if(current_user.equals("admin")){
            adminAdd.setVisibility(View.VISIBLE);
        }
        adminAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to pending to approve
                Intent listOfApprovals = new Intent(MainActivity.this,adminScreen.class);
                //openInventory.putExtra("user_Name", user_name);
                startActivity(listOfApprovals);
            }
        });

        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this,"button pressed",Toast.LENGTH_SHORT).show();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTxt_studentID.getText().toString().isEmpty()){
                    item student;
                    arrayList.clear();
                    arrayList.add (db.searchStudentByID("studentInfo",Integer.parseInt(editTxt_studentID.getText().toString())));
                    myAdapter = new MyAdapter(MainActivity.this,arrayList);
                    l1.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();
                }
                else if(!editTxt_studentName.getText().toString().isEmpty()){
                    arrayList = db.searchDataByName("studentInfo", editTxt_studentName.getText().toString());
                    myAdapter = new MyAdapter(MainActivity.this, arrayList);
                    l1.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();
                }
                else{
                    loadDataInListView();
                }
            }
        });


    }



    public void sortNameBy(String level){
        arrayList = db.orderDataByName("studentInfo",level);
        myAdapter = new MyAdapter(this, arrayList);
        l1.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
    }


    public void searchPersonByName(View view){
        if(!editTxt_studentID.getText().toString().isEmpty()){
            item student = new item();
            student = db.searchStudentByID("studentInfo",Integer.parseInt(editTxt_studentID.getText().toString()));
            arrayList.clear();
            arrayList.add(student);
            myAdapter = new MyAdapter(this,arrayList);
            myAdapter.notifyDataSetChanged();
        }
        //else if(editTxt_studentName.getText().toString().isEmpty()){
            //loadDataInListView();
        //}
        else {
            arrayList = db.searchDataByName("studentInfo", editTxt_studentName.getText().toString());
            myAdapter = new MyAdapter(this, arrayList);
            l1.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
        }
    }

/*a broadcastreceiver will run to update the list when needed from my adapter class.
 this is only intended to update the listing in the list from other class.
* */
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String ItemName = intent.getStringExtra("id");
            //Toast.makeText(MainActivity.this,"ID: " + ItemName,Toast.LENGTH_SHORT).show();
            loadDataInListView();
        }
    };

    /*this function will run to retrieve the information from the database,
    apply it to the adapter which is handles the view and then applied to the list
    the adapter will be update to make sure the changes are applied.
    * */
    public void loadDataInListView(){
        //arrayList = db.getAllData();
        arrayList = db.getAllStudentData();
        myAdapter = new MyAdapter(this, arrayList);
        l1.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
    }


/*insert function will make sure we have the name of the person and the grade, checking before
accepting the insert to the database. if either name of grade is empty, the user must
re enter information before proceeding. once its okay, they can save to database and update
the list.
* */
    //this is the same thing as btnAdd.  I can make it listn or i can call from xml.
    public void insert(View v){

        //OPEN NEW INTENT TO ADD STUDENT
        Intent studRegForm = new Intent(MainActivity.this,registerStudent.class);
        startActivity(studRegForm);

    }
/*create a menu that way the user can choose if they would like to get sms notification
this menu can also hold other buttons for ease of use for user.
* */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }
/*the onOptionsItemSelected will be what action to take for the menu
it will listen to the choices that menu provides and provide an action to each one.
there will be a case for every available option. default - n/a.
* */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_one:
                //Toast.makeText(MainActivity.this, "edit later",Toast.LENGTH_SHORT).show();
                //myAdapter.hideDeleteButton();
                loadDataInListView();
                break;

            case R.id.action_done:
                finish();
                //loadDataInListView();
                break;

            case R.id.SMSpermission:
                Intent permSMS = new Intent(this, smsPermission.class);
                startActivity(permSMS);
                break;

            case R.id.action_sort_AZ:
                sortNameBy("ASC");
                break;

            case R.id.action_sort_ZA:
                sortNameBy("DESC");
                break;

            default:
                break;
        }
        return super.onContextItemSelected(item);
    }
}