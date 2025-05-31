package uoc.ds.pr.repository;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.BaseballCards;
import uoc.ds.pr.BaseballCardsHelper;
import uoc.ds.pr.exceptions.CollectionNotFoundException;
import uoc.ds.pr.model.CatalogedCard;
import uoc.ds.pr.model.Collection;
import uoc.ds.pr.util.DSLinkedList;

public class CollectionRepository {
    private BaseballCards baseballCards;
    private BaseballCardsHelper helper;
    protected DSLinkedList<Collection> collections;


    public CollectionRepository(BaseballCards baseballCards) {
        this.baseballCards = baseballCards;
        this.helper = baseballCards.getBaseballCardsHelper();
        this.collections = new DSLinkedList<>(Collection.CMP_ID);
    }

    public void addCollection(String name) {
        Collection collection = collections.get(new Collection(name));
        if (collection == null) {
            collection = new Collection(name);
            this.collections.insertEnd(collection);
        } else {
            // TO-DO
        }
    }

    public Collection getCollection(String name) {
        return collections.get(new Collection(name));
    }

    public Collection getCollectionOrThrow(String id) throws CollectionNotFoundException {
        Collection collection = getCollection(id);
        if (collection == null) {
            throw new CollectionNotFoundException();
        }
        return collection;
    }

    public void addCatalogedCard(CatalogedCard catalogedCard) {
        String sCollection = catalogedCard.getCollection();
        Collection collection = collections.get(new Collection(sCollection));
        if (collection == null) {
            collection = new Collection(sCollection);
            collections.insertEnd(collection);
        }
        collection.addCatalogedCard(catalogedCard);
    }

    public int numCatalogedCardsByCollection(String collectionName) {
        Collection collection = getCollection(collectionName);
        return (collection!=null?collection.numCatalogedCards():0);
    }

    public Iterator<CatalogedCard> getCatalogedCards(String collectionName) {
        Iterator<CatalogedCard> it = null;
        Collection collection = getCollection(collectionName);
        if (collection != null) {
            it = collection.catalogedCards();
        }
        return it;
    }
}
