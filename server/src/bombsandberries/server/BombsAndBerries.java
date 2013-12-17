package bombsandberries.server;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Main game class for libgdx
 * 
 * @author Paul Wagener
 */
public class BombsAndBerries implements ApplicationListener {

	Texture dropImage;

	OrthographicCamera camera;
	SpriteBatch batch;
	Rectangle bucket;

	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;

	@Override
	public void create() {
		dropImage = new Texture(Gdx.files.internal("res/droplet.png"));
		System.out.println(dropImage);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);

		batch = new SpriteBatch();

		bucket = new Rectangle();
		bucket.x = 0;
		bucket.y = 20;
		bucket.width = 64;
		bucket.height = 64;
	}

	@Override
	public void dispose() {
		System.out.println("Dispose");
	}

	@Override
	public void pause() {
		System.out.println("Pause");
	}

	int x = 0;

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		camera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(dropImage, bucket.x + x++, bucket.y);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void resume() {
		System.out.println("Resume");
	}

}
