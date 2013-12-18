package bombsandberries;

public class Player extends GameObject {

	private String studentNumber;
	private int score;

	public Player(int id, String studentNumber) {
		super(id);
		this.studentNumber = studentNumber;
		this.score = 0;
	}

	public String getStudentNumber() {
		return studentNumber;
	}
	
	public int getScore() {
		return score;
	}

}
