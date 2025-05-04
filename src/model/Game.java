package model;

import java.util.List;

public class Game {
    private List<Player> players;
    private Board board;
    private int current_player_id;
    private RuleSet rules;
    private boolean isGameOver;

    public Player getCurrentPlayer() {
        return players.get(current_player_id);
    }

    public void nextTurn(Player next_player) {
        current_player_id = next_player.getId();
    }

    public MoveResult applyMoveTo(Token token, int move) {
        return new MoveResult(); // TODO
    }
}