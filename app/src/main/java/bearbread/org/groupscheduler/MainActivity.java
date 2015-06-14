package bearbread.org.groupscheduler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bearbread.org.groupscheduler.eugene.MyInfo;
import bearbread.org.groupscheduler.eugene.database.dao.DATA;
import bearbread.org.groupscheduler.eugene.service.AgreeInvite;
import bearbread.org.groupscheduler.eugene.service.CacheService;
import bearbread.org.groupscheduler.eugene.service.CheckInvite;
import bearbread.org.groupscheduler.eugene.service.GetUserToServer;
import bearbread.org.groupscheduler.eugene.service.Invite;
import bearbread.org.groupscheduler.eugene.service.SignDialog;

/**
 * Created by Eugene J. Jeon on 2015-04-08.
 *
 * 아래에 실제 사용 예시를 나타낸 것입니다. - 참고 -
 */
public class MainActivity extends Activity {
    EditText etxt1;
    TextView txtView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        // 반드시 어플 처음 실행시 실행해야 하는 구문!
        MyInfo.Initialize(this);

        etxt1 = (EditText)findViewById(R.id.editText1);
        txtView = (TextView) findViewById(R.id.textView);
	}

    /*
    * 1; 그룹 생성!
    * 2; 회원가입창 띄우기! -> 가입하기!
    * 3; 폰에서 그룹 목록 확인
    * 4; 서버에서 유저 목록 확인
    * 5; 서버에서 지정된 그룹 스케줄 가져오기!
    * 6; 특정 유저 초대하기!
    * 7; 내가 초대받은 목록 불러오기!
    * 8; 초대 수락!
    * */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1 : // 그룹 생성
                // new Group(String admin, String name)
                List<DATA.User> users = DATA.getInstance(this).getUser();
                DATA.Group group = new DATA.Group(users.get(0).getId(), etxt1.getText().toString());        // 필요한 것 : 유저아이디, 그룹 이름
                DATA.Cache cache = new DATA.Cache(this, DATA.QUERY_TYPE.INSERT, group);
                Intent serviceIntent = new Intent(this, CacheService.class);
                serviceIntent.putExtra(DATA.TO, cache);
                startService(serviceIntent);
                break;

            case R.id.btn2 : // 회원가입 창 띄우기 | 임시
                new SignDialog(this).show();
                txtView.setText(MyInfo.GCM_ID);
                break;

            case R.id.btn3 : // 폰에서 그룹 목록 확인!
                List<DATA.Group> groups = DATA.getInstance(this).getGroup();
                String str = "";
                for (int i = 0; i < groups.size(); i++) str += groups.get(i).toString();
                txtView.setText(str);
                break;

            case R.id.btn4 : // 서버에서 유저 목록 확인!
                try {
                    ArrayList<HashMap<String, String>> list = new GetUserToServer(this).execute().get();
                    String value = "";

                    for (int i = 0; i < list.size(); i++) {
                        value += "#" + (i+1) + " : " + list.get(i).toString();
                        // 이부분이 직접 사용하는 방법! : 유저아이디와 유저이름을 확인할 수 있다.
                        value += "(" + list.get(i).get("UID") + ", " + list.get(i).get("UNAME") + ")" + "\n";
                    }
                    txtView.setText(value);
                } catch (Exception e) {
                }
                break;

            case R.id.btn5 : // 서버에서 지정된 그룹 스케줄 가져오기!
                //TODO: { ROW:x, COL:y } 형태의 ArrayList<HashMap<String, String>>으로 줄 것!
                break;

            case R.id.btn6 : // 특정 유저 초대하기!
                try {
                    /* 주의! 임의로 넣은 값이르모 실제로는 직접 구해서 넣어야 한다. */
                    String UID = String.valueOf("1051081221");      // 초대할 유저 아이디
                    String GID = String.valueOf("2");              // 초대할 그룹 아이디
                    String GNAME = "group name";                    // 초대할 그룹 이름

                    String result = new Invite(this).execute(UID, GID, GNAME).get();
                    if (result.equals("SUCCESS")) {
                        Toast.makeText(this, "초대 성공!", Toast.LENGTH_SHORT).show();
                    } else if (result.equals("FAIL")) {
                        Toast.makeText(this, "실패(이미 초대 했었다.)!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "실패(인터넷 확인)!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btn7 : // 내가 초대받은 목록 불러오기!
                try {
                    ArrayList<HashMap<String, String>> list = new CheckInvite(this).execute().get();
                    String value = "";

                    for (int i = 0; i < list.size(); i++) {
                        value += "#" + (i+1) + " : " + list.get(i).toString();
                        // 초대된 그룹아이디와 그룹명 알아내는 부분! : 초대받은 그룹 아이디, 그룹 이름
                        value += "(" + list.get(i).get("GID") + ", " + list.get(i).get("GNAME") + ")" + "\n";
                    }
                    txtView.setText(value);
                } catch (Exception e) {
                }
                break;

            case R.id.btn8 : // 초대 수락하기!
                /* 주의! 그룹 아이디는 임의로 넣은 것이므로 실제로는 구해서 넣어야 된다. */
                String groupName = "1";         // 그룹 아이디

                // 동의 버전
                new AgreeInvite(this).execute(AgreeInvite.AGREE, groupName);

                // 거절 버전
                //new AgreeInvite(this).execute(AgreeInvite.REFUSE, groupName);
                break;
        }
    }
}