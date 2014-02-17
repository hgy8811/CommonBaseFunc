package my.base.func.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

/**
 * 获取 手机基本配置信息 硬件 系统信息
 * @author 胡国银
 *
 */
public class PhoneBaseInfoHelper {

	private static final String TAG = PhoneBaseInfoHelper.class.getSimpleName();

	/**
	 * sdCard大小  
	 * @return 单位：字节
	 */
	public static long[] getSDCardMemory() { 
		long[] sdCardInfo=new long[2]; 
		String state = Environment.getExternalStorageState(); 
		if (Environment.MEDIA_MOUNTED.equals(state)) { 
			File sdcardDir = Environment.getExternalStorageDirectory(); 
			StatFs sf = new StatFs(sdcardDir.getPath()); 
			long bSize = sf.getBlockSize(); 
			long bCount = sf.getBlockCount(); 
			long availBlocks = sf.getAvailableBlocks(); 

			sdCardInfo[0] = bSize * bCount;//总大小 
			sdCardInfo[1] = bSize * availBlocks;//可用大小 
		} 
		return sdCardInfo; 
	} 

	/**
	 * 这个是手机内部存储的总空间大小  不是内存
	 * @return bytes
	 */
	public static long getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}

	/**
	 * 获取系统内存大小
	 * @param context
	 * @return MB
	 */
	public static String getTotalMemory(Context context) {  
		String str1 = "/proc/meminfo";// 系统内存信息文件  
		String str2;  
		String[] arrayOfString;  
		long initial_memory = 0;  

		try {  
			FileReader localFileReader = new FileReader(str1);  
			BufferedReader localBufferedReader = new BufferedReader(  
					localFileReader, 8192);  
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小  

			arrayOfString = str2.split("\\s+");  
			//			for (String num : arrayOfString) {  
			//				Log.i(str2, num + "\t");  
			//			}  

			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() <<10;// 获得系统总内存，单位是KB，乘以1024转换为Byte  
			localBufferedReader.close();  

		} catch (IOException e) {  
			
		}  
		return Formatter.formatFileSize(context, initial_memory).trim();// Byte转换为KB或者MB，内存大小规格化  
	} 
	/**
	 * CPU信息
	 * @return
	 */
	public static String[] getCpuInfo() {
		String str1 = "/proc/cpuinfo";
		String str2="";
		String[] cpuInfo={"",""};
		String[] arrayOfString;
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			str2 = localBufferedReader.readLine();
			Log.i("cpu1",str2);
			arrayOfString = str2.split("\\s+");
			for (int i = 2; i < arrayOfString.length; i++) {
				cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";  //CPU的型号
			}
			str2 = localBufferedReader.readLine();
			Log.i("cpu2",str2);
			arrayOfString = str2.split("\\s+");                  //CPU的频率  //非也！！
			cpuInfo[1] += arrayOfString[2];
			localBufferedReader.close();
		} catch (IOException e) {
		}
		return cpuInfo;
	}

	/**
	 * 获取cpu 型号
	 * @return ex: ARMv7 Processor rev 2 (v7l)
	 */
	public static String getCpuModel(){
		String str = "";

		BufferedReader bufferedReader = null;

		try {
			FileReader fr = new FileReader("/proc/cpuinfo");
			if(fr != null){
				bufferedReader = new BufferedReader(fr, 1024);
				try {
					str = bufferedReader.readLine();
					bufferedReader.close();
					fr.close();
				} catch (IOException e) {
					Log.i(TAG, "Could not read from file /proc/cpuinfo", e);
				}
			}
		} catch (FileNotFoundException e) {
			Log.i(TAG, "Could not open file /proc/cpuinfo", e);
		}
		Log.i(TAG, "getCpu str: " + str);
		if(str != null){
			int i = str.indexOf(':') + 1;
			str = str.substring(i);
		}

		return str.trim();
	}
	/**
	 * 系统的版本信息
	 * @return
	 */
	public static String[] getSysVersion(Context context){  
		String[] version={"null","null","null","null","null"};  
		String str1 = "/proc/version";  
		String str2;  
		String[] arrayOfString;  
		try {  
			FileReader localFileReader = new FileReader(str1);  
			BufferedReader localBufferedReader = new BufferedReader(  
					localFileReader, 8192);  
			str2 = localBufferedReader.readLine();  
			arrayOfString = str2.split("\\s+");  //防止 //s+
			version[0]=arrayOfString[2];//KernelVersion  
			localBufferedReader.close();  
		} catch (IOException e) {  
		}  
		version[1] = Build.VERSION.RELEASE; // firmware version  设备的系统版本   如2.2
		version[2]=Build.MODEL;            //model  设备型号
		version[3]=Build.DISPLAY;          //system version 版本号   定制版本不一样
		version[4] = Build.VERSION.SDK_INT +""; // 设备SDK版本    8

		return version;  
	}

	/**
	 * 获取当前使用网络 
	 * @param context
	 * @return 2g,3g,wifi,unknow,null
	 */
	public static String getNetWorkType(Context context){  
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);    
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();    
		if (networkinfo == null || !networkinfo.isAvailable()) {// 当前网络不可用
			return null;
		} else {  // 当前网络可用，可执行交互操作 
			int type = networkinfo.getType();
			int subType = networkinfo.getSubtype();

			if(type == ConnectivityManager.TYPE_WIFI){ //wifi网络
				return "wifi";
			}else if(type == ConnectivityManager.TYPE_MOBILE){ //手机网络
				switch (subType) {
				case TelephonyManager.NETWORK_TYPE_EVDO_0:  //3g
				case TelephonyManager.NETWORK_TYPE_EVDO_A:  //电信3G
				case TelephonyManager.NETWORK_TYPE_HSDPA:  //联通3g
				case TelephonyManager.NETWORK_TYPE_HSPA:  //3g
				case TelephonyManager.NETWORK_TYPE_HSUPA:  //3g
				case TelephonyManager.NETWORK_TYPE_UMTS:  //联通3g
					// NOT AVAILABLE YET IN API LEVEL 7  
				case TelephonyManager.NETWORK_TYPE_EHRPD:  
				case TelephonyManager.NETWORK_TYPE_EVDO_B:  
				case TelephonyManager.NETWORK_TYPE_HSPAP:  
				case TelephonyManager.NETWORK_TYPE_LTE:  
					return "3g";
				case TelephonyManager.NETWORK_TYPE_1xRTT:  //2g
				case TelephonyManager.NETWORK_TYPE_CDMA:  //电信2G
				case TelephonyManager.NETWORK_TYPE_EDGE:  //移动2G
				case TelephonyManager.NETWORK_TYPE_GPRS:  //联通2G
				case TelephonyManager.NETWORK_TYPE_IDEN:  //2g
					return "2g";
				default:
					return "unknow";
				}
			}else{
				return "unknow";
			}
		}

	}

	public static boolean isUploadFile(Context context){
		String netType = getNetWorkType(context);
		
		if(TextUtils.isEmpty(netType)){
			return false;
		}
		
		if(netType.equals("wifi") || netType.equals("3g")){
			return true;
		}
		
		return false;
	}
	/**
	 * 获取cpu核数
	 * @return
	 */
	public static int getNumCores() {

		//Private Class to display only CPU devices in the directory listing
		class CpuFilter implements FileFilter {
			@Override
			public boolean accept(File pathname) {
				//Check if filename is "cpu", followed by a single digit number
				if(Pattern.matches("cpu[0-9]", pathname.getName())) {
					return true;
				}
				return false;
			}      
		}

		try {
			//Get directory containing CPU info
			File dir = new File("/sys/devices/system/cpu/");
			//Filter to only list the devices we care about
			File[] files = dir.listFiles(new CpuFilter());
			//Return the number of cores (virtual CPU devices)
			return files.length;
		} catch(Exception e) {
			//Default to return 1 core
			return 1;
		}
	}

	/**
	 * cpu 最大频率 KHz
	 * @return
	 */
	public static String getMaxCpuFreq() {
		String result = "";
		ProcessBuilder cmd;
		try {
			String[] args = { "/system/bin/cat",
			"/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[24];
			while (in.read(re) != -1) {
				result = result + new String(re);
			}
			in.close();
			if(!TextUtils.isEmpty(result)){
				double r = Double.parseDouble(result);
				DecimalFormat df = new DecimalFormat("##0.00");  
				result = df.format(r/1000000) + "GHz";
			}else{
				result = "N/A";
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			result = "N/A";
		}
		return result.trim();
	}
	/**
	 * cpu 最小频率  KHz
	 * @return
	 */
	public static String getMinCpuFreq() {
		String result = "";
		ProcessBuilder cmd;
		try {
			String[] args = { "/system/bin/cat",
			"/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[24];
			while (in.read(re) != -1) {
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			result = "N/A";
		}
		return result.trim();
	}

	/**
	 * 当前频率 KHz
	 * @return
	 */
	public static String getCurCpuFreq() {
		String result = "N/A";
		try {
			FileReader fr = new FileReader(
					"/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
			BufferedReader br = new BufferedReader(fr);
			String text = br.readLine();
			result = text.trim();
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.trim();
	}
	/**
	 * 获取手机imei  并不严谨
	 * @param context
	 * @return
	 * Returns the unique device ID, for example, 
	 * the IMEI for GSM and the MEID or ESN for CDMA phones. 
	 * Return null if device ID is not available. 
	 */
	public static String getIMEI(Context context){
		TelephonyManager telephonyManager=(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId=telephonyManager.getDeviceId(); 
		if(TextUtils.isEmpty(deviceId))
			deviceId="0";
		return deviceId;
	}

	
	/**
	 * 获取uuid  android id + device id
	 * @param context
	 * @return
	 */
	public static String getUDID(Context context){
		TelephonyManager telephonyManager=(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String uuid = android.provider.Settings.System.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID)+"-"+telephonyManager.getDeviceId();
		return uuid;
	}

	/***
	 * 获取 设备序列号  硬件标识 若有则唯一
	 * 但并不是每个设备都会获取到
	 * @return
	 */
	public static final String getDeviceSerial()
	{
		String serial;

		if(android.os.Build.VERSION.SDK_INT >= 9)
		{
			serial = Build.SERIAL;
			if(serial == null)
				serial = "";
			return serial;
		}

		try
		{
			Method method = Class.forName("android.os.Build").getDeclaredMethod("getString", new Class[] {
					Class.forName("java.lang.String")
			});
			if(!method.isAccessible())
				method.setAccessible(true);
			serial = (String)method.invoke(new Build(), new Object[] {
				"ro.serialno"
			});
		}
		catch(ClassNotFoundException classnotfoundexception)
		{
			classnotfoundexception.printStackTrace();
			return "";
		}
		catch(NoSuchMethodException nosuchmethodexception)
		{
			nosuchmethodexception.printStackTrace();
			return "";
		}
		catch(InvocationTargetException invocationtargetexception)
		{
			invocationtargetexception.printStackTrace();
			return "";
		}
		catch(IllegalAccessException illegalaccessexception)
		{
			illegalaccessexception.printStackTrace();
			return "";
		}
		return serial;
	}

	/**
	 * 反编译 友盟    //手机的唯一标示
	 * @param context
	 * @return  //机制处理：imei->macAddress->android_id 
	 */
	public static String getPhoneId(Context context){
		String id = "";
		TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

		if(telephonyManager  == null){
			Log.e(TAG,"getPhoneId No imei");
		}

		if(isHasPermission(context,"android.permission.READ_PHONE_STATE")){
			id = telephonyManager.getDeviceId();
		}

		if(TextUtils.isEmpty(id)){
			Log.e(TAG,"getPhoneId No imei");
			id = getMacAddress(context);
			if(TextUtils.isEmpty(id)){
				Log.e(TAG, "getPhoneId  Failed to take mac as IMEI. Try to use Secure.ANDROID_ID instead.");
				id = android.provider.Settings.Secure.getString(context.getContentResolver(), "android_id");
				Log.i(TAG, "getDeviceId: Secure.ANDROID_ID: " + id);
				return id;
			}
		}
		return id;
	}

	/**
	 * 检查 当前包是否具有  指定的权限
	 * @param context
	 * @param permissionName  所检查权限的名字
	 * @return  是否具有此权限
	 */
	public static boolean isHasPermission(Context context,String permissionName){
		PackageManager localPackageManager = context.getPackageManager();
		if (localPackageManager.checkPermission(permissionName, context.getPackageName()) != PackageManager.PERMISSION_GRANTED ) {
			return false;
		}
		return true;
	}

	/**
	 *  获取mac地址
	 * @param context
	 * @return
	 */
	public static String getMacAddress(Context context){
		try
		{
			WifiManager localWifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
			if (isHasPermission(context, "android.permission.ACCESS_WIFI_STATE")) {
				WifiInfo localWifiInfo = localWifiManager.getConnectionInfo();
				return localWifiInfo.getMacAddress();
			}
			Log.e(TAG , "getMacAddress: Could not get mac address.[no permission android.permission.ACCESS_WIFI_STATE");
		}
		catch (Exception localException) {
			Log.e(TAG, "getMacAddress: Could not get mac address." + localException.toString());
		}
		return "";
	}

	/**
	 * 定位 获取当前所在位置信息
	 * @param context
	 * @return location
	 */
	public static Location getLocation(Context context){
		LocationManager locationManager = null;
		try {
			locationManager = (LocationManager)context.getSystemService("location");
			Location location;
			if (isHasPermission(context, "android.permission.ACCESS_FINE_LOCATION"))
			{
				location = locationManager.getLastKnownLocation("gps");

				if (location != null) {
					Log.i(TAG, "get location from gps:" + location.getLatitude() + "," + location.getLongitude());

					return location;
				}
			}
			if (isHasPermission(context, "android.permission.ACCESS_COARSE_LOCATION"))
			{
				location = locationManager.getLastKnownLocation("network");

				if (location != null) {
					Log.i(TAG, "get location from network:" + location.getLatitude() + "," + location.getLongitude());
					return location;
				}
			}

			Log.i(TAG, "Could not get location from GPS or Cell-id, lack ACCESS_COARSE_LOCATION or ACCESS_COARSE_LOCATION permission?");

			return null;
		} catch (Exception localException) {
			Log.i(TAG, localException.getMessage());
		}
		return null;
	}

	/**
	 * 是否 网络已连接或正在连接
	 * @param context
	 * @return
	 */
	public static boolean isConnectNetwork(Context context){
		try
		{
			ConnectivityManager localConnectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo Info = localConnectivityManager.getActiveNetworkInfo();
			if (Info != null) {
				return Info.isConnectedOrConnecting();
			}
			return false; } catch (Exception localException) {
			}
		return true;
	}

	/**
	 * 是否网络已连接
	 * @param context
	 * @return
	 */
	public static boolean isConnectedNetwork(Context context){
		try
		{
			ConnectivityManager localConnectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo Info = localConnectivityManager.getActiveNetworkInfo();
			if (Info != null) {
				return Info.isConnected();
			}
			return false; 
		} catch (Exception localException) {
		}
		return false;
	}
	
	
}
