package global.java.json.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import gabywald.global.json.JSONException;
import gabywald.global.json.JSONObject;
import gabywald.global.json.JSONValue;

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
		
		// TODO continue tests on JSONObject !!
	}

	@Test
	void testJSONObjectString() {
		try {
			JSONObject jso = new JSONObject( "{}" );
			Assertions.assertNotNull( jso );
			Assertions.assertEquals("{}", jso.toString());
			Assertions.assertTrue( jso.isObject() );
			Assertions.assertEquals( 0, jso.getKeySet().size() );
		} catch (JSONException e) {
			Assertions.assertTrue( false );
		}
		
		try {
			JSONObject jso = new JSONObject( "{ \"key\" : 42 }" );
			Assertions.assertNotNull( jso );
			Assertions.assertEquals("{\"key\":42}", jso.toString());
			Assertions.assertTrue( jso.isObject() );
			Assertions.assertEquals( 1, jso.getKeySet().size() );
			Assertions.assertTrue( jso.has("key") );
			JSONValue jsv = jso.get( "key" );
			Assertions.assertNotNull( jsv );
			Assertions.assertTrue( jsv.isNumber() );
			Assertions.assertEquals( 42, jsv.getInteger() );
		} catch (JSONException e) {
			Assertions.assertTrue( false );
		}
		
		// TODO continue tests on JSONObject !!

	}

}
