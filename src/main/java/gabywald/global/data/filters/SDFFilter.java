package gabywald.global.data.filters;

import gabywald.global.data.Utils;

/**
 * Specific FileFilter for BibTeX files...
 * @author Gabriel Chandesris (2011)
 */
public class SDFFilter extends GenericFileFilter {
	public SDFFilter() 
		{ super(Utils.sdf); }

	@Override
	public String getDescription() 
		{ return "SDF Files"; }
}
