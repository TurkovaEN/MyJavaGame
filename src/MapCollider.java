import javafx.geometry.Rectangle2D;

public class MapCollider {
    public boolean checkCollision(Rectangle2D rect, int dir) {
        for (int i = (int) rect.getMinY() / Map.TILE_SIZE; i < (rect.getMaxY() / Map.TILE_SIZE); i++) {
            for (int j = (int) rect.getMinX() / Map.TILE_SIZE; j < (rect.getMaxX() / Map.TILE_SIZE); j++) {
                if (i >= 0 && i < Map.H && j >= 0 && j < Map.W && Map.tileMap[i].charAt(j) == '#') {
                    return true;
                }
            }
        }
        return false;
    }

    public void handleCollision(Rectangle2D rect, double[] dxdy, boolean[] onGround, int dir) {
        for (int i = (int) rect.getMinY() / Map.TILE_SIZE; i < (rect.getMaxY() / Map.TILE_SIZE); i++) {
            for (int j = (int) rect.getMinX() / Map.TILE_SIZE; j < (rect.getMaxX() / Map.TILE_SIZE); j++) {
                if (i >= 0 && i < Map.H && j >= 0 && j < Map.W && Map.tileMap[i].charAt(j) == '#') {
                    if (dir == 0) { // Horizontal collision
                        if (dxdy[0] > 0) rect.setRect(j * Map.TILE_SIZE - rect.getWidth(), rect.getMinY(), rect.getWidth(), rect.getHeight());
                        if (dxdy[0] < 0) rect.setRect(j * Map.TILE_SIZE + Map.TILE_SIZE, rect.getMinY(), rect.getWidth(), rect.getHeight());
                        dxdy[0] = 0;
                    } else { // Vertical collision
                        if (dxdy[1] > 0) {
                            rect.setRect(rect.getMinX(), i * Map.TILE_SIZE - rect.getHeight(), rect.getWidth(), rect.getHeight());
                            dxdy[1] = 0;
                            onGround[0] = true;
                        }
                        if (dxdy[1] < 0) {
                            rect.setRect(rect.getMinX(), i * Map.TILE_SIZE + Map.TILE_SIZE, rect.getWidth(), rect.getHeight());
                            dxdy[1] = 0;
                        }
                    }
                    break;
                }
            }
        }
    }
}