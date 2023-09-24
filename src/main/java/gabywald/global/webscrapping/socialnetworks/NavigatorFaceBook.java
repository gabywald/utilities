package gabywald.global.webscrapping.socialnetworks;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.htmlunit.ElementNotFoundException;
import org.htmlunit.FailingHttpStatusCodeException;
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
public class NavigatorFaceBook {

	public static void main(String[] args) {
		
		PropertiesLoader propsLoaderNavigator	= new PropertiesLoader("exampleNavigator.properties");
		PropertiesLoader propsLoaderSocNetWrks	= new PropertiesLoader("socialnetworks.properties");
		MainUseNavigator lin					= MainUseNavigator.loadPropertiesInNavigator( propsLoaderNavigator );
		lin.setHost( propsLoaderSocNetWrks.getProperty( "facebook.url" ) );

		Navigator ws = NavigatorBuilder.buildWebScrapper( lin );
		
	   try {
	        String url = propsLoaderSocNetWrks.getProperty( "linkedin.url" );
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
	        final HtmlTextInput usernameTextField		= loginForm.getInputByName("email");
	        final HtmlPasswordInput passwordTextField	= loginForm.getInputByName("pass");
	        usernameTextField.setValueAttribute( propsLoaderSocNetWrks.getProperty( "facebook.username" ) ); // Your FaceBook Username
	        passwordTextField.setValueAttribute( propsLoaderSocNetWrks.getProperty( "facebook.password" ) ); // Your FaceBook Password
	        final HtmlPage responsePage					= button.click();
	        String htmlBody								= responsePage.getWebResponse().getContentAsString();
	        System.out.println(htmlBody);
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
