package uoc.ds.pr.repository;

import edu.uoc.ds.adt.nonlinear.DictionaryAVLImpl;
import edu.uoc.ds.adt.nonlinear.HashTable;
import edu.uoc.ds.adt.nonlinear.graphs.DirectedEdge;
import edu.uoc.ds.adt.nonlinear.graphs.DirectedGraphImpl;
import edu.uoc.ds.adt.nonlinear.graphs.Edge;
import edu.uoc.ds.adt.nonlinear.graphs.Vertex;
import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.traversal.Iterator;
import java.time.LocalDate;
import uoc.ds.pr.BaseballCardsPR3;
import uoc.ds.pr.exceptions.CardCollectorNotFoundException;
import uoc.ds.pr.model.CardCollector;
import uoc.ds.pr.model.CatalogedCard;
import uoc.ds.pr.util.OrderedVector;

public class CardCollectorRepository {

  private static final int MAX_TOP_COLLECTORS = 5;

  private final BaseballCardsPR3 baseballCards;
  private final DictionaryAVLImpl<String, CardCollector> collectors;
  private final DirectedGraphImpl<CardCollector, Object> socialGraph;
  private final OrderedVector<CardCollector> topCollectors;

  public CardCollectorRepository(BaseballCardsPR3 baseballCards) {
    this.baseballCards = baseballCards;
    this.collectors = new DictionaryAVLImpl<>();
    this.socialGraph = new DirectedGraphImpl<>();
    this.topCollectors = new OrderedVector<>(MAX_TOP_COLLECTORS, CardCollector.COMPARATOR_RARE_CARDS);
  }

  public CardCollector addCollector(String collectorId, String name, String surname, LocalDate birthDate,
      double balance) {
    CardCollector cardCollector = collectors.get(collectorId);
    if (cardCollector == null) {
      cardCollector = new CardCollector(collectorId, name, surname, birthDate, balance);
      collectors.put(collectorId, cardCollector);
      socialGraph.newVertex(cardCollector);
    } else {
      cardCollector.setName(name);
      cardCollector.setSurname(surname);
      cardCollector.setBirthday(birthDate);
      cardCollector.setBalance(balance);
    }
    return cardCollector;
  }

  public CardCollector getCardCollector(String collectorId) throws CardCollectorNotFoundException {
    CardCollector cardCollector = collectors.get(collectorId);
    if (cardCollector == null) {
      throw new CardCollectorNotFoundException();
    }
    return cardCollector;
  }

  public int numCardCollectors() {
    return collectors.size();
  }

  public void addToWishlist(CatalogedCard card, CardCollector cardCollector) {
    cardCollector.addToWishlist(card);
  }

  public boolean isInWishlist(CatalogedCard card, CardCollector cardCollector) {
    return cardCollector.isInWishlist(card);
  }

  public void removeFromWishlist(CatalogedCard card, CardCollector cardCollector) {
    cardCollector.removeFromWishlist(card);
  }

  public void addCard(CatalogedCard card, CardCollector cardCollector) {
    cardCollector.addCard(card);
    updateTopCollectos(cardCollector);
  }

  private void updateTopCollectos(CardCollector cardCollector) {
    if (cardCollector.getNumRareCards() > 0) {
      topCollectors.update(cardCollector);
    }
  }

  public Iterator<CardCollector> getTop5ByRareCards() {
    return topCollectors.values();
  }

  public int numCardsInWishlist(String collectorId) {
    CardCollector cardCollector = collectors.get(collectorId);
    return cardCollector.numCardsInWishlist();
  }

  public int numCardsByCardCollector(String cardCollectorId) {
    CardCollector cardCollector = collectors.get(cardCollectorId);
    return (cardCollector != null) ? cardCollector.getNumRareCards() : 0;
  }

  public void addFollower(CardCollector followed, CardCollector follower) {
    Vertex<CardCollector> followerVertex = socialGraph.getVertex(follower);
    Vertex<CardCollector> followedVertex = socialGraph.getVertex(followed);

    if (followerVertex == null) {
      followerVertex = socialGraph.newVertex(follower);
    }
    if (followedVertex == null) {
      followedVertex = socialGraph.newVertex(followed);
    }

    socialGraph.newEdge(followerVertex, followedVertex);
  }

