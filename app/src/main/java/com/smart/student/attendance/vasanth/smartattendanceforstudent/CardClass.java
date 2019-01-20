package com.smart.student.attendance.vasanth.smartattendanceforstudent;

public class CardClass {
    private int indexNumber;
    private String nameOfPerson;

    public CardClass(int indexNumber, String nameOfPerson) {
        this.indexNumber = indexNumber;
        this.nameOfPerson = nameOfPerson;
    }

    public int getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(int indexNumber) {
        this.indexNumber = indexNumber;
    }

    public String getNameOfPerson() {
        return nameOfPerson;
    }

    public void setNameOfPerson(String nameOfPerson) {
        this.nameOfPerson = nameOfPerson;
    }
}
