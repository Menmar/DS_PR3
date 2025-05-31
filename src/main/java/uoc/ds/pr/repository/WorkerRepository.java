package uoc.ds.pr.repository;

import static uoc.ds.pr.BaseballCards.MAX_NUM_WORKERS;

import uoc.ds.pr.BaseballCards;
import uoc.ds.pr.BaseballCardsHelper;
import uoc.ds.pr.exceptions.WorkerNotFoundException;
import uoc.ds.pr.model.CatalogedCard;
import uoc.ds.pr.model.Loan;
import uoc.ds.pr.model.Worker;
import uoc.ds.pr.util.DSArray;

public class WorkerRepository {
    private BaseballCards baseballCards;
    private BaseballCardsHelper helper;
    protected DSArray<Worker> workers;

    public WorkerRepository(BaseballCards baseballCards) {
        this.baseballCards = baseballCards;
        this.helper = baseballCards.getBaseballCardsHelper();
        workers = new DSArray<>(MAX_NUM_WORKERS);
    }


    public void addCataloguedCard(String workerId, CatalogedCard catalogedCard)
            throws WorkerNotFoundException {

        Worker worker = workers.get(workerId);
        if (worker == null) {
            throw new WorkerNotFoundException();
        }
        worker.addCataloguedCard(catalogedCard);
    }

    public void addWorker(String id, String name, String surname) {
        Worker worker = workers.get(id);
        if (worker == null) {
            worker = new Worker(id, name, surname);
            this.workers.put(id, worker);
        }
        else {
            worker.update(name, surname);
        }
    }

    public Worker getWorker(String id) {
        return workers.get(id);
    }

    public Worker getWorkerOrThrow(String id) throws WorkerNotFoundException {
        Worker worker = workers.get(id);
        if (worker == null) {
            throw new WorkerNotFoundException();
        }
        return worker;
    }

    public int numWorkers() {
        return workers.size();
    }

    public boolean exist(String workerId) {
        return (getWorker(workerId) != null);
    }

    public int numCatalogedCards(String workerId)  {
        Worker worker = getWorker(workerId);

        return (worker!=null?worker.numCatalogedCards():0);
    }

    public int numLoans(String workerId) {
        Worker worker = getWorker(workerId);
        return (worker!=null?worker.numLoans():0);
    }

    public void addNewLoan(Worker worker, Loan loan) {
        worker.addLoan(loan);
        loan.setWorker(worker);
    }
}
