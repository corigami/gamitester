package com.gamisweb.gamitester;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.gamisweb.utility.ExamDBHelper;
import com.gamisweb.utility.ExamInfo;
import com.gamisweb.utility.Io;
import com.gamisweb.utility.QInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

//import android.util.Log;
/*import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.gamisweb.gamitester.WebDataAsyncTask;
*/

public class MainActivity extends Activity {

    public final static String EXTRA_MESSAGE = "com.gamisweb.gamitester.MESSAGE";
    ArrayList<ExamInfo> webExamData = new ArrayList<ExamInfo>();
    private boolean consent = false;
    private int count = 0;
    private TextView homeScreenLayout1;
    private String homeScreenText = "This is the homescreen text";
    private String selectedExam = "";
    private SimpleCursorAdapter cursorDataAdapter;
    private ExamInfoAdapter examInfoAdapter;
    private ExamDBHelper dbUtil;
    private Context context;
    private Context webExamContext;
    private CheckBox checkBoxWeb;
    private boolean webSelected;
    private ListView examListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

    }

    protected void onStart(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!consent) setContentView(R.layout.splash_screen);
        else setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    public void exitApp(View view) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void setActivityMainLayout(View view) {
        consent = true;
        setContentView(R.layout.activity_main);
        Button showQuestions = (Button) findViewById(R.id.buttonShowQuestions);
        if (selectedExam == "") showQuestions.setVisibility(View.GONE);
        else showQuestions.setVisibility(View.VISIBLE);

    }

    private void createDatabase() throws IOException {

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            private ProgressDialog pd;

            @Override
            protected void onPreExecute() {
                pd = new ProgressDialog(context);
                pd.setTitle("Generating test data from embedded text file");
                pd.setMessage("Please wait");
                pd.setCancelable(false);
                pd.setIndeterminate(true);
                pd.show();

            }

            @Override
            protected Void doInBackground(Void... arg0) {
                try {
                    dbUtil = new ExamDBHelper(context);
                    AssetManager aM = context.getAssets();
                    InputStream fileToRead = aM.open("set1.txt");
                    InputStreamReader inputStreamReader = new InputStreamReader(fileToRead);
                    BufferedReader buff = new BufferedReader(inputStreamReader);
                    ArrayList<QInfo> questionArray = Io.getTextQuestions(buff);
                    QInfo questionStore = new QInfo();
                    dbUtil.open();
                    int i;
                    String examTitle = "examnum_" + count;
                    dbUtil.createExam(examTitle, "Author# Corey Willinger", "This is test data for exam #" + count);
                    dbUtil.createNewExamTable(examTitle);
                    for (i = 0; i < questionArray.size(); i++) {
                        questionStore = questionArray.get(i);
                        dbUtil.createQuestion(examTitle, questionStore.getSection(), questionStore.getText(), questionStore.getChoice(0), questionStore.getChoice(1), questionStore.getChoice(2),
                                questionStore.getChoice(3), Integer.toString(questionStore.getCorrect()), Integer.toString(questionStore.getWeight()));
                    }
                    dbUtil.close();
                    buff.close();
                    inputStreamReader.close();
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    //TODO catch block
                    e.printStackTrace();
                } catch (IOException e2) {
                    //TODO catch block
                    e2.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                pd.dismiss();
                findViewById(R.id.buttonAddExam).setEnabled(true);
            }

        };
        task.execute((Void[]) null);
        setHomeScreenLayout1("You added Exam #" + count + ", by Author #" + count + " to the database");
        count++;


    }

    public void buttonAddExamOnClick(View view) {    //creates listener onClick to tell the program what to do when button1 is clicked.
        findViewById(R.id.buttonAddExam).setEnabled(false);
        try {
            createDatabase();
        } catch (IOException e) {
            //TODO catch block
            e.printStackTrace();
        }
    }

    public void webCheckBoxOnClick(View view) {
        webSelected = findViewById(R.id.checkboxWeb).isSelected();
        if (webSelected) webSelected = false;
        else if (!webSelected) webSelected = true;
    }

    public void buttonSelectExamOnClick(View view) {    //creates listener onClick to tell the program what to do when button1 is clicked.
        if (getDatabaseSize() > 0)
            showExamList();
        else setHomeScreenLayout1("You must add a database first");
    }

    public void buttonCopyToWebOnClick(View view) {

    }

    public void buttonShowQuestionsOnClick(View view) {
        Intent intent = new Intent(this, ShowQuestionsActivity.class);
        intent.putExtra(EXTRA_MESSAGE, selectedExam);
        startActivity(intent);
    }

    public void buttonDeleteDatabaseOnClick(View view) {
        deleteDatabase();
    }

    private void deleteDatabase() {
        ExamDBHelper examHelper = new ExamDBHelper(this);
        examHelper.open();
        examHelper.deleteLocalDatabase(selectedExam);
        examHelper.close();
        count = getDatabaseSize() + 1;
    }

    public void setHomeScreenLayout1(String displayText) {
        homeScreenLayout1 = (TextView) findViewById(R.id.homeScreenTextView1);
        homeScreenLayout1.setText(displayText);
        homeScreenLayout1.postInvalidate();
    }

    public void setHomeScreenText(String string) {
        homeScreenText = string;
    }

    public String getHomeScreenText() {
        return homeScreenText;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("result");
                setHomeScreenLayout1("You selected the following exam ");
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                selectedExam = result;
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private int getDatabaseSize() {
        ExamDBHelper examHelper = new ExamDBHelper(this);
        examHelper.open();
        Cursor cursor = examHelper.fetchAllExams();
        int size = cursor.getCount();
        examHelper.close();
        return size;
    }

    private void showExamList() {
        setContentView(R.layout.exam_listview_layout);
        examListView = (ListView) findViewById(R.id.exam_listview); // create the adapter using the cursor pointing to the desired data as well as the layout information

        if (webSelected) {
            WebDataAsyncTask test = new WebDataAsyncTask(this, examListView);
            webExamData = test.getExamData();
            //  new GetWebExamData().execute();

        } else if (!webSelected) {
            ExamDBHelper examHelper = new ExamDBHelper(this);
            examHelper.open();
            Cursor cursor = examHelper.fetchAllExams();

            //Creates a new temp database if none exists
            if (cursor.getCount() == 0) {
                try {
                    createDatabase();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                cursor = examHelper.fetchAllExams();
            }

            // The desired columns to be bound
            @SuppressWarnings("static-access")
            String[] columns = new String[]
                    {
                            //examHelper.getAuthor(),examHelper.getTitle()
                            examHelper.KEY_TITLE,
                            examHelper.KEY_AUTHOR,
                            examHelper.KEY_DESC
                    };

            int[] to = new int[]{R.id.examTitle, R.id.examAuthor}; // the XML defined views which the data will be bound to
            cursorDataAdapter = new SimpleCursorAdapter(this, R.layout.exam_list_entries_layout, cursor, columns, to, 0);
            examListView.setAdapter(cursorDataAdapter); // Assign adapter to ListView

            examHelper.close();
        }

             examListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                if(!webSelected){
                Cursor cursor = (Cursor) listView.getItemAtPosition(position); // Get the cursor, positioned to the corresponding row in the result set
                System.out.println("You chose item #:"+position+1);
                selectedExam = cursor.getString(cursor.getColumnIndexOrThrow("Title")); // Get the selected exam from this row in the database.
                }
                else if(webSelected){
                selectedExam = ((ExamInfo)listView.getItemAtPosition(position)).getExamTitle();

                    }

                setContentView(R.layout.activity_main);
                setHomeScreenLayout1("Selected Exam: "+selectedExam);
            }

        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.optionsDeleteDatabase:
                deleteDatabase();
                return true;
            case R.id.optionsAddExam:
                try {
                    createDatabase();
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            case R.id.optionsCopyToWeb:

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}