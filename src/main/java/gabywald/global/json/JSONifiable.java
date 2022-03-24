package gabywald.global.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Gabriel Chandesris (2014)
 */
public abstract class JSONifiable {
	
	private Map<String, JSONValue> mapKeyValues;
	
	protected JSONifiable() {
		this.mapKeyValues = new HashMap<String, JSONValue>();
	}
	
	protected void put(String key, JSONValue value) 
		{ this.mapKeyValues.put(key, value); }
	
	protected Set<String> ketSet() {
		return this.mapKeyValues.keySet();
	}
	
	public JSONValue get(String key) {
		return this.mapKeyValues.get(key);
	}
	
	public JSONObject toJSON() {
		this.setKeyValues();
		JSONObject localJSONobject	= new JSONObject();
		Iterator<String> iterator	= this.mapKeyValues.keySet().iterator();
		while (iterator.hasNext()) {
			String key		= iterator.next();
			JSONValue val	= this.mapKeyValues.get(key);
			localJSONobject.put(key, val);
		} // END "while (iterator.hasNext())"
		return localJSONobject;
	}
	
	public String toRecord() {
		String toReturn = new String( "" );
		toReturn += this.toJSON().toString();
		return toReturn;
	}
	
	protected abstract void setKeyValues();
	
	protected abstract <T extends JSONifiable> T reloadFrom(String json) throws JSONException;
	
	// protected abstract <T extends JSONifiable> T reload(String json) throws JSONException;
	
	/**
	 * 
	 * @param <T> This is the type parameter
	 * @param classe (Class&lt;T&lt;) Class definition which implements JSONifiable
	 * @param json (String)
	 * @return (&lt;T extends JSONifiable&gt;) could be null (malformed JSON, non-corresponding keys...). 
	 */
	public static <T extends JSONifiable> T reload(Class<T> classe, String json) {
		T toReturn = null;
		try {
			toReturn = classe.newInstance();
			toReturn.reloadFrom(json);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return toReturn;
	}
	
	public String JSONstringification() {
		this.setKeyValues();
		return this.toRecord();
	}
	
	public JSONObject JSONjsonification() {
		this.setKeyValues();
		return this.toJSON();
	}
	
	public static List<JSONValue> generateList(List<? extends JSONifiable> list) {
		List<JSONValue> jsonListToReturn = new ArrayList<JSONValue>();
		Iterator<? extends JSONifiable> iterator = list.iterator();
		while (iterator.hasNext()) 
			{ jsonListToReturn.add(iterator.next().toJSON()); }
		return jsonListToReturn;
	}
	
	public static JSONArray generateArray(List<? extends JSONifiable> list) {
		List<JSONValue> jsonListToReturn = new ArrayList<JSONValue>();
		Iterator<? extends JSONifiable> iterator = list.iterator();
		while (iterator.hasNext()) 
			{ jsonListToReturn.add(iterator.next().toJSON()); }
		return new JSONArray(jsonListToReturn);
	}
	
	public static JSONArray generateArray(Collection<? extends JSONifiable> list) {
		List<JSONValue> jsonListToReturn = new ArrayList<JSONValue>();
		Iterator<? extends JSONifiable> iterator = list.iterator();
		while (iterator.hasNext()) 
			{ jsonListToReturn.add(iterator.next().toJSON()); }
		return new JSONArray(jsonListToReturn);
	}
	
	public static JSONArray generateStrArray(List<String> listStrings) {
		List<JSONValue> jsonListToReturn = new ArrayList<JSONValue>();
		Iterator<String> iterator = listStrings.iterator();
		while (iterator.hasNext()) 
			{ jsonListToReturn.add( JSONValue.instanciate( iterator.next() ) ); }
		return new JSONArray(jsonListToReturn);
	}

}
