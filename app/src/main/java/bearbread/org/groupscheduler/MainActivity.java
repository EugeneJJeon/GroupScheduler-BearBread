package bearbread.org.groupscheduler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import bearbread.org.groupscheduler.database.dao.GSDataDAO;
import bearbread.org.groupscheduler.service.CacheService;

/**
 * Created by Eugene J. Jeon on 2015-04-08.
 */
public class MainActivity extends Activity {
    EditText etxt1;
    EditText etxt2;
    EditText etxt3;
    TextView txtView;
    TextView txtView1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        etxt1 = (EditText)findViewById(R.id.editText1);
        etxt2 = (EditText) findViewById(R.id.editText2);
        etxt3 = (EditText) findViewById(R.id.editText3);
        txtView = (TextView) findViewById(R.id.textView);
        txtView1 = (TextView) findViewById(R.id.textView1);
	}

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1 :
                Toast.makeText(this, "유저 입력", Toast.LENGTH_SHORT).show();

                // new UserTO(String id, String name, String tel)
                GSDataDAO.UserTO userTO = new GSDataDAO.UserTO(etxt1.getText().toString(), etxt2.getText().toString(), etxt3.getText().toString());
                GSDataDAO.CacheTO cacheTO = new GSDataDAO.CacheTO(this, GSDataDAO.TYPE.INSERT, userTO);
                Intent serviceIntent = new Intent(this, CacheService.class);
                serviceIntent.putExtra(GSDataDAO.TO, cacheTO);
                startService(serviceIntent);
                break;
            case R.id.btn2 :
                // new GroupTO(String admin, String name) : id는 자동생성 -> long getId()
                GSDataDAO.GroupTO groupTO = new GSDataDAO.GroupTO(etxt2.getText().toString(), etxt3.getText().toString());
                GSDataDAO.CacheTO cache = new GSDataDAO.CacheTO(this, GSDataDAO.TYPE.INSERT, groupTO);

                // new ScheduleTO(long groupID, String owner, String name, String start_date, String end_date)
                GSDataDAO.ScheduleTO scheduleTO = new GSDataDAO.ScheduleTO(groupTO.getId(), "스케줄주인", "스케줄이름", "20150512", "20150512");
                GSDataDAO.CacheTO newCache = new GSDataDAO.CacheTO(this, GSDataDAO.TYPE.INSERT, scheduleTO);

//                // db 확인 | cache 확인
//                List<GSDataDAO.UserTO> users = GSDataDAO.getInstance(this).getUserTO();
//                String str = "";
//                for (int i = 0; i < users.size(); i++) {
//                    str += users.get(i).toString();
//                }
//                txtView.setText(str);
//
//                List<GSDataDAO.CacheTO> onlineAsyncCaches = GSDataDAO.getInstance(this).getCacheTO();
//                str = "";
//                for (int i = 0; i < onlineAsyncCaches.size(); i++) {
//                    str += onlineAsyncCaches.get(i).toString();
//                }
//                txtView1.setText(str);
//
//                List<GSDataDAO.CacheTO> caches = GSDataDAO.getInstance(this).getCacheTO();
//                for (int i = 0; i < caches.size(); i++) {
//                    Log.i("???????", String.valueOf(i) + " - query : " + caches.get(i).getQuery());
//                    GSDataDAO.getInstance(this).delete(caches.get(i));
//                    break;
//                }

                break;
        }
    }
}
