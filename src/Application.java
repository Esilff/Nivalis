import nivalis.engine.window.Window;
import nivalis.tools.game.Game;

public class Application {
    public static void main(String[] args) {
        Game game = new Game("Nivalis");
        game.setScene(new TestScene(game));
        game.run();
    }
}
