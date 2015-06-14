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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by Eugene J. Jeon on 2015-06-02.
 */
public class Invite extends AsyncTask<String, Void, String> {
    private final String URL = "http://www.dcafe.xyz:8912/";
    private final String TAG = "INVITE";

    private Context mContext;
    private ProgressDialog mDialog;

    public Invite(Context context) {
        mContext = context;
    }

    //TODO: 가입할 때, GMS 아이디 알아내서 서버로 보내고 저장한다. 그전에 GMS-ID 길이알아내고 DB만들어야된다.


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

        try {
            HttpPost request = new HttpPost(URL + "INVITE");
            request.setHeader("Content-Type", "application/json; charset=utf-8");

            // 폰번호는 자기 자신의 아이디이므로 db 접근 보다 자주 사용 할 것 같음.
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UID", Integer.parseInt(strings[0]));
            jsonObject.put("GID", Integer.parseInt(strings[1]));
            jsonObject.put("GNAME", strings[2]);

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

        return value;
    }

    // Background 작업이 끝난 후, UI 작업
    @Override
    protected void onPostExecute(String string) {
        setDialog(false);
        super.onPostExecute(string);
    }

    // Dialog 관련 | 생성/제거
    private void setDialog(boolean set) {
        if (set) {
            mDialog = new ProgressDialog(mContext);
            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mDialog.setMessage("초대 날리는 중...");
            mDialog.show();
        }
        else {
            mDialog.dismiss();
        }
    }
}
