import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Map {
    private static final int H = 23;
    private static final int W = 50;
    private static final int TILE_SIZE = 32;

    private Image stoneTexture;
    public static String[] tileMap = {
            "#################################################",
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

    public Map(String stonePath) {
        stoneTexture = new Image(stonePath);
    }

    public void draw(GraphicsContext gc) {
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                if (tileMap[i].charAt(j) == '#') {
                    gc.drawImage(stoneTexture, j * TILE_SIZE, i * TILE_SIZE);
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