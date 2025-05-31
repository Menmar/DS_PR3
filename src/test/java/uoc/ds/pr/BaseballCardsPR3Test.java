package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uoc.ds.pr.enums.AuctionType;
import uoc.ds.pr.enums.CardRating;
import uoc.ds.pr.enums.CollectorLevel;
import uoc.ds.pr.enums.WorkerRole;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;
import uoc.ds.pr.util.CardCollectorData;
import uoc.ds.pr.util.CardCollectorHelper;
import uoc.ds.pr.util.DateUtils;

import javax.smartcardio.Card;

public class BaseballCardsPR3Test extends BaseballCardsPR2Test{
    protected BaseballCardsHelperPR3 helperPR3;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.helperPR3 = baseballCards.getBaseballCardsHelperPR3();
    }



    @Test
    public void addCollectorTest() {
        CardCollectorHelper.addCardsCollectorData(baseballCards, CardCollectorData.cardCollectorData);
        Assert.assertEquals(10, helperPR3.numCardCollectors());

        baseballCards.addCollector("CCC10", "Joseph", "Bin", DateUtils.createLocalDate("07-01-1974"), 2000);
        Assert.assertEquals(11, helperPR3.numCardCollectors());

        baseballCards.addCollector("CCC10", "Joseph", "Been", DateUtils.createLocalDate("07-01-1974"), 2000);
        Assert.assertEquals(11, helperPR3.numCardCollectors());

        CardCollector collector = helperPR3.getCardCollector("CCC10");
        Assert.assertEquals("Been", collector.getSurname());

        baseballCards.addCollector("CCC11", "Marc", "Morató", DateUtils.createLocalDate("07-01-1974"), 2000);
        Assert.assertEquals(12, helperPR3.numCardCollectors());

    }


    @Test
    public void getWorkersByRoleTest() throws DSException {
        Assert.assertThrows(NoWorkerException.class, () ->
                baseballCards.getWorkersByRole(WorkerRole.AUCTIONEER));

        Iterator<Worker> it =  baseballCards.getWorkersByRole(WorkerRole.CATALOGER);
        Worker worker1 = it.next();
        Assert.assertEquals("workerId1", worker1.getId());

        Worker worker2 = it.next();
        Assert.assertEquals("workerId2", worker2.getId());

        Worker worker3 = it.next();
        Assert.assertEquals("workerId3", worker3.getId());

        Worker worker4 =it.next();
        Assert.assertEquals("workerId4", worker4.getId());

        Assert.assertFalse(it.hasNext());

        Iterator<Worker> it2 =  baseballCards.getWorkersByRole(WorkerRole.LENDER);
        Worker worker5 = it2.next();
        Assert.assertEquals("workerIdLDR1", worker5.getId());

        Worker worker6 = it2.next();
        Assert.assertEquals("workerIdLDR2", worker6.getId());
        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void getLevelTest() throws DSException {
        Assert.assertThrows(CardCollectorNotFoundException.class, () ->
                baseballCards.getLevel("XXXXX"));

        addCollectorTest();
        CardCollector cardCollector = helperPR3.getCardCollector("CC105");
        cardCollector.updatePoints(850);

        Assert.assertEquals(CollectorLevel.GOLD, baseballCards.getLevel("CC105"));
    }


    private void addAuctioneerWorkers() throws DSException {
        baseballCards.addWorker("workerIAUCT1", "Kil", "Kemoi", WorkerRole.AUCTIONEER);
        baseballCards.addWorker("workerIAUCT2", "Opi", "Wan", WorkerRole.AUCTIONEER);
    }

    @Test
    public void addOpenAuctionTest() throws DSException {
        Assert.assertThrows(CardCollectorNotFoundException.class, () ->
                baseballCards.getLevel("XXXXX"));

        addAuctioneerWorkers();

        Assert.assertThrows(CatalogedCardNotFoundException.class, () ->
            baseballCards.addAuction("AUCTION_1", "XXXX", "workerIAUCT1", AuctionType.OPEN_BID, 200));

        Assert.assertThrows(WorkerNotFoundException.class, () ->
                baseballCards.addAuction("AUCTION_1", "C155", "xxxxx", AuctionType.OPEN_BID, 200));


        super.catalogCardTest();

        Assert.assertThrows(WorkerNotAllowedException.class, () ->
                baseballCards.addAuction("AUCTION_1", "C155", "workerId1", AuctionType.OPEN_BID, 200));

        baseballCards.addAuction("AUCTION_1", "C155", "workerIAUCT1", AuctionType.OPEN_BID, 200);

        Auction auction = helperPR3.getAuction("AUCTION_1");
        Assert.assertEquals("AUCTION_1", auction.getId());
        Assert.assertEquals(1, helperPR3.numOpenAuctions());
        Assert.assertEquals(0, helperPR3.numClosedAuctions());

        Assert.assertThrows(AuctionAlreadyExistsException.class, () ->
            baseballCards.addAuction("AUCTION_1", "C155", "workerIAUCT1", AuctionType.OPEN_BID, 200));

        Assert.assertThrows(AuctionAlreadyExists4CardException.class, () ->
            baseballCards.addAuction("AUCTION_2", "C155", "workerIAUCT1", AuctionType.OPEN_BID, 200));

        Assert.assertThrows(CatalogedCardAlreadyAuctionedException.class, () ->
            baseballCards.lendCard("LOAN_999", "entityId1", "C155", "workerIdLDR2",
                DateUtils.createLocalDate("01-04-2025"), DateUtils.createLocalDate("01-05-2025")));

    }

    @Test
    public void addClosedAuctionTest() throws DSException {
        super.catalogCardTest();

        addAuctioneerWorkers();
        baseballCards.addAuction("AUCTION_1", "C155", "workerIAUCT1", AuctionType.CLOSED_BID, 200);
        Auction auction = helperPR3.getAuction("AUCTION_1");
        Assert.assertEquals("AUCTION_1", auction.getId());
        Assert.assertEquals(0, helperPR3.numOpenAuctions());
        Assert.assertEquals(1, helperPR3.numClosedAuctions());

    }

    @Test
    public void addOpenBidTest() throws DSException {

        addOpenAuctionTest();
        addCollectorTest();

        baseballCards.addOpenBid("AUCTION_1", "CC110", 200);
        Bid bid = helperPR3.getCurrentWinner("AUCTION_1");
        Assert.assertEquals("CC110", bid.getCardCollector().getCollectorId());
        Assert.assertEquals(0, helperPR3.numCardsByCardCollector("CCC10"));

        baseballCards.addOpenBid("AUCTION_1", "CC109", 250);
        bid = helperPR3.getCurrentWinner("AUCTION_1");
        Assert.assertEquals("CC109", bid.getCardCollector().getCollectorId());
        Assert.assertEquals(0, helperPR3.numCardsByCardCollector("CCC10"));

        baseballCards.addOpenBid("AUCTION_1", "CC110", 250);
        bid = helperPR3.getCurrentWinner("AUCTION_1");
        Assert.assertEquals("CC109", bid.getCardCollector().getCollectorId());
        Assert.assertEquals(0, helperPR3.numCardsByCardCollector("CCC10"));

        baseballCards.addOpenBid("AUCTION_1", "CC110", 350);
        bid = helperPR3.getCurrentWinner("AUCTION_1");
        Assert.assertEquals("CC110", bid.getCardCollector().getCollectorId());
        Assert.assertEquals(0, helperPR3.numCardsByCardCollector("CCC10"));

        baseballCards.addOpenBid("AUCTION_1", "CC109", 355);
        bid = helperPR3.getCurrentWinner("AUCTION_1");
        Assert.assertEquals("CC109", bid.getCardCollector().getCollectorId());
        Assert.assertEquals(0, helperPR3.numCardsByCardCollector("CCC10"));
    }

    @Test
    public void addClosedBidTest() throws DSException {
        addClosedAuctionTest();
        addCollectorTest();

        baseballCards.addClosedBid("AUCTION_1", "CC101");
        CardCollector CC101 = helperPR3.getCardCollector("CC101");
        Assert.assertEquals(CollectorLevel.BRONZE, CC101.getLevel());
        Bid bid = helperPR3.getCurrentWinner("AUCTION_1");
        Assert.assertEquals("CC101", bid.getCardCollector().getCollectorId());

        baseballCards.addClosedBid("AUCTION_1", "CC106");
        CardCollector CC106 = helperPR3.getCardCollector("CC106");
        Assert.assertEquals(CollectorLevel.SILVER, CC106.getLevel());
        bid = helperPR3.getCurrentWinner("AUCTION_1");
        Assert.assertEquals("CC106", bid.getCardCollector().getCollectorId());

        baseballCards.addClosedBid("AUCTION_1", "CC103");
        CardCollector CC103 = helperPR3.getCardCollector("CC103");
        Assert.assertEquals(CollectorLevel.BRONZE, CC103.getLevel());
        bid = helperPR3.getCurrentWinner("AUCTION_1");
        Assert.assertEquals("CC106", bid.getCardCollector().getCollectorId());

        baseballCards.addClosedBid("AUCTION_1", "CC105");
        CardCollector CC105 = helperPR3.getCardCollector("CC105");
        Assert.assertEquals(CollectorLevel.PLATINUM, CC105.getLevel());
        bid = helperPR3.getCurrentWinner("AUCTION_1");
        Assert.assertEquals("CC105", bid.getCardCollector().getCollectorId());


        baseballCards.addClosedBid("AUCTION_1", "CC105");
        CardCollector CC109 = helperPR3.getCardCollector("CC109");
        Assert.assertEquals(CollectorLevel.DIAMOND, CC109.getLevel());
        bid = helperPR3.getCurrentWinner("AUCTION_1");
        Assert.assertEquals("CC105", bid.getCardCollector().getCollectorId());

    }

    @Test
    public void awardTest() throws DSException {
        addOpenBidTest();

        Auction auction = helperPR3.getAuction("AUCTION_1");
        CardCollector CCC11 = helperPR3.getCardCollector("CC109");
        Assert.assertEquals(8000, CCC11.getPoints());
        Assert.assertEquals(CollectorLevel.DIAMOND, CCC11.getLevel());
        Assert.assertEquals(1100, CCC11.getBalance(),0);

        Assert.assertEquals(0, helperPR3.numCardsByCardCollector("CC109"));
        Assert.assertEquals(8, helper.numCatalogedCards());

        Assert.assertFalse(auction.isClosed());
        Bid bid = baseballCards.award("AUCTION_1", "workerIAUCT1");
        Assert.assertTrue(auction.isClosed());
        Assert.assertEquals("CC109", bid.getCardCollector().getCollectorId());
        Assert.assertEquals(CardRating.FIVE_STARS, bid.getCatalogedCard().getCardRating());
        Assert.assertEquals(9000, CCC11.getPoints());
        Assert.assertEquals(CollectorLevel.DIAMOND, CCC11.getLevel());
        Assert.assertEquals(1100 - bid.getPrice(), CCC11.getBalance(),0);

        Assert.assertEquals(1, helperPR3.numCardsByCardCollector("CC109"));
    }

    @Test
    public void addToWishlistTest() throws DSException {

        Assert.assertThrows(CatalogedCardNotFoundException.class, () ->
            baseballCards.addToWishlist("XXXX", "CCC10"));

        catalogCardTest();

        Assert.assertThrows(CardCollectorNotFoundException.class, () ->
                baseballCards.addToWishlist("C155", "XXXX"));

        addCollectorTest();
        baseballCards.addToWishlist("C154", "CCC10");
        Assert.assertEquals(1, helperPR3.numCardsInWishlist("CCC10"));

        Assert.assertThrows(CatalogedCardAlreadyInWishlistException.class, () ->
        baseballCards.addToWishlist("C154", "CCC10"));

        Assert.assertEquals(1, helperPR3.numCardsInWishlist("CCC10"));
        Assert.assertEquals(0, helperPR3.numCardsByCardCollector("CCC10"));

        addAuctioneerWorkers();

        baseballCards.addAuction("AUCTION_1", "C155", "workerIAUCT1", AuctionType.OPEN_BID, 200);
        baseballCards.addOpenBid("AUCTION_1", "CCC10", 200);
        Bid bid = baseballCards.award("AUCTION_1", "workerIAUCT1");
        Assert.assertEquals("CCC10", bid.getCardCollector().getCollectorId());
        Assert.assertEquals(1, helperPR3.numCardsByCardCollector("CCC10"));

        Assert.assertThrows(CardAlreadyInOwnCollectionException.class, () ->
                baseballCards.addToWishlist("C155", "CCC10"));
    }

    @Test
    public void isInWishlistTest() throws DSException {
        addToWishlistTest();

        Assert.assertThrows(CatalogedCardNotFoundException.class, () ->
            baseballCards.isInWishlist("XXX", "CCC10"));

        Assert.assertThrows(CardCollectorNotFoundException.class, () ->
                baseballCards.isInWishlist("C154", "XXXX"));

        Assert.assertTrue(baseballCards.isInWishlist("C154", "CCC10"));

    }

    @Test
    public void addCollectionToWishlistTest() throws DSException {
        addCollectorTest();
        catalogCardTest();

        Assert.assertThrows(CollectionNotFoundException.class, () ->
                baseballCards.addCollectionToWishlist("XXX", "CC110"));

        Assert.assertThrows(CardCollectorNotFoundException.class, () ->
                baseballCards.addCollectionToWishlist("Topps Chrome", "XXXX"));

        Assert.assertEquals(6, helper.numCatalogedCardsByCollection("Topps Chrome"));
        Assert.assertEquals(0, helperPR3.numCardsInWishlist("CC110"));
        baseballCards.addCollectionToWishlist("Topps Chrome", "CC110");
        Assert.assertEquals(6, helperPR3.numCardsInWishlist("CC110"));
    }

    @Test
    public void getAuctionWinnerTest1() throws DSException {
        this.addOpenBidTest();

        Assert.assertThrows(AuctionNotFoundException.class, () ->
                baseballCards.getAuctionWinner("XXXX"));

        Assert.assertThrows(AuctionStillOpenException.class, () ->
                baseballCards.getAuctionWinner("AUCTION_1"));

        addAuctioneerWorkers();

        baseballCards.award("AUCTION_1", "workerIAUCT1");

        Bid bid = baseballCards.getAuctionWinner("AUCTION_1");
        Assert.assertEquals("CC109", bid.getCardCollector().getCollectorId());

    }

    @Test
    public void best5CollectionByRareCards() throws DSException {
        awardTest();
        Iterator<CardCollector> it = baseballCards.best5CollectorsByRareCards();
        CardCollector cardCollector = it.next();

        Assert.assertEquals("CC109", cardCollector.getCollectorId());


    }

}
