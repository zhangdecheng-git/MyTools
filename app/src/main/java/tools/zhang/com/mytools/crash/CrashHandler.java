package tools.zhang.com.mytools.crash;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

public class CrashHandler implements UncaughtExceptionHandler {

	private static CrashHandler instance;

	public static CrashHandler getInstance() {
		if (instance == null) {
			instance = new CrashHandler();
		}
		return instance;
	}

	public void init(Context ctx) {
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 核心方法，当程序crash 会回调此方法， Throwable中存放这错误日志
	 */
	@Override
	public void uncaughtException(Thread arg0, Throwable arg1) {
		saveCrash(arg1);
		arg1.printStackTrace();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public void saveCrash(Throwable arg1) {
		String logPath;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			logPath = Environment.getExternalStorageDirectory().getAbsolutePath()
					+ File.separator
					+ "log";

			File file = new File(logPath);
			if (!file.exists()) {
				file.mkdirs();
			}
			try {
				FileWriter fw = new FileWriter(logPath + File.separator
						+ "errorlog.log", true);
				fw.write(new Date() + "\n");
				// 错误信息
				// 这里还可以加上当前的系统版本，机型型号 等等信息
				StackTraceElement[] stackTrace = arg1.getStackTrace();
				fw.write(arg1.getMessage() + "\n");
				for (StackTraceElement element : stackTrace) {
					fw.write("file:" + element.getFileName() + " class:"
							+ element.getClassName() + " method:"
							+ element.getMethodName() + " line:"
							+ element.getLineNumber() + "\n");
				}
				fw.write("\n");
				fw.close();
				// 上传错误信息到服务器
				// uploadToServer();
			} catch (IOException e) {
				Log.e("crash handler", "load file failed...", e.getCause());
			}
		}
	}
}