package bombsandberries;
import java.io.BufferedReader;
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
	Socket serverConnection;

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
			String status = new BufferedReader(new InputStreamReader(
					serverConnection.getInputStream())).readLine();
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
		sendCommandAndSynchronize('L');
	}

	/**
	 * Move your player character one space to the right
	 */
	public void moveRight() {
		sendCommandAndSynchronize('R');
	}

	/**
	 * Move your player character one space to the top
	 */
	public void moveUp() {
		sendCommandAndSynchronize('U');
	}

	/**
	 * Move your player character one space to the bottom
	 */
	public void moveDown() {
		sendCommandAndSynchronize('D');
	}

	/**
	 * Stand still
	 */
	public void idle() {
		sendCommandAndSynchronize('I');
	}

	/**
	 * Drops a bomb on the current location of the player
	 */
	public void dropBomb() {
		sendCommandAndSynchronize('B');
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

	private void sendCommandAndSynchronize(char command) {

		synchronize();
	}

	/**
	 * Wait for a new gamestate from the server and update all local objects to
	 * the new game state.
	 */
	private void synchronize() {

	}
}
