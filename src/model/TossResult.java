package model;

public enum TossResult {
    BACKDO(-1),
    STOP(0),
    DO(1),
    GAE(2),
    GEOL(3),
    YUT(4),
    MO(5);

    private final int value;

    // Constructor
    TossResult(int value) {
        this.value = value;
    }

    // Getter
    public int getValue() {
        return value;
    }
}
