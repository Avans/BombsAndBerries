package bombsandberries.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import bombsandberries.BombsAndBerriesClient;

public class Server implements Runnable {
	private HashMap<String, String> students;

	private Game game;

	public static void main(String args[]) {
		Game game = new Game();
		new Server(game).run();
	}

	public Server(Game game) {
		students = new HashMap<String, String>();
		students.put("2074759", "Lennard Albarda");
		students.put("2075114", "Peter Askey");
		students.put("2081696", "Dion Thiers");
		students.put("2053365", "Frank Blom");
		students.put("2071940", "Job Cuppen");
		students.put("2073405", "Milan Damen");
		students.put("2075826", "Jari van Herpen");
		students.put("2059663", "Brian Hofmans");
		students.put("2078300", "Harry Kostman");
		students.put("2030657", "Gertjan Smits");
		students.put("2072707", "Joris Kuijpers");
		students.put("2075725", "Tim Spaans");
		students.put("2070482", "Max Nijholt");
		students.put("2078498", "Gabri‘l Wessels Beljaars");
		students.put("2071245", "Joris van Orsouw");
		students.put("2072534", "Steve van Zoest");
		students.put("2080419", "Robin van Ballegooij");
		students.put("2076048", "Vikash Chotkan");
		students.put("2074654", "Rik Dieltjens");
		students.put("2073654", "Niek van de Donk");
		students.put("2071706", "Saphira Hoevenaars");
		students.put("2070580", "Niek Hoffmans");
		students.put("2076741", "Tijn Kanters");
		students.put("2080456", "Joeri van Mourik");
		students.put("2077695", "Tj van Os");
		students.put("2055474", "Michael Schouten");
		students.put("2063567", "Daan Stroink");
		students.put("2071434", "Marthijn Verhoeven");
		students.put("2071771", "Teun Vos");
		students.put("2079895", "Jasper Woertman");
		students.put("2076193", "Thomas Philipse");
		students.put("2071857", "Nick van Batenburg");

		this.game = game;
	}

	public void run() {
		// Error catching loop
		while (true) {
			System.out.println("Starting Game Server");
			ServerSocket serverSocket = null;
			try {
				serverSocket = new ServerSocket(BombsAndBerriesClient.PORT);

				// Socket accepting loop
				while (true) {
					Socket socket = serverSocket.accept();
					try {
						System.out.println("Accepted!");
						// Do the handshake (player sends studentNumber, we send
						// "OK")
						PlayerConnection playerConnection = new PlayerConnection(
								socket);
						String studentNumber = playerConnection.readLine();
						System.out.println("Studentnumber: " + studentNumber);

						if (!students.containsKey(studentNumber)) {
							playerConnection
									.writeString("Student number "
											+ studentNumber
											+ " is unknown. Please contact the teacher.");
						} else if (game.studentIsConnected(studentNumber)) {
							playerConnection.writeString("Student number "
									+ studentNumber + " is already connected");
						} else {
							playerConnection.writeString("OK");
							game.addNewPlayer(studentNumber, playerConnection);
						}

					} catch (Exception e) {
						System.err.println(e.getMessage());
					}
				}

			} catch (IOException e) {
				System.err.println("Error accepting a connection. Restarting");
				e.printStackTrace();
				try {
					serverSocket.close();
				} catch (IOException e1) {
				}
			}
		}

	}
}
