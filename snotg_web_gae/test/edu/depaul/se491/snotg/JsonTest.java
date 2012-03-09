package edu.depaul.se491.snotg;

import static org.junit.Assert.assertEquals;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

/**
 * Testing json object library with this class.
 *
 */
public class JsonTest {

	/**
	 * Testing json object by parsing a users location.
	 *
	 */
	@Test
	public void testParseUserLocationJson() {
		String jsonString = "{\"userName\":\"mike\",\"latit\":\"123.1234\",\"longit\":\"555.4444\"}";

		try {
			ObjectMapper mapper = new ObjectMapper();
			UserLocationJson user = mapper.readValue(
					jsonString.getBytes(),
					UserLocationJson.class);

			mapper.writeValue(System.out, user); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Testing method of parsing username from email.
	 *
	 */
	@Test
	public void parseUsernameFromGoogleAccount() {
		String acct = "mmich8266@gmail.com";

		String username = acct.substring(0, acct.indexOf("@"));
		assertEquals("mmich8266", username);
	}
}