  public Iterator<CardCollector> getFollowers(CardCollector cardCollector) {
    Vertex<CardCollector> vertex = socialGraph.getVertex(cardCollector);
    LinkedList<CardCollector> followers = new LinkedList<>();
    if (vertex != null) {
      Iterator<Edge<Object, CardCollector>> edgeIterator = socialGraph.edgedWithDestination(vertex);
      while (edgeIterator.hasNext()) {
        DirectedEdge<Object, CardCollector> edge = (DirectedEdge<Object, CardCollector>) edgeIterator.next();
        followers.insertEnd(edge.getVertexSrc().getValue());
      }
    }
    return followers.values();
  }

  public Iterator<CardCollector> getFollowings(CardCollector cardCollector) {
    Vertex<CardCollector> vertex = socialGraph.getVertex(cardCollector);
    LinkedList<CardCollector> followings = new LinkedList<>();
    if (vertex != null) {
      Iterator<Edge<Object, CardCollector>> edgeIterator = socialGraph.edgesWithSource(vertex);
      while (edgeIterator.hasNext()) {
        DirectedEdge<Object, CardCollector> edge = (DirectedEdge<Object, CardCollector>) edgeIterator.next();
        followings.insertEnd(edge.getVertexDst().getValue());
      }
    }
    return followings.values();
  }

  public Iterator<CardCollector> getRecommendations(CardCollector cardCollector) {
    LinkedList<CardCollector> recommendations = new LinkedList<>();
    HashTable<CardCollector, Object> added = new HashTable<>();

    Vertex<CardCollector> vertex = socialGraph.getVertex(cardCollector);
    if (vertex == null) {
      return recommendations.values();
    }

    Iterator<CardCollector> followings = getFollowings(cardCollector);
    while (followings.hasNext()) {
      CardCollector following = followings.next();
      Iterator<CardCollector> followingOfFollowing = getFollowings(following);
      while (followingOfFollowing.hasNext()) {
        CardCollector recommendation = followingOfFollowing.next();
        if (!recommendation.equals(cardCollector) && !isFollowing(cardCollector, recommendation)
            && added.get(recommendation) == null) {
          recommendations.insertEnd(recommendation);
          added.put(recommendation, new Object());
        }
      }
    }
    Iterator<CardCollector> cofollower = getFollowings(cardCollector);
    while (cofollower.hasNext()) {
      CardCollector personUserFollows = cofollower.next();
      Iterator<CardCollector> followingOfFollowing = getFollowers(personUserFollows);
      while (followingOfFollowing.hasNext()) {
        CardCollector coFollowerCandidate = followingOfFollowing.next();
        if (!coFollowerCandidate.equals(cardCollector) &&
            !isFollowing(cardCollector, coFollowerCandidate) &&
            added.get(coFollowerCandidate) == null) {
          recommendations.insertEnd(coFollowerCandidate);
          added.put(coFollowerCandidate, new Object());
        }
      }
    }
    return recommendations.values();
  }

  public boolean isFollowing(CardCollector follower, CardCollector followed) {
    Iterator<CardCollector> followings = getFollowings(follower);
    while (followings.hasNext()) {
      if (followings.next().getCollectorId().equals(followed.getCollectorId())) {
        return true;
      }
    }
    return false;
  }

  public int numFollowers(String collectorId) {
    CardCollector cardCollector = collectors.get(collectorId);
    if (cardCollector == null) {
      return 0;
    }
    int count = 0;
    Iterator<CardCollector> followers = getFollowers(cardCollector);
    while (followers.hasNext()) {
      followers.next();
      count++;
    }
    return count;
  }

  public int numFollowings(String collectorId) {
    CardCollector cardCollector = collectors.get(collectorId);
    if (cardCollector == null) {
      return 0;
    }
    int count = 0;
    Iterator<CardCollector> followings = getFollowings(cardCollector);
    while (followings.hasNext()) {
      followings.next();
      count++;
    }
    return count;
  }
}
