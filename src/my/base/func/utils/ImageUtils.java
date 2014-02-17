package my.base.func.utils;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;

public class ImageUtils {

	 /**  
	  * 从Assets中读取图片  
	  */  
	public static Bitmap getImageFromAssetsFile(Context context,String fileName){  
	      Bitmap image = null;  
	      AssetManager am = context.getResources().getAssets();  
		      try{  
		          InputStream is = am.open(fileName);  
		          image = BitmapFactory.decodeStream(is);  
		          is.close();  
			  }catch (IOException e){  
		          e.printStackTrace();  
		      }  
	      return image;  
	  	 }  
	
	/**
	 * 从assets 目录加载.9图片资源
	 * @param context
	 * @param fileName
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static NinePatchDrawable getNineImageFromAssetsFile(Context context,String fileName){  	
			NinePatchDrawable patchy = null;
			AssetManager am = context.getResources().getAssets();  
		      try{  
		          InputStream is = am.open(fileName);  
		          Bitmap bitmap = BitmapFactory.decodeStream(is); 
		          byte[] chunk = bitmap.getNinePatchChunk();
		          patchy = new NinePatchDrawable(bitmap, chunk,new Rect(), null);
		          is.close();  
			  }catch (IOException e){  
		          e.printStackTrace();  
		      }
            return patchy;
	}
	
	/**
	 * textView 带颜色 下滑线的文字
	 * @param str
	 * @param color
	 * @return
	 */
	public static SpannableString getTextView(String str,int color){
        SpannableString sp = new SpannableString(str); 
        //下划线
        sp.setSpan(new UnderlineSpan(), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
       
        //前景色
        sp.setSpan(new ForegroundColorSpan(color), 0 ,str.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
	}
	
	/** 
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
	 */  
	public static int dip2px(Context context, float dpValue) {  
	    final float scale = context.getResources().getDisplayMetrics().density;  
	    return (int) (dpValue * scale + 0.5f);  
	}  
	
	/** 
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
	 */  
	public static int px2dip(Context context, float pxValue) {  
	    final float scale = context.getResources().getDisplayMetrics().density;  
	    return (int) (pxValue / scale + 0.5f);  
	}
	
	/**
	 * 获取随机 用户名   多次获取会重复哦  可以用作测试
	 * @return
	 */
	public static String getRandomAccount(){
		return "wd"+String.valueOf((int)(Math.random()*9))+String.valueOf((int)(Math.random()*9))+String.valueOf((int)(Math.random()*9))+String.valueOf((int)(Math.random()*9))
				+String.valueOf((int)(Math.random()*9))+String.valueOf((int)(Math.random()*9))+String.valueOf((int)(Math.random()*9))+String.valueOf((int)(Math.random()*9));
	}
	
	
	public static void setSharePreferences(Context context,String key, String value){
		SharedPreferences settings = context.getSharedPreferences("weedongsdk.xml", 0);    
		SharedPreferences.Editor editor = settings.edit(); 
		editor.putString(key, value);
		editor.commit();
//		Log.i("download", "保存key----value:"+key+" "+value);
	}
	
	public static String getStringKeyForValue(Context context,String key){
		SharedPreferences mSettings = context.getSharedPreferences("weedongsdk.xml", 0);
		String value = mSettings.getString(key, "");
//		Log.i("download", "获取key----value:"+key+" "+value);
		return value;
	}
	
	public static void setSharePreferences(Context context,String key, boolean value){
		SharedPreferences settings = context.getSharedPreferences("weedongsdk.xml", 0);    
		SharedPreferences.Editor editor = settings.edit(); 
		editor.putBoolean(key, value);
		editor.commit();
//		Log.i("download", "保存key----value:"+key+" "+value);
	}
	
	public static Boolean getStringKeyForBoolValue(Context context,String key){
		SharedPreferences mSettings = context.getSharedPreferences("weedongsdk.xml", 0);
		Boolean value = mSettings.getBoolean(key, true);
		return value;
	}
	
	public static float getWidth(Context context,float width){
		if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){   
//		    Log.i("info", "横屏。。。。。"); // 横屏  
		    return 450 + width;
		}else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {   
//		    Log.i("info", "竖屏。。。。。"); // 竖屏  
		    return 290 + width;
		}else{
			return 290 + width;
		}
	}
	
	/**
     * 得到application节点中的META_DATA的值
     */
    public static String getMetaData(Context context,String key){
		try {
			ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);
			return appInfo.metaData.get(key).toString();
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
}