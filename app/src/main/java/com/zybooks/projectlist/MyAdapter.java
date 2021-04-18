package com.zybooks.projectlist;


import android.Manifest;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;

/*MY ADAPTER CLASS will display on how each item will display as
it will have the option to delete the item, and increase/decrease grade

* */
public class MyAdapter extends BaseAdapter implements ListAdapter {

    Context context;
    ArrayList<item>arrayList;
    DatabaseHelper db;
    String deleteItem;
    ImageButton deleteButton;

    private NotificationManagerCompat notificationManager;

    public MyAdapter(Context context, ArrayList<item> arrayList){
        this.context = context;
        this.arrayList = arrayList;

    }

/*get the current size of the array list
* */
    @Override
    public int getCount() {

        return this.arrayList.size();
    }
/*get the current item position that it is in.
* */
    @Override
    public Object getItem(int position) {

        return arrayList.get(position);
    }
/*getItemId default:get the position of the from the id.
* */
    @Override
    public long getItemId(int position) {
        return position;
    }
/*get view is what is displays in each item list.  this is customizeable and can include
anything we like for each item to display.
Currently each item should hold
    - delete button
    - display name
    - grade
    - increase and decrease button for grade
* */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //variables for each item list.
        notificationManager = NotificationManagerCompat.from(context);


        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.mycustomlistview,null);



        TextView t2_name = (TextView)convertView.findViewById(R.id.name_txt);
        TextView t3_Grade = (TextView)convertView.findViewById(R.id.customlist_studentID);

        deleteButton = (ImageButton) convertView.findViewById(R.id.button_delete);
        //deleteButton.setVisibility(convertView.INVISIBLE);

        db = new DatabaseHelper(context.getApplicationContext());

        item Item = arrayList.get(position);

        t2_name.setText(Item.getfName());
        t3_Grade.setText(String.valueOf(Item.getId()));

/*ENHANCEMENT:
When clicked on the display name, get the name and id and save it so we can pass
it to the detail class.  It will open it afterwards with all of student information.
  */

        convertView.setClickable(true);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newWindow = new Intent(context,InfoDetail.class);
                newWindow.putExtra("the_name",Item.getfName());
                newWindow.putExtra("the_id",Item.getId());
                context.startActivity(newWindow);
            }
        });
//end new


        deleteItem = t2_name.getText().toString();

        //ImageButton buttonDecreaseQty = (ImageButton) convertView.findViewById(R.id.button_decrease);

        //ImageButton buttonAddQty = (ImageButton) convertView.findViewById(R.id.button_Increase);


/*the delete button will delete the entry that the user wanted to delete
it will check if it is delete able first prior to moving forward to make sure it was
successfully deleted.  then notify the list in main activity.
* */
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                deleteItem = t2_name.getText().toString();

                String Title = "Deleting: " + Item.getfName();
                String msg = "Are you sure you would like to delete ID #: " + Item.getId() +
                        " named " + Item.getfName() + ".";

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setTitle(Title);
                builder.setMessage(msg);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //SEND MSG TO ACTIVITY TO UPDATE. passing ID for testing purposes only.
                        Intent intent = new Intent("custom-message");
                        intent.putExtra("id",String.valueOf(Item.getId()));//sending ID incase I want to delete it from main screen.
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                        //                //DELETE
                        boolean checkdeleteddata = db.deleteData(Item.getId());
                        if(checkdeleteddata == true){
                            Toast.makeText(context, "Success: Entry deleted: " + deleteItem, Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(context, "Unsuccessful: Entry not deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Canceled: Entry not deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create().show();
            }
        });

/*button decrease will check the amount it currently has, there will be a grading scale
from 0-4 with 0 being F and 4 being A. it will display a message when the student is
has a C and below.
* */
        /*
        buttonDecreaseQty.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String QtyString = t3_Grade.getText().toString();
                int QtyInt = Integer.parseInt(QtyString);

                if(QtyInt > 0){
                    QtyInt = QtyInt - 1;
                    db.QTYupdate(Item.getId(), Integer.toString(QtyInt));
                }
                if(QtyInt< 3){
                    String stud_status = "error";

                    switch(QtyInt){
                        case 0:
                            stud_status = Item.getfName() + " is failing with an F";
                            break;
                        case 1:
                            stud_status = Item.getfName() + " is almost failing with a D";
                            break;
                        case 2:
                            stud_status = Item.getfName() + " needs improvement";
                            break;
                        case 3:
                            stud_status = Item.getfName() + " has a B. good job!";
                            break;
                        default:
                            break;
                    }

                    if(ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != -1){
                        sendOnChannel1(stud_status,QtyInt);
                    }
                }
                t3_Grade.setText(String.valueOf(QtyInt));
            }
        });
*/
/*button Add will increase the students grade and it will have a max cap of 4 since the
max grade the student can have is a A which equals a 4.
* */
/*
        buttonAddQty.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something

                String QtyString = t3_Grade.getText().toString();
                int QtyInt = Integer.parseInt(QtyString) + 1;
                if(QtyInt > 4){
                    QtyInt = 4;
                }
                t3_Grade.setText(String.valueOf(QtyInt));

                //REPORT TO DATABASE TO RECORD
                db.QTYupdate(Item.getId(),Integer.toString(QtyInt));
                }
        });*/
        return convertView;
    }


    /*the function will serve the purpose of sending notification to through SMS.
    this will let the user know in a different format then an alert box.
    incase notification needs to be sent with app away.
    * */
    public void sendOnChannel1(String msg, int qty){


        Notification notification = new NotificationCompat.Builder(context, com.zybooks.projectlist.notification.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle("UPDATE")
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1,notification);
    }


    /*ENHANCEMENT.
    an alert box that can be customized to reflect a notification to the user using alert box.
    current msg is hardcoded and can improve by having an option to customize the message.
    this message box will have option:
    - ok
    - cancel

    * */
    public void alertBox(String title, int msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(msg);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //DO SOMETHING
                //Toast.makeText(context,"worked",Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //DO SOMETHING
            }
        });
        builder.create().show();
    }


}