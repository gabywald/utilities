package gabywald.global.webscrapping;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.htmlunit.FailingHttpStatusCodeException;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlPage;

import gabywald.global.webscrapping.NavigatorBuilder.NavigatorBuilderField;
import gabywald.utilities.others.PropertiesLoader;

/**
 * Example of Main Use of a Navigator for Web Scrapping. 
 * @author Gabriel Chandesris (2020-2021)
 */
public class MainUseNavigator implements INavigatorBuildConfig {

	public static void main(String[] args) {
		// 'exampleNavigator.properties'
		PropertiesLoader propsLoader	= new PropertiesLoader("exampleNavigator.properties");
		MainUseNavigator musws			= MainUseNavigator.loadPropertiesInNavigator( propsLoader );
		musws.host = propsLoader.getProperty("host");

		Navigator ws = NavigatorBuilder.buildWebScrapper( musws );
		
		Map<String, String> externalLinks = new HashMap<String, String>();
		Map<String, String> internalLinks = new HashMap<String, String>();
		
		Queue<String> queue = new ConcurrentLinkedQueue<String>();
		
		List<String> dejaVu	= new ArrayList<String>();

		try {
			HtmlPage page = ws.page( musws.getHost() );
			
			// System.out.println( page );
			// System.out.println( page.asXml() );
			
			System.out.println( page.getBaseURI() );
			System.out.println( page.getUrl() );
			System.out.println( page.getBaseURI().substring(0, page.getBaseURI().lastIndexOf("/")) + 1);
			
			String baseURL = page.getBaseURI().substring(0, page.getBaseURI().lastIndexOf("/") + 1);
			
			int i = 0;
			do {
				List<HtmlAnchor> ahrefList = page.getByXPath("//a");
				for (HtmlAnchor anchor : ahrefList) {
					// System.out.println( anchor + " => " + anchor.asXml() );
					
					String href = anchor.getHrefAttribute();
					String titl = anchor.getTextContent();
					if (href.startsWith( "http" )) {
						externalLinks.put(href, titl);
					} else {
						System.out.println( anchor.getHrefAttribute() + " {" + anchor.getTextContent() + "}" );
						if (href.startsWith( "mailto" )) { ; }
						else {
							internalLinks.put(href, titl);
						}
					}
				}
				
				baseURL = page.getBaseURI().substring(0, page.getBaseURI().lastIndexOf("/") + 1);
				
				for (String key : internalLinks.keySet()) {
					System.out.println( key + "\t {" + internalLinks.get(key) + "}" );
					if ( ( ! key.endsWith(".pdf")) && ( ! key.endsWith(".exe")) && ( ! key.endsWith(".zip")) ) {
						queue.add(baseURL + key);
					}
				}
				
				while (queue.peek() != null)  {
					String toView = queue.poll();
					System.out.println( "=> {" + toView + "}" );
					if (dejaVu.contains( toView )) {
						System.out.println( "\t deja vu !" );
					} else { 
						page = ws.page( toView );
						dejaVu.add( toView );
						break;
					}
				}
				
				i++;
			} while( (i < 50) && ( ! queue.isEmpty()) );
			
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static MainUseNavigator loadPropertiesInNavigator(PropertiesLoader pl) {
		MainUseNavigator musws			= new MainUseNavigator();
		for (NavigatorBuilder.NavigatorBuilderField field : NavigatorBuilderField.values()) {
			String value = ( pl.getProperty(field.getName()) );
			if (value != null) {
				switch(field) {
				case CONNECTION_TIME : 
					musws.connectTimeout	= Duration.parse( value );
					break;
				case READ_TIME : 
					musws.readTimeout		= Duration.parse( value );
					break;
				case BROWSER_USER_AGENT : 
					musws.userAgent			= value;
					break;
				case PROXY_HOST :
					musws.proxyHost			= value;
					break; 
				case PROXY_PORT : 
					musws.proxyPort			= Integer.valueOf( value );
					break;
				case CSS_ENABLED : 
					musws.cssenabled		= Boolean.valueOf( value );
					break;
				case JS_ENABLED : 
					musws.jsenabled			= Boolean.valueOf( value );
					break;
				default : 
					// TODO exception or ignore ?!
				}
			} // END "if (value != null)"
		}
		return musws;
	}

	private boolean cssenabled		= false;
	private boolean jsenabled		= false;
	
	private String host				= null;
	private Duration readTimeout	= null;
	private Duration connectTimeout	= null;

	private String userAgent		= null;
	private String proxyHost		= null;
	private Integer proxyPort		= null;
	
	@Override
	public boolean getCSSEnabled() {
		return this.cssenabled;
	}

	@Override
	public boolean getJSEnabled() {
		return this.jsenabled;
	}

	@Override
	public String getHost() {
		return this.host;
	}

	@Override
	public Duration getReadTimeout() {
		return this.readTimeout;
	}

	@Override
	public Duration getConnectTimeout() {
		return this.connectTimeout;
	}

	@Override
	public String getUserAgent() {
		return this.userAgent;
	}

	@Override
	public String getProxyHost() {
		return this.proxyHost;
	}

	@Override
	public Integer getProxyPort() {
		return this.proxyPort;
	}
	
	@Override
	public String toString() {
		return  "host: "                + this.host             + "\n"
				+ "readTimeOut: "       + this.readTimeout      + "\n"
				+ "connectTimeOut: "    + this.connectTimeout   + "\n"
				+ "userAgent: "         + this.userAgent        + "\n"
				+ "proxyHost: "         + this.proxyHost        + "\n"
				+ "proxyPort: "         + this.proxyPort        + "\n";
	}

	@Override
	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public void setReadTimeout(Duration rTimeOut) {
		this.readTimeout = rTimeOut;
	}

	@Override
	public void setConnectTimeout(Duration cTimeOut) {
		this.connectTimeout = cTimeOut;
	}

	@Override
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	@Override
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	@Override
	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}

	@Override
	public void setCSSEnabled(boolean cssenabled) {
		this.cssenabled = cssenabled;
	}

	@Override
	public void setJSEnabled(boolean jsEnabled) {
		this.jsenabled = jsEnabled;
	}

}
