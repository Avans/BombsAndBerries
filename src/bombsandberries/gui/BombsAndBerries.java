package bombsandberries.gui;

import org.lwjgl.opengl.GL11;

import bombsandberries.Berry;
import bombsandberries.Bomb;
import bombsandberries.server.Explosion;
import bombsandberries.server.Game;
import bombsandberries.server.Server;
import bombsandberries.server.ServerPlayer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Main game class for libgdx
 * 
 * @author Paul Wagener
 */
public class BombsAndBerries implements ApplicationListener {

	private Game game;
	private OrthographicCamera camera;
	private SpriteBatch batch;

	private Texture grassTile;

	private Texture smileyTexture;
	private Texture bombTexture;
	private Texture berryTexture;
	private Texture explosionTexture;

	public static final int WIDTH = 800;
	public static final int HEIGHT = 800;

	private BitmapFont font;

	@Override
	public void create() {
		game = new Game();

		// Start server thread
		new Thread(new Server(game)).start();

		// start gameticks thread
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

		grassTile = new Texture(Gdx.files.internal("res/grasstile.png"));
		smileyTexture = new Texture(Gdx.files.internal("res/smiley.png"));
		bombTexture = new Texture(Gdx.files.internal("res/bomb.png"));
		explosionTexture = new Texture(Gdx.files.internal("res/explosion.png"));
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
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		camera.update();
		final int tileWidth = WIDTH / Game.WIDTH;
		final int tileHeight = HEIGHT / Game.HEIGHT;
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		// Draw background tiles
		for (int x = 0; x < Game.WIDTH; x++) {
			for (int y = 0; y < Game.HEIGHT; y++) {
				drawAtPosition(batch, grassTile, x, y);
			}
		}

		synchronized (game) {

			// Draw bombs
			for (Bomb bomb : game.getBombs()) {
				drawAtPosition(batch, bombTexture, bomb.getX(), bomb.getY());
			}

			// Draw berries
			for (Berry berry : game.getBerries()) {
				drawAtPosition(batch, berryTexture, berry.getX(), berry.getY());
			}

			// Draw players
			for (ServerPlayer player : game.getPlayers()) {
				// Smooth to actual position
				player.setAnimatedX(player.getAnimatedX()
						+ (player.getX() - player.getAnimatedX()) * 0.15);
				player.setAnimatedY(player.getAnimatedY()
						+ (player.getY() - player.getAnimatedY()) * 0.15);

				drawAtPosition(batch, smileyTexture,
						(float) player.getAnimatedX(),
						(float) player.getAnimatedY());
				font.draw(batch, player.getScore() + " - " + player.getName(),
						(int) (player.getAnimatedX() * tileWidth), HEIGHT
								- (int) (player.getAnimatedY() * tileHeight));
			}

			// Draw explosions
			for (Explosion explosion : game.getExplosions()) {
				drawAtPosition(batch, explosionTexture, explosion.getX(),
						explosion.getY());
			}
		}

		batch.end();
	}

	private void drawAtPosition(SpriteBatch batch, Texture texture, float x,
			float y) {

		final int tileHeight = HEIGHT / Game.HEIGHT;
		final int tileWidth = WIDTH / Game.WIDTH;
		batch.draw(texture, x * tileWidth,
				HEIGHT - y * tileHeight - tileHeight, tileWidth, tileHeight);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void resume() {
		System.out.println("Resume");
	}

}
