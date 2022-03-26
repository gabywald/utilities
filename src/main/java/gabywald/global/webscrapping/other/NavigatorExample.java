package gabywald.global.webscrapping.other;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

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
	public static void main(String[] args) {

		PropertiesLoader propsLoaderNavigator	= new PropertiesLoader("exampleNavigator.properties");
		PropertiesLoader propsLoaderLocal		= new PropertiesLoader("ligueludiqueWebScraping.properties");
		MainUseNavigator lin					= MainUseNavigator.loadPropertiesInNavigator( propsLoaderNavigator );
		lin.setHost( propsLoaderLocal.getProperty( "website.url" ) );

		Navigator ws = NavigatorBuilder.buildWebScrapper( lin );

		try {
			String url = propsLoaderLocal.getProperty( "website.url" );
			final WebClient webClient = new WebClient();
			webClient.getOptions().setJavaScriptEnabled(false);
			webClient.getOptions().setCssEnabled(false);

			final HtmlPage loginPage = webClient.getPage(url);
			// Get Form By name 
			// final HtmlForm loginForm					= loginPage.getFormByName("login__form");
			List<HtmlForm> forms = loginPage.getForms();
			HtmlForm loginForm = null;
			if (forms.size() == 1) {
				loginForm = forms.get(0);
			}
			// Use Form
			final HtmlSubmitInput button				= loginForm.getInputByName("login");
			final HtmlTextInput usernameTextField		= loginForm.getInputByName("username");
			final HtmlPasswordInput passwordTextField	= loginForm.getInputByName("password");
			usernameTextField.setValueAttribute( propsLoaderLocal.getProperty( "website.username" ) ); // Username
			passwordTextField.setValueAttribute( propsLoaderLocal.getProperty( "website.password" ) ); // Password
			final HtmlPage responsePage					= button.click();
			String htmlBody								= responsePage.getWebResponse().getContentAsString();
			// System.out.println(htmlBody);

			// for (NameValuePair item : responsePage.getWebResponse().getResponseHeaders()) {
			// 	System.out.println(item.getName() + " : " + item.getValue()); // + " [" + item.getClass() + "]"
			// }
			// for (Cookie cookie : webClient.getCookieManager().getCookies()) 
			// 	{ System.out.println( cookie ); }

			HtmlPage nextPage = webClient.getPage( propsLoaderLocal.getProperty( "list.Troll2Jeux" ) );
			// TODO parsing, grabing and getting data from this page and similar to generate "calendar data"


			HtmlPage logoutPage = webClient.getPage( propsLoaderLocal.getProperty( "website.logout" ) );
			System.out.println( logoutPage.getBody().asText().contains("Connexion") );
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
}
