package tools.zhang.com.mytools.volley;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.lang.reflect.Method;

import testpush.zhang.com.volley.R;

/**
 * Created by zhangdecheng on 2017/3/7.
 */
public class VolleyActivity extends Activity {
    private static final String TAG = "VolleyActivity";
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);

        mRequestQueue = Volley.newRequestQueue(this);

        findViewById(R.id.start_volley).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStringRequest();
            }
        });

        findViewById(R.id.newbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLibAndReflect();
            }
        });
    }

    private void startStringRequest() {
        String url = "http://openbox.mobilem.360.cn/topic/detail?forceNoCache=1&showTitleBar=0&id=235&os=21&vc=300050050&v=5.0.50&md=LG-H778&sn=4.341100969027444&cpu=qualcomm+msm8926&ca1=armeabi-v7a&ca2=armeabi&m=395de6da2b0faf8619bed7fe71c83f39&m2=8e812fd567fa75fff83e1ebad9fcf996&ch=&ppi=720_1188&startCount=1&re=1&cpc=1&snt=-1&nt=1&s_3pk=1&ui_version=green&prepage=appgroup_perhome&curpage=topic";

        Request request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                success(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                error(volleyError);
            }
        });
        request.setTag(this);
        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));
        mRequestQueue.add(request);
    }

    private void success(String json) {
        Log.d(TAG, "json:" + json);
        Toast.makeText(this, "json:" + json, 1).show();
    }

    private void error(VolleyError error) {
        Log.d(TAG, "json:" + error.toString());
        Toast.makeText(this, "json:" + error.toString(), 1).show();
    }



    private void userLibAndReflect() {
        // 因为未引用utils整个lib工程，所以只能反射调用
        String libraries = "";
        try {
            Class cls = Class.forName("testpush.zhang.com.utils.LibrariesUtils");
            Method method = cls.getMethod("getLibraryString", String.class);
            libraries += method.invoke(null, "libraries");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 如果引用lib，可以这么调用
        // String gg = LibrariesUtils.getLibraryString("Volley");

        Toast.makeText(VolleyActivity.this, libraries, 0).show();
    }
}
