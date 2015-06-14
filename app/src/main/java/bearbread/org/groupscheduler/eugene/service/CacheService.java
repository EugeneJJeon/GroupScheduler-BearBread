package bearbread.org.groupscheduler.eugene.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import bearbread.org.groupscheduler.eugene.database.dao.DATA;

/**
 * Created by Eugene J. Jeon on 2015-05-08.
 */
public class CacheService extends Service implements Runnable {
    private static final String         TAG = "CacheService";

    private int                         mStartId;           // 시작 ID
    private Handler                     mHandler;           // 서비스에 대한 스레드에 연결된 Handler
    private boolean                     mRunning;           // 서비스 동작여부 flag
    private DATA.Cache                  mCache;             // 넘겨받은 Cache

    /*  서비스가 처음으로 생성되면 호출 (최초 생성시 한 번만 호출)
     * 이 메소드 안에서 초기의 설정 작업을 하면되고 서비스가 이미 실행중이면 이 메소드는 호출되지 않는다.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        //Note: can start a new thread and use it for long background processing from here.
        mHandler = new Handler();
        mRunning = false;

        Log.d(TAG, "onCreate");
    }

    /*  다른 컴포넌트가 startService()를 호출해서 서비스가 시작되면 이 메소드가 호출
     * 만약 연결된 타입(bind)의 서비스를 구현한다면 이 메소드는 재정의 할 필요가 없다.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*  Service를 TaskKiller에게서 죽지 않게 하는 방법 중 한 가지가, 바로 Foreground Service로 실행하는 것입니다. */
        // startForeground(int startId, Notification notification);
        //  만약 Notification을 없애고 싶다면 아래와 같이 만들면 됩니다. 단, Android 4.2 버전 이전만 Framework 버그로 인해 가능 부분입니다.
        // startForeground(startId, new Notification());

        // Intent로 부터 Cache값 추출!
        mCache = intent.getParcelableExtra(DATA.TO);
        mStartId = startId;
        mHandler.post(this);
        mRunning = true;

        return START_STICKY;
    }

    /* action AsyncService */
    @Override
    public void run() {
        if (!mRunning) {
            Log.e(TAG, "destroy after run");
            return;
        }

        // Cache 저장 & 그룹 아이디 발급!
//        CACHE.SIZE = (int)DATA.getInstance(this).getCacheSize();
//        GROUP.SIZE = (int)DATA.getInstance(this).getGroupSize();
//        SCHEDULE.SIZE = (int)DATA.getInstance(this).getScheduleSize();
        DATA.getInstance(this).insert(mCache);

        // 동기화 서비스 실행!
        if (!AsyncService.Running) {
            Log.i(TAG, "startService(AsyncService)");
            Intent intent = new Intent(this, AsyncService.class);
            intent.putExtra(DATA.TO, mCache);
            startService(intent);
        }

        Log.i(TAG, "그만!!!");
        stopSelf(mStartId);

        // 다음 작업을 다시 요구
        Log.e(TAG, "Handler re-post(#" + mStartId + ")");
        mHandler.post(this);
    }

    /*  서비스가 소멸되는 도중에 이 메소드가 호출되며 주로 Thread, Listener, BroadcastReceiver와 같은 자원들을 정리하는데 사용
     * TaskKiller에 의해 서비스가 강제종료될 경우에는 이 메소드가 호출되지 않는다.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        mRunning = false;

        Log.d(TAG, "onDestroy");
    }

    /*  원격 메소드 호출을 위해 사용!
     *  다른 컴포넌트가 startService()를 호출해서 서비스가 시작되면 이 메소드가 호출
     * 이 메소드에서 "IBinder"를 반환해서 서비스와 컴포넌트가 통신하는데 사용하는 인터페이스를 제공
     * 만약 시작 타입의 서비스를 구현한다면 "null"을 반환하면 된다.
     */
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}
