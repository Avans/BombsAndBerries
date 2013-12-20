package bombsandberries.server;

import java.io.IOException;

import bombsandberries.Command;
import bombsandberries.Player;

public class ServerPlayer extends Player {

	private PlayerConnection connection;
	
	private double animatedX;
	private double animatedY;

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
			System.out.println(x);
			x++;
		} else if (command == Command.UP && y > 0) {
			y--;
		} else if (command == Command.DOWN && y < Game.HEIGHT - 1) {
			y++;
		} 
	}

	public void increaseScore(int delta) {
		score += delta;
	}
	
	@Override
	public void setPosition(int x, int y) {
		super.setPosition(x, y);
		setAnimatedX(x);
		setAnimatedY(y);
	}

	public double getAnimatedY() {
		return animatedY;
	}

	public void setAnimatedY(double animatedY) {
		this.animatedY = animatedY;
	}

	public double getAnimatedX() {
		return animatedX;
	}

	public void setAnimatedX(double animatedX) {
		this.animatedX = animatedX;
	}

	public void decreaseScore(int delta) {
		score -= delta;
		if(score < 0)
			score = 0;
	}

}
