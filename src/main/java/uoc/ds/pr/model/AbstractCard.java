package uoc.ds.pr.model;

import uoc.ds.pr.CardStatus;

public abstract class AbstractCard {

    private String cardId;
    private String player;
    private int publicationYear;
    private String collection;
    private CardStatus status;

    public AbstractCard(String cardId) {
        this.cardId = cardId;
    }

    public AbstractCard(String cardId, String player, int publicationYear, String collection, CardStatus status) {
        this(cardId);
        this.player = player;
        this.publicationYear = publicationYear;
        this.collection = collection;
        this.status = status;
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
}
