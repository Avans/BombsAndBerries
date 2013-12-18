package bombsandberries.server;

import java.io.IOException;
import java.util.Collection;

import bombsandberries.Command;
import bombsandberries.Player;

public class ServerPlayer extends Player {

	private PlayerConnection connection;

	public ServerPlayer(int id, String studentNumber, String name, 
			PlayerConnection connection) {
		super(studentNumber, name);
		this.connection = connection;
	}

	public void sendGameState(String string) throws IOException {
		connection.writeString(string);
	}

	public Command getCommand() throws IOException {
		char c = connection.getSingleChar();
		if (c == ' ') {
			return null;
		} else {
			return Command.getCommand(c);
		}
	}

	public void executeMoveCommand(Command command) {
		if (command == Command.LEFT && x > 0) {
			x--;
		} else if (command == Command.RIGHT && x < Game.WIDTH - 1) {
			x++;
		} else if (command == Command.UP && y > 0) {
			y--;
		} else if (command == Command.DOWN && y < Game.HEIGHT - 1) {
			y++;
		} 
	}

}
