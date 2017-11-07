package com.banger.videorecord.util;
import android.util.Log;

import com.banger.videorecord.http.bean.ParamName;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * JSON转换工具类 使用前调用getInstance方法
 * 
 * @author zhujm
 * 
 * @date 2016-4-11
 * @toJson 将对象转换成json字符串
 * @getValue 在json字符串中，根据key值找到value
 * @json2Map 将json格式转换成map对象
 * @json2Bean 将json转换成bean对象
 * @json2List 将json格式转换成List对象
 * @Obj2Map obj 转为 map
 * 
 */
public class GsonUtil {
	private static final String TAG = "GsonUtil";
	private Gson gson;

	private GsonUtil() {
		gson = new Gson();
	}

	//普通json转换
	private static class gsonUtilHolder {
		private static GsonUtil instance = new GsonUtil();
	}

	/**
	 * 使用方法前调用getInstance,获得gsonUtil实例
	 * 
	 * @return GsonUtil
	 */
	public static GsonUtil getInstance() {
		return gsonUtilHolder.instance;
	}

	/**
	 * 将对象转换成json字符串
	 * 
	 * @param obj bean
	 * @return string
	 */
	public String toJson(Object obj) {
		if (obj == null) {
			return "";
		}
		return gson.toJson(obj);
	}
	/**
	 * 将Map转换成json字符串
	 *
	 * @param map bean
	 * @return string
	 */
	public String mapToJson(Map map) {
		if (map == null) {
			return "";
		}
		return new JSONObject(map).toString();
	}
	/**
	 * 在json字符串中，根据key值找到value
	 * 
	 * @param data json
	 * @param key key
	 * @return string
	 */

	public String getValue(String data, String key) {
			try {
				JSONObject jsonObject = new JSONObject(data);
				return jsonObject.getString(key);
			} catch (JSONException e) {
				Log.e(TAG, "getValue: "+e.getMessage());
			}
		return null;
	}


	/**
	 * 将json格式转换成map对象
	 * 
	 * @param json json
	 * @return map
	 */
	public Map<String, Object> json2Map(String json) {
		try {
			Map<String, Object> objMap = null;
			if (gson != null) {
				Type type = new TypeToken<Map<String, Object>>() {
				}.getType();
				objMap = gson.fromJson(json, type);
			}
			if (objMap == null) {
				// objMap = new HashMap<String, Object>();
				return null;
			}
			return objMap;
		}catch (Exception e){
			Log.e(TAG, "json2Map: "+e.getMessage());
		}
		return null;
	}

	/**
	 * 将json转换成bean对象
	 * 
	 * @param <T> t
	 * @param json json
	 * @param clazz bean
	 * @return bean
	 */
	public <T> T json2Bean(String json, Class<T> clazz) {
		System.out.println("zzzz+json+"+json);
		try {
		T obj = null;
		if (gson != null) {
			obj = gson.fromJson(json, clazz);
		}
		return obj;
		}catch (Exception e){
			Log.e(TAG, "json2Bean: "+e.getMessage());
		}
		return null;
	}

	/**
	 * 将json格式转换成List对象
	 *
	 * @param type list
	 * @param json json
	 * @return list
	 */
	public <T> T json2List(String json, Type type) {
		try {
		if (gson != null) {
			return gson.fromJson(json, type);
			}
		}catch (Exception e){
			Log.e(TAG, "json2List: "+e.getMessage());
		}
		return null;
	}

	/**
	 * obj 转为 map
	 * 
	 * @param obj
	 *            需要转的对象
	 * @return map
	 */
	public Map<String, Object> Obj2Map(Object obj) {
		try {
			if (obj != null) {
				return json2Map(toJson(obj));
			}
		}catch (Exception e){
			Log.e(TAG, "Obj2Map: "+e.getMessage());
		}
		return null;
	}

	/**
	 *
	 * @param json 字符串
	 * @return 是否为正确的json格式
	 */
	public static boolean isGoodJson(String json) {
		try {
			new JsonParser().parse(json);
			return true;
		} catch (JsonParseException e) {
			return false;
		}
	}
	//Retrofit json转换
	/**
	 *
	 * @return
     */
	public static Gson newInstance() {
		GsonBuilder builder = new GsonBuilder();

		builder.setFieldNamingStrategy(new AnnotateNaming());

		return builder.create();
	}

	private static class AnnotateNaming implements FieldNamingStrategy {

		@Override
		public String translateName(Field field) {
			ParamName a = field.getAnnotation(ParamName.class);
			return a != null ? a.value() : FieldNamingPolicy.IDENTITY.translateName(field);
		}
	}
}
