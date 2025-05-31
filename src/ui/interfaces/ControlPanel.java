package ui.interfaces;

import model.TossResult;

public interface ControlPanel {
    void showTossButtons();
    String resultTranslate(TossResult res);
    void showResultOnly(String res);
}
