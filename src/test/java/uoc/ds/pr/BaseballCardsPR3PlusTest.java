package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uoc.ds.pr.exceptions.DSException;
import uoc.ds.pr.model.CardCollector;
import uoc.ds.pr.util.DateUtils;

public class BaseballCardsPR3PlusTest {

  private BaseballCardsPR3 baseballCards;
  private BaseballCardsHelperPR3 helperPR3;

  private void addCardCollectorTest() {
    CardCollector cardCollectorJUAN = baseballCards.addCollector("JUAN", "Juan", "Simó",
        DateUtils.createLocalDate("09-11-1986"), 2000);
    CardCollector cardCollectorANNA = baseballCards.addCollector("ANNA", "Anna", "Lluna",
        DateUtils.createLocalDate("11-03-1977"), 1500);
    CardCollector cardCollectorALBERTO = baseballCards.addCollector("ALBERTO", "Alberto", "López",
        DateUtils.createLocalDate("11-05-1975"), 900);
    CardCollector cardCollectorPATRICIA = baseballCards.addCollector("PATRICIA", "Patricia", "Gomez",
        DateUtils.createLocalDate("01-05-1972"), 900);
    CardCollector cardCollectorMARTA = baseballCards.addCollector("MARTA", "Marta", "Roma",
        DateUtils.createLocalDate("01-11-1976"), 950);
    CardCollector cardCollectorPEDRO = baseballCards.addCollector("PEDRO", "Pedro", "Estaban",
        DateUtils.createLocalDate("01-11-1978"), 955);
    CardCollector cardCollectorPABLO = baseballCards.addCollector("PABLO", "Pablo", "Marmol",
        DateUtils.createLocalDate("08-02-1982"), 975);

    Assert.assertEquals(7, helperPR3.numCardCollectors());
  }


  @Before
  public void setUp() throws Exception {
    this.baseballCards = new BaseballCardsPR3Impl();
    this.helperPR3 = baseballCards.getBaseballCardsHelperPR3();
    addCardCollectorTest();
  }

  @Test
  public void addFollowerTest() throws DSException {

    baseballCards.addFollower("ANNA", "JUAN");
    Assert.assertEquals(1, helperPR3.numFollowers("ANNA"));
    Assert.assertEquals(1, helperPR3.numFollowings("JUAN"));

    baseballCards.addFollower("ALBERTO", "JUAN");
    Assert.assertEquals(1, helperPR3.numFollowers("ALBERTO"));
    Assert.assertEquals(2, helperPR3.numFollowings("JUAN"));

    baseballCards.addFollower("PATRICIA", "ANNA");
    Assert.assertEquals(1, helperPR3.numFollowers("PATRICIA"));
    Assert.assertEquals(1, helperPR3.numFollowings("ANNA"));

    baseballCards.addFollower("PABLO", "PATRICIA");
    Assert.assertEquals(1, helperPR3.numFollowers("PABLO"));
    Assert.assertEquals(1, helperPR3.numFollowings("PATRICIA"));

    baseballCards.addFollower("ALBERTO", "MARTA");
    Assert.assertEquals(2, helperPR3.numFollowers("ALBERTO"));
    Assert.assertEquals(1, helperPR3.numFollowings("MARTA"));

    baseballCards.addFollower("ALBERTO", "PEDRO");
    Assert.assertEquals(3, helperPR3.numFollowers("ALBERTO"));
    Assert.assertEquals(1, helperPR3.numFollowings("PEDRO"));
  }


  @Test
  public void getFollowersTest() throws DSException {

    addFollowerTest();
    Iterator<CardCollector> it = baseballCards.getFollowers("ANNA");
    CardCollector cardCollectorJUAN = it.next();
    Assert.assertEquals("JUAN", cardCollectorJUAN.getCollectorId());
  }


  @Test
  public void getFollowingTest() throws DSException {

    addFollowerTest();
    Iterator<CardCollector> it = baseballCards.getFollowings("JUAN");
    CardCollector cardCollectorANNA = it.next();
    Assert.assertEquals("ANNA", cardCollectorANNA.getCollectorId());
  }

  @Test
  public void getRecommendationTest() throws DSException {

    addFollowerTest();
    Iterator<CardCollector> it = baseballCards.recommendations("JUAN");
    CardCollector cardCollectorPATRICIA = it.next();
    Assert.assertEquals("PATRICIA", cardCollectorPATRICIA.getCollectorId());
    CardCollector cardCollectorMARTA = it.next();
    Assert.assertEquals("MARTA", cardCollectorMARTA.getCollectorId());
    CardCollector cardCollectorPEDRO = it.next();
    Assert.assertEquals("PEDRO", cardCollectorPEDRO.getCollectorId());
    Assert.assertFalse(it.hasNext());
  }


}
