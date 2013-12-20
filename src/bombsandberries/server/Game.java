package bombsandberries.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import bombsandberries.*;
import bombsandberries.json.JSONArray;
import bombsandberries.json.JSONObject;

public class Game {
	public static final int WIDTH = GameControl.SPACES_WIDTH;
	public static final int HEIGHT = GameControl.SPACES_HEIGHT;

	private static final int NUMBER_OF_BERRIES = 5;
	private static int newId = 0;

	private Set<Bomb> bombs;
	private Set<ServerPlayer> players;
	private Set<Berry> berries;
	private Set<Explosion> explosions;
	private HashMap<String, Integer> scores;

	private Sound explosionSound;
	private Sound berrySound;

	public Game() {
		explosions = new HashSet<Explosion>();
		bombs = new HashSet<Bomb>();
		players = new HashSet<ServerPlayer>();
		berries = new HashSet<Berry>();

		scores = new HashMap<String, Integer>();

		explosionSound = Gdx.audio.newSound(Gdx.files
				.internal("res/explosion.wav"));
		berrySound = Gdx.audio
				.newSound(Gdx.files.internal("res/berry-get.wav"));
	}

	public ServerPlayer addNewPlayer(String studentNumber, String name,
			PlayerConnection connection) {
		ServerPlayer player = new ServerPlayer(getNewId(), studentNumber, name,
				connection);
		Random random = new Random();
		player.setPosition(random.nextInt(Game.WIDTH),
				random.nextInt(Game.HEIGHT));
		// Restore score
		if(scores.containsKey(studentNumber))
			player.setScore(scores.get(studentNumber));
		players.add(player);
		return player;
	}

	public synchronized void tick() {
		System.out.println("TICK " + players.size());

		Set<ServerPlayer> playersToRemove = new HashSet<ServerPlayer>();
		Set<ServerPlayer> playersToSendGameState = new HashSet<ServerPlayer>();

		// Remove all explosions
		explosions.clear();

		// Execute move commands
		Set<Bomb> bombsToDrop = new HashSet<Bomb>();
		for (ServerPlayer player : players) {
			try {
				Command command = player.getCommand();
				if (command == Command.DROP_BOMB && player.getScore() > 0) {
					Bomb bomb = new Bomb(player.getX(), player.getY());
					bombsToDrop.add(bomb);
				} else {
					if (command != null) {
						player.executeMoveCommand(command);
						System.out.println(player + " executes command "
								+ command);
						playersToSendGameState.add(player);
					}
				}
			} catch (IOException e) {
				playersToRemove.add(player);
			}
		}
		players.removeAll(playersToRemove);

		// Execute bomb explosions
		for (ServerPlayer player : players) {
			Bomb bomb = getBombAtPosition(player.getX(), player.getY());
			if (bomb != null) {
				bombs.remove(bomb);
				player.setScore(0);
				recordScore(player);
				explosions.add(new Explosion(player.getX(), player.getY()));
				explosionSound.play();
			}
		}

		// Drop new bombs
		bombs.addAll(bombsToDrop);

		// Execute picking up of cherries
		for (ServerPlayer player : players) {
			Berry berry = getBerryAtPosition(player.getX(), player.getY());
			if (berry != null) {
				player.increaseScore();
				recordScore(player);
				berries.remove(berry);
				berrySound.play();
			}
		}

		// Generate new berries at random locations
		while (berries.size() < NUMBER_OF_BERRIES) {
			Random random = new Random();
			int x = random.nextInt(WIDTH);
			int y = random.nextInt(HEIGHT);
			if (getBerryAtPosition(x, y) == null
					&& getPlayerAtPosition(x, y) == null)
				berries.add(new Berry(x, y));
		}

		// Send gamestate to clients
		String gameState = encodeGameState();
		for (ServerPlayer player : players) {
			try {
				player.sendGameState(gameState);
			} catch (IOException e) {
			}
		}
	}

	private void recordScore(ServerPlayer player) {
		scores.put(player.getStudentNumber(), player.getScore());
	}

	private Object getPlayerAtPosition(int x, int y) {
		for (Player player : players) {
			if (player.getX() == x && player.getY() == y)
				return player;
		}
		return null;
	}

	private Berry getBerryAtPosition(int x, int y) {
		for (Berry berry : berries) {
			if (berry.getX() == x && berry.getY() == y)
				return berry;
			;
		}
		return null;
	}

	private Bomb getBombAtPosition(int x, int y) {
		for (Bomb bomb : bombs) {
			if (bomb.getX() == x && bomb.getY() == y)
				return bomb;
		}
		return null;
	}

	public synchronized String encodeGameState() {
		JSONObject state = new JSONObject();

		// Encode players
		JSONArray players_json = new JSONArray();
		for (ServerPlayer player : players) {
			JSONObject player_json = new JSONObject();
			player_json.put("x", player.getX());
			player_json.put("y", player.getY());
			player_json.put("score", player.getScore());
			player_json.put("student_number", player.getStudentNumber());
			player_json.put("name", player.getName());
			players_json.put(player_json);
		}
		state.put("players", players_json);

		// Encode bombs
		JSONArray bombs_json = new JSONArray();
		for (Bomb bomb : bombs) {
			JSONObject bomb_json = new JSONObject();
			bomb_json.put("x", bomb.getX());
			bomb_json.put("y", bomb.getY());
			bombs_json.put(bomb_json);
		}
		state.put("bombs", bombs_json);

		// Encode berries
		JSONArray berries_json = new JSONArray();
		for (Berry berry : berries) {
			JSONObject berry_json = new JSONObject();
			berry_json.put("x", berry.getX());
			berry_json.put("y", berry.getY());
			berries_json.put(berry_json);
		}
		state.put("berries", berries_json);

		return state.toString();
	}

	private int getNewId() {
		newId++;
		return newId;
	}

	public boolean studentIsConnected(String studentNumber) {
		for (ServerPlayer player : players) {
			if (player.getStudentNumber().equals(studentNumber)) {
				return true;
			}
		}
		return false;
	}

	public Set<ServerPlayer> getPlayers() {
		return players;
	}

	public Set<Bomb> getBombs() {
		return bombs;
	}

	public Set<Berry> getBerries() {
		return berries;
	}

	public Set<Explosion> getExplosions() {
		return explosions;
	}

}
