package uoc.ds.pr.model;

import java.util.Comparator;

public class Bid {

  public static final Comparator<Bid> COMPARE_PRICE = (b1, b2) -> Double.compare(b1.getPrice(), b2.getPrice());
  public static final Comparator<Bid> COMPARE_LEVEL = (b1, b2) -> Integer.compare(
      b1.getCardCollector().getLevel().getValue(), b2.getCardCollector().getLevel().getValue());

  private final CardCollector cardCollector;
  private final CatalogedCard catalogedCard;
  private final double price;

  public Bid(CardCollector cardCollector, CatalogedCard catalogedCard, double price) {
    this.cardCollector = cardCollector;
    this.catalogedCard = catalogedCard;
    this.price = price;
  }

  public CardCollector getCardCollector() {
    return cardCollector;
  }

  public CatalogedCard getCatalogedCard() {
    return catalogedCard;
  }

  public double getPrice() {
    return price;
  }

}
