package com.gamisweb.gamitester;
import java.util.ArrayList;

import com.gamisweb.utility.QInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class QInfoAdapter extends ArrayAdapter<QInfo> {
	private TextView section;
	private TextView text;
	private TextView answerA;
	private TextView answerB;
	private TextView answerC;
	private TextView answerD;

	// declaring our ArrayList of items
	private ArrayList<QInfo> objects;

	/* here we must override the constructor for ArrayAdapter
	* the only variable we care about now is ArrayList<Qinfo> objects,
	* because it is the list of objects we want to display.
	*/
	public QInfoAdapter(Context context, int textViewResourceId, ArrayList<QInfo> objects) {
		super(context, textViewResourceId, objects);
		this.objects = objects;
	}

	/*
	 * we are overriding the getView method here - this is what defines how each
	 * list item will look.
	 */
	public View getView(int position, View convertView, ViewGroup parent){

		// assign the view we are converting to a local variable
		View v = convertView;

		// first check to see if the view is null. if so, we have to inflate it.
		// to inflate it basically means to render, or show, the view.
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.review_view, null);
		}

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 * 
		 * Therefore, i refers to the current Item object.
		 */
		QInfo i = objects.get(position);

		if (i != null) {

			// This is how you obtain a reference to the TextViews.
			// These TextViews are created in the XML files we defined.

			section = (TextView) v.findViewById(R.id.reviewSection);
			text = (TextView) v.findViewById(R.id.reviewText);
			answerA = (TextView) v.findViewById(R.id.reviewAnswerA);
			answerB = (TextView) v.findViewById(R.id.reviewAnswerB);
			answerC = (TextView) v.findViewById(R.id.reviewAnswerC);
			answerD = (TextView) v.findViewById(R.id.reviewAnswerD);



			// check to see if each individual textview is null.
			// if not, assign some text!
			if (section != null){
				section.setText(i.getSection());
			}
			if (text != null){
				text.setText(i.getNumber() + ":  " + i.getText());

			if (answerA != null){
				answerA.setText("A) "+i.getChoice(0));
				if(i.getMarked()==0) answerA.setBackgroundColor(answerA.getContext().getResources().getColor(R.color.holo_red_light));
				if(i.getCorrect()==0) answerA.setBackgroundColor(answerA.getContext().getResources().getColor(R.color.holo_green_light));
			}
			if (answerB != null){
				answerB.setText("B) "+i.getChoice(1));
				if(i.getMarked()==1) answerB.setBackgroundColor(answerB.getContext().getResources().getColor(R.color.holo_red_light));
				if(i.getCorrect()==1) answerB.setBackgroundColor(answerB.getContext().getResources().getColor(R.color.holo_green_light));
			}
			if (answerC != null){
				answerC.setText("C) "+i.getChoice(2));
				if(i.getMarked()==2) answerC.setBackgroundColor(answerC.getContext().getResources().getColor(R.color.holo_red_light));
				if(i.getCorrect()==2) answerC.setBackgroundColor(answerC.getContext().getResources().getColor(R.color.holo_green_light));
			}
			if (answerD != null){
				answerD.setText("D) "+i.getChoice(3));
				if(i.getMarked()==3) answerD.setBackgroundColor(answerD.getContext().getResources().getColor(R.color.holo_red_light));
				if(i.getCorrect()==3) answerD.setBackgroundColor(answerD.getContext().getResources().getColor(R.color.holo_green_light));
			}

		
		}

		// the view must be returned to our activity
		}
		return v;


	}
}
