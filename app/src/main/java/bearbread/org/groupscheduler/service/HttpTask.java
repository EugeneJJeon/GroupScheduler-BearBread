package bearbread.org.groupscheduler.service;

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
public class HttpTask extends AsyncTask<String, Void, Boolean> {
    private final String URL = "http://www.dcafe.xyz:8912/";
    private final String TAG = "HttpTask";

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            HttpPost request = new HttpPost(URL + "GS");
            request.setHeader("Content-Type", "application/json; charset=utf-8");

            JSONObject jsonObject = new JSONObject();
            Log.i(TAG, "what is this ?? -> " + strings[0]);
            jsonObject.put("query", strings[0]);
            jsonObject.put("group", strings[1]);

            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            HttpEntity httpEntity = stringEntity;
            request.setEntity(httpEntity);

            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(request);

            HttpEntity httpEntityResponse = response.getEntity();
            InputStream inputStream = httpEntityResponse.getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, HTTP.UTF_8));

            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null) result += line;
            inputStream.close();
            Log.i("SendMessageToServer", "result is [" + result + "]");
            if (result.equals("OK") || result.equals("TRUE")) return true;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(Boolean value) {
        super.onPostExecute(value);

        /*
		* http://onycomict.com/board/bbs/board.php?bo_table=class&wr_id=132
		* */
        Log.i(TAG, "onPostExecute : " + value);
    }
}
