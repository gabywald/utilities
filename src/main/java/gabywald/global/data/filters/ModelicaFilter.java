package gabywald.global.data.filters;

import gabywald.global.data.Utils;

/**
 * Images FileFilter...
 * @author Gabriel Chandesris (2011, 2020)
 */
public class ModelicaFilter extends GenericFileFilter {
	public ModelicaFilter() 
		{ super(Utils.mod); }

	@Override
	public String getDescription() 
		{ return "Modelica Files"; }

}
