package uoc.ds.pr.repository;

import edu.uoc.ds.adt.sequential.Queue;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.BaseballCards;
import uoc.ds.pr.BaseballCardsHelper;
import uoc.ds.pr.CardStatus;
import uoc.ds.pr.exceptions.CatalogedCardNotFoundException;
import uoc.ds.pr.model.CatalogedCard;
import uoc.ds.pr.model.Collection;
import uoc.ds.pr.model.Loan;
import uoc.ds.pr.model.StoredCard;
import uoc.ds.pr.util.CardWareHouse;
import uoc.ds.pr.util.DSLinkedList;

public class CardRepository {
    private BaseballCards baseballCards;
    private BaseballCardsHelper helper;
    protected DSLinkedList<CatalogedCard> catalogedCards;
    protected CardWareHouse cardWareHouse;
    protected DSLinkedList<Collection> collections;
    protected CatalogedCard catalogedCardMostExposing;

    public CardRepository(BaseballCards baseballCards) {
        this.baseballCards = baseballCards;
        this.helper = baseballCards.getBaseballCardsHelper();
        this.catalogedCards = new DSLinkedList<>(CatalogedCard.CMP);
        this.cardWareHouse = new CardWareHouse();
        collections = new DSLinkedList<>(Collection.CMP_ID);
    }

    public CatalogedCard catalogCard() {
        StoredCard storedCard = cardWareHouse.getCardPendingCataloging();
        CatalogedCard catalogedCard = catalogedCards.get(new CatalogedCard(storedCard));
        catalogedCard = new CatalogedCard(storedCard);
        catalogedCards.insertEnd(catalogedCard);
        return catalogedCard;
    }


    public boolean isEmpty() {
        return cardWareHouse.isEmpty();
    }

    public void storecard(String cardId, String player, int publicationYear, String collection, CardStatus status) {
        cardWareHouse.storeCard(cardId, player, publicationYear, collection, status);
    }

    public int numStoredCards() {
        return cardWareHouse.numStoredCards();
    }

    public int numCardCases() {
        return cardWareHouse.numCardCases();
    }

    public int numCatalogedCards() {
        return catalogedCards.size();
    }

    public CatalogedCard getCatalogedCard(String cardId) {
        return catalogedCards.get(new CatalogedCard(cardId));
    }

    public CatalogedCard getCatalogedCardOrThrow(String cardId) throws CatalogedCardNotFoundException {
        CatalogedCard catalogedCard =  catalogedCards.get(new CatalogedCard(cardId));
        if (catalogedCard == null)  {
            throw new CatalogedCardNotFoundException();
        }
        return catalogedCard;
    }

    public void updateCardMostExposing(CatalogedCard newCatalogedCard) {
        if (catalogedCardMostExposing == null) {
            catalogedCardMostExposing = newCatalogedCard;
        }
        else {
            if (catalogedCardMostExposing.numLoans() < newCatalogedCard.numLoans()){
                catalogedCardMostExposing = newCatalogedCard;
            }
        }
    }

    public CatalogedCard getMostShownCard() {
        return catalogedCardMostExposing;
    }

    public int numLoansByCard(String cardId) {
        CatalogedCard catalogedCard = getCatalogedCard(cardId);
        return (catalogedCard!=null?catalogedCard.numLoans():0);
    }

    public void addNewLoan(CatalogedCard catalogedCard, Loan loan) {
        catalogedCard.addLoan(loan);
    }

    public Iterator<Loan> getLoansByCard(String cardId) {
        CatalogedCard catalogedCard = getCatalogedCard(cardId);
        return (catalogedCard!=null?catalogedCard.getAllLoans():null);
    }

    public CardWareHouse.Position getPosition(String cardId) {
        return cardWareHouse.getPosition(cardId);
    }

    public int getNumCardsInLastCase() {
        Queue<StoredCard> q = cardWareHouse.peek();
        return (q!=null?q.size():0);
    }
}
