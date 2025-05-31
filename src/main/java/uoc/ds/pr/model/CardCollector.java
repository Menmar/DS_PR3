package uoc.ds.pr.model;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;
import uoc.ds.pr.LevelHelper;
import uoc.ds.pr.enums.CollectorLevel;
import uoc.ds.pr.util.DSLinkedList;

public class CardCollector {

  public static final Comparator<CardCollector> COMPARATOR_ID = (c1, c2) -> c1.getCollectorId()
      .compareTo(c2.getCollectorId());
  public static final Comparator<CardCollector> COMPARATOR_LEVEL = (c1, c2) -> Integer.compare(c2.getLevel().getValue(),
      c1.getLevel().getValue());
  public static final Comparator<CardCollector> COMPARATOR_RARE_CARDS = (c1, c2) -> Integer.compare(
      c2.getNumRareCards(), c1.getNumRareCards());


  private String collectorId;
  private String name;
  private String surname;
  private LocalDate birthday;
  private double balance;
  private int points;
  private DSLinkedList<CatalogedCard> cards;
  private DSLinkedList<CatalogedCard> wishlist;
  private int numRareCards;

  public CardCollector(String collectorId, String name, String surname, LocalDate birthday, double balance) {
    this.collectorId = collectorId;
    this.name = name;
    this.surname = surname;
    this.birthday = birthday;
    this.balance = balance;
    this.points = 0;
    this.cards = new DSLinkedList<>(CatalogedCard.CMP_CARDID);
    this.wishlist = new DSLinkedList<>(CatalogedCard.CMP_CARDID);
  }

  public String getCollectorId() {
    return collectorId;
  }

  public void setCollectorId(String collectorId) {
    this.collectorId = collectorId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public LocalDate getBirthday() {
    return birthday;
  }

  public void setBirthday(LocalDate birthday) {
    this.birthday = birthday;
  }

  public double getBalance() {
    return balance;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }

  public void decreaseBalance(double amount) {
    this.balance -= amount;
  }

  public int getPoints() {
    return points;
  }

  public void setPoints(int points) {
    this.points = points;
  }

  public void addPoints(int points) {
    this.points += points;
  }

  public void updatePoints(int points) {
    this.points = points;
  }

  public DSLinkedList<CatalogedCard> getCards() {
    return cards;
  }

  public void setCards(DSLinkedList<CatalogedCard> cards) {
    this.cards = cards;
  }

  public DSLinkedList<CatalogedCard> getWishlist() {
    return wishlist;
  }

  public void setWishlist(DSLinkedList<CatalogedCard> wishlist) {
    this.wishlist = wishlist;
  }

  public int getNumRareCards() {
    return numRareCards;
  }

  public void setNumRareCards(Integer numRareCards) {
    this.numRareCards = numRareCards;
  }

  public CollectorLevel getLevel() {
    return LevelHelper.getLevel(this.points);
  }

  public void addToWishlist(CatalogedCard card) {
    wishlist.insertEnd(card);
  }

  public boolean isInWishlist(CatalogedCard card) {
    return wishlist.get(card) != null;
  }

  public void removeFromWishlist(CatalogedCard card) {
    wishlist.remove(card);
  }

  public void addCard(CatalogedCard card) {
    cards.insertEnd(card);
    if (card.getStoredCard().getCardRating().getValue() == 1000) {
      numRareCards++;
    }
    addPoints(card.getStoredCard().getCardRating().getValue());
  }

  public boolean hasCard(CatalogedCard card) {
    return cards.get(card) != null;
  }

  public int numCards() {
    return cards.size();
  }

  public int numCardsInWishlist() {
    return wishlist.size();
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CardCollector that = (CardCollector) o;
    return Objects.equals(collectorId, that.collectorId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(collectorId);
  }
}
