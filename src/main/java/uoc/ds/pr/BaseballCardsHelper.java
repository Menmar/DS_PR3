package uoc.ds.pr;

import uoc.ds.pr.model.Entity;
import uoc.ds.pr.model.Player;
import uoc.ds.pr.model.Worker;

public interface BaseballCardsHelper {
    Entity getEntity(String id);
    int numEntities();
    Worker getWorker(String id);
    int numWorkers();

    Player getPlayer(String id);

    int numStoredCards();
    int numCardCases();

    int numCatalogedCardsByWorker(String workerId1);

    int numCatalogedCards();

    int numCatalogedCardsByPlayer(String player);

    int numCatalogedCardsByCollection(String collection);

    int numLoansByEntity(String entityId);

    int numLoans();

    int numLoansByWorker(String workerId);

    int numLoansByCard(String cardId);

    int numClosedLoansByEntity(String entityId);
}
