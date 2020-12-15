package global.java.json.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import gabywald.global.json.JSONException;
import gabywald.global.json.JSONValue;

/**
 * 
 * @author Gabriel Chandesris (2020)
 */
class JSONValueTests {

	@Test
	void testJSONValueInteger() throws JSONException {
		JSONValue jso = JSONValue.instanciate(42);
		Assertions.assertNotNull( jso );
		Assertions.assertFalse( jso.isString() );
		Assertions.assertFalse( jso.isArray() );
		Assertions.assertFalse( jso.isBoolean() );
		Assertions.assertTrue( jso.isNumber() );
		Assertions.assertFalse( jso.isObject() );
		Assertions.assertFalse( jso.isTrue() );
		Assertions.assertFalse( jso.isFalse() );
		Assertions.assertEquals( 42, jso.getInteger() );
		Assertions.assertThrows(JSONException.class, () -> jso.getDouble() );
		Assertions.assertThrows(JSONException.class, () -> jso.getLong() );
	}
	
	@Test
	void testJSONValueDouble() throws JSONException {
		JSONValue jso = JSONValue.instanciate(42.0);
		Assertions.assertNotNull( jso );
		Assertions.assertFalse( jso.isString() );
		Assertions.assertFalse( jso.isArray() );
		Assertions.assertFalse( jso.isBoolean() );
		Assertions.assertTrue( jso.isNumber() );
		Assertions.assertFalse( jso.isObject() );
		Assertions.assertFalse( jso.isTrue() );
		Assertions.assertFalse( jso.isFalse() );
		Assertions.assertEquals( 42.0, jso.getDouble() );
		Assertions.assertThrows(JSONException.class, () -> jso.getInteger() );
		Assertions.assertThrows(JSONException.class, () -> jso.getLong() );
	}
	
	@Test
	void testJSONValueLong() throws JSONException {
		JSONValue jso = JSONValue.instanciate((long)42);
		Assertions.assertNotNull( jso );
		Assertions.assertFalse( jso.isString() );
		Assertions.assertFalse( jso.isArray() );
		Assertions.assertFalse( jso.isBoolean() );
		Assertions.assertTrue( jso.isNumber() );
		Assertions.assertFalse( jso.isObject() );
		Assertions.assertFalse( jso.isTrue() );
		Assertions.assertFalse( jso.isFalse() );
		Assertions.assertEquals( (long)42, jso.getLong() );
		Assertions.assertThrows(JSONException.class, () -> jso.getDouble() );
		Assertions.assertThrows(JSONException.class, () -> jso.getInteger() );
	}
	
	@Test
	void testJSONValueString() throws JSONException {
		JSONValue jso = JSONValue.instanciate( "string" );
		Assertions.assertNotNull( jso );
		Assertions.assertTrue( jso.isString() );
		Assertions.assertFalse( jso.isArray() );
		Assertions.assertFalse( jso.isBoolean() );
		Assertions.assertFalse( jso.isNumber() );
		Assertions.assertFalse( jso.isObject() );
		Assertions.assertFalse( jso.isTrue() );
		Assertions.assertFalse( jso.isFalse() );
		Assertions.assertEquals( "string", jso.getString() );
	}
	
	@Test
	void testJSONValueBooleanTrue() throws JSONException {
		JSONValue jso = JSONValue.instanciate( true );
		Assertions.assertNotNull( jso );
		Assertions.assertFalse( jso.isString() );
		Assertions.assertFalse( jso.isArray() );
		Assertions.assertTrue( jso.isBoolean() );
		Assertions.assertFalse( jso.isNumber() );
		Assertions.assertFalse( jso.isObject() );
		Assertions.assertTrue( jso.isTrue() );
		Assertions.assertFalse( jso.isFalse() );
		Assertions.assertEquals( true, jso.getBoolean() );
	}
	
	@Test
	void testJSONValueBooleanFalse() throws JSONException {
		JSONValue jso = JSONValue.instanciate( false );
		Assertions.assertNotNull( jso );
		Assertions.assertFalse( jso.isString() );
		Assertions.assertFalse( jso.isArray() );
		Assertions.assertTrue( jso.isBoolean() );
		Assertions.assertFalse( jso.isNumber() );
		Assertions.assertFalse( jso.isObject() );
		Assertions.assertFalse( jso.isTrue() );
		Assertions.assertTrue( jso.isFalse() );
		Assertions.assertEquals( false, jso.getBoolean() );
	}

	// TODO complete JSONValue tests (JSONObject, JSONArray)
	
}
