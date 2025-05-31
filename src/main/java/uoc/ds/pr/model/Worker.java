package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import uoc.ds.pr.util.DSLinkedList;

public class Worker {
    private String id;
    private String name;
    private String surname;

    private int totalCatalogBooks;

    private DSLinkedList<CatalogedCard> catalogedCards;
    private List<Loan> loanList;
    private DSLinkedList<Loan> closedLoanList;

    public Worker(String id, String name, String surname) {
        setId(id);
        update(name, surname);
        catalogedCards = new DSLinkedList(CatalogedCard.CMP);
        loanList = new LinkedList<>();
        closedLoanList = new DSLinkedList<>(Loan.CMP_ID);
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

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void update(String name, String surname) {
        setName(name);
        setSurname(surname);
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

}