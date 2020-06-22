package gabywald.global.useragent;

import java.net.URLConnection;
import java.net.URL;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.InputStream;
import java.util.Random;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.util.Iterator;

/**
 * <p>Title: Client HTTP Request class</p>
 * <p>Description: this class helps to send POST HTTP requests with various form data,
 * including files. Cookies can be added to be included in the request.</p>
 * @author Vlad Patryshev <a href="http://www.devx.com/Java/Article/17679/1954">http://www.devx.com/Java/Article/17679/1954</a>
 * @author Gabriel HCandesris (2011, 2020) for rewriting
 * @version 1.0
 */
public class ClientHttpRequest {
	private URLConnection connection;
	private OutputStream os	= null;
	private Map cookies		= new HashMap();

	protected void connect() throws IOException 
		{ if (this.os == null) 
			{ this.os = this.connection.getOutputStream();  } }

	protected void write(char c) throws IOException {
		this.connect();
		this.os.write(c);
	}

	protected void write(String s) throws IOException {
		this.connect();
		this.os.write(s.getBytes());
	}

	protected void newline() throws IOException {
		this.connect();
		this.write("\r\n");
	}

	protected void writeln(String s) throws IOException {
		this.connect();
		this.write(s);
		this.newline();
	}

	private static Random random = new Random();

	protected static String randomString() 
		{ return Long.toString(ClientHttpRequest.random.nextLong(), 36); }

	private String boundary = "---------------------------" 
			+ ClientHttpRequest.randomString() 
			+ ClientHttpRequest.randomString() 
			+ ClientHttpRequest.randomString();

	private void boundary() throws IOException {
		this.write("--");
		this.write(this.boundary);
	}

