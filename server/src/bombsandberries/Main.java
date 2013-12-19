package bombsandberries;
public class Main {

	public static void main(String[] args) {
		BombsAndBerriesClient game = new BombsAndBerriesClient("192.168.178.20", "2073405");

		while (true) {
			game.moveRight();
			game.moveDown();
			game.dropBomb();
		}
	}
}
