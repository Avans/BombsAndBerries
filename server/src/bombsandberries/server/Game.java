package bombsandberries.server;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import bombsandberries.*;

public class Game {
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	private static int newId = 0;

	private Set<Bomb> bombs;
	private Set<ServerPlayer> players;
	private Set<Berry> berries;

	public Game() {
		bombs = new HashSet<Bomb>();
		players = new HashSet<ServerPlayer>();
		berries = new HashSet<Berry>();
	}

	public ServerPlayer addNewPlayer(String studentNumber, String name,
			PlayerConnection connection) {
		ServerPlayer player = new ServerPlayer(getNewId(), studentNumber, name,
				connection);
		players.add(player);
		return player;
	}

	public void tick() {
		System.out.print("TICK ");
		// Execute move and drop bombs commands
		Set<ServerPlayer> playersToRemove = new HashSet<ServerPlayer>();
		Set<ServerPlayer> playersToSendGameState = new HashSet<ServerPlayer>();

		for (ServerPlayer player : players) {
			try {
				Command command = player.getCommand();
				if (command != null) {
					player.executeMoveCommand(command);
					System.out.println(player + " executes command " + command);
					playersToSendGameState.add(player);
				}
			} catch (IOException e) {
				playersToRemove.add(player);
			}
		}
		players.removeAll(playersToRemove);
		System.out.println("(" + players.size() + ")");

		// Execute bomb explosions

		// Execute picking up of cherries

		// Send gamestate to clients
		String gameState = encodeGameState();
		for (ServerPlayer player : players) {
			try {
				player.sendGameState(gameState);
			} catch (IOException e) {
			}
		}
	}

	private String encodeGameState() {
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
			berries_json.put(berry);
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

}
