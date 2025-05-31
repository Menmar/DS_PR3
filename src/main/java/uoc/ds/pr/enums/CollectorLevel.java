package uoc.ds.pr.enums;

public enum CollectorLevel {
        BRONZE(0),
        SILVER(1),
        GOLD(2),
        PLATINUM(3),
        DIAMOND(4);
        private final int value;

        CollectorLevel(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
}
