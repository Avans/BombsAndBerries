package bombsandberries;
public class Main {

	public static void main(String[] args) {
		BombsAndBerriesClient game = new BombsAndBerriesClient("20f71940");

		System.out.println("We stoppen er weer mee");
		System.exit(0);
		while (true) {
			System.out.println("Hoi");
			game.moveLeft();
			game.moveUp();
			game.moveRight();
			game.moveDown();
		}
	}
}
