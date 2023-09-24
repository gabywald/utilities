package gabywald.global.webscrapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.htmlunit.BrowserVersion;
import org.htmlunit.PluginConfiguration;
import org.htmlunit.WebClient;

/**
 * Builder for Navigator / WebScrapping. 
 * @author Gabriel Chandesris (2020)
 */
public class NavigatorBuilder {
	
	private static final Logger logger = LoggerFactory.getLogger(NavigatorBuilder.class);

	/**
	 * Some fields definition used to build a Navigator's instance. 
	 * @author Gabriel Chandesris (2020)
	 */
	public enum NavigatorBuilderField {
		CSS_ENABLED			("css-enabled"),
		JS_ENABLED			("js-enabled"), 
		CONNECTION_TIME		("connect-timeout"), 
		READ_TIME			("read-timeout"), 
		BROWSER_USER_AGENT	("user-agent"),
		PROXY_HOST			("proxy-host"), 
		PROXY_PORT			("proxy-port");
		
		private String name = null;
		
		private NavigatorBuilderField(String name) {
			this.name = name;
		}
		
		public String getName() { 
			return this.name;
		}
	}

	private Map<NavigatorBuilderField, String> mapOfValues = new HashMap<>();

	public NavigatorBuilder cssEnabled(Boolean cssEnabled) {
		this.mapOfValues.put(NavigatorBuilderField.CSS_ENABLED, cssEnabled.toString());
		return this;
	}

	public NavigatorBuilder jsEnabled(Boolean jsEnabled) {
		this.mapOfValues.put(NavigatorBuilderField.JS_ENABLED, jsEnabled.toString());
		return this;
	}

	public NavigatorBuilder connectTimeOut(Long connectTimeOut) {
		this.mapOfValues.put(NavigatorBuilderField.CONNECTION_TIME, connectTimeOut.toString());
		return this;
	}

	public NavigatorBuilder readTimeOut(Integer readTimeOut) {
		this.mapOfValues.put(NavigatorBuilderField.READ_TIME, readTimeOut.toString());
		return this;
	}

	public NavigatorBuilder browserUserAgent(String userAgent) {
		this.mapOfValues.put(NavigatorBuilderField.BROWSER_USER_AGENT, userAgent);
		return this;
	}

	public NavigatorBuilder proxyHost(String proxyHost) {
		if (proxyHost == null) { return this; }
		this.mapOfValues.put(NavigatorBuilderField.PROXY_HOST, proxyHost);
		return this;
	}

	public NavigatorBuilder proxyPort(Integer proxyPort) {
		if (proxyPort == null) { return this; }
		this.mapOfValues.put(NavigatorBuilderField.PROXY_PORT, proxyPort.toString());
		return this;
	}

