package uoc.ds.pr.util;

import static uoc.ds.pr.BaseballCards.MAX_CARD_IN_STOREDCARDCASE;

import edu.uoc.ds.adt.sequential.FiniteContainer;
import edu.uoc.ds.adt.sequential.Queue;
import edu.uoc.ds.adt.sequential.QueueArrayImpl;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.CardStatus;
import uoc.ds.pr.model.StoredCard;

public class CardWareHouse {

    StackLinkedList<QueueArrayImpl<StoredCard>> storedCards;

    public CardWareHouse() {
        storedCards = new StackLinkedList<>();
    }

    public Queue newQueue() {
        QueueArrayImpl<StoredCard> lastQueue = new QueueArrayImpl<StoredCard>(MAX_CARD_IN_STOREDCARDCASE);
        storedCards.push(lastQueue);
        return lastQueue;
    }


    public void storeCard(String cardId, String player, int publicationYear, String collection, CardStatus status) {
        storeCard(new StoredCard(cardId, player, publicationYear, collection, status));
    }
    public void storeCard(StoredCard card) {
        Queue<StoredCard> lastQueue = this.storedCards.peek();
        if (( lastQueue == null) || ((FiniteContainer)lastQueue).isFull() ) {
            lastQueue = newQueue();
        }
        lastQueue.add(card);
    }

    public Queue<StoredCard> peek(){
        return storedCards.peek();
    }

    public Queue<StoredCard> pop(){
        return storedCards.pop();
    }

    public Position getPosition(String cardId) {
        Iterator<QueueArrayImpl<StoredCard>> it = this.storedCards.values();
        boolean found = false;
        int numCardCase = 0;
        int num = 0;

        while (it.hasNext() && !found) {
            num = processQueue(cardId, it.next());
            found = num >= 0;
            if (!found) {
                numCardCase++;
            }
        }
        return (found?new Position(numCardCase, num):null);
    }

    protected int processQueue(String bookId, QueueArrayImpl<StoredCard> queue) {
        int num = -1;
        boolean found = false;
        Iterator<StoredCard> it = queue.values();
        StoredCard card = null;
        while (it.hasNext() && !found) {
            card = it.next();
            found = card.getCardId().equals(bookId);
            num++;
        }
        return (found?num:-1);


    }

    public StoredCard getCardPendingCataloging() {
        StoredCard pending = null;
        Queue<StoredCard> firstQueue = this.storedCards.peek();
        if (firstQueue.size()>1) {
            pending = firstQueue.poll();
        }
        else {
            pending = storedCards.peek().poll();
            storedCards.deleteLast();
        }
        return pending;
    }

    public int numStoredCards() {
        int numCards = 0;
        Iterator<QueueArrayImpl<StoredCard>> it = storedCards.values();
        while (it.hasNext()) {
            numCards+=it.next().size();
        }

        return (numCards);
    }

    public boolean isEmpty() {
        return (this.numStoredCards()==0);
    }

    public int numCardCases() {
        return this.storedCards.size();
    }


    public class Position {
        private int numCardCase;
        private int num;

        public Position(int numQueue, int num) {
            this.numCardCase = numQueue;
            this.num = num;
        }

        public int getCardCase() {
            return numCardCase;
        }

        public int getNum() {
            return num;
        }
    }
}
