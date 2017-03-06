package tools.zhang.com.mytools.video;

import android.app.Activity;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import tools.zhang.com.mytools.R;

/**
 * Created by zhangdecheng on 2017/2/3.
 */
public class VideoActivity2 extends Activity implements View.OnTouchListener, GestureDetector.OnGestureListener {
    private static final String TAG = "VideoActivity2";

    /**
     * 播放控件
     */
    private VideoView videoView;

    /**
     * 播放控制条
     */
    private SeekBar sb_play;

    /**
     * 音量控制条
     */
    private SeekBar sb_volume;

    /**
     * 播放、暂停控制按钮
     */
    private ImageView iv_playControl;

    /**
     * 屏幕切换按钮
     */
    private ImageView iv_screenSwitch;

    /**
     * 音量图标
     */
    private ImageView iv_volume;

    /**
     * 当前播放进度
     */
    private TextView tv_currentTime;

    /**
     * 视频总时间
     */
    private TextView tv_totalTime;

    /**
     * 音量控制LinearLayout
     */
    private LinearLayout ll_volumeControl;

    /**
     * 整个布局容器
     */
    private RelativeLayout rl_video;

    /**
     * 包含各种控制View的布局
     */
    private LinearLayout ll_control;

    /**
     * 播放进度更新消息
     */
    private static final int UPDATE_TIME = 1;

    /**
     * 屏幕宽度（竖屏或横屏状态下）
     */
    private int screenWidth;

    /**
     * 屏幕高度（竖屏或横屏状态下）
     */
    private int screenHeight;

    /**
     * 音频管理器
     */
    private AudioManager audioManager;

    /**
     * 手势识别
     */
    private GestureDetector detector;

    /**
     * 音量变化广播接收器
     */
    private VolumeReceiver volumeReceiver;

    private int currentPosition;

    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int currentTime = videoView.getCurrentPosition();
            Utils.updateTimeFormat(tv_currentTime, currentTime);
            sb_play.setProgress(currentTime);
            uiHandler.sendEmptyMessageDelayed(UPDATE_TIME, 500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_2);
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        detector = new GestureDetector(this, this);
        initUI();
        initEvent();
        //注册音量变化广播接收器
        volumeReceiver = new VolumeReceiver(this, iv_volume, sb_volume);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        registerReceiver(volumeReceiver, filter);
        //为videoView设置视频路径
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
//        videoView.setVideoPath(path + "/360Download/123.f4v");
        videoView.setVideoPath(path + "/360Download/1.mp4");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if ( videoView.canPause() ) {
            setPauseStatus();
            videoView.pause();
            currentPosition = videoView.getCurrentPosition();
        }
        if ( uiHandler.hasMessages(UPDATE_TIME) ) {
            uiHandler.removeMessages(UPDATE_TIME);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ( videoView.canSeekForward() ) {
            videoView.seekTo(currentPosition);
        }
        if ( getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ) {
            setSystemUiHide();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if ( videoView.canPause() ) {
            setPauseStatus();
            videoView.pause();
        }
        if ( uiHandler.hasMessages(UPDATE_TIME) ) {
            uiHandler.removeMessages(UPDATE_TIME);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ( videoView.canPause() ) {
            videoView.pause();
        }
        if ( uiHandler.hasMessages(UPDATE_TIME) ) {
            uiHandler.removeMessages(UPDATE_TIME);
        }
        unregisterReceiver(volumeReceiver);
    }

    private void initUI() {
        videoView = (VideoView) findViewById(R.id.vv_player);
        sb_play = (SeekBar) findViewById(R.id.sb_play);
        sb_volume = (SeekBar) findViewById(R.id.sb_volume);
        iv_playControl = (ImageView) findViewById(R.id.iv_playControl);
        iv_screenSwitch = (ImageView) findViewById(R.id.iv_screenSwitch);
        iv_volume = (ImageView) findViewById(R.id.iv_volume);
        tv_currentTime = (TextView) findViewById(R.id.tv_currentTime);
        tv_totalTime = (TextView) findViewById(R.id.tv_totalTime);
        ll_volumeControl = (LinearLayout) findViewById(R.id.ll_volumeControl);
        ll_control = (LinearLayout) findViewById(R.id.ll_control);
        rl_video = (RelativeLayout) findViewById(R.id.rl_video);
        sb_volume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        sb_volume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
    }

    /**
     * 设置播放状态
     */
    private void setPlayStatus() {
        iv_playControl.setImageResource(R.drawable.pause_btn_style);
        sb_play.setMax(videoView.getDuration());
        Utils.updateTimeFormat(tv_totalTime, videoView.getDuration());
    }

    /**
     * 设置暂停状态
     */
    private void setPauseStatus() {
        iv_playControl.setImageResource(R.drawable.play_btn_style);
    }

    private void initEvent() {
        iv_playControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( videoView.isPlaying() ) {
                    setPauseStatus();
                    videoView.pause();
                    uiHandler.removeMessages(UPDATE_TIME);
                } else {
                    setPlayStatus();
                    videoView.start();
                    uiHandler.sendEmptyMessage(UPDATE_TIME);
                }
            }
        });

