package com.gamisweb.utility;

import java.io.Serializable;

public class ExamInfo implements Serializable

{
    /**
     *
     */
    private static final long serialVersionUID = 2L;
    private String examDatabaseID;
    private String examTitle;
    private String examAuthor;
    private String examDescript;
    private boolean localObject;

    public ExamInfo() {
        examDatabaseID = "";
        examTitle = "";
        examAuthor = "";
        examDescript = "";
    }

    public ExamInfo(String i, String t, String a, String d, boolean l) {
        this.examDatabaseID = i;
        this.examTitle = t;
        this.examAuthor = a;
        this.examDescript = d;
        this.localObject = l;
    }

    public String getExamDatabaseID() {
        return examDatabaseID;
    }

    public void setExamDatabaseID(String examDatabaseID) {
        this.examDatabaseID = examDatabaseID;
    }

    public String getExamTitle() {
        return examTitle;
    }

    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
    }

    public String getExamAuthor() {
        return examAuthor;
    }

    public void setExamAuthor(String examAuthor) {
        this.examAuthor = examAuthor;
    }

    public String getExamDescript() {
        return examDescript;
    }

    public void setExamDescript(String examDescript) {
        this.examDescript = examDescript;
    }

    public boolean isLocalObject() {
        return localObject;
    }

    public void setLocalObject(boolean localObject) {
        this.localObject = localObject;
    }

    public ExamInfo copy(ExamInfo toCopy) {
        ExamInfo newCopy = new ExamInfo();
        newCopy.examDatabaseID = toCopy.examDatabaseID;
        newCopy.examTitle = toCopy.examTitle;
        newCopy.examAuthor = toCopy.examAuthor;
        newCopy.examDescript = toCopy.examDescript;
        newCopy.localObject = toCopy.localObject;

        return newCopy;

    }

}







