package nl.avans.bombsandberries;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;


/**
 * Main game class for libgdx
 * 
 * @author Paul Wagener
 */
public class BombsAndBerries implements ApplicationListener {

	@Override
	public void create() {
		System.out.println("Create");
	}

	@Override
	public void dispose() {
		System.out.println("Dispose");
	}

	@Override
	public void pause() {
		System.out.println("Pause");
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void resize(int width, int height) {
		System.out.println("Resize " + width + " " + height);
	}

	@Override
	public void resume() {
		System.out.println("Resume");
	}

}
