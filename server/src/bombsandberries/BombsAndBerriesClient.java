package bombsandberries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class BombsAndBerriesClient {

	// Size of the game grid the players move on
	public final static int SPACES_WIDTH = 20;
	public final static int SPACES_HEIGHT = 20;

	public final static String HOST = "localhost";
	public final static int PORT = 54321;

	// Game state
	private ArrayList<Player> players;
	private ArrayList<Bomb> bombs;
	private ArrayList<Berry> berries;

	private String studentNumber;

	// Connection to the game server
	private Socket serverConnection;
	private BufferedReader reader;

	public BombsAndBerriesClient(String studentNumber) {
		this.studentNumber = studentNumber;

		players = new ArrayList<Player>();
		bombs = new ArrayList<Bomb>();
		berries = new ArrayList<Berry>();

		// Make a connection to the game server
		try {
			serverConnection = new Socket(HOST, PORT);
			serverConnection.setTcpNoDelay(true);

			// Send student number to the server
			serverConnection.getOutputStream().write(
					(studentNumber + "\n").getBytes());

			// Read possible error
			reader = new BufferedReader(new InputStreamReader(
					serverConnection.getInputStream()));
			String status = reader.readLine();
			if (!status.equals("OK")) { // Server should send "OK" if all is
										// well
				System.err
						.println("Error while connecting to the game server: "
								+ status);
				throw new Error();
			}

			System.out
					.println("Connected to the Bombs and Berries game server at "
							+ HOST + " on port " + PORT);

		} catch (Exception e) {
			System.err.println("Failed to connect to the game server.");
			throw new Error(e);
		}
	}

	/**
	 * Move your player character one space to the left
	 */
	public void moveLeft() {
		sendCommandAndSynchronize(Command.LEFT);
	}

	/**
	 * Move your player character one space to the right
	 */
	public void moveRight() {
		sendCommandAndSynchronize(Command.RIGHT);
	}

	/**
	 * Move your player character one space to the top
	 */
	public void moveUp() {
		sendCommandAndSynchronize(Command.UP);
	}

	/**
	 * Move your player character one space to the bottom
	 */
	public void moveDown() {
		sendCommandAndSynchronize(Command.DOWN);
	}

	/**
	 * Stand still
	 */
	public void idle() {
		sendCommandAndSynchronize(Command.IDLE);
	}

	/**
	 * Drops a bomb on the current location of the player
	 */
	public void dropBomb() {
		sendCommandAndSynchronize(Command.DROP_BOMB);
	}

	/**
	 * Get all the bombs on the playing field as they were when the last command
	 * was executed
	 */
	public ArrayList<Bomb> getBombs() {
		return bombs;
	}

	/**
	 * Get all the berries on the playing field as they were when the last
	 * command was executed
	 * 
	 * @return
	 */
	public ArrayList<Berry> getBerries() {
		return berries;
	}

	/**
	 * Get the properties of your own player
	 * 
	 * @return
	 */
	public Player getOwnPlayer() {
		for (Player player : players) {
			if (player.getStudentNumber().equals(studentNumber)) {
				return player;
			}
		}
		// Should not be possible
		return null;
	}

	/**
	 * Get the properties of all other players as they were when the last
	 * command was executed
	 * 
	 * @return
	 */
	public ArrayList<Player> getAllPlayer() {
		return players;
	}

	private void sendCommandAndSynchronize(Command command) {
		try {
			serverConnection.getOutputStream().write((int) command.getChar());
			synchronize();
		} catch (IOException e) {
			// Crash application on network error
			throw new Error(e);
		}

	}

	/**
	 * Wait for a new gamestate from the server and update all local objects to
	 * the new game state.
	 * @throws IOException 
	 */
	private void synchronize() throws IOException {
		String state = reader.readLine();
		System.out.println("State: " + state);
	}
}
