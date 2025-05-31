package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import java.time.LocalDate;
import uoc.ds.pr.enums.AuctionType;
import uoc.ds.pr.enums.CardRating;
import uoc.ds.pr.enums.CardStatus;
import uoc.ds.pr.enums.CollectorLevel;
import uoc.ds.pr.enums.WorkerRole;
import uoc.ds.pr.exceptions.AuctionAlreadyExists4CardException;
import uoc.ds.pr.exceptions.AuctionAlreadyExistsException;
import uoc.ds.pr.exceptions.AuctionClosedException;
import uoc.ds.pr.exceptions.AuctionNotFoundException;
import uoc.ds.pr.exceptions.AuctionStillOpenException;
import uoc.ds.pr.exceptions.BidPriceTooLowException;
import uoc.ds.pr.exceptions.CardAlreadyInOwnCollectionException;
import uoc.ds.pr.exceptions.CardCollectorNotFoundException;
import uoc.ds.pr.exceptions.CatalogedCardAlreadyAuctionedException;
import uoc.ds.pr.exceptions.CatalogedCardAlreadyInWishlistException;
import uoc.ds.pr.exceptions.CatalogedCardAlreadyLentException;
import uoc.ds.pr.exceptions.CatalogedCardAlreadyLoanedException;
import uoc.ds.pr.exceptions.CatalogedCardNotFoundException;
import uoc.ds.pr.exceptions.CollectionNotFoundException;
import uoc.ds.pr.exceptions.EntityNotFoundException;
import uoc.ds.pr.exceptions.FollowedException;
import uoc.ds.pr.exceptions.FollowerNotFound;
import uoc.ds.pr.exceptions.InvalidBidException;
import uoc.ds.pr.exceptions.MaximumNumberOfLoansException;
import uoc.ds.pr.exceptions.NoBidException;
import uoc.ds.pr.exceptions.NoCardCollectorException;
import uoc.ds.pr.exceptions.NoCardException;
import uoc.ds.pr.exceptions.NoFollowedException;
import uoc.ds.pr.exceptions.NoWorkerException;
import uoc.ds.pr.exceptions.WorkerNotAllowedException;
import uoc.ds.pr.exceptions.WorkerNotFoundException;
import uoc.ds.pr.model.Auction;
import uoc.ds.pr.model.Bid;
import uoc.ds.pr.model.CardCollector;
import uoc.ds.pr.model.CatalogedCard;
import uoc.ds.pr.model.Collection;
import uoc.ds.pr.model.Loan;
import uoc.ds.pr.model.Worker;
import uoc.ds.pr.repository.AuctionRepository;
import uoc.ds.pr.repository.CardCollectorRepository;
import uoc.ds.pr.repository.WorkerRoleRepository;

public class BaseballCardsPR3Impl extends BaseballCardsPR2Impl implements BaseballCardsPR3 {

  protected CardCollectorRepository cardCollectorRepository;
  protected AuctionRepository auctionRepository;
  protected WorkerRoleRepository workerRoleRepository;

  public BaseballCardsPR3Impl() {
    super();
    this.cardCollectorRepository = new CardCollectorRepository(this);
    this.auctionRepository = new AuctionRepository(this);
    this.workerRoleRepository = new WorkerRoleRepository(this, workerRepository);
  }

  @Override
  public void addWorker(String workerId, String name, String surname, WorkerRole role) {
    workerRepository.addWorker(workerId, name, surname, role);
    Worker worker = super.workerRepository.getWorker(workerId);
    if (worker != null) {
      this.workerRoleRepository.updateWorkerRole(worker, role);
    }
  }


  @Override
  public void storeCard(String cardId, String player, int publicationYear, String collection, CardStatus status,
      CardRating rating) {
    cardRepository.storecard(cardId, player, publicationYear, collection, status, rating);
  }

  @Override
  public CatalogedCard catalogCard(String workerId)
      throws WorkerNotFoundException, NoCardException, WorkerNotAllowedException {
    if (workerRepository.getWorker(workerId) == null) {
      throw new WorkerNotFoundException();
    }
    if (!workerRoleRepository.hasRole(workerId, WorkerRole.CATALOGER)) {
      throw new WorkerNotAllowedException();
    }

    return super.catalogCard(workerId);
  }

  public Loan lendCard(String loanId, String entityId, String cardId, String workerId, LocalDate date,
      LocalDate expirationDate)
      throws WorkerNotFoundException, CatalogedCardAlreadyLoanedException, NoCardException, MaximumNumberOfLoansException, EntityNotFoundException, CatalogedCardNotFoundException, WorkerNotAllowedException, CatalogedCardAlreadyAuctionedException {
    if (entityRepository.getEntity(entityId) == null) {
      throw new EntityNotFoundException();
    }

    if (workerRepository.getWorker(workerId) == null) {
      throw new WorkerNotFoundException();
    }

    if (!workerRoleRepository.hasRole(workerId, WorkerRole.LENDER)) {
      throw new WorkerNotAllowedException();
    }

    if (auctionRepository.isCardInAuction(cardId)) {
      throw new CatalogedCardAlreadyAuctionedException();
    }

    return super.lendCard(loanId, entityId, cardId, workerId, date, expirationDate);
  }

