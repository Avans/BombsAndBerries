package bombsandberries;
public class Main {

	public static void main(String[] args) {
		BombsAndBerriesClient game = new BombsAndBerriesClient("066995970");

		while (true) {
			System.out.println("Hoi");
			game.moveLeft();
			game.moveUp();
			game.moveRight();
			game.moveDown();
		}
	}
}
