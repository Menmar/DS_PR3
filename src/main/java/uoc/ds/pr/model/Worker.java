package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.List;
import java.util.Comparator;
import uoc.ds.pr.enums.WorkerRole;
import uoc.ds.pr.util.DSLinkedList;

public class Worker {

    public static final Comparator<Worker> CMP_ID = (w1, w2) -> w1.getId().compareTo(w2.getId());
    private String id;
    private String name;
    private String surname;
    private WorkerRole role;
    private int totalCatalogBooks;
    private DSLinkedList<CatalogedCard> catalogedCards;
    private List<Loan> loanList;
    private DSLinkedList<Loan> closedLoanList;

    public Worker(String id, String name, String surname, WorkerRole role) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.totalCatalogBooks = 0;
        this.catalogedCards = new DSLinkedList<>(CatalogedCard.CMP_CARDID);
        this.loanList = new DSLinkedList<>(Loan.CMP_ID);
        this.closedLoanList = new DSLinkedList<>(Loan.CMP_ID);
    }

    public Worker(String id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.totalCatalogBooks = 0;
        this.catalogedCards = new DSLinkedList<>(CatalogedCard.CMP_CARDID);
        this.loanList = new DSLinkedList<>(Loan.CMP_ID);
        this.closedLoanList = new DSLinkedList<>(Loan.CMP_ID);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void update(String name, String surname, WorkerRole role) {
        setName(name);
        setSurname(surname);
        setRole(role);
    }

    public void update(String name, String surname) {
        setName(name);
        setSurname(surname);
    }

    private void setRole(WorkerRole role) {
        this.role = role;
    }

    public void addCataloguedCard(CatalogedCard catalogedBook) {
        catalogedCards.insertEnd(catalogedBook);
    }

    public int numCatalogedCards() {
        return catalogedCards.size();
    }

    public void addLoan(Loan loan) {
        loanList.insertEnd(loan);
    }

    public void addClosedLoan(Loan loan) {
        closedLoanList.insertEnd(loan);
    }

    public int numLoans() {
        return loanList.size();
    }

    public int numClosedLoans() {
        return closedLoanList.size();
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Worker getWorker(Worker worker) {
        return worker;
    }
}