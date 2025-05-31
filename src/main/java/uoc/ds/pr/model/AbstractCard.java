package uoc.ds.pr.model;

import uoc.ds.pr.enums.CardRating;
import uoc.ds.pr.enums.CardStatus;

public abstract class AbstractCard {

    private final String cardId;
    private String player;
    private int publicationYear;
    private String collection;
    private CardStatus status;
    private CardRating rating;

    public AbstractCard(String cardId) {
        this.cardId = cardId;
    }

    public AbstractCard(String cardId, String player, int publicationYear, String collection, CardStatus status,
        CardRating rating) {
        this(cardId);
        this.player = player;
        this.publicationYear = publicationYear;
        this.collection = collection;
        this.status = status;
        this.rating = rating;
    }

    public String getCardId() {
        return cardId;
    }

    public String getPlayer() {
        return player;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public String getCollection() {
        return collection;
    }

    public CardStatus getStatus() {
        return status;
    }

    public CardRating getCardRating() {
        return rating;
    }

    public void setCardRating(CardRating rating) {
        this.rating = rating;
    }
}
