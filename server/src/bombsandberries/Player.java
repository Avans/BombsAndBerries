package bombsandberries;

public class Player extends GameObject {

	private String studentNumber;
	private String name;
	private int score;

	public Player(int id, String studentNumber, String name) {
		super(id);
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

}
