package org.tangsi.util.json;

public class JsonUtil {

	public static String toJson(Object object) {
		if(null == object) return "null";
		return new JsonRender().setStringBuilder(new StringBuilder()).setJsonFormat(new JsonFormat()).render(object);
	}
	
}
