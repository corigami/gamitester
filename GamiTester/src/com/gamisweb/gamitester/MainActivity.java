package com.gamisweb.gamitester;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.gamisweb.utility.ExamDBHelper;
import com.gamisweb.utility.ExamInfo;
import com.gamisweb.utility.Io;
import com.gamisweb.utility.QInfo;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import com.gamisweb.gamitester.WebDataAsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
                pd.setTitle("Generating Exam from embedded text file");
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
        showExamList();
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
        count = 0;
    }

    public void setHomeScreenLayout1(String displayText) {
        homeScreenLayout1 = (TextView) findViewById(R.id.homeScreenTextView1);
        homeScreenLayout1.setText(displayText);
        homeScreenLayout1.postInvalidate();
    }

    public String getHomeScreenText() {
        return homeScreenText;
    }

    public void setHomeScreenText(String string) {
        homeScreenText = string;
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
            examHelper.close();
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
                Cursor cursor = (Cursor) listView.getItemAtPosition(position); // Get the cursor, positioned to the corresponding row in the result set
                selectedExam = cursor.getString(cursor.getColumnIndexOrThrow("Title")); // Get the selected exam from this row in the database.
                setContentView(R.layout.activity_main);

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

    /*private class GetWebExamData extends AsyncTask<Void, Void, ArrayList<ExamInfo>> {
        protected void onPreExecute() {
            //TODO preExecute Code
        }

        protected ArrayList<ExamInfo> doInBackground(Void... params) {
            JSONArray jArray;
            String result = "";
            String newResult = "";
            InputStream is = null;
            StringBuilder sb = null;
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            ArrayList<ExamInfo> temp = new ArrayList<ExamInfo>();
            //http post
            try {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://www.gamisweb.com/gamitester/index.php");
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();
                } catch (Exception e) {
                    Log.e("log_tag", "Error in http connection" + e.toString());
                }

                try { //convert response to string
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                    sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");

                    String line = "0";
                    int counter = 1;

                    while ((line = reader.readLine()) != null) {
                        if (counter == 7) sb.append(line);
                        counter++;
                    }
                    is.close();
                    result = sb.toString();
                    newResult = result.replaceAll("\\<.*?\\>", "");
                } catch (Exception e) {
                    Log.e("log_tag", "Error converting result " + e.toString());
                }
                String ct_id;//paring data
                String ct_title;
                String ct_author;
                String ct_descript;

                jArray = new JSONArray(newResult);
                JSONObject json_data;


                for (int i = 0; i < jArray.length(); i++) {
                    ExamInfo tempExamInfo = new ExamInfo();
                    json_data = jArray.getJSONObject(i);
                    tempExamInfo.setExamDatabaseID(json_data.getString("_id"));
                    System.out.println("Id from JSON = " + tempExamInfo.getExamDatabaseID());
                    tempExamInfo.setExamTitle(json_data.getString("Title"));
                    System.out.println("Title from JSON = " + tempExamInfo.getExamTitle());
                    tempExamInfo.setExamAuthor(json_data.getString("Author"));
                    System.out.println("Author from JSON = " + tempExamInfo.getExamAuthor());
                    tempExamInfo.setExamDescript(json_data.getString("Description"));
                    System.out.println("Description from JSON = " + tempExamInfo.getExamDescript());
                    webExamData.add(tempExamInfo);
                   *//* for(int x=0;x<temp.size();x++){
                        System.out.println("Title at position "+x+" = "+temp.get(x).getExamTitle());
                    }*//*
                }

            } catch (Exception e2) {
                //TODO catch block
                e2.printStackTrace();
            }

            return webExamData;

        }

        @Override
        protected void onPostExecute(ArrayList<ExamInfo> examInfo) {
            System.out.println("webExamData.size() = " + webExamData.size());
            System.out.println("First title = " + webExamData.get(0).getExamTitle());
            System.out.println("Second title = " + webExamData.get(1).getExamTitle());
            examInfoAdapter = new ExamInfoAdapter(context, R.layout.exam_list_entries_layout, webExamData);
            examListView.setAdapter(examInfoAdapter);

        }
    }*/
}