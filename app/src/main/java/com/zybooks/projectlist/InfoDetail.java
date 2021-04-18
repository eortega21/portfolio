package com.zybooks.projectlist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

/*Info detail class provides the detail information about the current list item.
this will give details information included but not limited to:
- ID
- first & last name
- description
- grade
- pass/fail identifier
must give information about current item.
*/
public class InfoDetail extends AppCompatActivity{

    //VARIABLES

    DatabaseHelper db;
    TextView txtV_ID,txtV_fName,txtV_lName, txtV_details,txtV_grade,
             txtV_desc,txtV_grade_txt;

    EditText editTxt_fName, editTxt_lName, editTxt_desc;
    Button btn_exit, btn_update;
    Spinner spin;

    item stud;

    String item_name = "";
    Integer item_id;

    boolean editStudentInfo = false;

        @Override
        protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.info_detail);

            db = new DatabaseHelper(this);
            txtV_details = (TextView)findViewById(R.id.txtview_details);

            txtV_ID = (TextView)findViewById(R.id.txtV_infoDetail_id);
            txtV_fName = (TextView)findViewById(R.id.txtV_infoDetail_name_first);
            txtV_lName = (TextView)findViewById(R.id.txtV_infoDetail_name_Last);
            txtV_grade_txt = (TextView)findViewById(R.id.txtV_infoDetail_grade);
            txtV_grade = (TextView)findViewById(R.id.txtV_name_grade);
            txtV_desc = (TextView)findViewById(R.id.txtV_infoDetail_description);

            editTxt_fName = (EditText)findViewById(R.id.editTxt_infoDetail_fName);
            editTxt_lName = (EditText)findViewById(R.id.editTxt_infoDetail_lName);
            editTxt_desc = (EditText)findViewById(R.id.editTxt_infoDetail_description);

            spin = (Spinner)findViewById(R.id.spinner_choice);

            btn_exit = (Button)findViewById(R.id.btn_exit_infoDetail);
            btn_update = (Button)findViewById(R.id.btn_Update_infoUpdate);

            stud = new item();

            //change color on background
            //boo.setBackgroundResource(R.color.Green);

            Intent intent = getIntent();
            //item_name = intent.getStringExtra("the_name");
            item_id   = intent.getIntExtra("the_id",0);

            //if(!item_name.equals("")){
            //    txtV_details.setText("Details on: " + item_name + " ID: " + item_id);
            //}

            if(!item_id.equals("")) {
                stud =  db.searchStudentByID("studentInfo", item_id);

                txtV_ID.setText(String.valueOf(stud.id));
                txtV_fName.setText(stud.fname);
                txtV_lName.setText(stud.lname);
                txtV_grade_txt.setText(converGrade_int2String(stud.grades));
                gradeLevelColor(stud.grades);//change color on textbox
                txtV_grade.setText(String.valueOf(stud.grades));
                txtV_desc.setText(stud.description);
            }



            /*if the exit button is clicked, the user will exit the screen going back to main.
            * */
            btn_exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            /*if the update button was pressed create edit boxes that will allow the
            information to be updated. a pop up box will inform confirm the changes
            made.
            * */
            btn_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //make text view boxes invisible and edit text boxes visible.

                    if(editStudentInfo) {

                        //UPDATE THE GRADE LETTER SO I CAN SAVE IT.
                        updateStudentInfo(editTxt_fName.getText().toString(),editTxt_lName.getText().toString(),converGrade(spin.getSelectedItem().toString()),editTxt_desc.getText().toString());


                        txtV_fName.setVisibility(View.VISIBLE);
                        txtV_lName.setVisibility(View.VISIBLE);
                        txtV_desc.setVisibility(View.VISIBLE);
                        txtV_grade_txt.setVisibility(View.VISIBLE);

                        editTxt_fName.setVisibility(View.INVISIBLE);
                        editTxt_lName.setVisibility(View.INVISIBLE);
                        editTxt_desc.setVisibility(View.INVISIBLE);
                        spin.setVisibility(View.INVISIBLE);
                        editStudentInfo = false;
                    }
                    else{

                        editTxt_fName.setText(txtV_fName.getText().toString());
                        editTxt_lName.setText(txtV_lName.getText().toString());
                        editTxt_desc.setText(txtV_desc.getText().toString());

                        txtV_fName.setVisibility(View.INVISIBLE);
                        txtV_lName.setVisibility(View.INVISIBLE);
                        txtV_desc.setVisibility(View.INVISIBLE);
                        txtV_grade_txt.setVisibility(View.INVISIBLE);

                        editTxt_fName.setVisibility(View.VISIBLE);
                        editTxt_lName.setVisibility(View.VISIBLE);
                        editTxt_desc.setVisibility(View.VISIBLE);
                        spin.setVisibility(View.VISIBLE);

                        spin = (Spinner)findViewById(R.id.spinner_choice);
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.grade,android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin.setAdapter(adapter);

                        spin.setSelection(stud.grades);

                        editStudentInfo = true;
                    }

                    //current message sent to the user with an alert box.
                    /*
                    String alertTitle ="Updating ID " + item_id;
                    String alertMSG = "making changes in ID " + item_id + " are currently" +
                            "a work in progress";
                    alertMSG alert = new alertMSG();
                    alert.alertBoxOK(InfoDetail.this,alertTitle,alertMSG);
                    */

                }
            });
        }

        public boolean updateStudentInfo(String fname,String lname, int grade, String desc){
            boolean studentInfo_updating = false;
                //if updating first name
                if(!txtV_fName.getText().toString().equals(fname)){
                    //check first: if name is ok to save/eligibility
                    studentInfo_updating = true;
                }

                if(!txtV_lName.getText().toString().equals(lname)){
                    //check first: if name is ok to save/eligibility
                    studentInfo_updating = true;
                }

                if(!txtV_desc.getText().toString().equals(desc)){
                    //check first: if name is ok to save/eligibility
                    studentInfo_updating = true;
                }
                if(stud.grades != grade){
                    studentInfo_updating = true;
                }

                if(studentInfo_updating) {
                    //change to boolean.
                    db.update_studentInfo(stud.id, fname,lname, grade,desc);

                    studentInfoUpdated();

                    //current message sent to the user with an alert box.
                    String alertTitle ="Updated student: " + item_id;
                    String alertMSG = "student udpated";
                    alertMSG alert = new alertMSG();
                    alert.alertBoxOK(InfoDetail.this,alertTitle,alertMSG);
                }
            return studentInfo_updating;
        }

        public int converGrade(String grade){
            int gradeInt = 0;
            switch(grade){
                case "A":
                    gradeInt = 4;
                    break;
                case "B":
                    gradeInt = 3;
                    break;
                case "C":
                    gradeInt = 2;
                    break;
                case "D":
                    gradeInt = 1;
                    break;
                case "F":
                    gradeInt = 0;
                    break;
                default:
                    break;
            }
            return gradeInt;
        }
    public String converGrade_int2String(int grade){
        String gradeStr = "";
        switch(grade){
            case 0:
                gradeStr = "F";
                break;
            case 1:
                gradeStr = "D";
                break;
            case 2:
                gradeStr = "C";
                break;
            case 3:
                gradeStr = "B";
                break;
            case 4:
                gradeStr = "A";
                break;
            default:
                break;
        }
        return gradeStr;
    }

        public void studentInfoUpdated(){
            stud =  db.searchStudentByID("studentInfo", item_id);

            txtV_ID.setText(String.valueOf(stud.id));
            txtV_fName.setText(stud.fname);
            txtV_lName.setText(stud.lname);
            txtV_grade_txt.setText(converGrade_int2String(stud.grades));
            gradeLevelColor(stud.grades);
            txtV_grade.setText(String.valueOf(stud.grades));
            txtV_desc.setText(stud.description);

        }

        public void gradeLevelColor(int grade){
            switch(grade){
                case 0:
                case 1:
                    txtV_grade.setBackgroundResource(R.color.Red);
                    break;
                case 2:
                    txtV_grade.setBackgroundResource(R.color.Orange);
                    break;
                case 3:
                case 4:
                    txtV_grade.setBackgroundResource(R.color.Green);
                    break;
                default:
                    txtV_grade.setBackgroundResource(R.color.white);
                    break;
            }
        }
}

