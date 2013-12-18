package bombsandberries;
public class Main {

	public static void main(String[] args) {
		BombsAndBerriesClient game = new BombsAndBerriesClient("2071940");

		while (true) {
			game.moveLeft();
			
			System.out.println(game.getOwnPlayer().x + " after left " + game.getOwnPlayer().y);
			game.moveRight();
			System.out.println(game.getOwnPlayer().x + " after right" + game.getOwnPlayer().y);
		}
	}
}
