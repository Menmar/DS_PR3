package uoc.ds.pr.model;

import edu.uoc.ds.traversal.Iterator;
import java.util.Comparator;
import uoc.ds.pr.util.DSLinkedList;

public class CatalogedCard {
    public static final Comparator<CatalogedCard> CMP = (cb1, cb2)->cb1.getCardId().compareTo(cb2.getCardId());
    public static final Comparator<CatalogedCard> CMP_BOOKID = (cb1, cb2)->cb1.getCardId().compareTo(cb2.getCardId());
    public static final Comparator<CatalogedCard> CMP_V = (cb1, cb2) -> Integer.compare(cb1.loanList.size(), cb2.loanList.size());

    public DSLinkedList<Loan> loanList;

    private StoredCard storedCard;

    private int copies;
    private boolean isLoaned;

    public CatalogedCard(StoredCard storedCard) {
        this.storedCard = storedCard;
        this.loanList = new DSLinkedList<>(Loan.CMP_ID);

    }

    public CatalogedCard(String cardId) {
        this.storedCard = new StoredCard(cardId);
    }

    public String getCardId() {
        return (storedCard !=null? storedCard.getCardId():null);
    }


    public int numLoans() {
        return this.loanList.size();
    }

    public void addLoan(Loan loan) {
        loanList.insertEnd(loan);
    }

    public Iterator<Loan> getAllLoans() {
        return loanList.values();
    }

    public StoredCard getStoredCard() {
        return storedCard;
    }

    public String getPlayer() {
        return storedCard.getPlayer();
    }

    public String getCollection() {
        return storedCard.getCollection();
    }

    public void loaned() {
        isLoaned = true;
    }

    public void unloaned() {
        isLoaned = false;
    }

    public boolean isLoaned() {
        return isLoaned;
    }
}
