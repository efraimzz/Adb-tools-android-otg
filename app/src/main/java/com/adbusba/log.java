package com.adbusba;

import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

public class log extends Thread {
	private static final ThreadLocal PARAMETER_BUFFER = new ThreadLocal();
	private static final LinkedBlockingQueue QUEUE = new LinkedBlockingQueue();
	private static final SimpleDateFormat TIME_FORMAT1 = new SimpleDateFormat("HH:mm:ss.SSS");
	private static final SimpleDateFormat TIME_FORMAT2 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	String packagename="com.adbusba";

	/*static {
	 log log = new log();
	 log.setDaemon(true);
	 log.start();
	 }*/

	public static void a(char c) {
		z(String.valueOf(c));
	}

	public static void a(double d) {
		z(String.valueOf(d));
	}

	public static void a(float f) {
		z(String.valueOf(f));
	}

	public static void a(int i) {
		z(String.valueOf(i));
	}

	public static void a(long j) {
		z(String.valueOf(j));
	}

	public static void a(Object obj) {
		z(y(obj));
	}

	public static void a(boolean z) {
		z(String.valueOf(z));
	}

	public static void b() {
		ThreadLocal threadLocal = PARAMETER_BUFFER;
		z(((StringBuilder) threadLocal.get()).toString());
		threadLocal.remove();
	}

