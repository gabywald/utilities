package gabywald.global.data.filters;

import gabywald.global.data.Utils;
import gabywald.global.data.filters.GenericFileFilter;

/**
 * Specific FileFilter for BibTeX files...
 * @author Gabriel Chandesris (2011)
 */
public class BibTeXFilter extends GenericFileFilter {
	public BibTeXFilter() 
		{ super(Utils.bib); }

	@Override
	public String getDescription() 
		{ return "BibTeX Files"; }
}
