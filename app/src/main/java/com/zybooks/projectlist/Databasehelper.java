package com.zybooks.projectlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;

/*The database helper class is design to use the SQLite database.  The user will need
  two databases
  1) database to keep the username and password
  2) database to provide the list.
* */
class DatabaseHelper extends SQLiteOpenHelper {
    Context context;
    /*this class will create the name of the database that we will be using.
    * */
    public DatabaseHelper(@Nullable Context context) {
        super(context, "List.db", null, 3);
    }
    /*onCreate will create the tables that we are going to be using for the application.
    The two tables that we will be using will
    1)a list info which will have the following:
        - ID as an integer and it will be the primary key. this integer will autoincrement.
        - Name as text, to get get the information of
        - School ID to identify the student.
    2)login table to verify the users with the following:
        - ID as an integer and it will be the primary key. this integer will autoincrement.
    * */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ListInfo (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, QTY TEXT)");
        db.execSQL("CREATE TABLE studentInfo (ID INTEGER PRIMARY KEY, FIRSTNAME TEXT, LASTNAME TEXT,GRADE INTEGER, DESCRIPTION TEXT)");
        db.execSQL("CREATE TABLE loginInfo (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, PW TEXT)");
        db.execSQL("CREATE TABLE loginApproval (ID INTEGER, USERNAME TEXT, PW TEXT)");
    }

    /*this function will update the versions for both the tables.
    * */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ListInfo");
        db.execSQL("DROP TABLE IF EXISTS loginInfo");
        db.execSQL("DROP TABLE IF EXISTS loginApproval");
        db.execSQL("DROP TABLE IF EXISTS studentInfo");
        onCreate(db);
    }
    /*boolean userLoging will take to strings.
        1) username
        2) password
      the database will verify if both the username and the password match what is
      stored in the database. if it matches, return true.
      if it does not match, return false.
    * */
    public boolean userLogin(String user_name, String user_pw) {

        boolean choice = false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM loginInfo",null);

        while(cursor.moveToNext()){

            //1 = id, 2 = name, 3 = quantity
            if(user_name.equals(cursor.getString(1)) && user_pw.equals(cursor.getString(2))){
                choice = true;
                //Log.d("match","true");
            }

            //Log.d("user","\tID: " + cursor.getString(0) + "\tname: " + cursor.getString(1) + "\tpw: " + cursor.getString(2));
        }
        return choice;
    }

    /*check if the userName exist that way we do not duplicate the same username
    in the database and cause bugs in the system. It will review through the database
    to see if it has the username available.
    * */
    public boolean checkUserExist(String table_name, String user_name){
        boolean choice = false;

        String sqlString = "SELECT * FROM " + table_name;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlString,null);

        while(cursor.moveToNext()){
            if(user_name.equals(cursor.getString(1))){
                choice = true;
                //Log.d("match","true");
            }
        }
        return choice;
    }


    public boolean userPendingApproval(Integer ID, String name, String pw){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", ID);
        contentValues.put("USERNAME", name);
        contentValues.put("PW",pw);

        long result = db.insert("loginApproval", null, contentValues);

        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    /*add user function will add the user to the database. it has two parameters
    1)username
    2)password
    the username and the password are both stored together and inserted to the database.
    if the result of the insert data was successful, report to user.
    */
    public boolean addUser(String nameEntered, String passwordEntered){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", nameEntered);
        contentValues.put("PW",passwordEntered);

        long result = db.insert("loginInfo", null, contentValues);

        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    /*insert data inserts information into the list.  it will include two parameters.
    1)name
    2)age
    this will include the name and age of the student into the database.if the
    result of the insert data was successful, report to user.
    * */

    public boolean insertData(String name, String age){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put("QTY",age);

        long result = db.insert("ListInfo", null, contentValues);

        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    /*WIP: enhancement to the way we can handle the data for both
      insertData() and addUser() can be combine to enhance code re-usability.
      param will be
      TABLE NAME, CONTENT VALUE NAME, THE NAME, CONTENT VALUE DATA, DATA
      this code will be reusable for any table that has 2 content values to insert.
    * */

    //ID INTEGER PRIMARY KEY, FIRSTNAME TEXT, LASTNAME TEXT,GRADE INTEGER, DESCRIPTION TEXT)")
    public boolean _insertData(String table_name,Integer ID, String first_name,String last_name,
                               int grade, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("ID", ID);
        contentValues.put("FIRSTNAME", first_name);
        contentValues.put("LASTNAME",last_name);
        contentValues.put("GRADE",grade);
        contentValues.put("DESCRIPTION",description);


        long result = db.insert(table_name, null, contentValues);

        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    /*delete data will need the user_id in order to delete form of an integer.
    When deleting from the list, we search through the database to find the ID.  Once
    the id is found, the id is deleted, if it is not found, inform the user by
    returning false.
    * */
    public boolean deleteData(int user_id) {
        SQLiteDatabase DB = this.getWritableDatabase();

        Cursor cursor = DB.rawQuery("Select * from ListInfo where ID = ?", new String[]{Integer.toString(user_id)});

        long result = DB.delete("studentInfo","ID=?",new String[]{Integer.toString(user_id)});
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }
    //ENHANCEMENT
    /*delete data will need the user_id in order to delete form of an integer.
    When deleting from the list, we search through the database to find the ID.  Once
    the id is found, the id is deleted, if it is not found, inform the user by
    returning false.
    * */
    public boolean delete_Data(String table, String user_name) {
        SQLiteDatabase DB = this.getWritableDatabase();

        long result = DB.delete(table,"USERNAME=?",new String[]{user_name});
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }


    /*obsolete.
    QTY update will update the quantity of each item that it contains.
    this will have two parameters.
    1) user id
    2) qty
    the function will look through the list and find the user id and update the
    qty,quantity, of the item.
    * */
    public void QTYupdate(int user_id, String QTY) {
        SQLiteDatabase DB = this.getWritableDatabase();

        ContentValues ContentValues = new ContentValues();
        ContentValues.put("QTY", QTY);

        DB.update("ListInfo",ContentValues,"ID=?",new String[]{Integer.toString(user_id)});

        //Log.d("ID found: ", "user ID: " + user_id + "\n QTY: " + QTY);
    }
    //enhance for update student
    //db.execSQL("CREATE TABLE studentInfo (ID INTEGER PRIMARY KEY, FIRSTNAME TEXT, LASTNAME TEXT,GRADE INTEGER, DESCRIPTION TEXT)");
    public void update_studentInfo(int user_id, String fname, String lname, int grade, String desc){
        SQLiteDatabase DB = this.getWritableDatabase();

        ContentValues ContentValues = new ContentValues();
        ContentValues.put("FIRSTNAME", fname);
        ContentValues.put("LASTNAME", lname);
        ContentValues.put("GRADE",grade);
        ContentValues.put("DESCRIPTION",desc);

        DB.update("studentInfo",ContentValues,"ID=?",new String[]{Integer.toString(user_id)});
    }


    /*to get all data from the list, the user can call out get all data and
    it will search through the database to return everyone in the list.
    ENHANCEMENTS: can include a param to pull a table.
    getAllData(String table_name) so we may pull the table we select with
    3 variables. ID, name, data.
    * */
    public ArrayList<item> getAllData(){

        ArrayList<item> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ListInfo",null);

        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String qty = cursor.getString(2);

            item Item = new item(id, name, qty);

            arrayList.add(Item);
        }
        return arrayList;
    }


    //ID INTEGER PRIMARY KEY, FIRSTNAME TEXT, LASTNAME TEXT,GRADE INTEGER, DESCRIPTION TEXT)")
    public ArrayList<item> getAllStudentData(){

        ArrayList<item> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM studentInfo",null);

        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String fname = cursor.getString(1);
            String lname = cursor.getString(2);
            int grade = cursor.getInt(3);
            String desc = cursor.getString(4);

            item Item = new item(id, fname,lname, grade, desc);

            arrayList.add(Item);
        }
        return arrayList;
    }

    //ENHANCEMENT AND WILL REPLACE getAllData()
    public ArrayList<item> getAll_Data(String table){
        ArrayList<item> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String getTable = "SELECT * FROM " + table;

        Cursor cursor = db.rawQuery(getTable,null);

        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String pw = cursor.getString(2);

            item Item = new item(id, name, pw);
            arrayList.add(Item);
        }
        return arrayList;
    }

    //APPROACH THIS DIFFERENTLY -- ADJUST IT. change arraylist type as we only want one person.
    public item searchStudentByID(String table, int search_ID){
        item student = new item();
        SQLiteDatabase db = this.getReadableDatabase();

        //String query = "SELECT * FROM  ListInfo WHERE NAME = "+search_name;
        String query = "select * FROM " + table;

        Cursor cursor = db.rawQuery(query,null);
        item Item;
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String fname = cursor.getString(1);
            String lname = cursor.getString(2);
            int grade = cursor.getInt(3);
            String desc = cursor.getString(4);
            if(id == search_ID) {
                Item = new item(id, fname);
                student.setId(id);
                student.setfName(fname);
                student.setlName(lname);
                student.setGradeN(grade);
                student.setDescription(desc);
            }
        }
        return student;
    }

    public ArrayList<item> searchDataByID(String table, int search_ID) {
        ArrayList<item> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        //String query = "SELECT * FROM  ListInfo WHERE NAME = "+search_name;
        String query = "select * FROM " + table + " WHERE ID =?";

        Cursor cursor = db.rawQuery(query, new String[]{Integer.toString(search_ID)});
        item Item;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            if (id == search_ID) {
                Item = new item(id, name);
                arrayList.add(Item);
            }
        }
        return arrayList;
    }

    public ArrayList<item> searchDataByName(String table, String search_name){
        ArrayList<item> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        //String query = "SELECT * FROM  ListInfo WHERE NAME = "+search_name;
        String query = "select * FROM " + table;

        Cursor cursor = db.rawQuery(query,null);
        item Item;
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
                if(name.equals(search_name)) {
                    Item = new item(id, name);
                    arrayList.add(Item);
                }
        }
        return arrayList;
    }
    // long result = DB.delete(table,"USERNAME=?",new String[]{user_name});
    public ArrayList<item> orderDataByName(String table, String level){
        ArrayList<item> arrayList = new ArrayList<item>();
        SQLiteDatabase db = this.getReadableDatabase();

        //level can be ASC = ASCENDING or DESC = DESCENDING
        String query = "SELECT * FROM " + table + " ORDER BY FIRSTNAME "+ level;

        Cursor cursor = db.rawQuery(query,null);
        item Item;
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            Item = new item(id, name);
            arrayList.add(Item);
        }
        return arrayList;
    }


}