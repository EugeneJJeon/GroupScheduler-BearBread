package bearbread.org.groupscheduler.eugene.service;

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
 * Created by Eugene J. Jeon on 2015-04-29.
 */
public class HttpTask extends AsyncTask<String, Void, String> {
    private final String URL = "http://www.dcafe.xyz:8912/";
    private final String TAG = "HttpTask";

    @Override
    protected String doInBackground(String... strings) {
        String value = "";
        try {
            HttpPost request = new HttpPost(URL + "GS");
            request.setHeader("Content-Type", "application/json; charset=utf-8");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user", Integer.parseInt(strings[0]));
            jsonObject.put("type", strings[1]);
            jsonObject.put("query", strings[2]);
            Log.i(TAG, "what is this ?? -> " + strings[2] + ", who ? -> " + strings[0]);

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

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(String value) {
        super.onPostExecute(value);

        /*
		* http://onycomict.com/board/bbs/board.php?bo_table=class&wr_id=132
		* */
        Log.i(TAG, "onPostExecute : " + value);
    }
}
