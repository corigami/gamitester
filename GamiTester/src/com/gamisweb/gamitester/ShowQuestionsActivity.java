package com.gamisweb.gamitester;

import java.util.ArrayList;
import com.gamisweb.utility.*;
import com.gamisweb.gamitester.QInfoAdapter;
import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class ShowQuestionsActivity extends Activity {
	public ExamDBHelper examHelper;
	public SimpleCursorAdapter dataAdapter;
	public String message ="";
	private TextView title;
	private TextView section;
	private TextView questionText;
	private TextView answerA;
	private TextView answerB;
	private TextView answerC;
	private TextView answerD;
	private int questionIndex=0;
	private int numberOfQuestions = 1;
	private Cursor cursor;
	private QInfo tempQInfo;
	private ArrayList<QInfo> questionArray = new ArrayList<QInfo>();
	private ArrayList<QInfo> missedQuestion = new ArrayList<QInfo>();
	private QInfoAdapter qAdapter;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		setContentView(R.layout.exam_view);
		title = (TextView)findViewById(R.id.textViewExamTitle);
		section= (TextView) findViewById(R.id.textViewSectionTitle);
		questionText = (TextView) findViewById(R.id.questionText_TextView);
		answerA = (TextView)findViewById(R.id.editTextAnswerA);
		answerB = (TextView)findViewById(R.id.editTextAnswerB);
		answerC = (TextView)findViewById(R.id.editTextAnswerC);
		answerD = (TextView)findViewById(R.id.editTextAnswerD);
		message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		tempQInfo = new QInfo();
		ExamDBHelper examHelper = new ExamDBHelper(this);
		examHelper.open();	 
		cursor = examHelper.fetchQuestions(message);
		examHelper.close();
		setTitle(message);
		questionPrompt();

	}
	private void generateQuestionArray(){
		for(int x=0;x < cursor.getCount();x++){
			for(int i=2;i<cursor.getColumnCount()-1;){
				tempQInfo.setSection(cursor.getString(i++));
				tempQInfo.setText(cursor.getString(i++));
				tempQInfo.setChoice(0, cursor.getString(i++));
				tempQInfo.setChoice(1, cursor.getString(i++));
				tempQInfo.setChoice(2, cursor.getString(i++));
				tempQInfo.setChoice(3, cursor.getString(i++));
				tempQInfo.setCorrect(cursor.getInt(i++));
			}
			questionArray.add(tempQInfo.copy(tempQInfo));
			cursor.moveToNext();
		}
		questionArray=Io.getRandQuestions(questionArray, numberOfQuestions);
	}

	private void displayQuestions(QInfo qInfo){
		resetAnswerColors();
		if(qInfo.getMarked()==0)answerA.setBackgroundColor(getResources().getColor(R.color.holo_blue_bright));
		if(qInfo.getMarked()==1)answerB.setBackgroundColor(getResources().getColor(R.color.holo_blue_bright));
		if(qInfo.getMarked()==2)answerC.setBackgroundColor(getResources().getColor(R.color.holo_blue_bright));
		if(qInfo.getMarked()==3)answerD.setBackgroundColor(getResources().getColor(R.color.holo_blue_bright));
		title.setText("Question #" + (questionIndex + 1));
		section.setText("Section:" + qInfo.getSection());
		questionText.setText(qInfo.getText());
		answerA.setText("A) "+ qInfo.getChoice(0));
		answerB.setText("B) "+ qInfo.getChoice(1));
		answerC.setText("C) "+ qInfo.getChoice(2));
		answerD.setText("D) "+ qInfo.getChoice(3));
	}

	private void displayFinalReport(){
		setContentView(R.layout.exam_listview_layout);
		setTitle(message);
		for(int i=0;i<(questionArray.size());i++)

		{
			if(questionArray.get(i).getMarked()!=questionArray.get(i).getCorrect())
				missedQuestion.add(questionArray.get(i));

		}

		qAdapter = new QInfoAdapter(this,R.layout.activity_review_missed, missedQuestion);
		ListView reviewMissed = (ListView) findViewById(R.id.exam_listview);
		reviewMissed.setAdapter(qAdapter);

		/*
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.review_missed, menu);
			return true;
		}

		 */
	}

	public void nextButtonOnClick(View view){

		cursor.moveToNext();
		questionIndex++;
		if(questionIndex<questionArray.size())
			displayQuestions(questionArray.get(questionIndex));
		else displayFinalReport();
	}	
	public void prevButtonOnClick(View view){
		if(questionIndex!=0){
			cursor.moveToPrevious();
			questionIndex--;
			displayQuestions(questionArray.get(questionIndex));
		}
	}
	public void selectAOnClick(View view){
		resetAnswerColors();
		answerA.setBackgroundColor(getResources().getColor(R.color.holo_blue_bright));
		questionArray.get(questionIndex).setMarked(0);
	}
	public void selectBOnClick(View view){
		resetAnswerColors();
		answerB.setBackgroundColor(getResources().getColor(R.color.holo_blue_bright));
		questionArray.get(questionIndex).setMarked(1);
	}
	public void selectCOnClick(View view){
		resetAnswerColors();
		answerC.setBackgroundColor(getResources().getColor(R.color.holo_blue_bright));
		questionArray.get(questionIndex).setMarked(2);
	}
	public void selectDOnClick(View view){
		resetAnswerColors();
		answerD.setBackgroundColor(getResources().getColor(R.color.holo_blue_bright));
		questionArray.get(questionIndex).setMarked(3);
	}
	public void resetAnswerColors(){
		answerA.setBackgroundColor(color.white);
		answerB.setBackgroundColor(color.white);	
		answerC.setBackgroundColor(color.white);	
		answerD.setBackgroundColor(color.white);	
	}
	private void questionPrompt(){

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Number of questions?");
		alert.setMessage("How many questions would you like to test on? \n Max = "+ cursor.getCount());

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				numberOfQuestions = Integer.parseInt(input.getText().toString());
				if(numberOfQuestions >cursor.getCount())
					numberOfQuestions = cursor.getCount();
				generateQuestionArray();
				displayQuestions(questionArray.get(questionIndex));
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}

		});
		alert.show();

	}
}