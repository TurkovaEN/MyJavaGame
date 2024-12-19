import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.InputStream;

public class Map {
    public static final int H = 24; // Высота карты
    public static final int W = 49; // Ширина карты
    public static final int TILE_SIZE = 32; // Размер тайла

    private Image stoneTexture; // Текстура камня
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

    // Конструктор, загружающий текстуру камня
    public Map(InputStream stonePath) {
        stoneTexture = new Image(stonePath);
    }

    // Метод для отрисовки карты
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


}