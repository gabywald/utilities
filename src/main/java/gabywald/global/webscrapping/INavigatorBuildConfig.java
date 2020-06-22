package gabywald.global.webscrapping;

import java.time.Duration;

/**
 * Parent Interface to provide a configuration for NavigatorBuilder. 
 * @author Gabriel Chandesris (2020)
 */
public interface INavigatorBuildConfig {
	
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
}
