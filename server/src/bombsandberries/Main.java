package bombsandberries;
public class Main {

	public static void main(String[] args) {
		GameControl game = new GameControl("192.168.178.20", "2073405");

		while (true) {
			Berry berry = game.getBerries().get(0);

			Player player = game.getOwnPlayer();
			if(berry.getX() < player.getX()) {
				game.moveLeft();
			}
			if(berry.getX() > player.getX()) {
				game.moveRight();
			}
			
			if(berry.getY() < player.getY()) {
				game.moveUp();
			}
			if(berry.getY() > player.getY()) {
				game.moveDown();
			}
			game.dropBomb();
		}
	}
}
