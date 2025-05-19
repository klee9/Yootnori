package ui.interfaces;

public interface TokenPanel {
    void setClickable(boolean clickable);
    void updateTokenPosition(int tid, int pid);
    void updateTokenPosition(int tid, double x, double y);
    int getClickedToken(Object e);
    int getCurrentToken();
}
