package gabywald.global.json;

/**
 * 
 * @author Gabriel Chandesris (2014)
 */
@SuppressWarnings("serial")
public class JSONException extends Exception {
	
	public JSONException(String request) 
		{ super(request); }

	public JSONException(Throwable cause) 
		{ super(cause); }

}
