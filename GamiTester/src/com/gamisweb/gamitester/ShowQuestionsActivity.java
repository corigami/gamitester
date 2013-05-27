package com.gamisweb.gamitester;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.gamisweb.utility.ExamDBHelper;
import com.gamisweb.utility.Io;
import com.gamisweb.utility.QInfo;

import java.util.ArrayList;


public class ShowQuestionsActivity extends Activity {
    public ExamDBHelper examHelper;
    public SimpleCursorAdapter dataAdapter;
    public String message = "";
    private TextView title;
    private TextView reviewTitle;
    private TextView reviewResults;
    private TextView section;
    private TextView questionText;
    private TextView answerA;
    private TextView answerB;
    private TextView answerC;
    private TextView answerD;
    private TextView weightTV;
    private int questionIndex = 0;
    private int numberOfQuestions = 1;
    private Cursor cursor;
    private QInfo tempQInfo;
    private ArrayList<QInfo> questionArray = new ArrayList<QInfo>();
    private ArrayList<QInfo> missedQuestion = new ArrayList<QInfo>();
    private QInfoAdapter qAdapter;


    /**
     * Called when the activity is first created.
     * sets view objects
     * opens database based on intent passed from Main Activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.exam_view);
        title = (TextView) findViewById(R.id.textViewExamTitle);
        section = (TextView) findViewById(R.id.textViewSectionTitle);
        questionText = (TextView) findViewById(R.id.questionText_TextView);
        answerA = (TextView) findViewById(R.id.editTextAnswerA);
        answerB = (TextView) findViewById(R.id.editTextAnswerB);
        answerC = (TextView) findViewById(R.id.editTextAnswerC);
        answerD = (TextView) findViewById(R.id.editTextAnswerD);
        weightTV = (TextView) findViewById(R.id.reviewWeight);
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        tempQInfo = new QInfo();
        examHelper = new ExamDBHelper(this);
        examHelper.open();
        cursor = examHelper.fetchAllQuestions(message);
        setTitle(message);
        questionPrompt();
        examHelper.close();

    }

    public void onStart(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void onResume() {
        super.onResume();
    }

    private void generateQuestionArray() {
        for (int x = 0; x < cursor.getCount(); x++) {
            for (int i = 0; i < (cursor.getColumnCount() - 1); ) {
                tempQInfo.setDatabaseID(cursor.getString(i++));
                tempQInfo.setSection(cursor.getString(i++));
                tempQInfo.setText(cursor.getString(i++));
                tempQInfo.setChoice(0, cursor.getString(i++));
                tempQInfo.setChoice(1, cursor.getString(i++));
                tempQInfo.setChoice(2, cursor.getString(i++));
                tempQInfo.setChoice(3, cursor.getString(i++));
                tempQInfo.setCorrect(cursor.getInt(i++));
                tempQInfo.setWeight(cursor.getInt(i++));
                System.out.println("Weight of Question #" + tempQInfo.getDatabaseID() + " = " + tempQInfo.getWeight());
            }
            questionArray.add(tempQInfo.copy(tempQInfo));
            cursor.moveToNext();
        }
        questionArray = Io.getRandQuestions(questionArray, numberOfQuestions);
    }

    private void displayQuestions(QInfo qInfo) {
        resetAnswerColors();
        if (qInfo.getMarked() == 0) answerA.setBackgroundColor(getResources().getColor(R.color.holo_blue_bright));
        if (qInfo.getMarked() == 1) answerB.setBackgroundColor(getResources().getColor(R.color.holo_blue_bright));
        if (qInfo.getMarked() == 2) answerC.setBackgroundColor(getResources().getColor(R.color.holo_blue_bright));
        if (qInfo.getMarked() == 3) answerD.setBackgroundColor(getResources().getColor(R.color.holo_blue_bright));

        title.setText("Question #" + (questionIndex + 1));
        section.setText("Section:" + qInfo.getSection());
        weightTV.setText("Weight: " + qInfo.getWeight());
        questionText.setText(qInfo.getText());
        answerA.setText("A) " + qInfo.getChoice(0));
        answerB.setText("B) " + qInfo.getChoice(1));
        answerC.setText("C) " + qInfo.getChoice(2));
        answerD.setText("D) " + qInfo.getChoice(3));
    }

    private void displayFinalReport() {
        setContentView(R.layout.results_listview_layout);
        setTitle(message);
        int oldWeight;
        int newWeight = 5;

        for (int i = 0; i < (questionArray.size()); i++) {
            oldWeight = questionArray.get(i).getWeight();
            if (questionArray.get(i).getMarked() != questionArray.get(i).getCorrect()) {
                missedQuestion.add(questionArray.get(i));
                if (oldWeight > 5) newWeight = 4;
                else if (oldWeight > 0) newWeight = oldWeight - 1;

            } else {
                if (oldWeight == 0) newWeight = 5;
                if (oldWeight < 9) newWeight = oldWeight + 1;
            }
            examHelper.open();
            examHelper.updateQuestionWeight(message, Integer.parseInt(questionArray.get(i).getDatabaseID()), Integer.toString(newWeight));
            examHelper.close();
        }


        qAdapter = new QInfoAdapter(this, R.id.results_listview, missedQuestion);
        ListView reviewMissed = (ListView) findViewById(R.id.results_listview);
        reviewMissed.setAdapter(qAdapter);
        reviewTitle = (TextView) findViewById(R.id.results_listview_TV0);
        reviewResults = (TextView) findViewById(R.id.results_listview_TV2);
        int missed = missedQuestion.size();
        reviewTitle.setText("Correct: " + Integer.toString((numberOfQuestions - missed)) + "    Missed: " + Integer.toString(missed));
        float percent = (numberOfQuestions - missed) * 100f / numberOfQuestions;
        reviewResults.setText(Float.toString(percent) + "%");


		/*
        @Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.review_missed, menu);
			return true;
		}

		 */
    }

    public void nextButtonOnClick(View view) {

        cursor.moveToNext();
        questionIndex++;
        if (questionIndex < questionArray.size())
            displayQuestions(questionArray.get(questionIndex));
        else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            final EditText input = new EditText(this);
            alert.setTitle("Done?");
            alert.setMessage("You have reached the end of the exam.  Click okay to see how you did. ");
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    displayFinalReport();
                    // Do something with value!
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

    public void prevButtonOnClick(View view) {
        if (questionIndex != 0) {
            cursor.moveToPrevious();
            questionIndex--;
            displayQuestions(questionArray.get(questionIndex));
        }
    }

    public void selectAOnClick(View view) {
        resetAnswerColors();
        answerA.setBackgroundColor(getResources().getColor(R.color.holo_blue_bright));
        questionArray.get(questionIndex).setMarked(0);
    }

    public void selectBOnClick(View view) {
        resetAnswerColors();
        answerB.setBackgroundColor(getResources().getColor(R.color.holo_blue_bright));
        questionArray.get(questionIndex).setMarked(1);
    }

    public void selectCOnClick(View view) {
        resetAnswerColors();
        answerC.setBackgroundColor(getResources().getColor(R.color.holo_blue_bright));
        questionArray.get(questionIndex).setMarked(2);
    }

    public void selectDOnClick(View view) {
        resetAnswerColors();
        answerD.setBackgroundColor(getResources().getColor(R.color.holo_blue_bright));
        questionArray.get(questionIndex).setMarked(3);
    }

    public void resetAnswerColors() {
        answerA.setBackgroundColor(color.white);
        answerB.setBackgroundColor(color.white);
        answerC.setBackgroundColor(color.white);
        answerD.setBackgroundColor(color.white);
    }

    private void questionPrompt() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Number of questions?");
        alert.setMessage("How many questions would you like to test on? \n Max = " + cursor.getCount());

        final NumberPicker numPick = new NumberPicker(ShowQuestionsActivity.this);
        int countByFive = cursor.getCount() - (cursor.getCount() % 5);
        String[] nums = new String[countByFive / 5];
        int incrementByFive = 0;
        for (int i = 0; i < nums.length; i++) {
            incrementByFive = incrementByFive + 5;
            nums[i] = Integer.toString(incrementByFive);
        }


        numPick.setMinValue(0);
        numPick.setMaxValue(nums.length - 1);
        numPick.setWrapSelectorWheel(true);
        numPick.setDisplayedValues(nums);
        if (countByFive < 250) numPick.setValue(countByFive / 5);
        else numPick.setValue(9);

        alert.setView(numPick);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                numberOfQuestions = (numPick.getValue() + 1) * 5;
                if (numberOfQuestions > cursor.getCount())
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