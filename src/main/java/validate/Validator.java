package validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.core.Logger;

public class Validator {

	public static boolean validateEmail(String email)
    {
        String email_pattern = "^[a-zA-Z0-9_#$%&’*+/=?^.-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pat = Pattern.compile(email_pattern);
        Matcher mat = pat.matcher(email);
        return mat.matches();
    }
	
	public static void validateStatus(Logger log, int status) {
		if(status == 200) {
			log.info("ok, response status is 200");
		} else {
			log.error("Response status is " + status);
		}
	}
	
}
