package uoc.ds.pr.enums;

public enum CardRating {
    ONE_STAR(100, "★☆☆☆☆"),
    TWO_STARS(200, "★★☆☆☆"),
    THREE_STARS(300, "★★★☆☆"),
    FOUR_STARS(400, "★★★★☆"),
    FIVE_STARS(1000, "★★★★★");

    private final int value;
    private final String display;

    CardRating(int value, String display) {
        this.value = value;
        this.display = display;
    }

    public int getValue() {
        return value;
    }

    public String getDisplay() {
        return display;
    }

    public static CardRating fromString(String value) {
        return fromValue(Integer.parseInt(value));
    }

    public static CardRating fromValue(int value) {
        CardRating cardRating;

        switch (value) {
            case 1:
                cardRating = ONE_STAR;
                break;
            case 2:
                cardRating = TWO_STARS;
                break;
            case 3:
                cardRating = THREE_STARS;
                break;
            case 4:
                cardRating = FOUR_STARS;
                break;
            case 5:
                cardRating = FIVE_STARS;
                break;
            default:
                cardRating = ONE_STAR;
        }
        return cardRating;
    }
}
