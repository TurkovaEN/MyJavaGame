import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.layout.Pane;
import javafx.geometry.Rectangle2D;
import java.io.InputStream;

public class SpecialKeyDoorInteraction extends KeyDoorInteraction {
    private String specialProperty; // Новое свойство, специфичное для этого класса

    // Конструктор, который вызывает конструктор базового класса
    public SpecialKeyDoorInteraction(InputStream keyPath, InputStream doorClosedPath, InputStream doorOpenedPath, String fontPath, String specialProperty) {
        super(keyPath, doorClosedPath, doorOpenedPath, fontPath); // Вызов конструктора базового класса
        this.specialProperty = specialProperty; // Инициализация нового свойства
    }

    // Новый метод, специфичный для SpecialKeyDoorInteraction
    public void useSpecialKey() {
        if (hasKey) {
            System.out.println("Using special key with property: " + specialProperty);
            // Логика использования специального ключа
        } else {
            System.out.println("No key available to use.");
        }
    }

    @Override
    public void handleKeyCollision() {
        super.handleKeyCollision(); // Вызов метода базового класса
        // Дополнительная логика для специального ключа
        System.out.println("Special key collected!");
    }
    @Override
    public boolean checkCollision(Rectangle2D playerRect) {
        return checkKeyCollision(playerRect) || checkDoorCollision(playerRect);
    }
}