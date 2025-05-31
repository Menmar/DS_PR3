package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.traversal.Iterator;
import java.util.Comparator;

public class Player {

    public static final Comparator<Player> CMP_ID = (p1, p2) -> p1.getId().compareTo(p2.getId());
    private String id;
    private String name;
    private final List<CatalogedCard> catalogedCardList;

    public Player() {
        catalogedCardList = new LinkedList<>();
    }

    public Player(String id, String name) {
        this();
        this.id = id;
        this.name = name;
    }

    public Player(String id) {
        this(id, id);
    }

    public String getId() {
        return id;
    }

    public void addCataloguedCard(CatalogedCard catalogedCard) {
        catalogedCardList.insertEnd(catalogedCard);
    }

    public int numCatalogedCards() {
        return catalogedCardList.size();
    }

    public Iterator<CatalogedCard> catalogedCards() {
        return catalogedCardList.values();
    }
}
