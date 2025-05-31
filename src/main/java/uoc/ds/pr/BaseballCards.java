package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.enums.CardRating;
import uoc.ds.pr.enums.CardStatus;
import uoc.ds.pr.enums.LoanStatus;
import uoc.ds.pr.enums.WorkerRole;

import java.time.LocalDate;


public interface BaseballCards {
    static final int MAX_NUM_ENTITIES = 25;
    static final int MAX_NUM_WORKERS = 35;
    static final int MAX_CARD_IN_STOREDCARDCASE = 50;
    static final int MAX_LOANS_BY_ENTITY = 100;



    void addEntity(String entityId, String name, String address);

    void addWorker(String workerId, String name, String surname, WorkerRole role);

    void storeCard(String cardId, String playerId, int publicationYear, String collection, CardStatus state, CardRating cardRating);

    CatalogedCard catalogCard(String workerId) throws NoCardException, WorkerNotFoundException, WorkerNotAllowedException;
    Loan lendCard(String loanId, String entityId, String cardId, String workerId, LocalDate date, LocalDate expirationDate)
         throws EntityNotFoundException, CatalogedCardNotFoundException, WorkerNotFoundException,
            NoCardException, CatalogedCardAlreadyLoanedException, MaximumNumberOfLoansException,
            WorkerNotAllowedException, CatalogedCardAlreadyAuctionedException;
    Loan givebackCard(String loanId, LocalDate date) throws LoanNotFoundException;
    int timeToBeCataloged(String cardId, int boxPreparationTime, int cardCatalogTime) throws StoredCardNotFoundException, InvalidLotPreparationTimeException, InvalidCatalogTimeException;
    Iterator<CatalogedCard> getAllCardsByPlayer(String player) throws NoCardException;
    Iterator<CatalogedCard> getAllCardsByCollection(String collection) throws NoCardException;
    Iterator<Loan> getAllLoansByEntity(String entityId) throws NoLoanException;
    Iterator<Loan> getAllLoansByState(String entityId, LoanStatus state) throws NoLoanException;
    Iterator<Loan> getAllLoansByCard(String cardId) throws NoLoanException;
    Entity getEntityTheMost() throws NoEntityException;
    CatalogedCard getMostShownCard() throws NoCardException;
    BaseballCardsHelper getBaseballCardsHelper();
}


