package com.gamisweb.gamitester;

import java.util.ArrayList;

import com.gamisweb.utility.QInfo;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
//import android.widget.TextView;



public class ReviewMissedActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.gamisweb.gamitester.MESSAGE";
	public String message ="";
	private QInfoAdapter qAdapter;
	private ArrayList<QInfo> missedQuestion;


	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.exam_listview_layout);
	    Intent intent = getIntent();
	    missedQuestion = (ArrayList<QInfo>)intent.getSerializableExtra(EXTRA_MESSAGE);
	    showResults();
	    }
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.review_missed, menu);
		return true;
	}
	
	*/
	
	private void showResults(){
	    setContentView(R.layout.exam_listview_layout);

	    qAdapter = new QInfoAdapter(this,R.layout.activity_review_missed, missedQuestion);
//	    setListAdapter(qAdapter);
	    
	    
		 ListView reviewMissed = (ListView) findViewById(R.id.exam_listview);
		 reviewMissed.setAdapter(qAdapter);
	}
//this is a test of the changes


}
