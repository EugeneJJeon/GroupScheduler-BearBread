package bearbread.org.groupscheduler.eugene;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

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

import bearbread.org.groupscheduler.eugene.service.ChangeRegId;

/**
 * Created by Eugene J. Jeon on 2015-06-03.
 */
public class MyInfo {
    private static MyInfo instance = null;
    public static void Initialize(Context context) {
        if (instance == null) {
            instance = new MyInfo(context);
        }
    }

    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    // SharedPreferences 에 저장할 때, Key 값으로 사용
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String TAG = "MyInfo";

    private final String URL = "http://www.dcafe.xyz:8912/";

    private String SENDER_ID = "975423462960";
    private GoogleCloudMessaging gcm;

    public static String GCM_ID;
    public static String PHONE_NUMBER;

    private Context mContext;

    private MyInfo(Context context) {
        mContext = context;

        // 구글 플레이 서비스 체크한 뒤, GMS 아이디 저장
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(mContext);
            GCM_ID = getRegistrationId(mContext);

            if (GCM_ID.isEmpty()) {
                registerInBackground();

                if (ChangeRegId.CAHNGE_REG_ID) {
                    updateRegisterInBackEnd();
                }
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }

        // 폰 번호 저장
        PHONE_NUMBER = getPhoneNumber();
    }

    // 구글 플레이 체크!
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity)mContext, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                ((Activity)mContext).finish();
            }
            return false;
        }
        return true;
    }

    // SharedPreference 를 확인하여 등록된 아이디를 찾는 기능
    // 등록된 아이디가 없다면, 빈 문자열 반환
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }

        // 앱 버전 확인 후, 버전이 변경되었다면 기존 등록 아이디 제거
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            ChangeRegId.CAHNGE_REG_ID = true;
            return "";
        }
        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context) {
        return context.getSharedPreferences(context.getClass().getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    // 아이디 서버에 요청, 그리고 앱에 등록
    private void registerInBackground() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(mContext);
                    }
                    GCM_ID = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + GCM_ID;
                    Log.i(TAG, msg);

                    // 등록 아이디를 저장해서 매번 아이디를 재발급받지 않도록 한다.
                    storeRegistrationId(mContext, GCM_ID);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                    Log.e(TAG, msg);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void voids) { super.onPostExecute(voids); }
        }.execute(null, null, null);
    }

    private void storeRegistrationId(Context context, String regid) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regid);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    private void updateRegisterInBackEnd() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                String value = "";
                try {
                    HttpPost request = new HttpPost(URL + "RESIGN");
                    request.setHeader("Content-Type", "application/json; charset=utf-8");

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("UID", Integer.parseInt(PHONE_NUMBER));
                    jsonObject.put("UGCMID", GCM_ID);

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

                return null;
            }

            @Override
            protected void onPostExecute(Void voids) { super.onPostExecute(voids); }
        }.execute(null, null, null);
    }

    // 폰 번호 얻기
    private String getPhoneNumber()
    {
        //TODO: 이슈! 핸드폰번호는 핸드폰에서만 되지...유심이 없는 기기는 안됨!
        TelephonyManager systemService = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = systemService.getLine1Number();
        phoneNumber = phoneNumber.substring(phoneNumber.length() - 10, phoneNumber.length());
        phoneNumber =  "0" + phoneNumber;

        if(phoneNumber ==null || phoneNumber.equals("")) {
            Toast.makeText(mContext, "사용할 수 없는 기기입니다. 유심을 확인해 주세요.", Toast.LENGTH_SHORT).show();
        }

        return phoneNumber;
    }
}
