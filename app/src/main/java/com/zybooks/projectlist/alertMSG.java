package com.zybooks.projectlist;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class alertMSG extends DialogFragment{
/*This class is created to to send custom notification to the user through an alert box
the user will get a pop up box with alerting them of anything required.
* */


    /*the alertBoxOK function will need the following:
    - current context using to display to box correctly.
    - a string for the title we would like to alert the user.
    - a string with the message we would like to inform the user about. this
      should be descriptive.

    + ok and cancel button included.
    * */
    public void alertBoxOK(Context passC,String alert_title, String alert_msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(passC);

        builder.setCancelable(true);
            builder.setTitle(alert_title);
            builder.setMessage(alert_msg);

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
