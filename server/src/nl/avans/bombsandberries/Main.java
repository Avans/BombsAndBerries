package nl.avans.bombsandberries;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;


public class Main {
	public static void main(String args[]) {
		new LwjglApplication(new BombsAndBerries(), "Bombs 'n Berries", 640, 480, false);
	}
}
