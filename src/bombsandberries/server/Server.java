package bombsandberries.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import bombsandberries.GameControl;

public class Server implements Runnable {
	private HashMap<String, String> students;

	private Game game;

	public static void main(String args[]) {

		Game game = new Game();

		new Thread(new Server(game)).start();

		while (true) {
			game.tick();

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}

	public Server(Game game) {
		students = new HashMap<String, String>();

		this.game = game;
	}

	public void run() {
		// Error catching loop
		while (true) {
			System.out.println("Starting Game Server");
			ServerSocket serverSocket = null;
			try {
				serverSocket = new ServerSocket(GameControl.PORT);

				// Socket accepting loop
				while (true) {
					Socket socket = serverSocket.accept();
					try {
						System.out.println("Accepted!");
						// Do the handshake (player sends studentNumber, we send
						// "OK")
						PlayerConnection playerConnection = new PlayerConnection(
								socket);
						String studentUsername = playerConnection.readLine();

						System.out.println("Student user: " + studentUsername);
						String fullname = students.get(studentUsername);
						if (fullname == null) {
							fullname = AvansFullName
									.getFullName(studentUsername);
						}

						if (fullname == null) {
							playerConnection
									.writeString("Student username "
											+ studentUsername
											+ " is unknown. Please contact the teacher.");
						} else if (game.studentIsConnected(studentUsername)) {
							playerConnection
									.writeString("Student " + studentUsername
											+ " is already connected");
						} else {
							playerConnection.writeString("OK");
							students.put(studentUsername, fullname);
							game.addNewPlayer(studentUsername,
									students.get(studentUsername),
									playerConnection);
							playerConnection
									.writeString(game.encodeGameState());
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
