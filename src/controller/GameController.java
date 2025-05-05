package controller;

/*UI 테스트 위해서 임시로 구현함--*/
import java.util.Random;
import model.TossResult;
/*--UI 테스트 위해서 임시로 구현함*/

public interface GameController {
    void startGame(int playerCount, int tokenCount, String shapeType);

    /*UI 테스트 위해서 임시로 구현함--*/
    Random random = new Random();

    public default TossResult RandomToss() {
        int index = random.nextInt(TossResult.values().length);
        return TossResult.values()[index];
    }

    public default TossResult SpecificToss(TossResult result) {
        return result; // 그대로 반환
    }
    /*--UI 테스트 위해서 임시로 구현함*/
}