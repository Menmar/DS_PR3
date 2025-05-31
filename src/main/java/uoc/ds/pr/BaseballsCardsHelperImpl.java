package uoc.ds.pr;

import uoc.ds.pr.model.Entity;
import uoc.ds.pr.model.Player;
import uoc.ds.pr.model.Worker;

public class BaseballsCardsHelperImpl implements BaseballCardsHelper {
    private BaseballCardsPR2Impl baseballCards;

    public BaseballsCardsHelperImpl(BaseballCardsPR2Impl baseballCards) {
        this.baseballCards = baseballCards;
    }


    @Override
    public Entity getEntity(String id) {
        return baseballCards.entityRepository.getEntity(id);
    }

    @Override
    public int numEntities() {
        return baseballCards.entityRepository.numEntities();
    }

    @Override
    public Worker getWorker(String id) {
        return baseballCards.workerRepository.getWorker(id);
    }

    @Override
    public int numWorkers() {
        return baseballCards.workerRepository.numWorkers();
    }

    @Override
    public Player getPlayer(String id) {
        return baseballCards.playerRepository.getPlayer(id);
    }

    @Override
    public int numStoredCards() {
        return baseballCards.cardRepository.numStoredCards();
    }

    @Override
    public int numCardCases() {
        return baseballCards.cardRepository.numCardCases();
    }

    @Override
    public int numCatalogedCardsByWorker(String workerId) {
        return baseballCards.workerRepository.numCatalogedCards(workerId);
    }

    @Override
    public int numCatalogedCards() {
        return baseballCards.cardRepository.numCatalogedCards();
    }

    @Override
    public int numCatalogedCardsByPlayer(String playerId) {
        return baseballCards.playerRepository.numCatalogedCards(playerId);
    }

    @Override
    public int numCatalogedCardsByCollection(String collection) {
        return baseballCards.collectionRepository.numCatalogedCardsByCollection(collection);
    }

    @Override
    public int numLoansByEntity(String entityId) {
        return baseballCards.entityRepository.numLoans(entityId);
    }

    @Override
    public int numLoans() {
        return baseballCards.loanRepository.numLoans();
    }

    @Override
    public int numLoansByWorker(String workerId) {
        return baseballCards.workerRepository.numLoans(workerId);
    }

    @Override
    public int numLoansByCard(String cardId) {
        return baseballCards.cardRepository.numLoansByCard(cardId);
    }

    @Override
    public int numClosedLoansByEntity(String entityId) {
        return baseballCards.entityRepository.numClosedLoansByEntity(entityId);
    }

}