	public static void b1(char c) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter1: ");
		stringBuilder.append(c);
		x(stringBuilder.toString());
	}

	public static void b1(double d) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter1: ");
		stringBuilder.append(d);
		x(stringBuilder.toString());
	}

	public static void b1(float f) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter1: ");
		stringBuilder.append(f);
		x(stringBuilder.toString());
	}

	public static void b1(int i) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter1: ");
		stringBuilder.append(i);
		x(stringBuilder.toString());
	}

	public static void b1(long j) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter1: ");
		stringBuilder.append(j);
		x(stringBuilder.toString());
	}

	public static void b1(Object obj) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter1: ");
		stringBuilder.append(y(obj));
		x(stringBuilder.toString());
	}

	public static void b1(boolean z) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter1: ");
		stringBuilder.append(z);
		x(stringBuilder.toString());
	}

	public static void b2(char c) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter2: ");
		stringBuilder.append(c);
		x(stringBuilder.toString());
	}

	public static void b2(double d) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter2: ");
		stringBuilder.append(d);
		x(stringBuilder.toString());
	}

	public static void b2(float f) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter2: ");
		stringBuilder.append(f);
		x(stringBuilder.toString());
	}

	public static void b2(int i) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter2: ");
		stringBuilder.append(i);
		x(stringBuilder.toString());
	}

	public static void b2(long j) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter2: ");
		stringBuilder.append(j);
		x(stringBuilder.toString());
	}

	public static void b2(Object obj) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter2: ");
		stringBuilder.append(y(obj));
		x(stringBuilder.toString());
	}

	public static void b2(boolean z) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter2: ");
		stringBuilder.append(z);
		x(stringBuilder.toString());
	}

	public static void b3(char c) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter3: ");
		stringBuilder.append(c);
		x(stringBuilder.toString());
	}

	public static void b3(double d) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter3: ");
		stringBuilder.append(d);
		x(stringBuilder.toString());
	}

	public static void b3(float f) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter3: ");
		stringBuilder.append(f);
		x(stringBuilder.toString());
	}

	public static void b3(int i) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter3: ");
		stringBuilder.append(i);
		x(stringBuilder.toString());
	}

	public static void b3(long j) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter3: ");
		stringBuilder.append(j);
		x(stringBuilder.toString());
	}

	public static void b3(Object obj) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter3: ");
		stringBuilder.append(y(obj));
		x(stringBuilder.toString());
	}

	public static void b3(boolean z) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter3: ");
		stringBuilder.append(z);
		x(stringBuilder.toString());
	}

	public static void b4(char c) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter4: ");
		stringBuilder.append(c);
		x(stringBuilder.toString());
	}

	public static void b4(double d) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter4: ");
		stringBuilder.append(d);
		x(stringBuilder.toString());
	}

	public static void b4(float f) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter4: ");
		stringBuilder.append(f);
		x(stringBuilder.toString());
	}

	public static void b4(int i) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter4: ");
		stringBuilder.append(i);
		x(stringBuilder.toString());
	}

	public static void b4(long j) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter4: ");
		stringBuilder.append(j);
		x(stringBuilder.toString());
	}

	public static void b4(Object obj) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter4: ");
		stringBuilder.append(y(obj));
		x(stringBuilder.toString());
	}

	public static void b4(boolean z) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter4: ");
		stringBuilder.append(z);
		x(stringBuilder.toString());
	}

	public static void b5(char c) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter5: ");
		stringBuilder.append(c);
		x(stringBuilder.toString());
	}

	public static void b5(double d) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter5: ");
		stringBuilder.append(d);
		x(stringBuilder.toString());
	}

	public static void b5(float f) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter5: ");
		stringBuilder.append(f);
		x(stringBuilder.toString());
	}

	public static void b5(int i) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter5: ");
		stringBuilder.append(i);
		x(stringBuilder.toString());
	}

	public static void b5(long j) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter5: ");
		stringBuilder.append(j);
		x(stringBuilder.toString());
	}

	public static void b5(Object obj) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter5: ");
		stringBuilder.append(y(obj));
		x(stringBuilder.toString());
	}

	public static void b5(boolean z) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter5: ");
		stringBuilder.append(z);
		x(stringBuilder.toString());
	}

	public static void b6(char c) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter6: ");
		stringBuilder.append(c);
		x(stringBuilder.toString());
	}

	public static void b6(double d) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter6: ");
		stringBuilder.append(d);
		x(stringBuilder.toString());
	}

	public static void b6(float f) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter6: ");
		stringBuilder.append(f);
		x(stringBuilder.toString());
	}

	public static void b6(int i) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter6: ");
		stringBuilder.append(i);
		x(stringBuilder.toString());
	}

	public static void b6(long j) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter6: ");
		stringBuilder.append(j);
		x(stringBuilder.toString());
	}

	public static void b6(Object obj) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter6: ");
		stringBuilder.append(y(obj));
		x(stringBuilder.toString());
	}

	public static void b6(boolean z) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameter6: ");
		stringBuilder.append(z);
		x(stringBuilder.toString());
	}

	public static void printStackTrace() {
		z(Log.getStackTraceString(new Exception("InjectedLog.printStackTrace")));
	}

	private static void x(String str) {
		ThreadLocal threadLocal = PARAMETER_BUFFER;
		StringBuilder stringBuilder = (StringBuilder) threadLocal.get();
		if (stringBuilder == null) {
			stringBuilder = new StringBuilder();
			threadLocal.set(stringBuilder);
		}
		if (stringBuilder.length() > 0) {
			stringBuilder.append((char)10);
		}
		stringBuilder.append(str);
	}

	private static String y(Object obj) {
		if (obj == null) {
			return "null";
		}
		Class cls = obj.getClass();
		return cls.isArray() ? cls == byte[].class ? Arrays.toString((byte[]) obj) : cls == short[].class ? Arrays.toString((short[]) obj) : cls == int[].class ? Arrays.toString((int[]) obj) : cls == long[].class ? Arrays.toString((long[]) obj) : cls == char[].class ? Arrays.toString((char[]) obj) : cls == float[].class ? Arrays.toString((float[]) obj) : cls == double[].class ? Arrays.toString((double[]) obj) : cls == boolean[].class ? Arrays.toString((boolean[]) obj) : Arrays.deepToString((Object[]) obj) : obj.toString();
	}

	private static void z(String str) {
		String str2 = "[TIME] [CLASS]\n->[METHOD]([LOCATION])\n[RESULT]\n--------------------\n";
		CharSequence charSequence = "[TIME]";
		if (str2.contains(charSequence)) {
			str2 = str2.replace(charSequence, TIME_FORMAT1.format(Long.valueOf(System.currentTimeMillis())));
		}
		StackTraceElement stackTraceElement = new Throwable().getStackTrace()[2];
		CharSequence fileName = stackTraceElement.getFileName();
		if (fileName == null) {
			fileName = "Unknown Source";
		}
		int lineNumber = stackTraceElement.getLineNumber();
		if (lineNumber >= 0) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(fileName);
			stringBuilder.append(":");
			stringBuilder.append(lineNumber);
			fileName = stringBuilder.toString();
		}
		CharSequence charSequence2 = "[METHOD]";
		QUEUE.offer(str2.replace("[RESULT]", str).replace("[CLASS]", stackTraceElement.getClassName()).replace(charSequence2, stackTraceElement.getMethodName()).replace("[LOCATION]", fileName));
	}

	public void run() {
		File file;
		File parentFile;
		FileOutputStream fileOutputStream = null;
		Throwable th = null;
		try {
			file = new File("[SDCARD]/MT2/logs/[PACKAGE]-[TIME].log".replace("[SDCARD]", Environment.getExternalStorageDirectory().getPath()).replace("[PACKAGE]", packagename).replace("[TIME]", TIME_FORMAT2.format(Long.valueOf(System.currentTimeMillis()))).replace('\\', '/').replace("//", "/"));
			parentFile = file.getParentFile();
			if (parentFile != null) {
				parentFile.mkdirs();
			}
			fileOutputStream = new FileOutputStream(file, true);
		} catch (Throwable e) {
			e.printStackTrace();
			th = e;
		}
		if (fileOutputStream == null) {
			try {
				file = new File("/data/data/" + packagename + "/logs");
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(TIME_FORMAT2.format(Long.valueOf(System.currentTimeMillis())));
				stringBuilder.append(".log");
				parentFile = new File(file, stringBuilder.toString());
				file.mkdirs();
				fileOutputStream = new FileOutputStream(parentFile);
			} catch (IOException e2) {
				e2.printStackTrace();
				throw new RuntimeException(th);
			}
		}
		try {
			Charset defaultCharset = Charset.defaultCharset();
			while (true) {
				LinkedBlockingQueue linkedBlockingQueue = QUEUE;
				fileOutputStream.write(((String) linkedBlockingQueue.take()).getBytes(defaultCharset));
				if (linkedBlockingQueue.isEmpty()) {
					fileOutputStream.flush();
				}
			}
		} catch (Throwable e3) {
			throw new RuntimeException(e3);
		}
	}




}

