package tools.zhang.com.mytools.proxy;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by zhangdecheng on 2016/11/1.
 */
public class ProxyMain {

    private static final String TAG = "ProxyMain";

    public static void test() {
        //1.创建委托对象
        RealSubject realSubject = new RealSubject();
        //2.创建调用处理器对象
        ProxyHandler handler = new ProxyHandler(realSubject);
        //3.动态生成代理对象
        Subject proxySubject = (Subject) Proxy.newProxyInstance(RealSubject.class.getClassLoader(), RealSubject.class.getInterfaces(), handler);
        //4.通过代理对象调用方法
        proxySubject.request();
        proxySubject.getName(1);
    }


    /**
     * 接口
     */
    interface Subject {
        void request();
        String getName(int id);
    }

    /**
     * 委托类
     */
    static class RealSubject implements Subject {
        public void request() {
            Log.e(TAG, "====RealSubject Request====");
        }

        @Override
        public String getName(int id) {
            Log.e(TAG, "====RealSubject getName====");
            if (id == 1) {
                return "name1";
            } else if (id == 2) {
                return "name2";
            } else {
                return "name other";
            }
        }
    }

    /**
     * 代理类的调用处理器
     * 这个类的目的是指定运行时将生成的代理类需要完成的具体任务,即代理类调用任何方法都会经过这个调用处理器类
     */
    static class ProxyHandler implements InvocationHandler {
        private Subject subject;

        public ProxyHandler(Subject subject) {
            this.subject = subject;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable {
            String methodName = method.getName();
            if ("request".equals(methodName)) {
                Log.e(TAG, "====request before==== args: " + (args != null ?args.toString() : null));//定义预处理的工作，当然你也可以根据 method 的不同进行不同的预处理工作
                Object result = method.invoke(subject, args);
                Log.e(TAG, "====request after==== result: " + result );
                return result;
            } else if ("getName".equals(methodName)) {
                Log.e(TAG, "====getName before==== args: " + (args != null ?args.toString() : null));//定义预处理的工作，当然你也可以根据 method 的不同进行不同的预处理工作
                Object result = method.invoke(subject, args);
                Log.e(TAG, "====getName after==== result: "  + result);
                return result;
            }

            Log.e(TAG, "====other before====");//定义预处理的工作，当然你也可以根据 method 的不同进行不同的预处理工作
            Object result = method.invoke(subject, args);
            Log.e(TAG, "====other after====" );
            return result;
        }
    }
}