package com.gamisweb.gamitester;

import java.io.*;
import com.gamisweb.gamitester.R;
import com.gamisweb.utility.*;
import java.util.ArrayList;
import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.gamisweb.gamitester.MESSAGE";
	private boolean consent=false;
	private int count=0;
	private TextView homeScreenLayout1;
	private String homeScreenText="This is the homescreen text";
	private String selectedExam = "";
	private SimpleCursorAdapter dataAdapter;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	protected void onStart(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void onResume() {
		super.onResume();
		if(!consent) setContentView(R.layout.splash_screen);
		else setContentView(R.layout.activity_main);

	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	 */

	public void exitApp(View view){
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	public void setActivityMainLayout(View view){
		consent = true;
		setContentView(R.layout.activity_main);
		Button showQuestions = (Button) findViewById(R.id.button4);
		if (selectedExam=="") showQuestions.setVisibility(View.GONE);
		else showQuestions.setVisibility(View.VISIBLE);
	}	

	public void reviewButtonOnClick(View view) { 	//creates listener onClick to tell the program what to do when button1 is clicked.
		try {
			createDatabase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public void examButtonOnClick(View view){ 	//creates listener onClick to tell the program what to do when button1 is clicked.
		selectExam();
	}	

	public void questionButtonOnClick(View view){
		Intent intent = new Intent(this, ShowQuestionsActivity.class);
		intent.putExtra(EXTRA_MESSAGE, selectedExam);
		startActivity(intent);
	}

	public void editButtonOnClick(View view){
		deleteDatabase();
	}
	private void deleteDatabase(){
		setHomeScreenLayout1("You sent the list of Exams to the log");
		ExamDBHelper examHelper = new ExamDBHelper(this);
		examHelper.open();
		examHelper.deleteLocalDatabase();
		examHelper.close();	
		count=0;
	}
	private void createDatabase()throws IOException{
		ExamDBHelper dbUtil = new ExamDBHelper(this);
		AssetManager aM = this.getAssets();
		InputStream fileToRead = aM.open("set1.txt");
		InputStreamReader inputStreamReader = new InputStreamReader(fileToRead);
		BufferedReader buff = new BufferedReader(inputStreamReader);
		ArrayList<QInfo> questionArray = Io.getTextQuestions(buff);
		QInfo questionStore = new QInfo();
		dbUtil.open();
		int i;
		for(i=0; i<questionArray.size(); i++)
		{
			questionStore=questionArray.get(i);
			dbUtil.createQuestion("Exam# "+count,questionStore.getSection(), questionStore.getText() , questionStore.getChoice(0), questionStore.getChoice(1), questionStore.getChoice(2),
					questionStore.getChoice(3), Integer.toString(questionStore.getCorrect()));
		}			
		dbUtil.createExam("Exam# "+count, "Author# "+count);
		dbUtil.close();
		setHomeScreenLayout1("You added Exam #" + count + ", by Author #" + count + " to the database");
		count++;
		buff.close();
		inputStreamReader.close();

	}
	public void setHomeScreenLayout1(){
		homeScreenLayout1=(TextView) findViewById(R.id.homeScreenTextView1);
		System.out.println("you got this far");
		homeScreenLayout1.setText(this.getHomeScreenText());
		homeScreenLayout1.postInvalidate();	
	}
	public void setHomeScreenLayout1(String displayText){
		homeScreenLayout1=(TextView) findViewById(R.id.homeScreenTextView1);
		homeScreenLayout1.setText(displayText);
		homeScreenLayout1.postInvalidate();		
	}

	public void setHomeScreenText(String string){
		homeScreenText = string;
	}
	public String getHomeScreenText(){
		return homeScreenText;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {

			if(resultCode == RESULT_OK){      
				String result=data.getStringExtra("result");
				setHomeScreenLayout1("You selected the following exam ");
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
				selectedExam = result;
			}
			if (resultCode == RESULT_CANCELED) {    
				//Write your code if there's no result
			}
		}
	}

	public void selectExam(){


		setContentView(R.layout.exam_listview_layout);
		displayListView();
	}
	private void displayListView() {
		ExamDBHelper examHelper = new ExamDBHelper(this);
		examHelper.open();	 
		Cursor cursor = examHelper.fetchAllExams();

		
		//Creates a new temp database if none exists
		if(cursor.getCount()==0){
			try {
				createDatabase();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		// The desired columns to be bound
		@SuppressWarnings("static-access")
		String[] columns = new String[]
				{
			//examHelper.getAuthor(),examHelper.getTitle()
			examHelper.KEY_TITLE,
			examHelper.KEY_AUTHOR
				};
		
		int[] to = new int[] {R.id.examTitle, R.id.examAuthor}; // the XML defined views which the data will be bound to

		
		ListView examListView = (ListView) findViewById(R.id.exam_listview); // create the adapter using the cursor pointing to the desired data as well as the layout information
		dataAdapter = new SimpleCursorAdapter(this, R.layout.exam_list_entries_layout, cursor, columns, to, 0); 
		examListView.setAdapter(dataAdapter); // Assign adapter to ListView
		examListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
				Cursor cursor = (Cursor) listView.getItemAtPosition(position); // Get the cursor, positioned to the corresponding row in the result set
				selectedExam = cursor.getString(cursor.getColumnIndexOrThrow("Title")); // Get the selected exam from this row in the database.
				setContentView(R.layout.activity_main);
				
			}

		});
		examHelper.close();
	}
}
