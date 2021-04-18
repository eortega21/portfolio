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

public class adapterList extends BaseAdapter implements ListAdapter {

    Context context;
    ArrayList<item> arrayList;
    DatabaseHelper db;
    String deleteItem;
    ImageButton deleteButton, approveButton;

    public adapterList(Context context, ArrayList<item> arrayList) {
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

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.admin_listview, null);


        TextView t2_name = (TextView) convertView.findViewById(R.id.txtV_admin_user_name);
        TextView t3_Grade = (TextView) convertView.findViewById(R.id.txtV_admin_user_ID);

        deleteButton = (ImageButton) convertView.findViewById(R.id.btn_admin_delete);

        approveButton = (ImageButton)convertView.findViewById(R.id.btn_admin_approve);

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
                Intent newWindow = new Intent(context, InfoDetail.class);
                newWindow.putExtra("the_name", Item.getfName());
                newWindow.putExtra("the_id", Item.getId());
                context.startActivity(newWindow);
            }
        });
//end new

            approveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add user requires name and password
                    //HOW DO I COLLECT THE INFORMATION TO PASS?
                    //config the item class to catch pw ass well.

                    boolean user_added = db.addUser(Item.getfName(),Item.getGrade());
                    if(user_added){
                        boolean delete_from_approval_list = db.delete_Data("loginApproval",Item.getfName());
                        if(delete_from_approval_list) {
                            Toast.makeText(context, Item.getId() + ": " + Item.getfName() + ", was added and removed from approval list", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent("adapterList_to_adminScreen");
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                            //approveButton.setVisibility(View.GONE);
                        }
                        else{
                            Toast.makeText(context, Item.getId() + ": " + Item.getfName() + ", was added and NOT removed from approval list", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(context, Item.getId() + ": " + Item.getfName() + ", was NOT added.", Toast.LENGTH_SHORT).show();
                    }
                }
            });



/*the delete button will delete the entry that the user wanted to delete
it will check if it is delete able first prior to moving forward to make sure it was
successfully deleted.  then notify the list in main activity.
* */
        deleteItem = t2_name.getText().toString();
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem = t2_name.getText().toString();

                //DELETE
                //UPGRADE CHECKDELETEDDATA FUNCITON
                boolean checkdeleteddata = db.delete_Data("loginApproval",Item.getfName());
                if (checkdeleteddata == true) {
                    Toast.makeText(context, "Entry deleted: " + deleteItem, Toast.LENGTH_SHORT).show();

                    //BROADCAST AS action_name
                    Intent intent = new Intent("adapterList_to_adminScreen");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                } else {
                    Toast.makeText(context, "Entry not deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return convertView;
    }

}



