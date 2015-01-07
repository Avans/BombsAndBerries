package bombsandberries;

public class Player extends GameObject {

	private String studentUsername;
	private String name;
	protected int score;

	public Player(String studentUsername, String name) {
		this(studentUsername, name, 0, 0);
	}

	public Player(String studentUsername, String name, int x, int y) {
		super(x, y);
		this.studentUsername = studentUsername;
		this.score = 0;
		this.name = name;
	}

	public String getStudentUsername() {
		return studentUsername;
	}

	public int getScore() {
		return score;
	}

	public String toString() {
		return name;
	};

	public String getName() {
		return name;
	}

	/**
	 * Don't even think about using this method, because it won't change your
	 * score on the server!
	 */
	public void setScore(int score) {
		this.score = score;
	}

}
