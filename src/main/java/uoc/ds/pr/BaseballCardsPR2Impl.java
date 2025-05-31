package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.enums.CardRating;
import uoc.ds.pr.enums.CardStatus;
import uoc.ds.pr.enums.LoanStatus;
import uoc.ds.pr.enums.WorkerRole;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;
import uoc.ds.pr.repository.*;
import uoc.ds.pr.util.CardWareHouse;

import java.time.LocalDate;


public class BaseballCardsPR2Impl implements BaseballCards {
    protected EntityRepository entityRepository;
    protected PlayerRepository playerRepository;
    protected CardRepository cardRepository;
    protected WorkerRepository workerRepository;
    protected CollectionRepository collectionRepository;
    protected LoanRepository loanRepository;

    public BaseballCardsPR2Impl() {
        entityRepository = new EntityRepository(this);
        playerRepository = new PlayerRepository(this);
        cardRepository = new CardRepository(this);
        workerRepository = new WorkerRepository(this);
        collectionRepository = new CollectionRepository(this);
        loanRepository = new LoanRepository(this);
    }

    @Override
    public void addEntity(String entityId, String name, String address) {
        entityRepository.addEntity(entityId, name, address);
    }

    public void addWorker(String id, String name, String surname, WorkerRole role) {
        workerRepository.addWorker(id, name, surname);
    }


    @Override
    public void storeCard(String cardId, String player, int publicationYear, String collection, CardStatus status,
        CardRating rating){
        cardRepository.storecard(cardId, player, publicationYear, collection, status);
    }

    @Override
    public CatalogedCard catalogCard(String workerId)
        throws NoCardException, WorkerNotFoundException, WorkerNotAllowedException {

        if (!workerRepository.exist(workerId)) {
            throw new WorkerNotFoundException();
        }
        if (cardRepository.isEmpty()) {
            throw new NoCardException();
        }

        CatalogedCard catalogedCard = cardRepository.catalogCard();
        workerRepository.addCataloguedCard(workerId, catalogedCard);
        playerRepository.addCatalogedCard(catalogedCard);
        collectionRepository.addCatalogedCard(catalogedCard);

        return catalogedCard;
    }



    @Override
    public Loan lendCard(String loanId, String entityId, String cardId, String workerId, LocalDate date, LocalDate expirationDate)
        throws EntityNotFoundException, CatalogedCardNotFoundException,
        WorkerNotFoundException, NoCardException, CatalogedCardAlreadyLoanedException, MaximumNumberOfLoansException, WorkerNotAllowedException, CatalogedCardAlreadyAuctionedException {

        Entity entity = entityRepository.getEntityOrThrow(entityId);

        if (entity.numCurrentLoans()>=MAX_LOANS_BY_ENTITY) {
            throw new MaximumNumberOfLoansException();
        }
        Worker worker = workerRepository.getWorkerOrThrow(workerId);
        CatalogedCard catalogedCard = cardRepository.getCatalogedCardOrThrow(cardId);
        if (catalogedCard.isLoaned()) {
            throw new CatalogedCardAlreadyLoanedException();
        }
        Loan loan = loanRepository.addNewLoan(entity, loanId, catalogedCard, date, expirationDate);
        entityRepository.addNewLoan(loan);
        workerRepository.addNewLoan(worker, loan);
        entityRepository.updateEntityMostExposing(entity);
        cardRepository.addNewLoan(catalogedCard, loan);
        cardRepository.updateCardMostExposing(catalogedCard);
        return loan;
    }

    @Override
    public Loan givebackCard(String loanId, LocalDate date) throws LoanNotFoundException {
        Loan loan = loanRepository.getLoanOrThrow(loanId);
        Worker worker = loan.getWorker();

        if (loan.isDelayed(date)) {
            loan.setStatus(LoanStatus.DELAYED);
        }
        else {
            loan.setStatus(LoanStatus.COMPLETED);
        }

        worker.addClosedLoan(loan);
        entityRepository.addNewClosedLoan(loan);

        return loan;
    }


    public int timeToBeCataloged(String cardId, int lotPreparationTime, int cardCatalogedTime) throws StoredCardNotFoundException, InvalidLotPreparationTimeException, InvalidCatalogTimeException {
        if (lotPreparationTime<0) {
            throw new InvalidLotPreparationTimeException();
        }
        if (cardCatalogedTime<0) {
            throw new InvalidCatalogTimeException();
        }

        CardWareHouse.Position position = cardRepository.getPosition(cardId);
        if (position == null) {
            throw new StoredCardNotFoundException();
        }

        int numStoredCardCase = cardRepository.numCardCases() - position.getCardCase() - 1;
        int numberInStoredCase = position.getNum() + 1;

        int numCardsInLastStoredCase = 0;
        int t1 = 0;
        if (numStoredCardCase==1) {
            numCardsInLastStoredCase = cardRepository.getNumCardsInLastCase();
            t1 = numStoredCardCase * (lotPreparationTime + (numCardsInLastStoredCase * cardCatalogedTime));
        }
        if (numStoredCardCase>1) {
            t1+= (numStoredCardCase-1) * (lotPreparationTime + (MAX_CARD_IN_STOREDCARDCASE * cardCatalogedTime));
        }
        int t2 = lotPreparationTime + numberInStoredCase * cardCatalogedTime;
        int t = t1 + t2;
        return t;
    }

    public Iterator<CatalogedCard> getAllCardsByPlayer(String playerName) throws NoCardException {
        Iterator<CatalogedCard> it = playerRepository.getCatalogedCards(playerName);
        if (it == null || !it.hasNext()) {
            throw new NoCardException();
        }
        return it;
    }

    public Iterator<CatalogedCard> getAllCardsByCollection(String collection) throws NoCardException {
        Iterator<CatalogedCard> it = collectionRepository.getCatalogedCards(collection);
        if (it == null || !it.hasNext()) {
            throw new NoCardException();
        }
        return it;
    }

    public Iterator<Loan> getAllLoansByEntity(String entityId) throws NoLoanException {
        Iterator<Loan> it = entityRepository.getLoansByEntity(entityId);
        if (it == null || !it.hasNext()) {
            throw new NoLoanException();
        }
        return it;
    }


    public Iterator<Loan> getAllLoansByState(String entityId, LoanStatus status) throws NoLoanException {
        Iterator<Loan> it = entityRepository.getAllLoansByState(entityId, status);
        if (it == null || !it.hasNext()) {
            throw new NoLoanException();
        }
        return it;
    }



    public  Iterator<Loan> getAllLoansByCard(String cardId) throws NoLoanException {
        Iterator<Loan> it = cardRepository.getLoansByCard(cardId);
        if (it == null || !it.hasNext()) {
            throw new NoLoanException();
        }
        return it;
    }

    @Override
    public Entity getEntityTheMost() throws NoEntityException {
        Entity entity = entityRepository.getEntityTheMost();
        if (entity == null) {
            throw new NoEntityException();
        }
        return entity;
    }



    @Override
    public CatalogedCard getMostShownCard() throws NoCardException {
        CatalogedCard catalogedCard = cardRepository.getMostShownCard();
        if (catalogedCard == null) {
            throw new NoCardException();
        }
        return catalogedCard;
    }


    @Override
    public BaseballCardsHelper getBaseballCardsHelper() {
        return new BaseballsCardsHelperImpl(this);
    }
}
