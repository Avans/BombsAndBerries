package bombsandberries;

public enum Command {
	LEFT('L'), RIGHT('R'), UP('U'), DOWN('D'), IDLE('I'), DROP_BOMB('B'), DEFUSE('U');

	private char c;

	private Command(char c) {
		this.c = c;
	}

	/**
	 * Get the command that corresponds to a particular character
	 * 
	 * @param c
	 * @return
	 */
	public static Command getCommand(char c) {
		for (Command command : Command.values()) {
			if (command.c == c)
				return command;
		}
		return null;
	}

	public char getChar() {
		return c;
	}

}
