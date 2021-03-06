package fw.jbiz.ext.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import fw.jbiz.ZObject;
import fw.jbiz.common.ZException;
import fw.jbiz.logic.interfaces.IResponseObject;

public class ZSimpleJsonObject extends ZObject  implements IResponseObject {

	static Logger logger = Logger.getLogger(ZSimpleJsonObject.class);
	
	private List<String> keys;
	private List<Object> values;

	/*
	 * ZGsonObject
	 * recommended.
	 * @Deprecated from 201804
	 */
	@Deprecated
	public ZSimpleJsonObject() {

		keys = new ArrayList<String>();
		values = new ArrayList<Object>();
	}
	
	@Override
	public IResponseObject add(String prop, Object value) {
		keys.add(prop);
		values.add(value);
		return this;
	}
	

	@Override
	public Object get(String prop) {
		for(int i=0; i< keys.size(); i++) {
			if (keys.get(i).equals(prop)) {
				return values.get(i);
			}
		}
		
		return null;
	}
	
	public void clear() {
		keys.clear();
		values.clear();
	}

	public String toString() {

		StringBuffer json = new StringBuffer();
		json.append("{");
		for (int size = keys.size(), i = 0; i < size; i++) {
			json.append("\"" + keys.get(i) + "\"");
			json.append(":");
			json.append(objectToJSON(values.get(i)) + ",");
		}

		if (json.length() > 1) {
			json.deleteCharAt(json.length() - 1);
		}
		json.append("}");
		return json.toString();
	}
	

	public static String objectToJSON(Object object) {
		
		if (object == null) {
			return "null";
		} else if (object instanceof String) {
			return "\"" + object.toString() + "\"";
		} else if (object instanceof Integer) {
			return String.valueOf(object);
		} else if (object instanceof Long) {
			return String.valueOf(object);
		} else if (object instanceof Float) {
			return String.valueOf(object);
		} else if (object instanceof Double) {
			return String.valueOf(object);
		} else if (object instanceof Boolean) {
			return ((Boolean) (object)) ? "1" : "0";
		} else if (object instanceof ZSimpleJsonObject) {
			return object.toString();
		} else if (object instanceof ZSimpleJsonArray) {
			return object.toString();
		} else {
			return "\"" + object.toString() + "\"";
		}
	}

	public static ZSimpleJsonObject parseJSON(String string) {
		ZSimpleJsonObject json = new ZSimpleJsonObject();
		string = string.trim();
		try {
			Map<Integer, ZSimpleJsonObject> map = new HashMap<Integer, ZSimpleJsonObject>();
			int len = string.length();
			int dep = 0;
			for (int i = 0; i < len; i++) {
				Character c = string.charAt(i);
				switch (c) {
				case ':':
					String key = new String();
					String value = new String();
					int s = -1;
					int e = -1;
					boolean fs = false;
					boolean fe = false;
					for (int j = i; j >= 0; j--) {
						char v = string.charAt(j);
						switch (v) {
						case '{':
						case ',':
							s = j + 1;
							fs = true;
							break;
						}
						if (fs) {
							break;
						}
					}
					for (int j = i; j < len; j++) {
						char v = string.charAt(j);
						switch (v) {
						case '}':
						case ',':
							e = j;
							fe = true;
							break;
						}
						if (fe) {
							break;
						}
					}

					if (s == -1 || e == -1) {
						throw new ZJsonException();
					} else {
						key = string.substring(s, i);
						value = string.substring(i + 1, e);
					}
					System.out.println(key + "=" + value);
					System.out.println("sss");
					break;
				case '{':
					++dep;
					ZSimpleJsonObject js = map.get(dep);
					if (js == null) {
						js = new ZSimpleJsonObject();
						map.put(dep, js);
					}
					break;
				case '}':
					--dep;
					break;
				case '[':
					break;
				case ']':
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@Override
	public void ending() {
		new ZException("not implemented.");
		
	}



}
