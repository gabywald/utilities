package gabywald.global.data.filters;

import gabywald.global.data.Utils;

/**
 * Specific FileFilter for CellML files...
 * @author Gabriel Chandesris (2011)
 */
public class CellMLFilter extends GenericFileFilter {
	public CellMLFilter() 
		{ super(Utils.cml); }

	@Override
	public String getDescription() 
		{ return "CellML Files"; }
}
