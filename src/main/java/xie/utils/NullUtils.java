package xie.utils;

/**
 * 对Null的处理
 */
public class NullUtils {

	/**
	 * 如果对象为Null，则使用给定默认值
	 * 
	 * @param obj 判断对象
	 * @param defaultVal 默认值
	 * @return
	 */
	public static <T> T ifNull(T obj, T defaultVal) {
		if (obj == null) {
			return defaultVal;
		}
		return obj;
	}
}
