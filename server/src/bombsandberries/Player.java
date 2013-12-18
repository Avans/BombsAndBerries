package bombsandberries;

public class Player extends GameObject {

	private String studentNumber;
	private String name;
	private int score;

	public Player(String studentNumber, String name) {
		this(studentNumber, name, 0, 0);
	}

	public Player(String studentNumber, String name, int x, int y) {
		super(x, y);
		this.studentNumber = studentNumber;
		this.score = 0;
		this.name = name;
	}

	public String getStudentNumber() {
		return studentNumber;
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

	void setScore(int score) {
		this.score = score;
		
	}

}
