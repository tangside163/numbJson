package org.tangsi.util.json;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.tangsi.util.Mirror;

public class JsonRender {

	private StringBuilder stringBuilder;
	
	/**
	 * josn格式控制
	 */
	private JsonFormat jsonFormat;

	public JsonRender setStringBuilder(StringBuilder stringBuilder) {
		this.stringBuilder = stringBuilder;
		return this;
	}
	
	public String render(Object object) {
		if(null == object) {
			this.stringBuilder.append("null");
			return this.stringBuilder.toString();
		}
		Class clazz = object.getClass();
		Mirror mirror = new Mirror(clazz);
		
		if(mirror.isCharacterLike()) {  //如果是类字符串
			String content = "";
			if(CharSequence.class == clazz) content = ((CharSequence)object).toString();
			else if(String.class == clazz) content = ((String)object).toString();
			else if(StringBuilder.class == clazz) content = ((StringBuilder)object).toString();
			else if(StringBuffer.class == clazz) content = ((StringBuffer)object).toString();
			else if(Character.class == clazz) content = ((Character)object).toString();
			
			string2Json(content);
			
		}else if(Boolean.class == clazz) { //布尔类型
			this.stringBuilder.append(((Boolean)object).booleanValue());
		}else if(AtomicBoolean.class == clazz) {  //布尔类型
            this.stringBuilder.append(((AtomicBoolean) object).get());
        }else if(mirror.isNumberLike()) {  //类数字
			if(Byte.class == clazz) {
				this.stringBuilder.append(((Byte)object).byteValue());
			}else if(Short.class == clazz) {
				this.stringBuilder.append(((Short)object).shortValue());
			}else if(Integer.class == clazz) {
				this.stringBuilder.append(((Integer)object).intValue());
			}else if(Long.class == clazz) {
				this.stringBuilder.append(((Long)object).longValue());
			}else if(Double.class == clazz) {
				this.stringBuilder.append(((Double)object).doubleValue());
			}else if(Float.class == clazz) {
				this.stringBuilder.append(((Float)object).floatValue());
			}else if(Number.class == clazz) {
				this.stringBuilder.append(((Number)object).doubleValue());
			}else if(BigDecimal.class == clazz) {
                this.stringBuilder.append(((BigDecimal) object).doubleValue());
            }else if(BigInteger.class == clazz) {
                this.stringBuilder.append(((BigInteger) object).intValue());
            }else if(AtomicInteger.class == clazz) {
                this.stringBuilder.append(((AtomicInteger) object).intValue());
            }else if(AtomicLong.class == clazz) {
                this.stringBuilder.append(((AtomicLong) object).longValue());
            }
		}else if(Map.class.isAssignableFrom(clazz)) {  //map
			map2Json(object);
		}else if(Collection.class.isAssignableFrom(clazz)) { //Collection
			this.collection2Json(object);
		}else if(clazz.isArray()) {  //数组
			//先转成集合，复用代码
			List list = Arrays.asList((Object[])object);
			List newList = new ArrayList();
			for(Object obj : list) {
				if(null != obj) newList.add(obj);
			}
			this.collection2Json(list);
		}else {  //带有多个field的pojo
			Field[] fields = clazz.getDeclaredFields();
			Map map = new HashMap();
			if(fields.length > 0) {
				for(Field field : fields) {
					field.setAccessible(true);
					try {
						map.put(field.getName(), field.get(object));
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			this.map2Json(map);
		}
		return this.stringBuilder.toString();
	}

	private void map2Json(Object object) {
		this.stringBuilder.append("{\n");
		this.jsonFormat.increaseIndent();
		Set<Entry<Object,Object>> set = ((Map)object).entrySet();

		List<MapItem> list = new ArrayList<MapItem>();
		for(Entry<Object,Object> entry : set) {
			Object keyObj = entry.getKey();
			if(null == keyObj) continue;
			list.add(new MapItem(keyObj.toString(), entry.getValue()));
		}
		
		Iterator<MapItem> iterator = list.iterator();
		String indentOffset = this.getCurrentIndentOffset();
		while(iterator.hasNext()) {
			this.appendMapItem(iterator.next(),iterator.hasNext(), indentOffset);
		}
		this.jsonFormat.decreaseIndent();
		this.stringBuilder.append(this.getCurrentIndentOffset()).append("}");
	}

	private void collection2Json(Object object) {
		this.stringBuilder.append("[\n");
		this.jsonFormat.increaseIndent();
		Iterator iterator = ((Collection)object).iterator();
		String indentOffset = this.getCurrentIndentOffset();
		while(iterator.hasNext()) {
			this.appendPojoItem(iterator.next(), iterator.hasNext(),  indentOffset);
		}

		this.jsonFormat.decreaseIndent();
		this.stringBuilder.append("]");
	}

	private void appendPojoItem(Object object, boolean hasNext,
			String indentOffset) {
		this.stringBuilder.append(indentOffset);
		this.render(object);
		if(hasNext) this.stringBuilder.append(",");
		this.stringBuilder.append("\n");
	}

	private void appendMapItem(MapItem item, boolean hasNext, String indentOffset) {
		
		this.stringBuilder.append(indentOffset);
		this.string2Json(item.getKey());
		this.stringBuilder.append(" : ");
		this.render(item.getValue());
		if(hasNext) this.stringBuilder.append(",");
		this.stringBuilder.append("\n");
	}

	private String getCurrentIndentOffset() {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<this.jsonFormat.getIndent(); i++) {
			sb.append(this.jsonFormat.getIndentChar());
		}
		return sb.toString();
	}

	private void string2Json(String content) {

		this.stringBuilder.append("\"").append(content).append("\"");
		
	}

	public JsonRender setJsonFormat(JsonFormat jsonFormat) {
		this.jsonFormat = jsonFormat;
		return this;
	}

}
