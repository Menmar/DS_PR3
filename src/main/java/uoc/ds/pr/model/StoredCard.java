package uoc.ds.pr.model;

import java.time.LocalDate;
import uoc.ds.pr.enums.CardRating;
import uoc.ds.pr.enums.CardStatus;

public class StoredCard extends AbstractCard {

    private LocalDate cardStorageDate;

    public StoredCard(String cardId, String player, int publicationYear, String collection, CardStatus status,
        CardRating rating) {
        super(cardId, player, publicationYear, collection, status, rating);
    }

    public StoredCard(String cardId) {
        super(cardId);
    }

    public String getCardId() {
        return super.getCardId();
    }
}
