package uoc.ds.pr.enums;

public enum LoanStatus {
        IN_PROGRESS(0),
        COMPLETED(1),
        DELAYED(2);
        private final int value;

        LoanStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
}
