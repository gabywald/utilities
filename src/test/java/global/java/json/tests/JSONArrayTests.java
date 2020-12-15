package global.java.json.tests;


import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import gabywald.global.json.JSONArray;
import gabywald.global.json.JSONException;
import gabywald.global.json.JSONObject;
import gabywald.global.json.JSONValue;

class JSONArrayTests {

	@Test
	void testJSONArray() {
		JSONArray jsa = new JSONArray();
		Assertions.assertNotNull( jsa );
		Assertions.assertTrue( jsa.isArray() );
		Assertions.assertEquals( 0, jsa.getValues().size() );
	}
	
	@Test
	void testJSONArrayError1() {
		Assertions.assertThrows(JSONException.class, () -> new JSONArray("") );
		
		Assertions.assertThrows(JSONException.class, () -> new JSONArray("]") );
		
		Assertions.assertThrows(JSONException.class, () -> new JSONArray("[") );
		
		// Assertions.assertThrows(JSONException.class, () -> new JSONArray("[ 42, ]") );
		// Assertions.assertThrows(JSONException.class, () -> new JSONArray("[ , 42 ]") );
	}
	
	@Test
	void testJSONArrayString01() throws JSONException {
		JSONArray jsa = new JSONArray( "[]" );
		Assertions.assertNotNull( jsa );
		Assertions.assertTrue( jsa.isArray() );
		Assertions.assertEquals( 0, jsa.getValues().size() );
	}
	
	@Test
	void testJSONArrayString02() throws JSONException {
		JSONArray jsa = new JSONArray( "[ 42 ]" );
		Assertions.assertNotNull( jsa );
		Assertions.assertTrue( jsa.isArray() );
		Assertions.assertEquals( 1, jsa.getValues().size() );
	}

	@Test
	void testJSONArrayString03() throws JSONException {
		JSONArray jsa = null;
		try { jsa = new JSONArray("[ , 42 ]"); }
		catch (JSONException e) { ; }
		Assertions.assertNotNull( jsa );
		Assertions.assertTrue( jsa.isArray() );
		Assertions.assertEquals( 2, jsa.getValues().size() );
		Assertions.assertNotNull( jsa.getValues().get( 0 ) );
		Assertions.assertNotNull( jsa.getValues().get( 1 ) );
		
		Assertions.assertEquals( JSONObject.NULL, jsa.getValues().get( 0 ) );
		Assertions.assertTrue( jsa.getValues().get( 1 ).isNumber() );
		Assertions.assertEquals( 42, jsa.getValues().get( 1 ).getInteger() );
	}
	
	@Test
	void testJSONArrayArray() {
		List<JSONValue> lJSV = new ArrayList<JSONValue>();
		lJSV.add( JSONValue.instanciate( false ) );
		lJSV.add( JSONValue.instanciate( true ) );
		lJSV.add( JSONValue.instanciate( 42 ) );
		lJSV.add( JSONValue.instanciate( 42.0 ) );
		lJSV.add( JSONValue.instanciate( (long)42 ) );
		lJSV.add( JSONValue.instanciate( "string" ) );
		lJSV.add( JSONValue.instanciate( 'c' ) );
		
		JSONArray jsa = new JSONArray( lJSV );
		Assertions.assertNotNull( jsa );
		Assertions.assertTrue( jsa.isArray() );
		Assertions.assertEquals( lJSV.size(), jsa.getValues().size() );
	}
	
	@Test
	void testJSONArrayAdd() {
		List<JSONValue> lJSV = new ArrayList<JSONValue>();
		lJSV.add( JSONValue.instanciate( false ) );
		lJSV.add( JSONValue.instanciate( true ) );
		lJSV.add( JSONValue.instanciate( 42 ) );
		lJSV.add( JSONValue.instanciate( 42.0 ) );
		lJSV.add( JSONValue.instanciate( (long)42 ) );
		lJSV.add( JSONValue.instanciate( "string" ) );
		lJSV.add( JSONValue.instanciate( 'c' ) );
		
		JSONArray jsa = new JSONArray(  );
		Assertions.assertNotNull( jsa );
		Assertions.assertTrue( jsa.isArray() );
		Assertions.assertEquals( 0, jsa.getValues().size() );
		
		for (JSONValue jsv : lJSV) {
			switch(jsv.getType()) {
			case FALSE:
				Assertions.assertTrue( jsv.isBoolean() );
				Assertions.assertTrue( jsv.isFalse() );
				jsa.add( false );break;
			case TRUE: 		
				Assertions.assertTrue( jsv.isBoolean() );
				Assertions.assertTrue( jsv.isTrue() );
				jsa.add( true );break;
			case STRING: 	
				Assertions.assertTrue( jsv.isString() );
				try { jsa.add( jsv.getString() ); } 
				catch (JSONException e) { ; }
				break;
			case NUMBER: 	
				Assertions.assertTrue( jsv.isNumber() );
				boolean added = false;
				try { 
					int toAdd = jsv.getInteger();
					jsa.add( toAdd );
					added = true;
				} catch (JSONException e) { ; }
				if ( ! added ) {
					try { 
						double toAdd = jsv.getDouble();
						jsa.add( toAdd );
						added = true;
					} catch (JSONException e) { ; }
				}
				if ( ! added ) {
					try { 
						long toAdd = jsv.getLong();
						jsa.add( toAdd );
						added = true;
					} catch (JSONException e) { ; }
				}
				Assertions.assertTrue( added );
			break;
			case ARRAY:	break;
			case NULL:	break;
			case OBJECT:break;
			default:	break;
			}
		}
		
		Assertions.assertEquals( lJSV.size(), jsa.getValues().size() );
	}

}
