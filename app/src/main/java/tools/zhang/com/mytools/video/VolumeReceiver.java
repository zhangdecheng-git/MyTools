package tools.zhang.com.mytools.video;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;

import tools.zhang.com.mytools.R;

/**
 * 音量变化广播接收器
 * Created by CZY on 2017/1/31.
 */
public class VolumeReceiver extends BroadcastReceiver {
    private static final String TAG = "VolumeReceiver";
    private ImageView iv_volume;
    private SeekBar seekBar_volume;
    private AudioManager audioManager;//音频管理器

    public VolumeReceiver(Context context, ImageView iv_volume, SeekBar seekBar_volume) {
        this.iv_volume = iv_volume;
        this.seekBar_volume = seekBar_volume;
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
            int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            Log.e(TAG, "onReceive: volume chang:" +  volume);
            if (volume == 0) {
                iv_volume.setImageResource(R.drawable.mute);
            } else {
                iv_volume.setImageResource(R.drawable.volume);
            }
            seekBar_volume.setProgress(volume);
        }
    }
}