	public Navigator build() {

		String userAgent    = (this.mapOfValues.containsKey(NavigatorBuilderField.BROWSER_USER_AGENT)) ? 
				this.mapOfValues.get(NavigatorBuilderField.BROWSER_USER_AGENT) : null;
				String proxyHost    = (this.mapOfValues.containsKey(NavigatorBuilderField.PROXY_HOST)) ? 
						this.mapOfValues.get(NavigatorBuilderField.PROXY_HOST) : null;
						Integer proxyPort   = (this.mapOfValues.containsKey(NavigatorBuilderField.PROXY_PORT)) ? 
								Integer.valueOf(this.mapOfValues.get(NavigatorBuilderField.PROXY_PORT)) : null;

		// WebScrapperBuilder.logger.debug( "userAgent: {{}}", userAgent); // alternate log here

		NavigatorBuilder.logger.debug("Some parameters for the web client of Web Scrapping. ", 
										Arrays.asList(	"userAgent: {" + userAgent + "}", 
														"proxyHost: {" + proxyHost + "}", 
														"proxyPort: {" + proxyPort + "}" ) );

		final BrowserVersion browser = (userAgent == null) ? BrowserVersion.getDefault() : 
			new BrowserVersion.BrowserVersionBuilder(BrowserVersion.BEST_SUPPORTED).setUserAgent(userAgent).build();

		WebClient webClient2Build = ( (proxyHost == null) || (proxyPort == null) ) ?
				new WebClient(browser) :  new WebClient(browser, proxyHost, proxyPort);

		final WebClient webClient = webClient2Build; // new WebClient();
		if (NavigatorBuilder.logger.isDebugEnabled()) {
			BrowserVersion bv = webClient.getBrowserVersion();

			List<String> paramsToShow = new ArrayList<>();
			paramsToShow.addAll(Arrays.asList(
					"getAcceptEncodingHeader: {"        + bv.getAcceptEncodingHeader() + "}", 
					"getApplicationCodeName: {"         + bv.getApplicationCodeName() + "}", 
					"getApplicationMinorVersion: {"     + bv.getApplicationMinorVersion() + "}", 
					"getApplicationName: {"             + bv.getApplicationName() + "}", 
					"getApplicationVersion: {"          + bv.getApplicationVersion() + "}", 
					"getBrowserLanguage: {"             + bv.getBrowserLanguage() + "}", 
					"getBrowserVersionNumeric: {"       + bv.getBrowserVersionNumeric()  + "}", 
					"getBuildId: {"                     + bv.getBuildId() + "}", 
					"getCpuClass: {"                    + bv.getCpuClass() + "}", 
					"getCssAcceptHeader: {"             + bv.getCssAcceptHeader() + "}", 
					"getHtmlAcceptHeader: {"            + bv.getHtmlAcceptHeader() + "}", 
					"getImgAcceptHeader: {"             + bv.getImgAcceptHeader() + "}", 
					"getNickname: {"                    + bv.getNickname() + "}", 
					"getPixesPerChar: {"                + bv.getPixesPerChar()  + "}", 
					"getPlatform: {"                    + bv.getPlatform() + "}", 
					"getProductSub: {"                  + bv.getProductSub() + "}", 
					"getScriptAcceptHeader: {"          + bv.getScriptAcceptHeader() + "}", 
					"getSystemLanguage: {"              + bv.getSystemLanguage() + "}", 
					"getUserAgent: {"                   + bv.getUserAgent() + "}", 
					"getUserLanguage: {"                + bv.getUserLanguage() + "}", 
					"getVendor: {"                      + bv.getVendor()  + "}", 
					"getXmlHttpRequestAcceptHeader: {"  + bv.getXmlHttpRequestAcceptHeader() + "}", 
					"getAcceptEncodingHeader: {"        + bv.getSystemTimezone() + "}" ) );

			for (String header : bv.getHeaderNamesOrdered()) {
				paramsToShow.add( "header: {" + header + "}");
			}
			for (PluginConfiguration pc : bv.getPlugins()) {
				paramsToShow.add( "plugin: {" + pc + "}");
			}

			NavigatorBuilder.logger.debug("Some elements of BrowserVersion instance in WebClient Scrapper. ", paramsToShow);
		} // END "if (WebScrapperBuilder.logger.isDebugEnabled())"

		if (this.mapOfValues.containsKey(NavigatorBuilderField.CSS_ENABLED)) {
			webClient.getOptions().setCssEnabled(Boolean.parseBoolean(this.mapOfValues.get(NavigatorBuilderField.CSS_ENABLED)));
		}

		if (this.mapOfValues.containsKey(NavigatorBuilderField.JS_ENABLED)) {
			webClient.getOptions().setJavaScriptEnabled(Boolean.parseBoolean(this.mapOfValues.get(NavigatorBuilderField.JS_ENABLED)));
		}

		if (this.mapOfValues.containsKey(NavigatorBuilderField.CONNECTION_TIME)) {
			webClient.getOptions().setConnectionTimeToLive(Long.parseLong(this.mapOfValues.get(NavigatorBuilderField.CONNECTION_TIME)));
		}

		if (this.mapOfValues.containsKey(NavigatorBuilderField.READ_TIME)) {
			webClient.getOptions().setTimeout(Integer.parseInt(this.mapOfValues.get(NavigatorBuilderField.READ_TIME)));
		}

		return new Navigator( webClient );
	}

	/**
	 * Build a WebScrapper's instance with given configuration (implemented interface). 
	 * @param config
	 * @return instance of WebScrapper. 
	 */
	public static Navigator buildWebScrapper(INavigatorBuildConfig config) {
		NavigatorBuilder wsb      = new NavigatorBuilder();
		return wsb
				.cssEnabled(config.getCSSEnabled()) // usually false !
				.jsEnabled(config.getJSEnabled())	// usually false !
				.connectTimeOut(config.getConnectTimeout().toMillis())
				.readTimeOut( (int) config.getReadTimeout().toMillis())
				.browserUserAgent(config.getUserAgent())
				.proxyHost(config.getProxyHost())
				.proxyPort(config.getProxyPort())
				.build();
	}

}
