import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.InputStream;

public class Map {
    public static final int H = 24;
    public static final int W = 49;
    public static final int TILE_SIZE = 32;

    private Image stoneTexture;
    public static String[] tileMap = {
            "#################################################",
            "#                                               #",
            "#                                               #",
            "#                                               #",
            "#                                               #",
            "#                                               #",
            "#                                               #",
            "#########                              ##########",
            "#                                               #",
            "#                                               #",
            "#                                               #",
            "#                                               #",
            "#                                               #",
            "#           #######         #########           #",
            "#                                               #",
            "#                                               #",
            "#                                               #",
            "#                                               #",
            "#                    ####                       #",
            "#                                               #",
            "#                                               #",
            "#                                               #",
            "#                                               #",
            "#################################################"
    };

    public Map(InputStream stonePath) {
        stoneTexture = new Image(stonePath);
    }
    public void draw(GraphicsContext gc) {
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                if (tileMap[i].charAt(j) == '#') {
                    // Масштабирование текстуры под размер TILE_SIZE
                    gc.drawImage(stoneTexture, 0, 0, stoneTexture.getWidth(), stoneTexture.getHeight(),
                            j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }

    public int getWidth() {
        return W * TILE_SIZE;
    }

    public int getHeight() {
        return H * TILE_SIZE;
    }
}