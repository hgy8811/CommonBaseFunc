package my.base.func.utils;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Proxy;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import my.base.func.R;

import org.apache.http.conn.util.InetAddressUtils;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;
/**
 * ������ һЩ ���ܺ��� ����
 * @author ������
 *
 */
public class CommonFunctionUtils {

	private static final String TAG = "CommonFunctionUtils";
	/**
	 * ��ȡӦ�ð汾��  versionCode
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context){  
		int versionCode = 0;  
		PackageManager packageManager = context.getPackageManager();  
		try {  
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);  
			versionCode = packageInfo.versionCode;  
		} catch (NameNotFoundException e) {  
			e.printStackTrace();  
		}  	      
		return versionCode;  
	}

	/**
	 * ��ȡӦ��versionName
	 * @param context
	 * @return
	 */
	public static String getVersion(Context context){
		String retVal = "$Revision: df3d962eabe7 $";
		PackageManager pm = context.getPackageManager();
		try{
			PackageInfo pi = pm.getPackageInfo( context.getPackageName(), 0 );
			retVal = pi.versionName;
		}catch( NameNotFoundException ex ){
			Log.e(TAG, "Can not find this application, really strange.", ex );
		}
		return retVal;
	}

	/**
	 * ��ȡ Ӧ����
	 * @param context
	 * @return
	 */
	public static String getAppName(Context context){
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packageInfo;
		String appName = "";
		try {
			packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			appName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

		return appName;	
	}

	/**
	 * ��ȡ Ӧ��icon
	 * @param context
	 * @return
	 */
	public static Drawable getAppIcon(Context context){
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packageInfo;
		Drawable appIcon = null;
		try {
			packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);

			appIcon = packageInfo.applicationInfo.loadIcon(context.getPackageManager());
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return appIcon;	
	}

	/**
	 * ��ȡ��ǰʹ������ �Ƿ����
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkAvailable(Context context){  
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);    
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();    
		if (networkinfo == null || !networkinfo.isAvailable()) {// ��ǰ���粻����
			return false;
		} 
		return true;
	}


	public static boolean checkNetWork(Context context){
		if(isNetWorkAvailable(context)){
			return true;
		}
		return false;
	}
	/**
	 * ��ȡip��ַ int ����
	 * @param context
	 * @return 
	 */
	public static long getIPAdress(Context context){
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);  
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();  
		long ipAddress = wifiInfo.getIpAddress();  
		if(ipAddress == 0)
			ipAddress = 1001;
		return ipAddress;
	}
	/**
	 * ��ȡip string����
	 * @param context
	 * @return
	 */
	public static String getStringIPAdress(Context context){
		long ip = getIPAdress(context);
		return ( (ip& 0xFF) + "." +((ip >>8)& 0xFF)+"."+((ip>>16)&0xFF)+"."+((ip >> 24 )&0xFF)); 
	}

	// ��ȡ����IP����
	public static String getLocalIpAddress() {
		try {
			String ipv4;
			ArrayList<NetworkInterface> mylist = Collections
					.list(NetworkInterface.getNetworkInterfaces());

			for (NetworkInterface ni : mylist) {

				ArrayList<InetAddress> ialist = Collections.list(ni
						.getInetAddresses());
				for (InetAddress address : ialist) {
					if (!address.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(ipv4 = address
							.getHostAddress())) {
						return ipv4;
					}
				}
			}
		} catch (SocketException ex) {

		}
		return null;
	}

	public static long ip2int(String ip) {
		String[] items = ip.split("\\.");
		return Long.valueOf(items[0]) << 24
				| Long.valueOf(items[1]) << 16
				| Long.valueOf(items[2]) << 8 | Long.valueOf(items[3]);
	}
	/**
	 * ��װ apk�ļ�
	 * @param file
	 * @param context
	 */
	public static void install( File file,Context context){
		Intent intent = new Intent( Intent.ACTION_VIEW );
		intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
		Uri uri = Uri.fromFile( file );
		intent.setDataAndType( uri, "application/vnd.android.package-archive" );
		context.startActivity( intent );
	}
	
	/**
	 * ��ȡ��Ļ�� ��� 
	 * @param context
	 * @return  height*width
	 */
	public static String getPhoneDisplay(Context context)
	{
		try
		{
			DisplayMetrics localDisplayMetrics = new DisplayMetrics();
			WindowManager localWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

			localWindowManager.getDefaultDisplay().getMetrics(localDisplayMetrics);

			int i = localDisplayMetrics.widthPixels;
			int j = localDisplayMetrics.heightPixels;

			return String.valueOf(j) + "*" + String.valueOf(i);
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}return "Unknown";
	}

