package global.java.json.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import gabywald.global.json.JSONException;
import gabywald.global.json.JSONObject;
import gabywald.global.json.JSONValue;

/**
 * 
 * @author Gabriel Chandesris (2020)
 */
class JSONObjectTests {

	@Test
	void testJSONObject() {
		JSONObject jso = new JSONObject();
		Assertions.assertNotNull( jso );
		Assertions.assertEquals("{}", jso.toString());
		
		Assertions.assertFalse( jso.isArray() );
		Assertions.assertFalse( jso.isBoolean() );
		Assertions.assertFalse( jso.isString() );
		
		Assertions.assertTrue( jso.isObject() );
		
		Assertions.assertEquals( 0, jso.getKeySet().size() );
	}
	
	@Test
	void testJSONObjectError1() {
		Assertions.assertThrows(JSONException.class, () -> new JSONObject("}"), "A JSONObject text must begin with '{'" );
		
		Assertions.assertThrows(JSONException.class, () -> new JSONObject("{"), "A JSONObject text must end with '}'" );
		
		Assertions.assertThrows(JSONException.class, () -> new JSONObject("{ \"key\" 42}"), "Expected a ':' after a key" );
		
		Assertions.assertThrows(JSONException.class, () -> new JSONObject("{ \"key\" : 42 \"123\"}"), "Expected a ',' or '}'" );
		
		Assertions.assertThrows(JSONException.class, () -> new JSONObject("{ \"key\" : 42, \"key2\" : \"123\" "), "Expected a ',' or '}'" );
	}
	
	@Test
	void testJSONObjectString01() {
		try {
			JSONObject jso = new JSONObject( "{}" );
			Assertions.assertNotNull( jso );
			Assertions.assertEquals("{}", jso.toString());
			Assertions.assertTrue( jso.isObject() );
			Assertions.assertEquals( 0, jso.getKeySet().size() );
		} catch (JSONException e) {
			Assertions.assertTrue( false );
		}
		
	}
	
	@Test
	void testJSONObjectString02() {
		
		try {
			JSONObject jso = new JSONObject( "{ \"key\" : 42 }" );
			Assertions.assertNotNull( jso );
			Assertions.assertEquals("{\"key\":42}", jso.toString());
			Assertions.assertTrue( jso.isObject() );
			Assertions.assertEquals( 1, jso.getKeySet().size() );
			Assertions.assertTrue( jso.has("key") );
			JSONValue jsv = jso.get( "key" );
			Assertions.assertNotNull( jsv );
			Assertions.assertFalse( jsv.isString() );
			Assertions.assertFalse( jsv.isArray() );
			Assertions.assertFalse( jsv.isBoolean() );
			Assertions.assertTrue( jsv.isNumber() );
			Assertions.assertFalse( jsv.isObject() );
			Assertions.assertFalse( jsv.isTrue() );
			Assertions.assertFalse( jsv.isFalse() );
			Assertions.assertEquals( 42, jsv.getInteger() );
			Assertions.assertThrows(JSONException.class, () -> jsv.getDouble() );
			Assertions.assertThrows(JSONException.class, () -> jsv.getLong() );
		} catch (JSONException e) {
			Assertions.assertTrue( false );
		}
		
	}
	
	@Test
	void testJSONObjectString03() {
		
		try {
			JSONObject jso = new JSONObject( "{ \"key\" : 42, \"key2\" : [\"123\"] }" );
			Assertions.assertNotNull( jso );
			Assertions.assertEquals("{\"key2\":[\"123\"], \"key\":42}", jso.toString());
			Assertions.assertTrue( jso.isObject() );
			Assertions.assertEquals( 2, jso.getKeySet().size() );
			
			Assertions.assertTrue( jso.has("key") );
			JSONValue jsv1 = jso.get( "key" );
			Assertions.assertNotNull( jsv1 );
			Assertions.assertFalse( jsv1.isString() );
			Assertions.assertFalse( jsv1.isArray() );
			Assertions.assertFalse( jsv1.isBoolean() );
			Assertions.assertTrue( jsv1.isNumber() );
			Assertions.assertFalse( jsv1.isObject() );
			Assertions.assertFalse( jsv1.isTrue() );
			Assertions.assertFalse( jsv1.isFalse() );
			Assertions.assertEquals( 42, jsv1.getInteger() );
			
			Assertions.assertTrue( jso.has("key2") );
			JSONValue jsv2 = jso.get( "key2" );
			Assertions.assertNotNull( jsv2 );
			Assertions.assertTrue( jsv2.isArray() );
			Assertions.assertEquals( 1, jsv2.getArray().getValues().size() );
			Assertions.assertNotNull( jsv2.getArray().getValues().get( 0 ) );
			JSONValue eltOfArray = jsv2.getArray().getValues().get( 0 );
			Assertions.assertTrue( eltOfArray.isString() );
			Assertions.assertFalse( eltOfArray.isArray() );
			Assertions.assertFalse( eltOfArray.isBoolean() );
			Assertions.assertFalse( eltOfArray.isNumber() );
			Assertions.assertFalse( eltOfArray.isObject() );
			Assertions.assertFalse( eltOfArray.isTrue() );
			Assertions.assertFalse( eltOfArray.isFalse() );
			Assertions.assertEquals( "123", jsv2.getArray().getValues().get( 0 ).getString() );

		} catch (JSONException e) {
			Assertions.assertTrue( false );
		}

	}
	
