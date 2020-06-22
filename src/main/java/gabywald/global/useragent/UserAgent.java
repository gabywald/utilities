package gabywald.global.useragent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class defines Web User Agents in Java (easy-to-use for requesting a page or a GET form). 
 * @author Gabriel Chandesris (2011)
 */
public class UserAgent {
	private static final String CHARSET	= "UTF-8";
	private URL url;
	private HttpURLConnection connection;
	private List<String> cookies;

	private String currentContent;

	private UserAgent() {
		this.init();
	}

	private UserAgent(String url) {
		// this.url		= new URL(url);
		this.init();

		this.currentContent = this.connectTo(url);

		try { this.connect(false); }
		catch (IOException e) 
			{ System.out.println(e.getMessage()); }
	}

	private void init() 
		{ this.connection = null; }

	public static UserAgent getUserAgent() 
		{ return new UserAgent(); }

	public boolean isConnected() 
		{ return (this.connection != null); }

	public static UserAgent getUserAgent(String url) {
		if ( (url == null) || (url.equals("")) )
			{ return UserAgent.getUserAgent(); }
		return new UserAgent(url);
	}

	public String getHeader() {
		String toReturn = new String("");

		if (!this.isConnected()) { toReturn += "Not connected !!"; }
		else {
			Map<String, List<String>> headersFieldsKeys = 
				this.connection.getHeaderFields();
			Set<String> keySet = headersFieldsKeys.keySet();
			String[] tabOfKeys = keySet.toArray(new String[0]);
			for (int i = 0 ; i < tabOfKeys.length ; i++) {
				List<String> datas = headersFieldsKeys.get(tabOfKeys[i]);
				/** System.out.println("\t'"+tabOfKeys[i]+"'\t("+datas.size()+")"); */
				int length			= ((tabOfKeys[i] == null)?4:tabOfKeys[i].length())+2;
				String completion	= new String("");
				while (length < 32) {
					completion += "\t";
					length += 8;
				}
				toReturn += "'"+tabOfKeys[i]+"'"+completion+"("+datas.size()+")"+"("+length+") [";
				String tmp = new String("");
				for (int j = 0 ; j < datas.size() ; j++) 
					{ tmp += datas.get(j)+", "; }
				toReturn += tmp.substring(0, ((tmp.length() == 0)?0:tmp.length()-2));
				toReturn += "]\n";
			}
		}

		return toReturn;
	}

	public String getContent() {

		if (this.currentContent != null) 
			{ return this.currentContent; }

		String toReturn = new String("");
		if (!this.isConnected()) { toReturn += "<NO CONTENT>"; }
		else {		
			try {
				InputStream ips 		= this.connection.getInputStream();
				InputStreamReader ipsr	= new InputStreamReader(ips);
				BufferedReader br		= new BufferedReader(ipsr);
				String line;
				while ((line = br.readLine()) != null) { 
					/** if (line.endsWith("\n")) 
						{ line = line.substring(0, line.length()-1); } /** chomp */
					if (!line.equals("")) { toReturn += line + "#####"; }
				}
				br.close(); 
			} 
			catch (IOException e) { System.out.println("error... => ''"+e.getMessage()+"''"); }
		}

		//		if (this.currentContent != null) { this.currentContent = null; }
		//		else { this.currentContent = toReturn; }

		return toReturn;
	}

	public String[] getContentAsTab() 
		{ return this.getContent().split("#####"); }

	public String connectTo(String url) 
		{ return this.connectTo(url, false); }

	public String connectTo(String url, boolean post) {
		this.disconnect();
		try { this.url = new URL(url); } 
		catch (MalformedURLException e) 
			{ return new String("Bad URL !"); }
		try { 

			this.connect(post);

			/** WARN cookies again... */
			if ( (!post) && (this.connection.getHeaderFields().get("Set-Cookie") != null) )
				{ this.cookies = this.connection.getHeaderFields().get("Set-Cookie"); }
		}
		catch (IOException e) 
			{ return new String("No Connection !"); }
		if (post)	{ return "submitting..."; }
		else		{ return this.getContent(); }
	}

	public String submitTo(String url, String[] paramsFields, String[] paramsValues) {

		if (paramsFields.length > paramsValues.length) {
			return new String("Not enough params values ! "
					+"("+paramsValues.length+" expected "+paramsFields.length+")");
		}

		String toReturn  = new String("");

		//Create Post String
		String data		= new String("");
		for (int i = 0 ; i < paramsFields.length ; i++ ) {
			try {
				data += ((i>0)?"&":"")+
							URLEncoder.encode(paramsFields[i], UserAgent.CHARSET)
								+"="+
							URLEncoder.encode(paramsValues[i], UserAgent.CHARSET);
			} catch (UnsupportedEncodingException e) 
				{ e.printStackTrace(); }
		}

		try {
			// Send Data To Page
			URL link				= new URL(url);
			HttpURLConnection conn	= (HttpURLConnection)link.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setAllowUserInteraction(true);
			OutputStreamWriter wr	= new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();
			
			System.out.println(conn.getResponseCode()+"\t"+conn.getResponseMessage());
			System.out.println(conn.getContentEncoding());
			System.out.println(conn.getRequestProperty("Cookie"));

			/** WARN cookies again... */
			if (conn.getHeaderFields().get("Set-Cookie") != null) { 
				this.cookies = conn.getHeaderFields().get("Set-Cookie");
				System.out.println(this.cookies);
				for (int i = 0 ; i < this.cookies.size() ; i++) 
					{ System.out.println("\t"+this.cookies.get(i)); }
				
				// conn.setRequestProperty("Cookie", this.cookies.get(1));
			}
			
			// Get The Response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ( (line = rd.readLine()) != null) {
				System.out.println(line);
				// you Can Break The String Down Here
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return toReturn; // this.getContent();
	}

	private void disconnect() {
		if (this.isConnected()) {
			// this.url.
			this.connection.disconnect();
			this.connection		= null;
			this.currentContent = null;
		} else { ; }
	}

	private void connect(boolean post) throws IOException {
		if (!this.isConnected()) {
			this.connection = (HttpURLConnection)this.url.openConnection();
			System.setProperty("http.agent", "");
			this.connection.setRequestProperty("User-Agent", 
					"Mozilla/5.0 [en] (X11; i; Linux 2.2.16 i6876; Nav) Sandbender 0.1 -- "
					+(int)Math.rint(Math.random()*100));
			/** WARN cookies... */
			if (this.cookies != null) {
				this.connection.setDoOutput(true);
				for (String cookie : this.cookies) 
				{ this.connection.addRequestProperty
					("Cookie", cookie.split(";", 2)[1]); }
			}
			if (post) {
				this.connection.setDoOutput(true); // Triggers POST.
				this.connection.setRequestProperty("Accept-Charset", UserAgent.CHARSET);
				this.connection.setRequestProperty("Content-Type", 
						"application/x-www-form-urlencoded;charset=" + UserAgent.CHARSET);
			}
		} else { ; }
	}


	/* convert from UTF-8 encoded HTML-Pages -> internal Java String Format */
	public static String convertFromUTF8(String s) {
		String out = null;
		try { out = new String(s.getBytes("ISO-8859-1"), "UTF-8"); } 
		catch (UnsupportedEncodingException e) { return null; }
		return out;
	}

	/* convert from internal Java String Format -> UTF-8 encoded HTML/JSP-Pages  */
	public static String convertToUTF8(String s) {
		String out = null;
		try { out = new String(s.getBytes("UTF-8")); } 
		catch (UnsupportedEncodingException e) { return null; }
		return out;
	}
}
