package tools.zhang.com.mytools.crash_getStringExtra;

import java.io.Serializable;

/**
 * Created by zhangdecheng on 2017/3/6.
 */
public class SerialData implements Serializable {
    public String name;
    public SerialData(String name) {
        this.name = name;
    }
}