	@Test
	void testJSONObjectString04() {
		
		try {
			JSONObject jso = new JSONObject( "{ \"key\" : 42, \"key2\" : [\"123\"], \"key3\" : false, \"key4\" : true }" );
			Assertions.assertNotNull( jso );
			Assertions.assertEquals("{\"key2\":[\"123\"], \"key3\":false, \"key4\":true, \"key\":42}", jso.toString());
			Assertions.assertTrue( jso.isObject() );
			Assertions.assertEquals( 4, jso.getKeySet().size() );
			
			Assertions.assertTrue( jso.has("key3") );
			JSONValue jsv1 = jso.get( "key3" );
			Assertions.assertNotNull( jsv1 );
			Assertions.assertFalse( jsv1.isString() );
			Assertions.assertFalse( jsv1.isArray() );
			Assertions.assertTrue( jsv1.isBoolean() );
			Assertions.assertFalse( jsv1.isNumber() );
			Assertions.assertFalse( jsv1.isObject() );
			Assertions.assertFalse( jsv1.isTrue() );
			Assertions.assertTrue( jsv1.isFalse() );
			Assertions.assertEquals( false, jsv1.getBoolean() );
			
			Assertions.assertTrue( jso.has("key4") );
			JSONValue jsv2 = jso.get( "key4" );
			Assertions.assertNotNull( jsv1 );
			Assertions.assertFalse( jsv2.isString() );
			Assertions.assertFalse( jsv2.isArray() );
			Assertions.assertTrue( jsv2.isBoolean() );
			Assertions.assertFalse( jsv2.isNumber() );
			Assertions.assertFalse( jsv2.isObject() );
			Assertions.assertTrue( jsv2.isTrue() );
			Assertions.assertFalse( jsv2.isFalse() );
			Assertions.assertEquals( true, jsv2.getBoolean() );

		} catch (JSONException e) {
			Assertions.assertTrue( false );
		}

	}
	
	@Test
	void testJSONObjectString05() {
		
		try {
			JSONObject jso = new JSONObject( "{ \"key\" : 42, \"key2\" : [\"123\"], \"key3\" : false, \"key4\" : true }" );
			Assertions.assertNotNull( jso );
			Assertions.assertEquals("{\"key2\":[\"123\"], \"key3\":false, \"key4\":true, \"key\":42}", jso.toString());
			Assertions.assertTrue( jso.isObject() );
			Assertions.assertEquals( 4, jso.getKeySet().size() );
			
			jso.put("booleanFalse", false);
			Assertions.assertEquals( 5, jso.getKeySet().size() );
			jso.put("booleanTrue", true);
			Assertions.assertEquals( 6, jso.getKeySet().size() );
			
			Assertions.assertTrue( jso.has("booleanFalse") );
			JSONValue jsv1 = jso.get( "booleanFalse" );
			Assertions.assertNotNull( jsv1 );
			Assertions.assertTrue( jsv1.isBoolean() );
			Assertions.assertTrue( jsv1.isFalse() );
			Assertions.assertFalse( jsv1.isTrue() );
			Assertions.assertEquals( false, jsv1.getBoolean() );
			
			Assertions.assertTrue( jso.has("booleanTrue") );
			JSONValue jsv2 = jso.get( "booleanTrue" );
			Assertions.assertNotNull( jsv2 );
			Assertions.assertTrue( jsv2.isBoolean() );
			Assertions.assertFalse( jsv2.isFalse() );
			Assertions.assertTrue( jsv2.isTrue() );
			Assertions.assertEquals( true, jsv2.getBoolean() );
			
			jso.put("doubleValue", 42.0);
			Assertions.assertEquals( 7, jso.getKeySet().size() );
			Assertions.assertTrue( jso.has("doubleValue") );
			JSONValue jsv3 = jso.get( "doubleValue" );
			Assertions.assertNotNull( jsv3 );
			Assertions.assertTrue( jsv3.isNumber() );
			Assertions.assertEquals( 42.0, jsv3.getDouble() );
			Assertions.assertThrows(JSONException.class, () -> jsv3.getInteger() );
			Assertions.assertThrows(JSONException.class, () -> jsv3.getLong() );
			
			jso.put("integerValue", 42);
			Assertions.assertEquals( 8, jso.getKeySet().size() );
			Assertions.assertTrue( jso.has("integerValue") );
			JSONValue jsv4 = jso.get( "integerValue" );
			Assertions.assertNotNull( jsv4 );
			Assertions.assertTrue( jsv4.isNumber() );
			Assertions.assertEquals( 42, jsv4.getInteger() );
			Assertions.assertThrows(JSONException.class, () -> jsv4.getDouble() );
			Assertions.assertThrows(JSONException.class, () -> jsv4.getLong() );
			
			jso.put("longValue", (long)42.0);
			Assertions.assertEquals( 9, jso.getKeySet().size() );
			Assertions.assertTrue( jso.has("longValue") );
			JSONValue jsv5 = jso.get( "longValue" );
			Assertions.assertNotNull( jsv5 );
			Assertions.assertTrue( jsv5.isNumber() );
			Assertions.assertEquals( (long)42.0, jsv5.getLong() );
			Assertions.assertThrows(JSONException.class, () -> jsv5.getInteger() );
			Assertions.assertThrows(JSONException.class, () -> jsv5.getDouble() );
			
		} catch (JSONException e) {
			Assertions.assertTrue( false );
		}

	}

}
