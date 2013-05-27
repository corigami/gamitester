package com.gamisweb.gamitester;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import com.gamisweb.utility.ExamInfo;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * Created by Corey's Desktop on 5/26/13.
 */
public class WebDataAsyncTask extends AsyncTask<ArrayList<ExamInfo>, Void, ArrayList<ExamInfo>> {

    private final static String gamisweb = "http://www.gamisweb.com/gamitester/";
    ExamInfoAdapter examInfoAdapter;
    private ArrayList<ExamInfo> gamiswebExamList = new ArrayList<ExamInfo>();
    private Context context;
    private ListView examListView;
    private ProgressDialog pd;

    public WebDataAsyncTask(Context passedContext, ListView listView) {
        context = passedContext;
        examListView = listView;


    }

    public void displayWebList() {
        examInfoAdapter = new ExamInfoAdapter(context, R.layout.exam_list_entries_layout, gamiswebExamList);
        examListView.setAdapter(examInfoAdapter);
        System.out.println("Size of gamiswebList: " + gamiswebExamList.size());

    }

    public ArrayList<ExamInfo> getExamData() {
        System.out.println("You got to getExamData");
        this.execute(gamiswebExamList);
        return gamiswebExamList;

    }

    protected void onPreExecute() {
        pd = new ProgressDialog(context);
        pd.setTitle("Getting List of Exams from Gamisweb");
        pd.setMessage("Please wait");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
    }

    protected ArrayList<ExamInfo> doInBackground(ArrayList<ExamInfo>... examInfoHolder) {
        ArrayList<ExamInfo> webExamData = new ArrayList<ExamInfo>();
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
                   /* for(int x=0;x<temp.size();x++){
                        System.out.println("Title at position "+x+" = "+temp.get(x).getExamTitle());
                    }*/
            }

        } catch (Exception e2) {
            //TODO catch block
            e2.printStackTrace();
        }

        return webExamData;

    }

    protected void onPostExecute(ArrayList<ExamInfo> examList) {
        pd.dismiss();
        gamiswebExamList = examList;
        this.displayWebList();
    }
}


