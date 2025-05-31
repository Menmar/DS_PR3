package uoc.ds.pr;

import uoc.ds.pr.exceptions.AuctionNotFoundException;
import uoc.ds.pr.exceptions.CardCollectorNotFoundException;
import uoc.ds.pr.model.Auction;
import uoc.ds.pr.model.Bid;
import uoc.ds.pr.model.CardCollector;

public interface BaseballCardsHelperPR3 {

  int numCardCollectors();

  CardCollector getCardCollector(String cardCollectorId) throws CardCollectorNotFoundException;

  Auction getAuction(String auctionId) throws AuctionNotFoundException;

  int numOpenAuctions();

  int numClosedAuctions();

  int numCardsInWishlist(String cardCollectorId);

  int numCardsByCardCollector(String cardCollectorId);

  Bid getCurrentWinner(String auctionId) throws AuctionNotFoundException;

  int numFollowers(String cardCollectorId);

  int numFollowings(String cardCollectorId);
}
