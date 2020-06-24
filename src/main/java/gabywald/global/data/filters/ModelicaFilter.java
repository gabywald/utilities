package gabywald.global.data.filters;

import gabywald.global.data.Utils;
import gabywald.global.data.filters.GenericFileFilter;

public class ModelicaFilter extends GenericFileFilter {
	public ModelicaFilter() 
		{ super(Utils.mod); }

	@Override
	public String getDescription() 
		{ return "Modelica Files"; }

}
