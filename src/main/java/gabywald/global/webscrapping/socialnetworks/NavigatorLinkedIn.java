package gabywald.global.webscrapping.socialnetworks;

import org.htmlunit.WebClient;
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
 * @author Gabriel Chandesris (2021)
 */
public class NavigatorLinkedIn {

	public static void main(String[] args) {
		
		PropertiesLoader propsLoaderNavigator	= new PropertiesLoader("exampleNavigator.properties");
		PropertiesLoader propsLoaderSocNetWrks	= new PropertiesLoader("socialnetworks.properties");
		MainUseNavigator lin					= MainUseNavigator.loadPropertiesInNavigator( propsLoaderNavigator );
		lin.setHost( propsLoaderSocNetWrks.getProperty( "linkedin.url" ) );

		Navigator ws = NavigatorBuilder.buildWebScrapper( lin );
		
	   try {
	        String url = propsLoaderSocNetWrks.getProperty( "linkedin.url" );
	        final WebClient webClient = new WebClient();
	        webClient.getOptions().setJavaScriptEnabled(false);
	        webClient.getOptions().setCssEnabled(false);

	        final HtmlPage loginPage = webClient.getPage(url);
	        // Get Form By name 
	        final HtmlForm loginForm					= loginPage.getFormByName("login__form");
	        // Use Form
	        final HtmlSubmitInput button				= loginForm.getInputByValue( "S’identifier" ); // loginForm.getInputByName("signin");
	        final HtmlTextInput usernameTextField		= loginForm.getInputByName("session_key");
	        final HtmlPasswordInput passwordTextField	= loginForm.getInputByName("session_password");
	        usernameTextField.setValueAttribute( propsLoaderSocNetWrks.getProperty( "linkedin.username" ) ); // Your Linkedin Username
	        passwordTextField.setValueAttribute( propsLoaderSocNetWrks.getProperty( "linkedin.password" ) ); // Your Linkedin Password
	        final HtmlPage responsePage					= button.click();
	        String htmlBody								= responsePage.getWebResponse().getContentAsString();
	        System.out.println(htmlBody);
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
		
	}
	
}
