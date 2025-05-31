package uoc.ds.pr.util;

import uoc.ds.pr.BaseballCardsPR3;

public class CardCollectorHelper {
    public static void addCardsCollectorData(BaseballCardsPR3 baseballCards, String[][] cardsCollectorData) {
        StoredCard card = null;
        CardCollector cardCollector= null;
        for (String[] cardCollectorData: cardsCollectorData) {
            cardCollector = baseballCards.addCollector(cardCollectorData[0], cardCollectorData[1], cardCollectorData[2],
                    DateUtils.createLocalDate(cardCollectorData[3]), Double.parseDouble(cardCollectorData[4]));

            cardCollector.setPoints(Integer.parseInt(cardCollectorData[5]));
        }
    }
}