package gabywald.global.jms;

import java.util.Scanner;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import gabywald.utilities.others.PropertiesLoader;
import net.andreinc.mockneat.types.enums.IBANType;
import net.andreinc.mockneat.types.enums.CreditCardType;
import net.andreinc.mockneat.types.enums.CVVType;

import static net.andreinc.mockneat.unit.celebrities.Celebrities.celebrities;

import static net.andreinc.mockneat.unit.financial.IBANs.ibans;
import static net.andreinc.mockneat.unit.financial.CreditCards.creditCards;
import static net.andreinc.mockneat.unit.financial.CVVS.cvvs;

/**
 * Example of use of JMS / Sender-Receiver with some MockNeat Usage. 
 * @author Gabriel Chandesris (2020)
 */
public class MainServer {

	public static void main(String[] args) {

		try {
			Context ctx				= MainServer.getContext("jndi.properties");
			
			ConnectionFactory cf	= (ConnectionFactory) ctx.lookup("connectionFactory");

			final MessageSender sender	= new MessageSender(cf);

			final MessageReceiver desti	= new MessageReceiver();
			desti.startListener(cf);

			sender.sendMessage( ibans().type(IBANType.FRANCE).get() );
			sender.sendMessage( ibans().type(IBANType.GERMANY).get() );
			sender.sendMessage( ibans().type(IBANType.SWITZERLAND).get() );
			
			sender.sendMessage( creditCards().types(CreditCardType.VISA_16).get() );
			sender.sendMessage( creditCards().types(CreditCardType.MASTERCARD).get() );
			
			sender.sendMessage( cvvs().type(CVVType.CVV4).get() );
			sender.sendMessage( cvvs().type(CVVType.CVV3).get() );
			
			Scanner scanner				= new Scanner(System.in);

			String inputData			= null;
			do {
				System.out.println("Enter data ('exit' to stop) : ");
				inputData				= scanner.nextLine();
				sender.sendMessage( inputData );
			} while ( ! inputData.equals( "exit" ) );
			
			scanner.close();

			sender.destroy();
			desti.destroy();

		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
		String actor			= celebrities().actors().get();
		String actress 			= celebrities().actresses().get();
		String jazzArtist		= celebrities().jazzArtists().get();
		String rockStar			= celebrities().rockStars().get();
		String ukPrimeMinister	= celebrities().ukPrimeMinisters().get();
		String usPresident		= celebrities().usPresidents().get();

		System.out.printf("%s / %s / %s / %s / %s / %s \n",
		               actor,
		               actress,
		               jazzArtist,
		               ukPrimeMinister,
		               usPresident,
		               rockStar);

	}
	
	public static Context getContext(String fileName) throws NamingException {
		PropertiesLoader propsLoader = new PropertiesLoader(fileName); 
		return new InitialContext(propsLoader.getProperties());
	}
}
