package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.enums.AuctionType;
import uoc.ds.pr.enums.CollectorLevel;
import uoc.ds.pr.enums.WorkerRole;

import java.time.LocalDate;

public interface BaseballCardsPR3 extends BaseballCards{



    CardCollector addCollector(String collectorId, String name, String surname, LocalDate birthday, double balance);
    Iterator getWorkersByRole(WorkerRole role) throws NoWorkerException;
    CollectorLevel getLevel(String collectorId) throws CardCollectorNotFoundException;
    void addAuction(String auctionId, String cardId, String workerId, AuctionType auctionType, double price)
            throws CatalogedCardNotFoundException, WorkerNotFoundException,
            WorkerNotAllowedException, CatalogedCardAlreadyLentException,
            AuctionAlreadyExistsException, AuctionAlreadyExists4CardException;
    void addOpenBid(String auctionId, String collectorId, double price) throws CardCollectorNotFoundException, AuctionNotFoundException,
            AuctionClosedException, BidPriceTooLowException;
    void addClosedBid(String auctionId, String collectorId) throws CardCollectorNotFoundException, AuctionClosedException, AuctionNotFoundException;
    Bid award(String auctionId, String workerId) throws AuctionNotFoundException, AuctionClosedException,  WorkerNotFoundException,
            WorkerNotAllowedException, NoBidException, InvalidBidException ;
    void addToWishlist(String cardId, String collectorId) throws CatalogedCardNotFoundException, CardCollectorNotFoundException,
            CatalogedCardAlreadyInWishlistException, CardAlreadyInOwnCollectionException ;
    boolean isInWishlist(String cardId, String collectorId) throws CatalogedCardNotFoundException, CardCollectorNotFoundException;
    void addCollectionToWishlist(String collectionId, String collectorId) throws CollectionNotFoundException, CardCollectorNotFoundException;
    Bid getAuctionWinner(String auctionId) throws AuctionNotFoundException, AuctionStillOpenException;
    Iterator best5CollectorsByRareCards() throws  NoCardCollectorException;
    void addFollower(String collectorId, String collectorFollowerId) throws FollowerNotFound, FollowedException;
    Iterator<CardCollector> getFollowers(String collectorId) throws FollowerNotFound, FollowedException ;
    Iterator<CardCollector> getFollowings(String collectorId) throws CardCollectorNotFoundException, NoFollowedException ;
    Iterator<CardCollector> recommendations(String collectorId) throws CardCollectorNotFoundException, NoFollowedException;
    BaseballCardsHelperPR3 getBaseballCardsHelperPR3();
}

