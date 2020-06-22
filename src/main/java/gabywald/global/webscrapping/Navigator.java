package gabywald.global.webscrapping;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * Wrapper of WebClient. 
 * @author Gabriel Chandesris (2020)
 */
public class Navigator {

	/** Browser instance. */
	private WebClient webClient = null;

	Navigator(WebClient client) {
		this.webClient = client;
	}

	public void setUseInsecureSSL(boolean b) {
		this.webClient.getOptions().setUseInsecureSSL(b);
	}

	/**
	 * To get a page from a given URL. 
	 * @param url
	 * @return
	 * @throws FailingHttpStatusCodeException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public HtmlPage page(String url) 
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		return this.webClient.getPage(url);
	}

	/** To clear the cache and the cookies. */
	public void clearCacheAndCookies() {
		this.webClient.getCache().clear();
		this.webClient.getCookieManager().clearCookies();
	}

	/**
	 * Get Form from a Page with it's name. 
	 * @param page
	 * @param formName
	 * @return
	 * @throws ElementNotFoundException
	 */
	public static HtmlForm getForm(HtmlPage page, String formName) 
			throws ElementNotFoundException {
		return page.getFormByName(formName);
	}

	/**
	 * To get a specific element in the page by it's ID. 
	 * @param page
	 * @param elementID
	 * @return
	 * @throws ElementNotFoundException
	 */
	public static DomElement getElement(HtmlPage page, String elementID) 
			throws ElementNotFoundException {
		return page.getElementById(elementID);
	}

	/**
	 * Get Input Text from a Form with it's name. 
	 * @param form
	 * @param inputTextName
	 * @return
	 * @throws ElementNotFoundException
	 */
	public static HtmlTextInput getInputText(HtmlForm form, String inputTextName)
			throws ElementNotFoundException {
		return form.getInputByName(inputTextName);
	}

	/**
	 * To set a value a text input in a form. 
	 * @param form
	 * @param inputTextName
	 * @param value
	 * @throws ElementNotFoundException
	 * @throws IOException
	 * @see Navigator#getInputText(HtmlForm, String)
	 */
	public static void setValueToInputText(HtmlForm form, String inputTextName, String value) 
			throws ElementNotFoundException, IOException {
		Navigator.getInputText(form, inputTextName).type(value);
	}

	/**
	 * To click on a submit Button, name of the button is given. 
	 * @param form
	 * @param buttonName
	 * @return
	 * @throws ElementNotFoundException
	 * @throws IOException
	 */
	public static HtmlPage clickOnSubmitButton(HtmlForm form, String buttonName) 
			throws ElementNotFoundException, IOException {
		return form.getInputByName( buttonName ).click();
	}

}
