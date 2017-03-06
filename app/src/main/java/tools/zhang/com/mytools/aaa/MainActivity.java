package tools.zhang.com.mytools.aaa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tools.zhang.com.jnilibrary.NdkUtils;
import tools.zhang.com.mytools.DisplayActivity;
import tools.zhang.com.mytools.R;
import tools.zhang.com.mytools.baseadapter.BaseAdapterActivity;
import tools.zhang.com.mytools.camera.CameraActivity;
import tools.zhang.com.mytools.checkbox.CheckBoxActivity;
import tools.zhang.com.mytools.crash.CrashHandler;
import tools.zhang.com.mytools.crash_getStringExtra.CrashGetStringMainActivity;
import tools.zhang.com.mytools.database.SqlUtils;
import tools.zhang.com.mytools.draw.DrawActivity;
import tools.zhang.com.mytools.fingerscanner.FingerScannerActivity;
import tools.zhang.com.mytools.fragment.viewpager.FragmentViewPagerActivity;
import tools.zhang.com.mytools.keepalive.TestKeepAliveActivity;
import tools.zhang.com.mytools.leak.LeakActivity;
import tools.zhang.com.mytools.parcelable.ParcelableMainActivity;
import tools.zhang.com.mytools.process.ProcessActivity;
import tools.zhang.com.mytools.propertyAnimation.PropertyActivity;
import tools.zhang.com.mytools.proxy.ProxyMain;
import tools.zhang.com.mytools.recyclerView.RecyclerViewActivity;
import tools.zhang.com.mytools.savinginstancestate.SavingStateActivity;
import tools.zhang.com.mytools.screen.ScreenActivity;
import tools.zhang.com.mytools.scroll.ScrollActivity;
import tools.zhang.com.mytools.setXfermode.PorterDuffXfermodeActivity;
import tools.zhang.com.mytools.setXfermode.XfermodeActivity;
import tools.zhang.com.mytools.threadpool.ScheduledUtils;
import tools.zhang.com.mytools.utils.CoreDaemonUtils;
import tools.zhang.com.mytools.video.VideoActivity;
import tools.zhang.com.mytools.video.VideoActivity2;
import tools.zhang.com.mytools.videorecord.RecordVideoActivity;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private static final String TAG = "MainActivity";

    private Context mContext;
    private ListView mListView;
    private ListAdapter mAdapter;
    private List<MainData> mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setOnItemClickListener(this);
        mAdapter = new ListAdapter(mContext);
        initData();
        mAdapter.setData(mDataList);
        mListView.setAdapter(mAdapter);
    }

    private void initData() {
        mDataList = new ArrayList<>();
        mDataList.add(new MainData(0, "Process"));
        mDataList.add(new MainData(1, "NDK"));
        mDataList.add(new MainData(2, "CoreDeamon"));
        mDataList.add(new MainData(3, "TestLeak"));
        mDataList.add(new MainData(4, "TestMemory"));
        mDataList.add(new MainData(5, "Draw"));
        mDataList.add(new MainData(6, "屏幕信息"));
        mDataList.add(new MainData(7, "属性动画"));
        mDataList.add(new MainData(8, "动态代理"));
        mDataList.add(new MainData(9, "拉活"));
        mDataList.add(new MainData(10, "数据库"));
        mDataList.add(new MainData(11, "全屏切换"));
        mDataList.add(new MainData(12, "手指识别扫描"));
        mDataList.add(new MainData(13, "activity view 状态保存"));
        mDataList.add(new MainData(14, "RecycleView"));
        mDataList.add(new MainData(15, "baseAdapter"));
        mDataList.add(new MainData(16, "相机"));
        mDataList.add(new MainData(17, "FragmentViewPager"));
        mDataList.add(new MainData(18, "Scroll"));
        mDataList.add(new MainData(19, "checkBox"));
        mDataList.add(new MainData(20, "setXfermode"));
        mDataList.add(new MainData(21, "PorterDuffXfermode"));
        mDataList.add(new MainData(22, "videoView"));
        mDataList.add(new MainData(23, "videoView2"));
        mDataList.add(new MainData(24, "parcelable序列化"));
        mDataList.add(new MainData(25, "scheduled线程池"));
        mDataList.add(new MainData(26, "模拟点击"));
        mDataList.add(new MainData(27, "静态代码块"));
        mDataList.add(new MainData(28, "捕获crash"));
        mDataList.add(new MainData(29, "录制视频"));
        mDataList.add(new MainData(30, "启动activity、service崩溃"));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        MainData mainData = (MainData) parent.getAdapter().getItem(position);
        switch (mainData.mNum) {
            case 0:
                testActivity(ProcessActivity.class);
                break;
            case 1:
                String ndkData = NdkUtils.fromJni();
                String ndkData2 = new NdkUtils().getNormalJni();
                Toast.makeText(mContext, "ndkData:" + ndkData +" , ndkData2:" + ndkData2, Toast.LENGTH_LONG).show();
                break;
            case 2:
                CoreDaemonUtils.tryStart(mContext);
                break;
            case 3:
                testActivity(LeakActivity.class);
                break;
            case 5:
                testActivity(DrawActivity.class);
                break;
            case 6:
                testActivity(DisplayActivity.class);
                break;
            case 7:
                testActivity(PropertyActivity.class);
            case 8:
                ProxyMain.test();
                break;
            case 9:
                testActivity(TestKeepAliveActivity.class);
                break;
            case 10:
                SqlUtils.testAddData(mContext);
                break;
            case 11:
                testActivity(ScreenActivity.class);
                break;
            case 12:
                testActivity(FingerScannerActivity.class);
                break;
            case 13:
                testActivity(SavingStateActivity.class);
                break;
            case 14:
                testActivity(RecyclerViewActivity.class);
                break;
            case 15:
                testActivity(BaseAdapterActivity.class);
                break;
            case 16:
                testActivity(CameraActivity.class);
                break;
            case 17:
                testActivity(FragmentViewPagerActivity.class);
                break;
            case 18:
                testActivity(ScrollActivity.class);
                break;
            case 19:
                testActivity(CheckBoxActivity.class);
                break;
            case 20:
                testActivity(XfermodeActivity.class);
                break;
            case 21:
                testActivity(PorterDuffXfermodeActivity.class);
                break;
            case 22:
                testActivity(VideoActivity.class);
                break;
            case 23:
                testActivity(VideoActivity2.class);
                break;
            case 24:
                testActivity(ParcelableMainActivity.class);
                break;
            case 25:
                ScheduledUtils.testScheduled();
                break;
            case 28:
                crash();
                break;
            case 29:
                testActivity(RecordVideoActivity.class);
                break;
            case 30:
                testActivity(CrashGetStringMainActivity.class);
                break;
            default:
                break;
        }
    }

    private void testActivity(Class clazz) {
        Intent intent = new Intent(mContext, clazz);
        mContext.startActivity(intent);
    }

    private void crash() {
        // 这样应用会崩，并把崩溃日志记录到本地
        //throw new RuntimeException("test exception");
        // 这样应用不崩，并把崩溃日志记录到本地
        try {
            throw new RuntimeException("uncaughtException exception");
        } catch (Exception e) {
            CrashHandler.getInstance().saveCrash(e);
        }
    }
}




