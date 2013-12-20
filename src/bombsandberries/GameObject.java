package bombsandberries;

public class GameObject {
	protected int x;
	protected int y;

	public GameObject(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public GameObject() {
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
