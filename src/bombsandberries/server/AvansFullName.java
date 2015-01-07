package bombsandberries.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AvansFullName {

	public static String getFullName(String username) {
		try {
			URL url = new URL("http://avans.paulwagener.nl/" + username);

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();

			int code = connection.getResponseCode();

			if (code != 200) {
				return null;
			}

			return new BufferedReader(new InputStreamReader(
					connection.getInputStream())).readLine();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
