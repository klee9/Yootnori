package model;

public enum TossResult {
    BACKDO(-1),
    DO(1),
    GAE(2),
    GEOL(3),
    YUT(4),
    MO(5);

    private final int value;

    TossResult(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
