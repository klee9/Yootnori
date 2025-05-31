package ui.interfaces;

import model.TossResult;

public interface BoardPanel {
    void showYutImage(TossResult tossResult);
    void removeYutImage();

    int autoClickPosition(int pid);

    int getCellSize();
    int getOffsetX();
    int getOffsetY();
}
