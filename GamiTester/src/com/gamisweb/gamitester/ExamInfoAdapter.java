package com.gamisweb.gamitester;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.gamisweb.utility.ExamInfo;

import java.util.ArrayList;


public class ExamInfoAdapter extends ArrayAdapter<ExamInfo> {
    private TextView title;
    private TextView author;
    private TextView descript;


    // declaring our ArrayList of items
    private ArrayList<ExamInfo> objects;

    /* here we must override the constructor for ArrayAdapter
    * the only variable we care about now is ArrayList<examInfo> objects,
    * because it is the list of objects we want to display.
    */
    public ExamInfoAdapter(Context context, int textViewResourceId, ArrayList<ExamInfo> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.exam_list_entries_layout, null);
        }

		/*
         * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 * 
		 * Therefore, i refers to the current Item object.
		 */
        ExamInfo i = objects.get(position);
        System.out.println("The position is at: " + position);
        try {
            title = (TextView) v.findViewById(R.id.examTitle);
            System.out.println("Exam title = " + i.getExamTitle());
            title.setText(i.getExamTitle());
            author = (TextView) v.findViewById(R.id.examAuthor);
            author.setText(i.getExamAuthor());
            descript = (TextView) v.findViewById(R.id.examDescription);
            descript.setText(i.getExamDescript());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // the view must be returned to our activity

        return v;


    }
}
