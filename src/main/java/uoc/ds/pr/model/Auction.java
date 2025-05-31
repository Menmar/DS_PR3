package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.Queue;
import edu.uoc.ds.adt.sequential.QueueArrayImpl;
import java.util.Comparator;
import uoc.ds.pr.enums.AuctionType;

public class Auction {

  public static final Comparator<Auction> COMPARATOR_ID = (a1, a2) -> a1.getId().compareTo(a2.getId());

  private String id;
  private CatalogedCard card;
  private Worker worker;
  private AuctionType type;
  private double price;
  private boolean closed;
  private Bid winningBid;
  private Queue<Bid> bids;

  public Auction(String id, CatalogedCard card, Worker worker, AuctionType type, double price) {
    this.id = id;
    this.card = card;
    this.worker = worker;
    this.type = type;
    this.price = price;
    this.closed = false;
    this.bids=new QueueArrayImpl<>();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public CatalogedCard getCard() {
    return card;
  }

  public void setCard(CatalogedCard card) {
    this.card = card;
  }

  public Worker getWorker() {
    return worker;
  }

  public void setWorker(Worker worker) {
    this.worker = worker;
  }

  public AuctionType getType() {
    return type;
  }

  public void setType(AuctionType type) {
    this.type = type;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public boolean isClosed() {
    return closed;
  }

  public void close() {
    this.closed = true;
  }

  public Bid getWinningBid() {
    return winningBid;
  }

  public void setWinningBid(Bid winningBid) {
    this.winningBid = winningBid;
  }

  public void addBid(Bid bid) {
    bids.add(bid);
    if (type == AuctionType.OPEN_BID) {
      evaluateOpenBid(bid);
    } else {
      evaluateClosedBid(bid);
    }
  }

  private void evaluateClosedBid(Bid bid) {
    if (winningBid == null || bid.getCardCollector().getLevel().getValue() > winningBid.getCardCollector().getLevel()
        .getValue()) {
      winningBid = bid;
    }
  }

  private void evaluateOpenBid(Bid bid) {
    if (winningBid == null || bid.getPrice() > winningBid.getPrice()) {
      winningBid = bid;
    }
  }

  public int numBids() {
    return bids.size();
  }

  public boolean isOpenType() {
    return type == AuctionType.OPEN_BID;
  }
}
