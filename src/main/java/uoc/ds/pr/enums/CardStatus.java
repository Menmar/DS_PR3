package uoc.ds.pr.enums;

public enum CardStatus {
        MINT(9),
        NEAR_MINT(8),
        VERY_GOOD(7),
        GOOD(6),
        PLAYED(5),
        POOR(4),
        VERY_POOR(3);
        private final int value;

        CardStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static CardStatus fromString(String value) {
            return fromValue(Integer.parseInt(value));
        }

        public static CardStatus fromValue(int value) {
            for (CardStatus status : CardStatus.values()) {
                if (status.getValue() == value) {
                    return status;
                }
            }
            throw new IllegalArgumentException("There is no Status with value:  " + value);
        }
}
