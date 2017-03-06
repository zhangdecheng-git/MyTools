package tools.zhang.com.mytools.video;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;

import tools.zhang.com.mytools.R;

/**
 * Created by zhangdecheng on 2017/2/3.
 */
public class VideoActivity extends Activity {
    VideoView videoView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoView = (VideoView) findViewById(R.id.video_view);
//        videoView.setVideoURI(Uri.parse("http://baidu.baomihua.com/watch/5342801548359263183.html?page=videoMultiNeed"));
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"360Download/1.mp4";
        videoView.setVideoPath(path);

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoView);
    }

}