  @Override
  public CardCollector addCollector(String collectorId, String name, String surname, LocalDate birthday,
      double balance) {
    return cardCollectorRepository.addCollector(collectorId, name, surname, birthday, balance);
  }

  @Override
  public Iterator<Worker> getWorkersByRole(WorkerRole role) throws NoWorkerException {
    Iterator<Worker> workerIterator = workerRoleRepository.getWorkersByRole(role);
    if (workerIterator == null || !workerIterator.hasNext()) {
      throw new NoWorkerException();
    }
    return workerIterator;
  }

  @Override
  public CollectorLevel getLevel(String collectorId) throws CardCollectorNotFoundException {
    CardCollector cardCollector = cardCollectorRepository.getCardCollector(collectorId);
    return cardCollector.getLevel();
  }

  @Override
  public void addAuction(String auctionId, String cardId, String workerId, AuctionType auctionType, double price)
      throws CatalogedCardNotFoundException, WorkerNotFoundException, WorkerNotAllowedException, CatalogedCardAlreadyLentException, AuctionAlreadyExistsException, AuctionAlreadyExists4CardException, AuctionNotFoundException {

    if (auctionRepository.getAuction(auctionId) != null) {
      throw new AuctionAlreadyExistsException();
    }

    Worker worker = workerRepository.getWorker(workerId);
    if (worker == null) {
      throw new WorkerNotFoundException();
    }

    if (!workerRoleRepository.hasRole(workerId, WorkerRole.AUCTIONEER)) {
      throw new WorkerNotAllowedException();
    }

    CatalogedCard card = cardRepository.getCatalogedCard(cardId);
    if (card == null) {
      throw new CatalogedCardNotFoundException();
    }

    if (card.isLoaned()) {
      throw new CatalogedCardAlreadyLentException();
    }

    if (auctionRepository.isCardInAuction(cardId)) {
      throw new AuctionAlreadyExists4CardException();
    }

    auctionRepository.addAuction(auctionId, card, worker, auctionType, price);

  }

  @Override
  public void addOpenBid(String auctionId, String collectorId, double price)
      throws CardCollectorNotFoundException, AuctionNotFoundException, AuctionClosedException, BidPriceTooLowException {
    Auction auction = auctionRepository.getAuction(auctionId);

    if (auction.isClosed()) {
      throw new AuctionClosedException();
    }

    CardCollector cardCollector = cardCollectorRepository.getCardCollector(collectorId);
    if (cardCollector == null) {
      throw new CardCollectorNotFoundException();
    }

    double currentHighestPrice = auction.getPrice();
    if (auction.getWinningBid() != null) {
      currentHighestPrice = auction.getWinningBid().getPrice();
    }

    if (price < currentHighestPrice) {
      throw new BidPriceTooLowException();
    }

    Bid bid = new Bid(cardCollector, auction.getCard(), price);
    auction.addBid(bid);
  }

  @Override
  public void addClosedBid(String auctionId, String collectorId)
      throws CardCollectorNotFoundException, AuctionClosedException, AuctionNotFoundException {
    Auction auction = auctionRepository.getAuction(auctionId);
    if (auction.isClosed()) {
      throw new AuctionClosedException();
    }

    CardCollector cardCollector = cardCollectorRepository.getCardCollector(collectorId);
    if (cardCollector == null) {
      throw new CardCollectorNotFoundException();
    }

    Bid bid = new Bid(cardCollector, auction.getCard(), auction.getPrice());
    auction.addBid(bid);
  }

  @Override
  public Bid award(String auctionId, String workerId)
      throws AuctionNotFoundException, AuctionClosedException, WorkerNotFoundException, WorkerNotAllowedException, NoBidException, InvalidBidException {
    Auction auction = auctionRepository.getAuction(auctionId);
    if (auction.isClosed()) {
      throw new AuctionClosedException();
    }

    Worker worker = workerRepository.getWorker(workerId);
    if (worker == null) {
      throw new WorkerNotFoundException();
    }

    if (!workerRoleRepository.hasRole(workerId, WorkerRole.AUCTIONEER)) {
      throw new WorkerNotAllowedException();
    }

    Bid winningBid = auction.getWinningBid();
    if (winningBid == null) {
      throw new NoBidException();
    }

    CardCollector winner = winningBid.getCardCollector();
    if (winner.getBalance() < winningBid.getPrice()) {
      throw new InvalidBidException();
    }

    winner.decreaseBalance(winningBid.getPrice());
    CatalogedCard card = winningBid.getCatalogedCard();
    if (winner.isInWishlist(card)) {
      winner.removeFromWishlist(card);
    }

    cardCollectorRepository.addCard(card, winner);

    auction.close();
    auctionRepository.closeAuction(auctionId);

    return winningBid;
  }

