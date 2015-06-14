package bearbread.org.groupscheduler.eugene.service;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import bearbread.org.groupscheduler.R;
import bearbread.org.groupscheduler.eugene.MyInfo;

/**
 * Created by Eugene J. Jeon on 2015-05-27.
 */
public class SignDialog extends Dialog implements View.OnTouchListener {
    public static final String TAG = "SIGN_DIALOG";
    private Context     mContext;
    private EditText    inputNameText;
    private Button      okButton;
    private String      myName;

    public SignDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sign);

        inputNameText = (EditText) findViewById(R.id.dialog_sign_input_name);
        okButton = (Button) findViewById(R.id.dialog_sign_button);

        okButton.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (view.equals(okButton)) {
            myName = inputNameText.getText().toString();
            try {
                /* update - ver.20150603 */
                String result = new SignUpTask(mContext).execute(myName, MyInfo.GCM_ID).get();

                if (result.equals("SUCCESS")) dismiss();
                else if (result.equals("NOUSIM")) {
                    // 어플 강제종료! <uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES" /> 필요!
                    ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Activity.ACTIVITY_SERVICE);
                    activityManager.killBackgroundProcesses(mContext.getPackageName());
                }
                else {
                    //TODO: 와이파이 혹은 3G/4G 활성화 유도!
                }
            } catch(Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        return false;
    }
}