	/**
	 * Creates a new multipart POST HTTP request on a freshly opened URLConnection
	 * @param connection an already open URL connection
	 * @throws IOException
	 */
	public ClientHttpRequest(URLConnection connection) throws IOException {
		this.connection = connection;
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + this.boundary);
	}

	/**
	 * Creates a new multipart POST HTTP request for a specified URL
	 * @param url the URL to send request to
	 * @throws IOException
	 */
	public ClientHttpRequest(URL url) throws IOException 
		{ this(url.openConnection()); }

	/**
	 * Creates a new multipart POST HTTP request for a specified URL string
	 * @param urlString the string representation of the URL to send request to
	 * @throws IOException
	 */
	public ClientHttpRequest(String urlString) throws IOException 
		{ this(new URL(urlString)); }


	@SuppressWarnings({ "unused" })
	private void postCookies() {
		StringBuffer cookieList = new StringBuffer();

		for (Iterator i = this.cookies.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry)(i.next());
			cookieList.append(entry.getKey().toString() + "=" + entry.getValue());
			if (i.hasNext()) 
				{ cookieList.append("; "); }
		}
		if (cookieList.length() > 0) 
			{ this.connection.setRequestProperty("Cookie", cookieList.toString()); }
	}

	/**
	 * Adds a cookie to the requst
	 * @param name cookie name
	 * @param value cookie value
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void setCookie(String name, String value) throws IOException 
		{ this.cookies.put(name, value); }

	/**
	 * Adds cookies to the request
	 * @param cookies the cookie "name-to-value" map
	 * @throws IOException
	 */
	public void setCookies(Map cookies) throws IOException {
		if (cookies == null) { return; }
		this.cookies.putAll(cookies);
	}

	/**
	 * Adds cookies to the request
	 * @param cookies array of cookie names and values (cookies[2*i] is a name, cookies[2*i + 1] is a value)
	 * @throws IOException
	 */
	public void setCookies(String[] cookies) throws IOException {
		if (cookies == null) { return; }
		for (int i = 0; i < cookies.length - 1; i+=2) 
		{ this.setCookie(cookies[i], cookies[i+1]); }
	}

	private void writeName(String name) throws IOException {
		this.newline();
		this.write("Content-Disposition: form-data; name=\"");
		this.write(name);
		this.write('"');
	}

	/**
	 * Adds a string parameter to the request
	 * @param name parameter name
	 * @param value parameter value
	 * @throws IOException
	 */
	public void setParameter(String name, String value) throws IOException {
		this.boundary();
		this.writeName(name);
		this.newline(); newline();
		this.writeln(value);
	}

	private static void pipe(InputStream in, OutputStream out) throws IOException {
		byte[] buf = new byte[500000];
		int nread;
		// int navailable;
		int total = 0;
		synchronized (in) {
			while((nread = in.read(buf, 0, buf.length)) >= 0) {
				out.write(buf, 0, nread);
				total += nread;
			}
		}
		out.flush();
		buf = null;
	}

	/**
	 * Adds a file parameter to the request
	 * @param name parameter name
	 * @param filename the name of the file
	 * @param is input stream to read the contents of the file from
	 * @throws IOException
	 */
	public void setParameter(String name, String filename, InputStream is) throws IOException {
		this.boundary();
		this.writeName(name);
		this.write("; filename=\"");
		this.write(filename);
		this.write('"');
		this.newline();
		this.write("Content-Type: ");
		String type = URLConnection.guessContentTypeFromName(filename);
		if (type == null) 
		{ type = "application/octet-stream"; }
		this.writeln(type);
		this.newline();
		ClientHttpRequest.pipe(is, os);
		this.newline();
	}

	/**
	 * Adds a file parameter to the request
	 * @param name parameter name
	 * @param file the file to upload
	 * @throws IOException
	 */
	public void setParameter(String name, File file) throws IOException 
	{ this.setParameter(name, file.getPath(), new FileInputStream(file)); }

	/**
	 * Adds a parameter to the request; if the parameter is a File, the file is uploaded, otherwise the string value of the parameter is passed in the request
	 * @param name parameter name
	 * @param object parameter value, a File or anything else that can be stringified
	 * @throws IOException
	 */
	public void setParameter(String name, Object object) throws IOException {
		if (object instanceof File) 
			{ this.setParameter(name, (File) object); } 
		else 
			{ this.setParameter(name, object.toString()); }
	}

	/**
	 * Adds parameters to the request
	 * @param parameters "name-to-value" map of parameters; if a value is a file, the file is uploaded, otherwise it is stringified and sent in the request
	 * @throws IOException
	 */
	public void setParameters(Map parameters) throws IOException {
		if (parameters == null) return;
		for (Iterator i = parameters.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry)i.next();
			this.setParameter(entry.getKey().toString(), entry.getValue());
		}
	}

	/**
	 * Adds parameters to the request
	 * @param parameters array of parameter names and values (parameters[2*i] is a name, parameters[2*i + 1] is a value); if a value is a file, the file is uploaded, otherwise it is stringified and sent in the request
	 * @throws IOException
	 */
	public void setParameters(Object[] parameters) throws IOException {
		if (parameters == null) return;
		for (int i = 0; i < parameters.length - 1; i += 2) 
			{ this.setParameter(parameters[i].toString(), parameters[i+1]); }
	}

	/**
	 * Posts the requests to the server, with all the cookies and parameters that were added
	 * @return input stream with the server response
	 * @throws IOException
	 */
	public InputStream post() throws IOException {
		this.boundary();
		this.writeln("--");
		this.os.close();
		return this.connection.getInputStream();
	}

	/**
	 * Posts the requests to the server, with all the cookies and parameters that were added before (if any), and with parameters that are passed in the argument
	 * @param parameters request parameters
	 * @return input stream with the server response
	 * @throws IOException
	 * @see setParameters
	 */
	public InputStream post(Map parameters) throws IOException {
		this.setParameters(parameters);
		return this.post();
	}

	/**
	 * Posts the requests to the server, with all the cookies and parameters that were added before (if any), and with parameters that are passed in the argument
	 * @param parameters request parameters
	 * @return input stream with the server response
	 * @throws IOException
	 * @see setParameters
	 */
	public InputStream post(Object[] parameters) throws IOException {
		this.setParameters(parameters);
		return this.post();
	}

	/**
	 * posts the requests to the server, with all the cookies and parameters that were added before (if any), and with cookies and parameters that are passed in the arguments
	 * @param cookies request cookies
	 * @param parameters request parameters
	 * @return input stream with the server response
	 * @throws IOException
	 * @see setParameters
	 * @see setCookies
	 */
	public InputStream post(Map cookies, Map parameters) throws IOException {
		this.setCookies(cookies);
		this.setParameters(parameters);
		return this.post();
	}

	/**
	 * Posts the requests to the server, with all the cookies and parameters that were added before (if any), and with cookies and parameters that are passed in the arguments
	 * @param cookies request cookies
	 * @param parameters request parameters
	 * @return input stream with the server response
	 * @throws IOException
	 * @see setParameters
	 * @see setCookies
	 */
	public InputStream post(String[] cookies, Object[] parameters) throws IOException {
		this.setCookies(cookies);
		this.setParameters(parameters);
		return this.post();
	}

	/**
	 * Posts the POST request to the server, with the specified parameter
	 * @param name parameter name
	 * @param value parameter value
	 * @return input stream with the server response
	 * @throws IOException
	 * @see setParameter
	 */
	public InputStream post(String name, Object value) throws IOException {
		this.setParameter(name, value);
		return this.post();
	}

	/**
	 * Posts the POST request to the server, with the specified parameters
	 * @param name1 first parameter name
	 * @param value1 first parameter value
	 * @param name2 second parameter name
	 * @param value2 second parameter value
	 * @return input stream with the server response
	 * @throws IOException
	 * @see setParameter
	 */
	public InputStream post(String name1, Object value1, String name2, Object value2) throws IOException {
		this.setParameter(name1, value1);
		return this.post(name2, value2);
	}

	/**
	 * Posts the POST request to the server, with the specified parameters
	 * @param name1 first parameter name
	 * @param value1 first parameter value
	 * @param name2 second parameter name
	 * @param value2 second parameter value
	 * @param name3 third parameter name
	 * @param value3 third parameter value
	 * @return input stream with the server response
	 * @throws IOException
	 * @see setParameter
	 */
	public InputStream post(String name1, Object value1, String name2, Object value2, String name3, Object value3) throws IOException {
		this.setParameter(name1, value1);
		return this.post(name2, value2, name3, value3);
	}

	/**
	 * Posts the POST request to the server, with the specified parameters
	 * @param name1 first parameter name
	 * @param value1 first parameter value
	 * @param name2 second parameter name
	 * @param value2 second parameter value
	 * @param name3 third parameter name
	 * @param value3 third parameter value
	 * @param name4 fourth parameter name
	 * @param value4 fourth parameter value
	 * @return input stream with the server response
	 * @throws IOException
	 * @see setParameter
	 */
	public InputStream post(String name1, Object value1, String name2, Object value2, String name3, Object value3, String name4, Object value4) throws IOException {
		this.setParameter(name1, value1);
		return this.post(name2, value2, name3, value3, name4, value4);
	}

	/**
	 * Posts a new request to specified URL, with parameters that are passed in the argument
	 * @param parameters request parameters
	 * @return input stream with the server response
	 * @throws IOException
	 * @see setParameters
	 */
	public static InputStream post(URL url, Map parameters) throws IOException 
		{ return new ClientHttpRequest(url).post(parameters); }

	/**
	 * Posts a new request to specified URL, with parameters that are passed in the argument
	 * @param parameters request parameters
	 * @return input stream with the server response
	 * @throws IOException
	 * @see setParameters
	 */
	public static InputStream post(URL url, Object[] parameters) throws IOException 
		{ return new ClientHttpRequest(url).post(parameters); }

	/**
	 * Posts a new request to specified URL, with cookies and parameters that are passed in the argument
	 * @param cookies request cookies
	 * @param parameters request parameters
	 * @return input stream with the server response
	 * @throws IOException
	 * @see setCookies
	 * @see setParameters
	 */
	public static InputStream post(URL url, Map cookies, Map parameters) throws IOException 
		{ return new ClientHttpRequest(url).post(cookies, parameters); }

	/**
	 * Posts a new request to specified URL, with cookies and parameters that are passed in the argument
	 * @param cookies request cookies
	 * @param parameters request parameters
	 * @return input stream with the server response
	 * @throws IOException
	 * @see setCookies
	 * @see setParameters
	 */
	public static InputStream post(URL url, String[] cookies, Object[] parameters) throws IOException 
		{ return new ClientHttpRequest(url).post(cookies, parameters); }

	/**
	 * Posts the POST request specified URL, with the specified parameter
	 * @param name parameter name
	 * @param value parameter value
	 * @return input stream with the server response
	 * @throws IOException
	 * @see setParameter
	 */
	public static InputStream post(URL url, String name1, Object value1) throws IOException 
		{ return new ClientHttpRequest(url).post(name1, value1); }

	/**
	 * Posts the POST request to specified URL, with the specified parameters
	 * @param name1 first parameter name
	 * @param value1 first parameter value
	 * @param name2 second parameter name
	 * @param value2 second parameter value
	 * @return input stream with the server response
	 * @throws IOException
	 * @see setParameter
	 */
	public static InputStream post(URL url, String name1, Object value1, String name2, Object value2) throws IOException 
		{ return new ClientHttpRequest(url).post(name1, value1, name2, value2); }

	/**
	 * Posts the POST request to specified URL, with the specified parameters
	 * @param name1 first parameter name
	 * @param value1 first parameter value
	 * @param name2 second parameter name
	 * @param value2 second parameter value
	 * @param name3 third parameter name
	 * @param value3 third parameter value
	 * @return input stream with the server response
	 * @throws IOException
	 * @see setParameter
	 */
	public static InputStream post(URL url, String name1, Object value1, String name2, Object value2, String name3, Object value3) throws IOException 
		{ return new ClientHttpRequest(url).post(name1, value1, name2, value2, name3, value3); }

	/**
	 * Posts the POST request to specified URL, with the specified parameters
	 * @param name1 first parameter name
	 * @param value1 first parameter value
	 * @param name2 second parameter name
	 * @param value2 second parameter value
	 * @param name3 third parameter name
	 * @param value3 third parameter value
	 * @param name4 fourth parameter name
	 * @param value4 fourth parameter value
	 * @return input stream with the server response
	 * @throws IOException
	 * @see setParameter
	 */
	public static InputStream post(URL url, String name1, Object value1, String name2, Object value2, String name3, Object value3, String name4, Object value4) throws IOException 
		{ return new ClientHttpRequest(url).post(name1, value1, name2, value2, name3, value3, name4, value4); }
}
