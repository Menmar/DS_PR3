package uoc.ds.pr.repository;

import static uoc.ds.pr.BaseballCards.MAX_NUM_ENTITIES;

import edu.uoc.ds.adt.sequential.Set;
import edu.uoc.ds.adt.sequential.SetLinkedListImpl;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.BaseballCards;
import uoc.ds.pr.BaseballCardsHelper;
import uoc.ds.pr.enums.LoanStatus;
import uoc.ds.pr.exceptions.EntityNotFoundException;
import uoc.ds.pr.model.CatalogedCard;
import uoc.ds.pr.model.Entity;
import uoc.ds.pr.model.Loan;
import uoc.ds.pr.util.DSArray;

public class EntityRepository {

    protected DSArray<Entity> entities;
    protected Entity entityMostExposing;
    private final BaseballCards baseballCards;
    private final BaseballCardsHelper helper;


    public EntityRepository(BaseballCards baseballCards) {
        this.baseballCards = baseballCards;
        this.helper = baseballCards.getBaseballCardsHelper();
        this.entities = new DSArray<>(MAX_NUM_ENTITIES);
    }

    public void addEntity(String entityId, String name, String address) {
        Entity entity = entities.get(entityId);
        if (entity == null) {
            entity = new Entity(entityId, name, address);
            this.entities.put(entityId, entity);
        } else {
            entity.update(name, address);

        }
    }

    public Entity getEntity(String id) {
        return entities.get(id);
    }

    public Entity getEntityOrThrow(String id) throws EntityNotFoundException {
        Entity entity = getEntity(id);
        if (entity == null) {
            throw new EntityNotFoundException();
        }
        return entity;
    }

    public int numEntities() {
        return entities.size();
    }

    public void addNewLoan(Loan loan) {
        Entity entity = loan.getEntity();
        entity.addLoan(loan);
    }

    public int numLoans(String entityId) {
        Entity entity = getEntity(entityId);
        return (entity != null ? entity.numLoans() : 0);
    }

    public void updateEntityMostExposing(Entity newEntity) {
        if (entityMostExposing == null) {
            entityMostExposing = newEntity;
        } else {
            if (entityMostExposing.numLoans() < newEntity.numLoans()) {
                entityMostExposing = newEntity;
            }
        }
    }

    public Entity getEntityTheMost() {
        return entityMostExposing;
    }

    public Iterator<Loan> getLoansByEntity(String entityId) {
        Entity entity = getEntity(entityId);
        return (entity != null ? entity.getLoans() : null);
    }

    public Iterator<Loan> getAllLoansByState(String entityId, LoanStatus state) {
        Set<Loan> loanSet = new SetLinkedListImpl<>();
        Iterator<Loan> it = getLoansByEntity(entityId);
        Loan loan = null;
        while (it.hasNext()) {
            loan = it.next();
            if (state.equals(loan.getStatus())) {
                loanSet.add(loan);
            }
        }
        return loanSet.values();
    }

    public Iterator<Loan> getClosedLoansByEntity(String entityId) {
        Entity entity = getEntity(entityId);
        return (entity != null ? entity.getClosedLoans() : null);
    }

    public void addNewClosedLoan(Loan loan) {
        Entity entity = loan.getEntity();
        entity.addClosedLoan(loan);
        CatalogedCard catalogedCard = loan.getCatalogedCard();
        catalogedCard.unloaned();
    }

    public int numClosedLoansByEntity(String entityId) {
        Entity entity = getEntity(entityId);
        return (entity != null ? entity.numClosedLoans() : 0);
    }
}
