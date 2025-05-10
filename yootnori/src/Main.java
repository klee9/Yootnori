//import controller.GameController;
//import controller.GameControllerImpl;
//import ui.MainWindow;
//
//import javax.swing.SwingUtilities;
//
//public class Main {
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            MainWindow ui = new MainWindow(); // UI 생성
//            GameController controller = new GameControllerImpl(ui); // Controller 생성
//            ui.setController(controller); // UI에 Controller 연결
//            ui.setVisible(true); // 화면 띄우기
//        });
//    }
//}

import controller.GameController;
import model.Game;
import model.Position;
import model.Board;
import model.RuleSet;
import ui.MainWindow;

import javax.swing.SwingUtilities;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            MainWindow ui = new MainWindow(); // UI 생성
            GameController controller = new GameController(ui, game); // Controller 생성
            ui.setController(controller); // UI에 Controller 연결
            ui.setVisible(true); // 화면 띄우기
        });
    }
}