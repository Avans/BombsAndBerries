package bombsandberries.gui;

import bombsandberries.server.Game;
import bombsandberries.server.Server;
import bombsandberries.server.ServerPlayer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Main game class for libgdx
 * 
 * @author Paul Wagener
 */
public class BombsAndBerries implements ApplicationListener {

	Texture dropImage;
	Texture grassTile;

	Game game;
	OrthographicCamera camera;
	SpriteBatch batch;
	Rectangle bucket;
	private Texture smileyTexture;
	private Texture bombTexture;
	private Texture berryTexture;

	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	
	private BitmapFont font;
	
	@Override
	public void create() {
		game = new Game();
		
		// Start server thread
		new Thread(new Server(game)).start();

		// Run gameticks thread
		new Thread() {
			public void run() {
				while (true) {
					game.tick();

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
					}
				}
			};
		}.start();
		
		dropImage = new Texture(Gdx.files.internal("res/droplet.png"));
		grassTile = new Texture(Gdx.files.internal("res/grasstile.png"));
		smileyTexture = new Texture(Gdx.files.internal("res/smiley.png"));
		bombTexture = new Texture(Gdx.files.internal("res/bomb.png"));
		berryTexture = new Texture(Gdx.files.internal("res/berry.png"));

		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);

		batch = new SpriteBatch();

		font = new BitmapFont();
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
		camera.update();
		final int tileWidth = WIDTH / Game.WIDTH;
		final int tileHeight = HEIGHT / Game.HEIGHT;
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		for (int x = 0; x < Game.WIDTH; x++) {
			for (int y = 0; y < Game.HEIGHT; y++) {
				drawAtPosition(batch, grassTile, x, y);
			}
		}

		for (ServerPlayer player : game.getPlayers()) {
			drawAtPosition(batch, smileyTexture, player.getX(), player.getY());
			font.draw(batch, player.getName(), player.getX() * tileWidth, HEIGHT - player.getY() * tileHeight);
		}
		
		batch.end();
		
		
	}

	private void drawAtPosition(SpriteBatch batch, Texture texture, float x,
			float y) {
		final int tileWidth = WIDTH / Game.WIDTH;
		final int tileHeight = HEIGHT / Game.HEIGHT;
		batch.draw(texture, x * tileWidth, HEIGHT - y * tileHeight - tileHeight, tileWidth,
				tileHeight);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void resume() {
		System.out.println("Resume");
	}

}
