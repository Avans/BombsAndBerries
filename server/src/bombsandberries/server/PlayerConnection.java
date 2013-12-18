package bombsandberries.server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

public class PlayerConnection {

	Socket socket;
	Reader reader;
	Writer writer;

	public PlayerConnection(Socket socket) throws IOException {
		reader = new InputStreamReader(socket.getInputStream());
		writer = new OutputStreamWriter(socket.getOutputStream());
	}

	/**
	 * Get a single command from the Player
	 * @return
	 * @throws IOException
	 */
	public char getSingleChar() throws IOException {

		if (reader.ready()) {
			return (char) reader.read();
		} else {
			return ' ';
		}
	}
	
	public void writeString(String string) throws IOException {
		writer.write((string + "\n").toCharArray());
	}
}
