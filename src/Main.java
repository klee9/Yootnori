import controller.GameController;
import model.Game;
import ui.fx.MainWindowFX;
import ui.swing.MainWindowSwing;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
         runSwing();
        // runFX();
    }

    private static void runSwing() {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            MainWindowSwing ui = new MainWindowSwing(); // UI 생성
            GameController controller = new GameController(ui, game); // Controller 생성
            ui.setController(controller); // UI에 Controller 연결
            ui.setVisible(true); // 화면 띄우기
        });
    }

    private static void runFX() {
        MainWindowFX ui = new MainWindowFX();
        ui.run();
    }
}
