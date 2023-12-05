package gabywald.global.webscrapping.other;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.htmlunit.ElementNotFoundException;
import org.htmlunit.FailingHttpStatusCodeException;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlPasswordInput;
import org.htmlunit.html.HtmlSubmitInput;
import org.htmlunit.html.HtmlTextInput;

import gabywald.global.webscrapping.MainUseNavigator;
import gabywald.global.webscrapping.Navigator;
import gabywald.global.webscrapping.NavigatorBuilder;
import gabywald.utilities.others.PropertiesLoader;


/**
 * Idea coming from https://stackoverflow.com/questions/36127360/login-to-linkedin-using-htmlunit-and-navigate-to-connections-web-page
 * <br/>Starting Web Scraper for "Ligue Ludique Forum"
 * @author Gabriel Chandesris (2022)
 */
public class NavigatorExample {
	
	// Day of week, number in month, month...
	private static final Pattern dateRecognition = 
			Pattern.compile( "^(.*) ([1-9]+)(er)? ([A-Za-zéû]+) ((20[0-9]{2}) )?[:-] ((.*))$" );
	
	public static void main(String[] args) {

		PropertiesLoader propsLoaderNavigator	= new PropertiesLoader("exampleNavigator.properties");
		PropertiesLoader propsLoaderLocal		= new PropertiesLoader("ligueludiqueWebScraping.properties");
		MainUseNavigator lin					= MainUseNavigator.loadPropertiesInNavigator( propsLoaderNavigator );
		lin.setHost( propsLoaderLocal.getProperty( "website.url" ) );

		Navigator navigator = NavigatorBuilder.buildWebScrapper( lin );

		try {
			final WebClient webClient = new WebClient();
			webClient.getOptions().setJavaScriptEnabled(false);
			webClient.getOptions().setCssEnabled(false);

			// NavigatorExample.login(webClient, propsLoaderLocal);

			// TODO iteration of different lists (with a meta-list !?)
			List<String> toiterate = Arrays.asList( propsLoaderLocal.getProperty( "list.ToIterate" ).split(";") ); 
			// toiterate.forEach(System.out::println);
			
			for (String list2analyse : toiterate) {
			
				HtmlPage nextPage = navigator.page( propsLoaderLocal.getProperty( "list." + list2analyse ) );
				// webClient.getPage( propsLoaderLocal.getProperty( "list.Troll2Jeux" ) );
				// System.out.println( nextPage.getBody() );
				
				for (HtmlAnchor elt : nextPage.getBody().getByXPath( "//a[@class='topictitle']" )
														.stream()
													    .filter(obj -> obj instanceof HtmlAnchor)
													    .map(HtmlAnchor.class::cast).collect(Collectors.toList())) {
					System.out.println( "[" + elt.getTextContent() + "] => [" + elt.getAttribute( "href" ) + "]" );
					// TODO analyse / parse date and name of the game
					String toReco = elt.getTextContent();
					Matcher match = dateRecognition.matcher( toReco );
					if (match.matches()) {
						System.out.println( "\t" + "RECO " + "{" + toReco + "}" );
						for (int i = 1 ; i < match.groupCount() ; i++) 
							{ System.out.println( "\t\t" + "" + "{" + match.group( i ) + "}" ); }
					} else {
						System.out.println( "\t" + "NOT RECO " + "{" + toReco + "}" );
					}
					
					// XXX if full date not found (year : analyse the initial / last post in thread)
					// XXX complete the found URLs !
				}
				// TODO parsing, grabing and getting data from this page and similar to generate "calendar data"
			}
			
			HtmlPage logoutPage = webClient.getPage( propsLoaderLocal.getProperty( "website.logout" ) );
			System.out.println( logoutPage.getBody().getTextContent().contains("Connexion") );
			webClient.close();
		} catch (ElementNotFoundException enfe) {
			enfe.printStackTrace();
		} catch (FailingHttpStatusCodeException fhsce) {
			fhsce.printStackTrace();
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}
	
	public static void login(WebClient webClient, PropertiesLoader propsLoaderLocal) 
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		String urlBaseWebsite = propsLoaderLocal.getProperty( "website.url" );
		final HtmlPage loginPage = webClient.getPage( urlBaseWebsite );
		List<HtmlForm> forms = loginPage.getForms();
		HtmlForm loginForm = null;
		if (forms.size() == 1) 
			{ loginForm = forms.get(0); }
		// Use Form
		final HtmlSubmitInput button				= loginForm.getInputByName("login");
		final HtmlTextInput usernameTextField		= loginForm.getInputByName("username");
		final HtmlPasswordInput passwordTextField	= loginForm.getInputByName("password");
		usernameTextField.setValueAttribute( propsLoaderLocal.getProperty( "website.username" ) ); // Username
		passwordTextField.setValueAttribute( propsLoaderLocal.getProperty( "website.password" ) ); // Password
		final HtmlPage responsePage					= button.click();
		String htmlBody								= responsePage.getWebResponse().getContentAsString();
		System.out.println(htmlBody);

		// for (NameValuePair item : responsePage.getWebResponse().getResponseHeaders()) {
		// 	System.out.println(item.getName() + " : " + item.getValue()); // + " [" + item.getClass() + "]"
		// }
		// for (Cookie cookie : webClient.getCookieManager().getCookies()) 
		// 	{ System.out.println( cookie ); }
	}
}
