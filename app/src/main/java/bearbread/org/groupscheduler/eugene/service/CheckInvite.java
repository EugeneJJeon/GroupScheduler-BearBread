package bearbread.org.groupscheduler.eugene.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import bearbread.org.groupscheduler.eugene.MyInfo;

/**
 * Created by Eugene J. Jeon on 2015-06-02.
 */
public class CheckInvite extends AsyncTask<Void, Void, ArrayList<HashMap<String, String>>> {
    private final String URL = "http://www.dcafe.xyz:8912/";
    private final String TAG = "CHECK INVITE";

    private Context mContext;
    private ProgressDialog mDialog;

    public CheckInvite(Context context) {
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
    protected ArrayList<HashMap<String, String>> doInBackground(Void... Void) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        String value = "";

        try {
            HttpPost request = new HttpPost(URL + "CHECK_INVITE");
            request.setHeader("Content-Type", "application/json; charset=utf-8");

            // 폰번호는 자기 자신의 아이디이므로 db 접근 보다 자주 사용 할 것 같음.
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ID", Integer.parseInt(MyInfo.PHONE_NUMBER));

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

            JSONArray jsonArray = new JSONArray(value);
            for (int i = 0; i < jsonArray.length(); i++) {
                HashMap<String, String> map = new HashMap<>();

                try {
                    JSONObject object = jsonArray.getJSONObject(i);
                    Iterator<String> iterator = object.keys();
                    while(iterator.hasNext()) {
                        String key = iterator.next();
                        map.put(key, object.getString(key));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                result.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    // Background 작업이 끝난 후, UI 작업
    @Override
    protected void onPostExecute(ArrayList<HashMap<String, String>> list) {
        setDialog(false);
        super.onPostExecute(list);
    }

    // Dialog 관련 | 생성/제거
    private void setDialog(boolean set) {
        if (set) {
            mDialog = new ProgressDialog(mContext);
            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mDialog.setMessage("유저목록 요청 중...");
            mDialog.show();
        }
        else {
            mDialog.dismiss();
        }
    }
}
