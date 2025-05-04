package model;

public class RuleSet {
    public boolean checkToss(int toss_result) {
        return true;
    }

    public boolean checkMove(int move_result) {
        return true;
    }

    public boolean canStack(Token curr_token, Token next_token) {
        return true;
    }

    public boolean canCapture(Token curr_token, Token next_token) {
        return true;
    }
}