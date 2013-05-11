package com.gamisweb.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class ExamDBHelper{
 
    private static final String TAG = "ExamDBHelper";
    private static final String DATABASE_NAME = "exam_database";
    private static final int DATABASE_VERSION = 1;      //Database Version
    
    /**
     * Context
     */
    private final Context mCtx;
 
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
     //Table Name
    public static final String EXAM_TABLE = "tb_Exams";
    public static final String EXAM_QUESTION_TABLE = "tb_Questions";
 
    /**
     * Exam_Table columns
     */
	public static final String KEY_TITLE = "Title"; //Also used 
 //   public String getTitle(){return KEY_TITLE;}
    public static final String KEY_AUTHOR= "Author";
 //   public String getAuthor(){return KEY_AUTHOR;}
    public static final String KEY_EXAMROWID = "_id"; 
        
    /**
     * Question_Table columns
     */
    //KEY_TITLE is used in the Question Table
    public static final String KEY_QUESTIONROWID = "_id";
    public static final String KEY_EXAMQUESTION = "ExamQuestion";
    public static final String KEY_SECTION = "Section";
    public static final String KEY_ANSWERA = "AnswerA";
    public static final String KEY_ANSWERB = "AnswerB";
    public static final String KEY_ANSWERC = "AnswerC";
    public static final String KEY_ANSWERD = "AnswerD";
    public static final String KEY_CORRECTANSWER = "CorrectAnswer";

    /**
    *Database creation sql statement
    */
   private static final String CREATE_EXAM_TABLE = "CREATE TABLE IF NOT EXISTS " + EXAM_TABLE + " (" + KEY_EXAMROWID + " integer primary key autoincrement, "
        + KEY_TITLE +" text not null, " + KEY_AUTHOR + " text not null);";
    
    private static final String CREATE_QUESTION_TABLE = "CREATE TABLE IF NOT EXISTS " + EXAM_QUESTION_TABLE + " (" + KEY_QUESTIONROWID + " integer primary key autoincrement, "
        +KEY_TITLE +" text not null, "+KEY_SECTION +" text not null, " +KEY_EXAMQUESTION +" text not null, " + KEY_ANSWERA + " text not null, "+ KEY_ANSWERB +" text not null, "
            		+ KEY_ANSWERC +" text not null, " + KEY_ANSWERD +" text not null, "+ KEY_CORRECTANSWER +" text not null);";
      
    /**
     * Inner private class. Database Helper class for creating and updating database.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        /**
         * onCreate method is called for the 1st time when database doesn't exists.
         */
        @Override
        public void onCreate(SQLiteDatabase mDb) {
            mDb.execSQL(CREATE_QUESTION_TABLE);
            mDb.execSQL(CREATE_EXAM_TABLE);
        }
        /**
         * onUpgrade method is called when database version changes.
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion);
        }
      
    }
 
    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public ExamDBHelper(Context ctx) {
        this.mCtx = ctx;
    }
    
    /**
     * This method is used for creating/opening connection
     * @return instance of DatabaseUtil
     * @throws SQLException
     */
    public ExamDBHelper open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        mDb.execSQL(CREATE_QUESTION_TABLE);
        Log.d("Database stuff", "You opened the Database");
        return this;
    }
    /**
     * This method is used for closing the connection.
     */
    public void close() {
    	
        mDbHelper.close();
        Log.d("Database stuff", "You closed the Database");
    }
 
    /**
     * This method is used to create/insert new record Exam record.
     * @param title
     * @param author
     * @return long
     */
    public long createExam(String title, String author) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_AUTHOR, author);
        return mDb.insert(EXAM_TABLE, null, initialValues);
    }
    

    /**
     * This method is used to create/insert new record Question record.
     * @param title
     * @param section
     * @param question
     * @param answer1
     * @param answer2
     * @param answer3
     * @param answer4
     * @param correctAnswer
     * @return long
     */
    public long createQuestion(String title, String section, String question, String answer1, String answer2, String answer3, String answer4, String correctAnswer) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_SECTION, section);
        initialValues.put(KEY_EXAMQUESTION, question);
        initialValues.put(KEY_ANSWERA, answer1);
        initialValues.put(KEY_ANSWERB, answer2);
        initialValues.put(KEY_ANSWERC, answer3);
        initialValues.put(KEY_ANSWERD, answer4);
        initialValues.put(KEY_CORRECTANSWER, correctAnswer);
        return mDb.insert(EXAM_QUESTION_TABLE, null, initialValues);
    }
    /**
     * This method will delete Exam record.
     * @param rowId
     * @return boolean
     */
    public boolean deleteExam(long rowId) {
        return mDb.delete(EXAM_TABLE, KEY_EXAMROWID + "=" + rowId, null) > 0;
    }
    /**
     * This method will delete Question record.
     * @param rowId
     * @return boolean
     */
    public boolean deleteQuestion(long rowId) {
        return mDb.delete(EXAM_QUESTION_TABLE, KEY_QUESTIONROWID + "=" + rowId, null) > 0;
    }
 
    /**
     * This method will return Cursor holding all the Exam records.
     * @return Cursor
     */
    public Cursor fetchAllExams() {
        return mDb.query(EXAM_TABLE, new String[] {KEY_EXAMROWID, KEY_TITLE, 
                KEY_AUTHOR}, null, null, null, null, null);
    }
    
    /**
     * This method will return Cursor holding all the Exam records.
     * @return Cursor
     */
    public Cursor fetchAllQuestions() {
        return mDb.query(EXAM_QUESTION_TABLE, new String[] {KEY_QUESTIONROWID, KEY_TITLE, KEY_SECTION, KEY_EXAMQUESTION, KEY_ANSWERA, KEY_ANSWERB, KEY_ANSWERC, KEY_ANSWERD, KEY_CORRECTANSWER}, null, null, null, null, null);
    }
    /**
     * This method will return Cursor holding the Question records from a specific Exam.
     * @param id
     * @param examTitle
     * @return Cursor
     * @throws SQLException
     */
  public Cursor fetchQuestions(String examTitle) throws SQLException {
    	String [] FROM = {KEY_QUESTIONROWID, KEY_TITLE,KEY_SECTION, KEY_EXAMQUESTION, KEY_ANSWERA, KEY_ANSWERB, KEY_ANSWERC, KEY_ANSWERD, KEY_CORRECTANSWER};
    	String where = "Title=?";
    	String[] whereArgs = new String[]{examTitle};
    	
     Cursor mCursor =  mDb.query(EXAM_QUESTION_TABLE, FROM, where, whereArgs, null, null, null);
        
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public void deleteLocalDatabase() {
        mDb.delete("tb_Exams", "1",new String[] {});
        Log.d("Database stuff", "Database Exam table succesfully deleted");
        mDb.delete("tb_Questions", "1",new String[] {});
        Log.d("Database stuff", "Database Question table succesfully deleted");
        mDb.close();
    }
 
    /**
     * This method will update Exam record.
     * @param id
     * @param author
     * @param title
     * @return boolean
     */
    public boolean updateExam(int id, String title, String author) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_AUTHOR, author);
        return mDb.update(EXAM_TABLE, args, KEY_EXAMROWID + "=" + id, null) > 0;
    }
    /**
     * This method will update Question record.
     * @param id
     * @param title
     * @param question
     * @param answer1
     * @param answer2
     * @param answer3
     * @param answer4
     * @param correctAnswer
     * @return boolean
     */
    public boolean updateQuestion(int id, String title, String section, String question, String answer1, String answer2, String answer3, String answer4, String correctAnswer) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_SECTION, section);
        args.put(KEY_EXAMQUESTION, question);
        args.put(KEY_ANSWERA, answer1);
        args.put(KEY_ANSWERB, answer2);
        args.put(KEY_ANSWERC, answer3);
        args.put(KEY_ANSWERD, answer4);
        args.put(KEY_CORRECTANSWER, correctAnswer);
		
        return mDb.update(EXAM_QUESTION_TABLE, args, KEY_EXAMROWID + "=" + id, null) > 0;
    }
}