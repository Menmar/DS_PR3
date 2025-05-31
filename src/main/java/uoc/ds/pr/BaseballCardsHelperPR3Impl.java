package uoc.ds.pr;

import uoc.ds.pr.exceptions.AuctionNotFoundException;
import uoc.ds.pr.exceptions.CardCollectorNotFoundException;
import uoc.ds.pr.model.Auction;
import uoc.ds.pr.model.Bid;
import uoc.ds.pr.model.CardCollector;

public class BaseballCardsHelperPR3Impl implements BaseballCardsHelperPR3 {

  private final BaseballCardsPR3Impl baseballCards;

  public BaseballCardsHelperPR3Impl(BaseballCardsPR3Impl baseballCards) {
    this.baseballCards = baseballCards;
  }

  @Override
  public int numCardCollectors() {
    return baseballCards.cardCollectorRepository.numCardCollectors();
  }

  @Override
  public CardCollector getCardCollector(String cardCollectorId) throws CardCollectorNotFoundException {
    return baseballCards.cardCollectorRepository.getCardCollector(cardCollectorId);
  }

  @Override
  public Auction getAuction(String auctionId) throws AuctionNotFoundException {
    return baseballCards.auctionRepository.getAuction(auctionId);
  }

  @Override
  public int numOpenAuctions() {
    return baseballCards.auctionRepository.numberOfOpenAuctions();
  }

  @Override
  public int numClosedAuctions() {
    return baseballCards.auctionRepository.numberOfClosedAuctions();
  }

  @Override
  public int numCardsInWishlist(String cardCollectorId) {
    return baseballCards.cardCollectorRepository.numCardsInWishlist(cardCollectorId);
  }

  @Override
  public int numCardsByCardCollector(String cardCollectorId) {
    return baseballCards.cardCollectorRepository.numCardsByCardCollector(cardCollectorId);
  }

  @Override
  public Bid getCurrentWinner(String auctionId) throws AuctionNotFoundException {
    return baseballCards.auctionRepository.getCurrentWinner(auctionId);
  }

  @Override
  public int numFollowers(String cardCollectorId) {
    return baseballCards.cardCollectorRepository.numFollowers(cardCollectorId);
  }

  @Override
  public int numFollowings(String cardCollectorId) {
    return baseballCards.cardCollectorRepository.numFollowings(cardCollectorId);
  }
}
