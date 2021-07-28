package gabywald.global.webscrapping.socialnetworks;

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
 * @author Gabriel Chandesris (2021)
 */
public class NavigatorFaceBook {

	public static void main(String[] args) {
		
		PropertiesLoader propsLoaderNavigator	= new PropertiesLoader("exampleNavigator.properties");
		PropertiesLoader propsLoaderSocNetWrks	= new PropertiesLoader("socialnetworks.properties");
		MainUseNavigator lin					= MainUseNavigator.loadPropertiesInNavigator( propsLoaderNavigator );
		lin.setHost( propsLoaderSocNetWrks.getProperty( "linkedin.url" ) );

		Navigator ws = NavigatorBuilder.buildWebScrapper( lin );
		
	   try {
	        String url = propsLoaderSocNetWrks.getProperty( "facebook.url" );
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
	        usernameTextField.setValueAttribute( propsLoaderSocNetWrks.getProperty( "facebook.username " ) ); // Your Linkedin Username
	        passwordTextField.setValueAttribute( propsLoaderSocNetWrks.getProperty( "facebook.password " ) ); // Your Linkedin Password
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
