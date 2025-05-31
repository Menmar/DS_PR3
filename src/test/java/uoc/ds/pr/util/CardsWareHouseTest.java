package uoc.ds.pr.util;

import edu.uoc.ds.adt.sequential.Queue;
import edu.uoc.ds.traversal.Iterator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uoc.ds.pr.enums.CardRating;
import uoc.ds.pr.enums.CardStatus;
import uoc.ds.pr.model.StoredCard;


public class CardsWareHouseTest {


    private CardWareHouse cardWareHouse;

    @Before
    public void setUp() {
        cardWareHouse = new CardWareHouse();
    }


    @Test
    public void storeCardTest() {
        Assert.assertEquals(0, cardWareHouse.numStoredCards());

        addCardsData(CardsData.baseballCards0);
        addCardsData(CardsData.baseballCards1);
        addCardsData(CardsData.baseballCards2);
        addCardsData(CardsData.baseballCards3);
        Assert.assertEquals(250, cardWareHouse.numStoredCards());
        Assert.assertEquals(5, cardWareHouse.numCardCases());


        cardWareHouse.storeCard("C201", "Julio Rodríguez", 2022, "Topps Chrome", CardStatus.GOOD, CardRating.THREE_STARS);
        cardWareHouse.storeCard("C202", "Spencer Torkelson", 2022, "Topps Chrome", CardStatus.MINT, CardRating.THREE_STARS);
        cardWareHouse.storeCard("C203", "Adley Rutschman", 2023, "Topps Chrome", CardStatus.POOR, CardRating.ONE_STAR);
        cardWareHouse.storeCard("C204", "Kenta Maeda", 2023, "Topps Chrome", CardStatus.POOR, CardRating.ONE_STAR);

        Assert.assertEquals(6, cardWareHouse.numCardCases());
        Assert.assertEquals(254, cardWareHouse.numStoredCards());
    }

    @Test
    public void storeCardTest2() {
        storeCardTest();
        Assert.assertEquals(6, cardWareHouse.numCardCases());
        Assert.assertEquals(254, cardWareHouse.numStoredCards());

        Queue<StoredCard> theQueue = cardWareHouse.pop();
        Iterator<StoredCard> it1 = theQueue.values();
        Assert.assertEquals("C201", it1.next().getCardId());
        Assert.assertEquals("C202", it1.next().getCardId());
        Assert.assertEquals("C203", it1.next().getCardId());

        Queue<StoredCard> theQueue2 = cardWareHouse.pop();
        Iterator<StoredCard> it2 = theQueue2.values();
        Assert.assertEquals("C151", it2.next().getCardId());
        Assert.assertEquals("C152", it2.next().getCardId());
        Assert.assertEquals("C153", it2.next().getCardId());

        Queue<StoredCard> theQueue3 = cardWareHouse.pop();
        Iterator<StoredCard> it3 = theQueue3.values();
        Assert.assertEquals("C101", it3.next().getCardId());
        Assert.assertEquals("C102", it3.next().getCardId());
        Assert.assertEquals("C103", it3.next().getCardId());

        Queue<StoredCard> theQueue4 = cardWareHouse.pop();
        Iterator<StoredCard> it4 = theQueue4.values();
        Assert.assertEquals("C051", it4.next().getCardId());
        Assert.assertEquals("C052", it4.next().getCardId());
        Assert.assertEquals("C053", it4.next().getCardId());

        Queue<StoredCard> theQueue5 = cardWareHouse.pop();
        Iterator<StoredCard> it5 = theQueue5.values();
        Assert.assertEquals("C001", it5.next().getCardId());
        Assert.assertEquals("C002", it5.next().getCardId());
        Assert.assertEquals("C003", it5.next().getCardId());

        Assert.assertEquals(50, cardWareHouse.numStoredCards());
        Assert.assertEquals(1, cardWareHouse.numCardCases());


    }

    @Test
    public void catalogingTest() {
        storeCardTest();
        Assert.assertEquals(6, cardWareHouse.numCardCases());
        Assert.assertEquals(254, cardWareHouse.numStoredCards());

        StoredCard c1 = cardWareHouse.getCardPendingCataloging();
        Assert.assertEquals(6, cardWareHouse.numCardCases());
        Assert.assertEquals(253, cardWareHouse.numStoredCards());

        StoredCard c2 = cardWareHouse.getCardPendingCataloging();
        Assert.assertEquals(6, cardWareHouse.numCardCases());
        Assert.assertEquals(252, cardWareHouse.numStoredCards());

        StoredCard c = null;
        for (int i=0; i<8; i++) {
            c = cardWareHouse.getCardPendingCataloging();
        }

        Assert.assertEquals(5, cardWareHouse.numCardCases());
        Assert.assertEquals(244, cardWareHouse.numStoredCards());
    }

    @Test
    public void getPositionTest() {
        storeCardTest();

        Assert.assertEquals(6, cardWareHouse.numCardCases());
        Assert.assertEquals(254, cardWareHouse.numStoredCards());

        CardWareHouse.Position position = cardWareHouse.getPosition("C201");
        Assert.assertEquals(5, position.getCardCase());
        Assert.assertEquals(0, position.getNum());

        position = cardWareHouse.getPosition("C202");
        Assert.assertEquals(5, position.getCardCase());
        Assert.assertEquals(1, position.getNum());

        position = cardWareHouse.getPosition("C203");
        Assert.assertEquals(5, position.getCardCase());
        Assert.assertEquals(2, position.getNum());

        position = cardWareHouse.getPosition("C204");
        Assert.assertEquals(5, position.getCardCase());
        Assert.assertEquals(3, position.getNum());

        position = cardWareHouse.getPosition("C151");
        Assert.assertEquals(4, position.getCardCase());
        Assert.assertEquals(0, position.getNum());

        position = cardWareHouse.getPosition("C152");
        Assert.assertEquals(4, position.getCardCase());
        Assert.assertEquals(1, position.getNum());

        position = cardWareHouse.getPosition("C153");
        Assert.assertEquals(4, position.getCardCase());
        Assert.assertEquals(2, position.getNum());

        position = cardWareHouse.getPosition("C154");
        Assert.assertEquals(4, position.getCardCase());
        Assert.assertEquals(3, position.getNum());

    }

    private void addCardsData(String[][] cardsData) {
        StoredCard card = null;
        for (String[] cardData: cardsData) {
            card = new StoredCard(cardData[0], cardData[1], Integer.parseInt(cardData[2]), cardData[3], CardStatus.fromString(cardData[4]), CardRating.fromString(cardData[5]));
            cardWareHouse.storeCard(card);
        }
    }

}
