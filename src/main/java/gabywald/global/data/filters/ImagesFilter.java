package gabywald.global.data.filters;

import gabywald.global.data.Utils;
import gabywald.global.data.filters.GenericFileFilter;

public class ImagesFilter extends GenericFileFilter {

	public ImagesFilter() 
		{ super(new String[] { Utils.gif, Utils.jpeg, Utils.jpg,  
								Utils.tif, Utils.tiff, Utils.png}); }
	
	@Override
	public String getDescription() 
		{ return "Images Files"; }

}
