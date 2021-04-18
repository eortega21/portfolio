package com.zybooks.projectlist;


/*DATA  in item list
each item list will have their data listed and it will contain
the id, the name and the grade, this is an enhancement from complete project of the list.

* */
public class item {


        int id,grades;
        String fname,lname,grade, description;

    //ID INTEGER PRIMARY KEY, FIRSTNAME TEXT, LASTNAME TEXT,GRADE INTEGER, DESCRIPTION TEXT)")
        public item(int id, String fname, String lname, int grade, String desc){
           this.id = id;
           this.fname = fname;
           this.lname = lname;
           this.grades = grade;
           this.description = desc;
        }
        public item(int id, String name, String qty){
            this.id = id;
            this.fname = name;
            this.grade = qty;
        }
        public item(int id, String name){
            this.id = id;
            this.fname = name;
        }
        public item(){

        }
//this will get id from the item
        public int getId() {
            return id;
        }
//this will set the id for specific item
        public void setId(int id) {
            this.id = id;
        }
//this will get the name from item

        public String getfName() {
            return fname;
        }
//this will set the name for item
        public void setfName(String name) {
            this.fname = name;
        }

//this will set the last name
        public void setlName(String lname){
            this.lname = lname;
        }

        public String getlName(){
            return lname;
        }
        //this will set the grade
        public void setGrade(String grade) {
            this.grade = grade;
        }
        //this will get the grade
        public String getGrade() {
            return grade;
        }

        //this will set the grade
        public void setGradeN(int grade) {
            this.grades = grade;
        }
        //this will get the grade
        public int getGradeN() {
            return grades;
        }



        public void setDescription(String desc){
            this.description = desc;
        }

        public String getDescription(){
            return description;
        }


}
