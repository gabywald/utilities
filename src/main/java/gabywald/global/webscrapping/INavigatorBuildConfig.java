package gabywald.global.webscrapping;

import java.time.Duration;

/**
 * Parent Interface to provide a configuration for NavigatorBuilder. 
 * @author Gabriel Chandesris (2020-2021)
 */
public interface INavigatorBuildConfig {
	
	/**
	 * Returns if CSS has to be enabled.
	 * @return the host URL. 
	 */
	public boolean getCSSEnabled();
	
	/**
	 * Returns if JS has to be enabled. 
	 * @return the host URL. 
	 */
	public boolean getJSEnabled();
	
	/**
	 * Returns the host.
	 * @return the host URL. 
	 */
	public String getHost();

	/**
	 * (Duration) timeout on waiting to read data. 
	 * @return Read timeout. 
	 */
	public Duration getReadTimeout();

	/**
	 * (Duration) timeout in making the initial connection to the server. 
	 * @return Connect timeout. 
	 */
	public Duration getConnectTimeout();

	/**
	 * The User Agent to use. 
	 * @return UserAgent
	 */
	public String getUserAgent();

	/**
	 * The Proxy Host to use. 
	 * @return ProxyHost
	 */
	public String getProxyHost();

	/**
	 * The Proxy Port to use. 
	 * @return ProxyPort
	 */
	public Integer getProxyPort();
	
	
	public void setCSSEnabled(boolean cssenabled);
	public void setJSEnabled(boolean jsEnabled);
	public void setHost(String host);
	public void setReadTimeout(Duration rTimeOut);
	public void setConnectTimeout(Duration cTimeOut);
	public void setUserAgent(String userAgent);
	public void setProxyHost(String proxyHost);
	public void setProxyPort(Integer proxyPort);
}