        sb_play.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if ( fromUser ) {
                    Log.e(TAG, "onProgressChanged: fromUser:" + fromUser +",progress:"+progress );
                    videoView.seekTo(progress);
                    Utils.updateTimeFormat(tv_currentTime, progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e(TAG, "onStartTrackingTouch: ");
                uiHandler.removeMessages(UPDATE_TIME);
                if ( !videoView.isPlaying() ) {
                    setPlayStatus();
                    videoView.start();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e(TAG, "onStopTrackingTouch: " );
                uiHandler.sendEmptyMessage(UPDATE_TIME);
            }
        });

        sb_volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                iv_playControl.setImageResource(R.drawable.play_btn_style);
                videoView.seekTo(0);
                sb_play.setProgress(0);
                Utils.updateTimeFormat(tv_currentTime, 0);
                videoView.pause();
                uiHandler.removeMessages(UPDATE_TIME);
            }
        });

        iv_screenSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    iv_screenSwitch.setImageResource(R.drawable.exit_full_screen);
                } else if ( getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    iv_screenSwitch.setImageResource(R.drawable.full_screen);
                }
            }
        });

        videoView.setOnTouchListener(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        if ( newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ) {
            setSystemUiHide();
            setVideoViewScale(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            iv_screenSwitch.setImageResource(R.drawable.exit_full_screen);
            ll_volumeControl.setVisibility(View.VISIBLE);
        } else if ( newConfig.orientation == Configuration.ORIENTATION_PORTRAIT ) {
            setSystemUiVisible();
            setVideoViewScale(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dp2px(this, 240f));
            iv_screenSwitch.setImageResource(R.drawable.full_screen);
            ll_volumeControl.setVisibility(View.GONE);
        }
    }

    private void setSystemUiHide() {
        if ( Build.VERSION.SDK_INT >= 19 ) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void setSystemUiVisible() {
        if ( Build.VERSION.SDK_INT >= 19 ) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }

    /**
     * 设置布局大小
     *
     * @param width  宽度
     * @param height 高度
     */
    private void setVideoViewScale(int width, int height) {
        ViewGroup.LayoutParams params = rl_video.getLayoutParams();
        params.width = width;
        params.height = height;
        rl_video.setLayoutParams(params);
        ViewGroup.LayoutParams layoutParams = videoView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        videoView.setLayoutParams(layoutParams);
    }

    @Override
    public void onBackPressed() {
        if ( getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            iv_screenSwitch.setImageResource(R.drawable.exit_full_screen);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        if ( ll_control.getVisibility() == View.VISIBLE ) {
            ll_control.setVisibility(View.GONE);
        } else {
            ll_control.setVisibility(View.VISIBLE);
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float offsetX = e1.getX() - e2.getX();
        float offsetY = e1.getY() - e2.getY();
        float absOffsetX = Math.abs(offsetX);
        float absOffsetY = Math.abs(offsetY);
        if ( (e1.getX() < screenWidth / 2) && (e2.getX() < screenWidth / 2) && (absOffsetX < absOffsetY) ) {
            changeBrightness(offsetY);
        } else if ( (e1.getX() > screenWidth / 2) && (e2.getX() > screenWidth / 2) && (absOffsetX < absOffsetY) ) {
            changeVolume(offsetY);
        }
        return true;
    }

    private void changeVolume(float offset) {
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int index = (int) (offset / screenHeight * maxVolume);
        int volume = Math.max(currentVolume + index, 0);
        volume = Math.min(volume, maxVolume);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        sb_volume.setProgress(volume);
    }

    private void changeBrightness(float offset) {
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        float brightness = attributes.screenBrightness;
        float index = offset / screenHeight / 2;
        brightness = Math.max(brightness + index, WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF);
        brightness = Math.min(WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL, brightness);
        attributes.screenBrightness = brightness;
        getWindow().setAttributes(attributes);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return detector.onTouchEvent(event);
    }


}
