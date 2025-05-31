package uoc.ds.pr;

public interface BaseballCardsHelperPR3 {

    int numCardCollectors();

    CardCollector getCardCollector(String cardCollectorId);

    Auction getAuction(String auctionId);

    int numOpenAuctions();

    int numClosedAuctions();

    int numCardsInWishlist(String cardCollectorId);

    int numCardsByCardCollector(String cardCollectorId);

    Bid getCurrentWinner(String auctionId);

    int numFollowers(String cardCollectorId);

    int numFollowings(String cardCollectorId);
}
