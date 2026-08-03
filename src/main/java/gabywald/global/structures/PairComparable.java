package gabywald.global.structures;

import gabywald.global.json.JSONException;
import gabywald.global.json.JSONValue;
import gabywald.global.json.JSONifiable;


/**
 * This class defines a 'Java-Like' Pair structure. 
 * @author Gabriel Chandesris (2013, 2015)
 * @param <FIRST> First parameter's type. 
 * @param <SECOND> Second paramater's type. 
 */
public class PairComparable<FIRST extends Comparable<FIRST>, SECOND extends Comparable<SECOND> > 
		extends JSONifiable
		implements Comparable<PairComparable<FIRST, SECOND>> {
	private FIRST first;
	private SECOND second;
	
	public PairComparable() {
		this.first	= null;
		this.second	= null;
	}

	public PairComparable(FIRST first, SECOND second) {
		this.first	= first;
		this.second	= second;
	}
	
	public FIRST getFirst()		{ return this.first; }
	public SECOND getSecond()	{ return this.second; }
	
	public void setFirst(FIRST first)		{ this.first = first; }
	public void setSecond(SECOND second)	{ this.second = second; }
	
	public boolean equals(PairComparable<FIRST, SECOND> toCompare) 
		{ return ( (this.first.equals(toCompare.first)) 
				&& (this.second.equals(toCompare.second)) ); }

	@Override
	public int compareTo(PairComparable<FIRST, SECOND> toCompare) 
		{ return this.second.compareTo(toCompare.second); }
		// { return this.first.compareTo(toCompare.first); }
	
	public String toString() 
		{ return "[" + this.toString(", ") + "]"; }
	
	public String toString(String separator) 
		{ return ((this.first != null)?this.first.toString():null) 
					+ separator 
					+ ((this.second != null)?this.second.toString():null); }

	@Override
	protected void setKeyValues() {
		this.put("first", JSONValue.instanciate( this.first.toString() ) );
		this.put("second", JSONValue.instanciate( this.second.toString() ) );
	}

	@Override
	protected <T extends JSONifiable> T reloadFrom(String json) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}
}
