package com.gamisweb.gamitester;


import com.gamisweb.utility.ExamDBHelper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
//import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class SelectExamActivity extends Activity {

	public ExamDBHelper examHelper;
	public SimpleCursorAdapter dataAdapter;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exam_listview_layout);
		displayListView();
	}
	private void displayListView() {
		ExamDBHelper examHelper = new ExamDBHelper(this);
		examHelper.open();	 
		Cursor cursor = examHelper.fetchAllExams();
		examHelper.close();
		@SuppressWarnings("static-access")
		String[] columns = new String[]
				{
			examHelper.KEY_TITLE,
			examHelper.KEY_AUTHOR
				};

		// the XML defined views which the data will be bound to
		int[] to = new int[] 
				{ 
				R.id.examTitle,
				R.id.examAuthor
				};

		// create the adapter using the cursor pointing to the desired data as well as the layout information
		ListView examListView = (ListView) findViewById(R.id.exam_listview);
		dataAdapter = new SimpleCursorAdapter(this, R.layout.exam_list_entries_layout, cursor, columns, to, 0);
		examListView.setAdapter(dataAdapter); 		  // Assign adapter to ListView

		examListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> listView, View view, 
					int position, long id) {
			
				Cursor cursor = (Cursor) listView.getItemAtPosition(position); 	// Get the cursor, positioned to the corresponding row in the result set
				String examSelected = cursor.getString(cursor.getColumnIndexOrThrow("Title")); // Get the selected exam from this row in the database.
				Intent returnIntent = new Intent();
				returnIntent.putExtra("result",examSelected);
				setResult(RESULT_OK,returnIntent);     
				finish();
			}

		});

	}
}