	/**
	 * ��ȡ  meta-data from AndroidManifest.xml
	 * @param context
	 * @param name
	 * @return
	 */
	public static String getMetaData(Context context,String name){

		if(TextUtils.isEmpty(name)){
			Log.i(TAG, "getMetaData name is empty");
			return null;
		}

		try {
			PackageManager packageManager = context.getPackageManager();
			ApplicationInfo localApplicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 128);

			if (localApplicationInfo != null) {
				String str = localApplicationInfo.metaData.getString(name);
				if (str != null) {
					return str.trim();
				}
				Log.i(TAG, "Could not read  meta-data from AndroidManifest.xml.");
			}

		}
		catch (Exception localException)
		{
			Log.i(TAG, "Could not read  meta-data from AndroidManifest.xml.", localException);
		}
		return null;
	}
	/**
	 * �ر� dialog
	 * @param dialog
	 */
	public static void cancelDialog(Dialog dialog){
		if(dialog != null){
			dialog.dismiss();
		}
		dialog = null;
	}

	/** �ж��Ƿ�ģ�������������TRUE����ǰ��ģ���� 
	 * @param context 
	 * @return 
	 */  
	public static boolean isEmulator(Context context){  
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);  
		String imei = tm.getDeviceId();  
		if (imei == null || imei.equals("000000000000000")){  
			return true;  
		}  
		return false;  
	}  

	/**
	 * ɾ��ָ��Ŀ¼��Ŀ¼�µ������ļ� 
	 * 
	 * @param path
	 */
	public static void deleteDir(String path){
		File file = new File(path);//�����ļ�·��
		if(file.exists() && file.isDirectory()){ //�ж����ļ� ����Ŀ¼
			if(file.listFiles().length == 0){ //Ŀ¼��û���ļ� ֱ��ɾ��
				//Log.i(TAG,"deleteDir:" + file.getName());
				file.delete();
			}else{//��������ļ��Ž����飬���ж��Ƿ����¼�Ŀ¼  
				File delFile[]=file.listFiles();  
				int i =file.listFiles().length;  
				for(int j=0;j<i;j++){  
					if(delFile[j].isDirectory()){  
						deleteDir(delFile[j].getAbsolutePath());//�ݹ����del������ȡ����Ŀ¼·��  
					}  
					delFile[j].delete();//ɾ���ļ�  
				}  
				file.delete();
			}
		}
	}

	/**
	 * ɾ��ָ��Ŀ¼�µ������ļ�  ��ɾ��ָ��Ŀ¼
	 * @param path
	 */
	public static void deleteSubFile(String path){
		File file = new File(path);
		if(file.exists()){
			if(file.isDirectory()){
				File[] files = file.listFiles();
				for (File subFile : files)
				{
					if (subFile.isDirectory())
						deleteDir(subFile.getAbsolutePath());
					else
						subFile.delete();
				}
			}
		}
	}

	/**
	 * �˳�Ӧ��
	 * @param context
	 */
	@SuppressWarnings("deprecation")
	public static void exitApp(Context context){
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
			Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.addCategory(Intent.CATEGORY_HOME);
			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(startMain);
			System.exit(0);
		} else {// android2.1
			ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			am.restartPackage(context.getPackageName());
		}
	}

	/**
	 * �������绷�� ����HttpURLConnection
	 * @param context  
	 * @param url  URL
	 * @return
	 */
	public static HttpURLConnection createConnection(Context context, URL url){
		HttpURLConnection connection = null;
		try
		{
			ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if ((wifiInfo.isAvailable()) && (wifiInfo.isConnected()))
			{
				connection = (HttpURLConnection)url.openConnection();
				return connection;
			}
			NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if ((mobileInfo != null) && (mobileInfo.isAvailable()) && (mobileInfo.isConnected()))
			{
				Log.d("net", "connect by mobile");
				String str = android.net.Proxy.getDefaultHost();
				if (str != null)
				{
					java.net.Proxy proxy = new java.net.Proxy(Proxy.Type.HTTP, new InetSocketAddress(android.net.Proxy.getDefaultHost(), android.net.Proxy.getDefaultPort()));
					connection = (HttpURLConnection)url.openConnection(proxy);
					return connection;
				}
				return (HttpURLConnection)url.openConnection();
			}
			Log.d("net", "connect by other");
			return (HttpURLConnection)url.openConnection();
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
			Log.d("net", "connect errpr");
			connection = null;
		}
		return connection;
	}
}
