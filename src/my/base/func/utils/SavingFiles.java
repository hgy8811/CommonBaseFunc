package my.base.func.utils;
import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

public class SavingFiles {
	
	private static final String TAG = SavingFiles.class.getSimpleName();
	/**
	 * 是否存在SDCard
	 * @return
	 */
	public static boolean isSDCardExist(){
		String status = Environment.getExternalStorageState(); 
		if(status.equals(Environment.MEDIA_MOUNTED)){
			return true;
		}else{
			return false;
		}
	}
	
	/* Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
   
	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	/**
	 * 获取 可存取文件的的 跟目录
	 * @param context
	 * @return
	 */
	public static File getSaveFileRootDir(Context context){
		
		if(isExternalStorageWritable()){  //可读写
			return Environment.getExternalStorageDirectory();
		}
		
		if(context == null)
			return null;
		
		return context.getFilesDir();
	}
	
//	public static File getCrashExternalRoot(){
//		File retVal = SavingFiles.getSaveFileRootDir(WdBaseInfo.gContext);
//		if( retVal != null ){
//			retVal = new File( retVal, "com.weedong.mobile" );
//			if( ! retVal.exists() && ! retVal.mkdirs() ){
//				retVal = null;
//			}
//		}
//		Log.i(TAG,"getCrashExternalRoot: "+ retVal.toString());
//		return retVal;
//	}
//	
//	public static File getLogExternalRoot(){
//		File retVal = SavingFiles.getSaveFileRootDir(WdBaseInfo.gContext);
//		if( retVal != null ){
//			retVal = new File( retVal, "com.weedong.mobile" + File.separator + "log" );
//			if( ! retVal.exists() && ! retVal.mkdirs() ){
//				retVal = null;
//			}
//		}
//		Log.i(TAG,"getLogExternalRoot: "+ retVal.toString());
//		
//		return retVal;
//	}
	
	public boolean isExternalStorageEnough(long desSize){
	boolean retval = false;
	String state = Environment.getExternalStorageState();
	if (Environment.MEDIA_MOUNTED.equals(state)) {
		File root = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(root.getPath());
		long bSize = sf.getBlockSize();
		long bAvaliableCount = sf.getAvailableBlocks();
		retval = desSize < bAvaliableCount * bSize ? true : false;
	}
	return retval;
}

}
