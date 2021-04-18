package com.zybooks.projectlist;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class registerStudent extends AppCompatActivity {
    EditText editText_reg_ID,editTxt_reg_firstName,editTxt_reg_lastName, editTxt_reg_grade, editTxt_reg_description;
    Button btn_reg_addStudent, btn_reg_cancel;
    Spinner spin_grade;
    DatabaseHelper db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);

        editText_reg_ID = (EditText)findViewById(R.id.txtV_register_student_ID);
        editTxt_reg_firstName = (EditText)findViewById(R.id.txtV_register_student_first_name);
        editTxt_reg_lastName = (EditText)findViewById(R.id.txtV_register_student_last_name);
        editTxt_reg_grade = (EditText)findViewById(R.id.txtV_register_student_grade);
        editTxt_reg_description = (EditText)findViewById(R.id.txtV_register_student_Description);

        btn_reg_addStudent = (Button)findViewById(R.id.btn_register_Add_student);
        btn_reg_cancel = (Button)findViewById(R.id.btn_register_cancel);

        spin_grade = (Spinner)findViewById(R.id.register_spinner_grade);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.grade,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_grade.setAdapter(adapter);

        db = new DatabaseHelper(getApplicationContext());


        btn_reg_addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isAllRequireDataEntered()){
                    //CREATE POP UP TO VERIFY INFORMATION FIRST PRIOR TO ADDING.

                    //TEMPORARILY GOING STRAIGHT TO ADDING.
                    boolean result1 = db.insertData(editTxt_reg_firstName.getText().toString(),editTxt_reg_grade.getText().toString());
                    boolean result = db._insertData("studentInfo", Integer.parseInt(editText_reg_ID.getText().toString()),editTxt_reg_firstName.getText().toString(),
                            editTxt_reg_lastName.getText().toString(),converGrade(spin_grade.getSelectedItem().toString()),editTxt_reg_description.getText().toString());


                    finish();
                    Toast.makeText(getApplicationContext(),result1+"Form complete, thank you! _:"+result,Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please complete the form",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_reg_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear_reg_text();
                finish();
            }
        });

    }

    public void clear_reg_text(){
        editText_reg_ID.setText("");
        editTxt_reg_firstName.setText("");
        editTxt_reg_lastName.setText("");
        editTxt_reg_grade.setText("");
        editTxt_reg_description.setText("");

   }

    //this will check if all the required boxes are filled out before entering it to the database

    public boolean isAllRequireDataEntered(){
        boolean reqComplete = false;

        if(editText_reg_ID.getText().toString().isEmpty() ||
                editText_reg_ID.getText().toString().length() != 5 ||
                editTxt_reg_firstName.getText().toString().isEmpty() ||
                editTxt_reg_lastName.getText().toString().isEmpty()){
            reqComplete = false;
        }
        else{
            reqComplete = true;
        }

        return reqComplete;
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


}
