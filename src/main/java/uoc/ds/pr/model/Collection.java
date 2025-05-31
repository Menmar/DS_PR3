package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.traversal.Iterator;
import java.util.Comparator;

public class Collection {
    public String getName() {
        return name;
    }

    public static final Comparator<Collection> CMP_ID = (c1, c2)->c1.getName().compareTo(c2.getName());
    private String name;
    private List<CatalogedCard> catalogedCards;


    public Collection() {
        catalogedCards = new LinkedList<>();
    }
    public Collection(String name) {
        this();
        this.name = name;
    }

    public void addCatalogedCard(CatalogedCard catalogedCard) {
        catalogedCards.insertEnd(catalogedCard);
    }

    public int numCatalogedCards() {
        return catalogedCards.size();
    }

    public Iterator<CatalogedCard> catalogedCards() {
        return  catalogedCards.values();
    }
}
