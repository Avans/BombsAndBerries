package bombsandberries.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class PlayerConnection {

	Socket socket;
	Reader reader;
	Writer writer;

	public PlayerConnection(Socket socket) throws IOException {
		this.socket = socket;
		this.reader = new InputStreamReader(socket.getInputStream());
		this.writer = new OutputStreamWriter(socket.getOutputStream());
	}

	/**
	 * Get a single command from the Player
	 * @return
	 * @throws IOException
	 */
	public char getSingleChar() throws IOException {
		try {
			socket.setSoTimeout(1);
			int read = reader.read();
			if(read == -1) {
				throw new IOException("End of stream reached");
			}
			return (char) read;
		} catch (SocketTimeoutException s) {
			return ' ';
		}
	}
	
	public String readLine() throws IOException {
		return new BufferedReader(reader).readLine();
	}
	
	public void writeString(String string) throws IOException {
		writer.write((string + "\n").toCharArray());
		writer.flush();
	}
}
