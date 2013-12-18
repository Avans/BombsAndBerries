package bombsandberries;
public class Main {

	public static void main(String[] args) {
		BombsAndBerriesClient game = new BombsAndBerriesClient("2071940");

		while (true) {
			game.moveLeft();
			game.moveUp();
			game.moveRight();
			game.moveDown();
		}
	}
}
