package bearbread.org.groupscheduler.eugene.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import bearbread.org.groupscheduler.eugene.MyInfo;
import bearbread.org.groupscheduler.eugene.database.dao.DATA;

/**
 * Created by Eugene J. Jeon on 2015-05-27.
 */
public class SignUpTask extends AsyncTask<String, Void, String>{
    private final String URL = "http://www.dcafe.xyz:8912/";
    private final String TAG = "SignUp";

    private Context             mContext;
    private ProgressDialog      mDialog;

    private String              myPhoneNumber;
    private String              myName;
    private String              myGCMId;

    public SignUpTask(Context context) {
        mContext = context;
    }

    // Background 작업 시작전, UI 작업
    @Override
    protected void onPreExecute() {
        setDialog(true);
        super.onPreExecute();
    }

    // Background 작업 진행
    @Override
    protected String doInBackground(String... strings) {
        String value = "";
        myPhoneNumber = MyInfo.PHONE_NUMBER;

        if(myPhoneNumber == null || myPhoneNumber.trim().equals("")){
            value = "NOUSIM";
        }
        else {
            myName = strings[0];
            myGCMId = strings[1];

            try {
                HttpPost request = new HttpPost(URL + "SIGN");
                request.setHeader("Content-Type", "application/json; charset=utf-8");

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ID", Integer.parseInt(myPhoneNumber));
                jsonObject.put("NAME", myName);
                /* update - ver.20150603 */
                jsonObject.put("GCM_ID", myGCMId);
                //TODO: 현재 한글이 전달 안됨! 수정 요망!

                StringEntity stringEntity = new StringEntity(jsonObject.toString());
                HttpEntity httpEntity = stringEntity;
                request.setEntity(httpEntity);

                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(request);

                HttpEntity httpEntityResponse = response.getEntity();
                InputStream inputStream = httpEntityResponse.getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, HTTP.UTF_8));

                String line = "";
                while ((line = bufferedReader.readLine()) != null) value += line;
                inputStream.close();
                Log.i("SendMessageToServer", "result is [" + value + "]");
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return value;
    }

    // Background 작업이 끝난 후, UI 작업
    @Override
    protected void onPostExecute(String string) {
        setDialog(false);
        if (string.equals("SUCCESS")) {
            Toast.makeText(mContext, "등록/확인 완료!", Toast.LENGTH_SHORT).show();
            DATA.User user = new DATA.User(myName);
            user.setId(Integer.parseInt(myPhoneNumber));
            user.setGCMId(myGCMId);
            DATA.getInstance(mContext).insert(user);
        }
        else if (string.equals("ALREADY")) Toast.makeText(mContext, "이미 등록된 기기(번호)입니다.", Toast.LENGTH_SHORT).show();
        else if (string.equals("NOUSIM")) Toast.makeText(mContext, "유심을 확인해주세요.", Toast.LENGTH_SHORT).show();
        else Toast.makeText(mContext, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
        super.onPostExecute(string);
    }

    // Dialog 관련 | 생성/제거
    private void setDialog(boolean set) {
        if (set) {
            mDialog = new ProgressDialog(mContext);
            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mDialog.setMessage("등록/확인 중...");
            mDialog.show();
        }
        else {
            mDialog.dismiss();
        }
    }
}
