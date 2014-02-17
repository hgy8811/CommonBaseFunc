package my.base.func.utils;

import android.os.Build;

/**
 * 获取SDK 版本的相关信息
 * @author huguoyin
 * @version 创建时间：2013-9-11  下午2:15:07
 *
 */
public class AndroidVersionUtils {

	public AndroidVersionUtils() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * android2.2 或更新
	 * @return
	 */
	public static boolean hasFroyo() {
		// Can use static final constants like FROYO, declared in later versions
		// of the OS since they are inlined at compile time. This is guaranteed behavior.
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	/**
	 * android2.3 或更新
	 * @return
	 */
	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	/**
	 * android3.0 或更新
	 * @return
	 */
	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	/**
	 * android3.1 或更新
	 * @return
	 */
	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	/**
	 * android4.1 或更新
	 * @return
	 */
	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}
}
