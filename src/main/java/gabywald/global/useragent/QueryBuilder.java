package gabywald.global.useragent;

import gabywald.global.exceptions.MessageException;

import java.util.HashMap;

/**
 * Aim of this class is to permit build of a Query URL from inheritant classes.  
 * @author Gabriel Chandesris (2011)
 */
public abstract class QueryBuilder {
	/** Hash map to build Queries. */
	protected HashMap<String, String> arguments;
	
	/**
	 * Constructor with sets of keys and set of values. 
	 * <br>Add 'db' and 'id' to the set of keys if they are NOT present ; sets to "". 
	 * <br>Same for 'type' (internal use).  
	 * @param keySet (String[])
	 * @param values (String[])
	 */
	protected QueryBuilder(String[] keySet, String[] values) {
		int newLengthOfKeySet	= keySet.length;
		boolean isDBpresent		= false;
		boolean isIDpresent		= false;
		boolean isTYPEpresent	= false;
		for (int i = 0 ; i < keySet.length ; i++) {
			if (keySet[i].equals("id"))		{ isIDpresent = true; }
			if (keySet[i].equals("db"))		{ isDBpresent = true; }
			if (keySet[i].equals("type"))	{ isTYPEpresent = true; }
		}
		if (!isIDpresent)	{ newLengthOfKeySet++; }
		if (!isDBpresent)	{ newLengthOfKeySet++; }
		if (!isTYPEpresent)	{ newLengthOfKeySet++; }
		String[] newKeySet = new String[newLengthOfKeySet];
		for (int i = 0 ; i < newLengthOfKeySet ; i++) {
			if (i < keySet.length) { newKeySet[i] = keySet[i]; }
			else { 
				if (!isIDpresent)		{ 
					newKeySet[i]	= "id";
					isIDpresent		= true;
				} else if (!isDBpresent)	{ 
					newKeySet[i]	= "db";
					isDBpresent		= true;
				} else if (!isTYPEpresent)	{ 
					newKeySet[i]	= "type";
					isTYPEpresent	= true;
				}
			}
		}
		
		this.arguments = new HashMap<String, String>(keySet.length);
		for (int i = 0 ; i < keySet.length ; i++) {
			String currentValue = new String("");
			if ( (values != null) && (i < values.length) ) 
				{ currentValue = ((values[i] != null)?values[i]:""); }
			this.arguments.put(keySet[i], currentValue);
		}
	}
	
	public void setIdentifier(String id) 
		{ this.arguments.put("id", id); }

	public void setIdentifier(int id) 
		{ this.setIdentifier(id + ""); }
	
	public void addIdentifier(int id) { 
		String ids = this.arguments.get("id");
		if ( (ids == null) || (ids.equals("")) ) 
			{ this.setIdentifier(id); }
		else { this.setIdentifier(ids+","+id); }
	}
	
	public void setDatabase(String db) 
		{ this.arguments.put("db", db); }
	
	public String getIdentifier()	{ return this.arguments.get("id"); }
	public String getDatabase()		{ return this.arguments.get("db"); }
	
	/**
	 * Gives the URL of query to submit to the Queryer / UserAgent. 
	 * <br>Has to be implemented into the following way : 
	 * <ol>
	 * 		<li>Getting / checking the 'type' (<i>int type = super.checkType&lt;SubClass extends QueryBuilder&gt;.DEFAULT_LINKS);</i>)</li>
	 * 		<li>Getting values of arguments. </li>
	 * 		<li>Checking arguments (and setting them if necessary) with switch among type. </li>
	 * 		<li>Building sub-link with switch among type (concatenation of arguments). </li>
	 * 		<li>Returning concatenation of link and sub-link. </li>
	 * </ol> 
	 * <br><b>NOTE : </b>(int) type has to be given by user in the inheritant constructor Class, 
	 * <br>and transmitted into the two arrays in the mother constructor in this Class... 
	 * @return (String)
	 * @see QueryBuilder#checkType(String[]) 
	 * @throws MessageException If an argument is necessary and not present...
	 */
	public abstract String getBuildedQuery() throws MessageException;
	
	/**
	 * Internal use of TYPE. 
	 * @param defaultLinks (String[]) Table / array of static Links. 
	 * @return (int) Index into the array. 
	 * @throws MessageException If 'type' does not correspond to a link in the array. 
	 */
	protected int checkType(String[] defaultLinks) throws MessageException {
		int type = Integer.parseInt(this.arguments.get("type"));
		if ( (type < 0) || (type >= defaultLinks.length) )
			{ throw new MessageException("Type '"+type+"' : BAD TYPE !!"); }
		return type;
	}
	
}
