package uoc.ds.pr.util;

import uoc.ds.pr.enums.CardRating;
import uoc.ds.pr.enums.CardStatus;
import uoc.ds.pr.BaseballCards;

public class CardsDataHelper {

    public static void addCardsData(BaseballCards baseballCards, String[][] cardsData) {
        StoredCard card = null;
        for (String[] cardData: cardsData) {
            baseballCards.storeCard(cardData[0], cardData[1], Integer.parseInt(cardData[2]), cardData[3], CardStatus.fromString(cardData[4]), CardRating.fromString(cardData[5]));
        }
    }

    public static String generateCardId(int number) {
        return String.format("C%03d", number);
    }
}
