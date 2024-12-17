import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.layout.Pane;
import javafx.geometry.Rectangle2D;

import java.io.InputStream;

public class KeyDoorInteraction {
    public boolean hasKey;
    public ImageView keySprite;
    public ImageView doorSprite;
    private Image keyTexture;
    private Image doorClosedTexture;
    private Image doorOpenedTexture;
    private Text keyText;

    // Конструктор, загружающий изображения ключа и двери
    public KeyDoorInteraction(InputStream keyPath, InputStream doorClosedPath, InputStream doorOpenedPath, String fontPath) {
        hasKey = false;
        keyTexture = new Image(keyPath);
        doorClosedTexture = new Image(doorClosedPath);
        doorOpenedTexture = new Image(doorOpenedPath);

        // Инициализация изображений для ключа и двери
        keySprite = new ImageView(keyTexture);
        doorSprite = new ImageView(doorClosedTexture);

        // Инициализация текста для отображения состояния ключа
        keyText = new Text();
        keyText.setFont(new Font(30));
        keyText.setFill(javafx.scene.paint.Color.WHITE);
        updateKeyText();

        // Начальные позиции для ключа и двери
        keySprite.setLayoutX(1420);
        keySprite.setLayoutY(160);
        doorSprite.setLayoutX(1280);
        doorSprite.setLayoutY(550);
    }

    // Метод для обновления текста состояния ключа
    public void updateKeyText() {
        String keyStatus = hasKey ? "yes" : "no";
        keyText.setText("Key: " + keyStatus);
    }

    // Метод для добавления ключа и двери на панель
    public void draw(Pane pane) {
        pane.getChildren().addAll(keySprite, doorSprite, keyText);
    }

    // Метод для проверки столкновения с ключом
    public boolean checkKeyCollision(Rectangle2D playerRect) {
        Rectangle2D keyBounds = new Rectangle2D(
                keySprite.getLayoutX(),
                keySprite.getLayoutY(),
                keySprite.getImage().getWidth(),
                keySprite.getImage().getHeight()
        );
        return keyBounds.intersects(playerRect);
    }

    // Метод для обработки столкновения с ключом
    public void handleKeyCollision() {
        hasKey = true;
        updateKeyText();
        keySprite.setLayoutX(-100); // Убираем ключ с экрана
        doorSprite.setImage(doorOpenedTexture); // Меняем изображение двери на открытую
    }

    // Метод для проверки столкновения с дверью
    public boolean checkDoorCollision(Rectangle2D playerRect) {
        Rectangle2D doorBounds = new Rectangle2D(
                doorSprite.getLayoutX(),
                doorSprite.getLayoutY(),
                doorSprite.getImage().getWidth(),
                doorSprite.getImage().getHeight()
        );
        return doorBounds.intersects(playerRect);
    }

    // Метод для проверки, открыта ли дверь
    public boolean isDoorOpen() {
        return hasKey;
    }

    // Метод для отображения сообщения о победе
    public void showWinMessage(Pane pane) {
        Text winText = new Text("You completed the level! Continuing the game is in development...");
        winText.setFont(new Font(32));
        winText.setFill(javafx.scene.paint.Color.GREEN);
        winText.setLayoutX((pane.getWidth() - winText.getLayoutBounds().getWidth()) / 2);
        winText.setLayoutY((pane.getHeight() - winText.getLayoutBounds().getHeight()) / 2);
        pane.getChildren().add(winText);
        // Пауза на 3 секунды
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pane.getChildren().remove(winText);
        }).start();
    }
}