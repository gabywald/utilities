package gabywald.global.data.filters;

import gabywald.global.data.Utils;

/**
 * Images FileFilter...
 * @author Gabriel Chandesris (2011, 2020)
 */
public class ImagesFilter extends GenericFileFilter {

	public ImagesFilter() 
		{ super(new String[] {	Utils.gif, Utils.jpeg, Utils.jpg,  
								Utils.tif, Utils.tiff, Utils.png }); }
	
	@Override
	public String getDescription() 
		{ return "Images Files"; }

}
