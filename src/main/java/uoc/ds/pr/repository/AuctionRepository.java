package uoc.ds.pr.repository;

import edu.uoc.ds.adt.nonlinear.Dictionary;
import edu.uoc.ds.adt.nonlinear.DictionaryAVLImpl;
import edu.uoc.ds.adt.sequential.LinkedList;
import uoc.ds.pr.BaseballCardsPR3;
import uoc.ds.pr.enums.AuctionType;
import uoc.ds.pr.exceptions.AuctionNotFoundException;
import uoc.ds.pr.model.Auction;
import uoc.ds.pr.model.Bid;
import uoc.ds.pr.model.CatalogedCard;
import uoc.ds.pr.model.Worker;

public class AuctionRepository {

  private final BaseballCardsPR3 baseballCards;
  private final Dictionary<String, Auction> auctions;
  private final LinkedList<Auction> openAuctions;
  private final LinkedList<Auction> closedAuctions;
  private final Dictionary<String, Auction> cardAuctions;

  public AuctionRepository(BaseballCardsPR3 baseballCards) {
    this.baseballCards = baseballCards;
    this.auctions = new DictionaryAVLImpl<>();
    this.openAuctions = new LinkedList<>();
    this.closedAuctions = new LinkedList<>();
    this.cardAuctions = new DictionaryAVLImpl<>();
  }

  public Auction addAuction(String auctionId, CatalogedCard card, Worker worker, AuctionType type, double price) {
    Auction auction = new Auction(auctionId, card, worker, type, price);
    auctions.put(auctionId, auction);
    cardAuctions.put(card.getCardId(), auction);

    if (type == AuctionType.OPEN_BID) {
      openAuctions.insertEnd(auction);
    } else {
      closedAuctions.insertEnd(auction);
    }
    return auction;
  }

  public Auction getAuction(String auctionId) {
    return auctions.get(auctionId);
  }

  public Auction getAuctionByCard(String cardId) {
    return cardAuctions.get(cardId);
  }

  public void closeAuction(String auctionId) throws AuctionNotFoundException {
    Auction auction = getAuction(auctionId);
    auction.close();
  }

  public boolean isCardInAuction(String cardId) {
    return cardAuctions.get(cardId) != null;
  }

  public int numberOfOpenAuctions() {
    return openAuctions.size();
  }

  public int numberOfClosedAuctions() {
    return closedAuctions.size();
  }

  public Bid getCurrentWinner(String auctionId) throws AuctionNotFoundException {
    Auction auction = getAuction(auctionId);
    return (auction != null) ? auction.getWinningBid() : null;
  }
}