  @Override
  public void addToWishlist(String cardId, String collectorId)
      throws CatalogedCardNotFoundException, CardCollectorNotFoundException, CatalogedCardAlreadyInWishlistException, CardAlreadyInOwnCollectionException {

    CatalogedCard card = cardRepository.getCatalogedCard(cardId);
    if (card == null) {
      throw new CatalogedCardNotFoundException();
    }

    CardCollector cardCollector = cardCollectorRepository.getCardCollector(collectorId);
    if (cardCollector.isInWishlist(card)) {
      throw new CatalogedCardAlreadyInWishlistException();
    }

    if (cardCollector.hasCard(card)) {
      throw new CardAlreadyInOwnCollectionException();
    }

    cardCollectorRepository.addToWishlist(card, cardCollector);

  }

  @Override
  public boolean isInWishlist(String cardId, String collectorId)
      throws CatalogedCardNotFoundException, CardCollectorNotFoundException {
    CatalogedCard card = cardRepository.getCatalogedCard(cardId);
    CardCollector cardCollector = cardCollectorRepository.getCardCollector(collectorId);
    if (card == null) {
      throw new CatalogedCardNotFoundException();
    }
    return cardCollector.isInWishlist(card);
  }

  @Override
  public void addCollectionToWishlist(String collectionId, String collectorId)
      throws CollectionNotFoundException, CardCollectorNotFoundException {
    Collection collection = collectionRepository.getCollection(collectionId);
    CardCollector cardCollector = cardCollectorRepository.getCardCollector(collectorId);
    if (collection == null) {
      throw new CollectionNotFoundException();
    }
    Iterator<CatalogedCard> cards = collection.catalogedCards();

    boolean cardAdded = false;

    while (cards.hasNext()) {
      CatalogedCard card = cards.next();
      if (cardCollector.hasCard(card) || cardCollector.isInWishlist(card)) {
        continue;
      }
      cardCollectorRepository.addToWishlist(card, cardCollector);
      cardAdded = true;
    }
    if (!cardAdded) {
      throw new CollectionNotFoundException();
    }
  }

  @Override
  public Bid getAuctionWinner(String auctionId) throws AuctionNotFoundException, AuctionStillOpenException {
    Auction auction = auctionRepository.getAuction(auctionId);
    if (auction == null) {
      throw new AuctionNotFoundException();
    }
    if (!auction.isClosed()) {
      throw new AuctionStillOpenException();
    }
    return auction.getWinningBid();
  }

  @Override
  public Iterator<CardCollector> best5CollectorsByRareCards() throws NoCardCollectorException {
    Iterator<CardCollector> cCIt = cardCollectorRepository.getTop5ByRareCards();
    if (!cCIt.hasNext()) {
      throw new NoCardCollectorException();
    }
    return cCIt;
  }

  @Override
  public void addFollower(String collectorId, String collectorFollowerId) throws FollowerNotFound, FollowedException {
    CardCollector followed = null;
    CardCollector follower = null;
    try {
      followed = cardCollectorRepository.getCardCollector(collectorId);
    } catch (CardCollectorNotFoundException e) {
      throw new FollowedException();
    }
    try {
      follower = cardCollectorRepository.getCardCollector(collectorFollowerId);
    } catch (CardCollectorNotFoundException e) {
      throw new FollowerNotFound();
    }

    cardCollectorRepository.addFollower(followed, follower);
  }

  @Override
  public Iterator<CardCollector> getFollowers(String collectorId) throws FollowerNotFound, FollowedException {
    try {
      CardCollector collector = cardCollectorRepository.getCardCollector(collectorId);
      Iterator<CardCollector> followers = cardCollectorRepository.getFollowers(collector);
      if (!followers.hasNext()) {
        throw new FollowerNotFound();
      }
      return followers;
    } catch (CardCollectorNotFoundException e) {
      throw new FollowedException();
    }
  }

  @Override
  public Iterator<CardCollector> getFollowings(String collectorId)
      throws CardCollectorNotFoundException, NoFollowedException {
    CardCollector collector = cardCollectorRepository.getCardCollector(collectorId);
    Iterator<CardCollector> followings = cardCollectorRepository.getFollowings(collector);
    if (!followings.hasNext()) {
      throw new NoFollowedException();
    }
    return followings;
  }

  @Override
  public Iterator<CardCollector> recommendations(String collectorId)
      throws CardCollectorNotFoundException, NoFollowedException {
    CardCollector collector = cardCollectorRepository.getCardCollector(collectorId);
    Iterator<CardCollector> followings = cardCollectorRepository.getFollowings(collector);
    if (!followings.hasNext()) {
      throw new NoFollowedException();
    }
    return cardCollectorRepository.getRecommendations(collector);
  }

  @Override
  public BaseballCardsHelperPR3 getBaseballCardsHelperPR3() {
    return new BaseballCardsHelperPR3Impl(this);
  }

  private Worker getWorkerHelper(String workerId) {
    return workerRepository.getWorker(workerId);
  }


}