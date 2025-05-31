package uoc.ds.pr.repository;

import java.time.LocalDate;
import uoc.ds.pr.BaseballCards;
import uoc.ds.pr.BaseballCardsHelper;
import uoc.ds.pr.exceptions.LoanNotFoundException;
import uoc.ds.pr.model.CatalogedCard;
import uoc.ds.pr.model.Entity;
import uoc.ds.pr.model.Loan;
import uoc.ds.pr.util.DSLinkedList;

public class LoanRepository {

    protected DSLinkedList<Loan> loanList;
    private final BaseballCards baseballCards;
    private final BaseballCardsHelper helper;

    public LoanRepository(BaseballCards baseballCards) {
        this.baseballCards = baseballCards;
        this.helper = baseballCards.getBaseballCardsHelper();
        this.loanList = new DSLinkedList<>(Loan.CMP_ID);
    }

    public void addLoan(Loan loan) {
        loanList.insertEnd(loan);
        CatalogedCard catalogedCard = loan.getCatalogedCard();
        catalogedCard.loaned();
    }

    public int numLoans() {
        return loanList.size();
    }

    public Loan addNewLoan(Entity entity, String loanId, CatalogedCard catalogedCard, LocalDate date,
        LocalDate expirationDate) {
        Loan loan = new Loan(loanId, entity, catalogedCard, date, expirationDate);
        addLoan(loan);
        return loan;
    }


    public Loan getLoan(String loanId) {
        Loan loan = loanList.get(new Loan(loanId));
        return loan;
    }

    public Loan getLoanOrThrow(String loanId) throws LoanNotFoundException {
        Loan loan = getLoan(loanId);
        if (loan == null) {
            throw new LoanNotFoundException();
        }
        return loan;
    }
}
