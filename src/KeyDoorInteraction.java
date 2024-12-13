import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.layout.Pane;
import javafx.geometry.Rectangle2D;

public class KeyDoorInteraction {
    public boolean hasKey;
    public ImageView keySprite;
    public ImageView doorSprite;
    private Image keyTexture;
    private Image doorClosedTexture;
    private Image doorOpenedTexture;
    private Text keyText;



    public KeyDoorInteraction(String keyPath, String doorClosedPath, String doorOpenedPath, String fontPath) {
        hasKey = false;
        keyTexture = new Image(keyPath);
        doorClosedTexture = new Image(doorClosedPath);
        doorOpenedTexture = new Image(doorOpenedPath);

        keySprite = new ImageView(keyTexture);
        doorSprite = new ImageView(doorClosedTexture);
        keyText = new Text();
        keyText.setFont(new Font(30));
        keyText.setFill(javafx.scene.paint.Color.WHITE);
        updateKeyText();

        // Initial positions of key and door
        keySprite.setLayoutX(1420);
        keySprite.setLayoutY(160);
        doorSprite.setLayoutX(1280);
        doorSprite.setLayoutY(550);
    }

    public void updateKeyText() {
        String keyStatus = hasKey ? "yes" : "no";
        keyText.setText("Key: " + keyStatus);
    }

    public void draw(Pane pane) {
        pane.getChildren().addAll(keySprite, doorSprite, keyText);
    }

    public boolean checkKeyCollision(Rectangle2D playerRect) {
        Rectangle2D keyBounds = new Rectangle2D(
                keySprite.getLayoutX(),
                keySprite.getLayoutY(),
                keySprite.getImage().getWidth(),
                keySprite.getImage().getHeight()
        );
        return keyBounds.intersects(playerRect);
        //return keySprite.getBoundsInParent().intersects(playerRect);
    }

    public void handleKeyCollision() {
        hasKey = true;
        updateKeyText();
        keySprite.setLayoutX(-100); // Remove key from screen
        doorSprite.setImage(doorOpenedTexture);
    }

    public boolean checkDoorCollision(Rectangle2D playerRect) {
        Rectangle2D doorBounds = new Rectangle2D(
                doorSprite.getLayoutX(),
                doorSprite.getLayoutY(),
                doorSprite.getImage().getWidth(),
                doorSprite.getImage().getHeight()
        );
        return doorBounds.intersects(playerRect);
        // return doorSprite.getBoundsInParent().intersects(playerRect);
    }

    public boolean isDoorOpen() {
        return hasKey;
    }

    public void showWinMessage(Pane pane) {
        Text winText = new Text("You completed the level! Continuing the game is in development...");
        winText.setFont(new Font(32));
        winText.setFill(javafx.scene.paint.Color.GREEN);
        winText.setLayoutX((pane.getWidth() - winText.getLayoutBounds().getWidth()) / 2);
        winText.setLayoutY((pane.getHeight() - winText.getLayoutBounds().getHeight()) / 2);
        pane.getChildren().add(winText);
        // Pause for 3 seconds
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