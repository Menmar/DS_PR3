package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.traversal.Iterator;

public class Entity {
    private String entityId;
    private String name;
    private String address;
    private int numCurrentLoans;
    private LinkedList<Loan> loanList;
    private LinkedList<Loan> closedLoans;

    public Entity(String entityId, String name, String address) {
        this.entityId = entityId;
        this.name = name;
        this.address = address;
        this.numCurrentLoans = 0;
        this.loanList = new LinkedList<>();
        this.closedLoans = new LinkedList<>();
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void update(String name, String address) {
        setName(name);
        setAddress(address);
    }

    public int numCurrentLoans() {
        return numCurrentLoans;
    }

    public void addLoan(Loan loan) {
        loanList.insertEnd(loan);
        numCurrentLoans++;
    }

    public int numLoans() {
        return loanList.size();
    }

    public Iterator<Loan> getLoans() {
        return loanList.values();
    }

    public Iterator<Loan> getClosedLoans() {
        return closedLoans.values();
    }

    public void addClosedLoan(Loan loan) {
        closedLoans.insertEnd(loan);
    }

    public int numClosedLoans() {
        return closedLoans.size();
    }
}
