package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uoc.ds.pr.enums.CardRating;
import uoc.ds.pr.enums.CardStatus;
import uoc.ds.pr.enums.LoanStatus;
import uoc.ds.pr.enums.WorkerRole;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.util.CardsData;
import uoc.ds.pr.util.CardsDataHelper;
import uoc.ds.pr.util.DateUtils;

import static uoc.ds.pr.BaseballCards.MAX_LOANS_BY_ENTITY;
import static uoc.ds.pr.util.CardsDataHelper.addCardsData;

public class BaseballCardsPR2Test {

    protected BaseballCardsPR3 baseballCards;
    protected BaseballCardsHelper helper;

    @Before
    public void setUp() throws Exception {
        this.baseballCards = FactoryBaseballsCards.getBaseballCards();
        this.helper = this.baseballCards.getBaseballCardsHelper();
    }


    @After
    public void tearDown() {
        this.baseballCards = null;
    }



    @Test
    public void addEntityTest() {
        Assert.assertEquals(4, helper.numEntities());
        baseballCards.addEntity("entityId5", "ACME Corporation", "Road Runner/Wile E. Coyote ");
        Assert.assertEquals(5, helper.numEntities());

        baseballCards.addEntity("entityId6", "ACME Counications", "Road Runner/Wile E. Coyote sector 9G ");
        Assert.assertEquals(6, helper.numEntities());

        baseballCards.addEntity("entityId6", "ACME Communications", "Road Runner/Wile E. Coyote sector 9G ");
        Assert.assertEquals(6, helper.numEntities());
    }

    @Test
    public void addWorkerTest() {
        Assert.assertEquals(6, helper.numWorkers());
        baseballCards.addWorker("workerId5", "Tato", "y Compañía", WorkerRole.CATALOGER);
        Assert.assertEquals(7, helper.numWorkers());

        baseballCards.addWorker("workerId6", "XXXXX", "YYYYY", WorkerRole.CATALOGER);
        Assert.assertEquals(8, helper.numWorkers());
        Worker w = helper.getWorker("workerId6");
        Assert.assertEquals("XXXXX", w.getName());

        baseballCards.addWorker("workerId6", "Tania", "y Compañía", WorkerRole.CATALOGER);
        Assert.assertEquals(8, helper.numWorkers());
        Assert.assertEquals("Tania", w.getName());
    }


    @Test
    public void storeCardTest() {

        addCardsData(baseballCards, CardsData.baseballCards1);
        addCardsData(baseballCards, CardsData.baseballCards2);
        addCardsData(baseballCards, CardsData.baseballCards3);

        Assert.assertEquals(200, helper.numStoredCards());

        baseballCards.storeCard("C201", "Julio Rodríguez", 2022, "Topps Chrome", CardStatus.GOOD, CardRating.FIVE_STARS);
        baseballCards.storeCard("C202", "Spencer Torkelson", 2022, "Donruss", CardStatus.MINT, CardRating.FIVE_STARS);
        baseballCards.storeCard("C203", "Adley Rutschman", 2023, "Topps Chrome", CardStatus.POOR, CardRating.ONE_STAR);

        Assert.assertEquals(203, helper.numStoredCards());
    }


    @Test
    public void catalogCardTest() throws DSException {
        Assert.assertThrows(WorkerNotFoundException.class, () ->
                baseballCards.catalogCard("XXXX"));

        Assert.assertThrows(NoCardException.class, () ->
                baseballCards.catalogCard("workerId1"));

        storeCardTest();
        Assert.assertEquals(203, helper.numStoredCards());
        Assert.assertEquals(5, helper.numCardCases());
        Assert.assertEquals(0, helper.numCatalogedCards());

        CatalogedCard catalogedCard1 = baseballCards.catalogCard("workerId1");
        Assert.assertEquals("C201", catalogedCard1.getCardId());
        Assert.assertEquals("Julio Rodríguez", catalogedCard1.getPlayer());
        Assert.assertEquals(202, helper.numStoredCards());
        Assert.assertEquals(5, helper.numCardCases());
        Assert.assertEquals(1, helper.numCatalogedCardsByWorker("workerId1"));

        CatalogedCard catalogedCard2 = baseballCards.catalogCard("workerId1");
        Assert.assertEquals("C202", catalogedCard2.getCardId());
        Assert.assertEquals("Spencer Torkelson", catalogedCard2.getPlayer());
        Assert.assertEquals(201, helper.numStoredCards());
        Assert.assertEquals(2, helper.numCatalogedCards());
        Assert.assertEquals(5, helper.numCardCases());
        Assert.assertEquals(2, helper.numCatalogedCardsByWorker("workerId1"));

        CatalogedCard catalogedCard3 = baseballCards.catalogCard("workerId1");
        Assert.assertEquals("C203", catalogedCard3.getCardId());
        Assert.assertEquals("Adley Rutschman", catalogedCard3.getPlayer());
        Assert.assertEquals(200, helper.numStoredCards());
        Assert.assertEquals(3, helper.numCatalogedCards());
        Assert.assertEquals(4, helper.numCardCases());
        Assert.assertEquals(3, helper.numCatalogedCardsByWorker("workerId1"));

        CatalogedCard catalogedCard4 = baseballCards.catalogCard("workerId2");
        Assert.assertEquals("C151", catalogedCard4.getCardId());
        Assert.assertEquals("Walker Buehler", catalogedCard4.getPlayer());
        Assert.assertEquals(4, helper.numCatalogedCards());
        Assert.assertEquals(199, helper.numStoredCards());
        Assert.assertEquals(4, helper.numCardCases());
        Assert.assertEquals(3, helper.numCatalogedCardsByWorker("workerId1"));
        Assert.assertEquals(1, helper.numCatalogedCardsByWorker("workerId2"));

        CatalogedCard catalogedCard5 = baseballCards.catalogCard("workerId2");
        Assert.assertEquals("C152", catalogedCard5.getCardId());
        Assert.assertEquals("Spencer Torkelson", catalogedCard5.getPlayer());
        Assert.assertEquals(5, helper.numCatalogedCards());
        Assert.assertEquals(198, helper.numStoredCards());
        Assert.assertEquals(4, helper.numCardCases());
        Assert.assertEquals(3, helper.numCatalogedCardsByWorker("workerId1"));
        Assert.assertEquals(2, helper.numCatalogedCardsByWorker("workerId2"));

        CatalogedCard catalogedCard6 = baseballCards.catalogCard("workerId3");
        Assert.assertEquals("C153", catalogedCard6.getCardId());
        Assert.assertEquals("Zack Greinke", catalogedCard6.getPlayer());
        Assert.assertEquals(6, helper.numCatalogedCards());
        Assert.assertEquals(197, helper.numStoredCards());
        Assert.assertEquals(4, helper.numCardCases());
        Assert.assertEquals(3, helper.numCatalogedCardsByWorker("workerId1"));
        Assert.assertEquals(2, helper.numCatalogedCardsByWorker("workerId2"));
        Assert.assertEquals(1, helper.numCatalogedCardsByWorker("workerId3"));

        CatalogedCard catalogedCard7 = baseballCards.catalogCard("workerId3");
        Assert.assertEquals("C154", catalogedCard7.getCardId());
        Assert.assertEquals("Spencer Torkelson", catalogedCard7.getPlayer());
        Assert.assertEquals(7, helper.numCatalogedCards());
        Assert.assertEquals(196, helper.numStoredCards());
        Assert.assertEquals(4, helper.numCardCases());
        Assert.assertEquals(3, helper.numCatalogedCardsByWorker("workerId1"));
        Assert.assertEquals(2, helper.numCatalogedCardsByWorker("workerId2"));
        Assert.assertEquals(2, helper.numCatalogedCardsByWorker("workerId3"));

        CatalogedCard catalogedCard8 = baseballCards.catalogCard("workerId3");
        Assert.assertEquals("C155", catalogedCard8.getCardId());
        Assert.assertEquals("Yu Darvish", catalogedCard8.getPlayer());
        Assert.assertEquals(8, helper.numCatalogedCards());
        Assert.assertEquals(195, helper.numStoredCards());


        Assert.assertEquals(1, helper.numCatalogedCardsByPlayer("Julio Rodríguez"));
        Assert.assertEquals(3, helper.numCatalogedCardsByPlayer("Spencer Torkelson"));
        Assert.assertEquals(1, helper.numCatalogedCardsByPlayer("Adley Rutschman"));
        Assert.assertEquals(1, helper.numCatalogedCardsByPlayer("Walker Buehler"));

        Assert.assertEquals(6, helper.numCatalogedCardsByCollection("Topps Chrome"));
        Assert.assertEquals(2, helper.numCatalogedCardsByCollection("Donruss"));

        baseballCards.addWorker("NO_CATALOGER", "Pepe", "Otilio", WorkerRole.AUCTIONEER);

        Assert.assertThrows(WorkerNotAllowedException.class, () ->
            baseballCards.catalogCard("NO_CATALOGER"));
    }


    @Test
    public void lendCardTest() throws DSException {
        Assert.assertThrows(EntityNotFoundException.class, () ->
                baseballCards.lendCard("LOANID1", "XXXX", "C001", "workerId1",
                        DateUtils.createLocalDate("01-04-2025"), DateUtils.createLocalDate("01-05-2025")));

        Assert.assertThrows(CatalogedCardNotFoundException.class, () ->
                baseballCards.lendCard("LOANID1", "entityId1", "XXXX", "workerIdLDR1",
                        DateUtils.createLocalDate("01-04-2025"), DateUtils.createLocalDate("01-05-2025")));

        Assert.assertThrows(WorkerNotFoundException.class, () ->
                baseballCards.lendCard("LOANID1", "entityId1", "C154", "XXXXX",
                        DateUtils.createLocalDate("01-04-2025"), DateUtils.createLocalDate("01-05-2025")));

        catalogCardTest();

        Loan loan1 = baseballCards.lendCard("LOANID1", "entityId1", "C154", "workerIdLDR1",
                DateUtils.createLocalDate("01-04-2025"), DateUtils.createLocalDate("01-05-2025"));
        Assert.assertEquals(LoanStatus.IN_PROGRESS, loan1.getStatus());
        Assert.assertEquals(1, helper.numLoans());
        Assert.assertEquals(1, helper.numLoansByEntity("entityId1"));
        Assert.assertEquals(1, helper.numLoansByWorker("workerIdLDR1"));

        Loan loan2 = baseballCards.lendCard("LOANID2", "entityId1", "C201", "workerIdLDR1",
                DateUtils.createLocalDate("01-04-2025"), DateUtils.createLocalDate("01-05-2025"));
        Assert.assertEquals(LoanStatus.IN_PROGRESS, loan2.getStatus());
        Assert.assertEquals(2, helper.numLoans());
        Assert.assertEquals(2, helper.numLoansByEntity("entityId1"));
        Assert.assertEquals(2, helper.numLoansByWorker("workerIdLDR1"));

        Loan loan3 = baseballCards.lendCard("LOANID3", "entityId1", "C202", "workerIdLDR1",
                DateUtils.createLocalDate("01-04-2025"), DateUtils.createLocalDate("01-05-2025"));
        Assert.assertEquals(LoanStatus.IN_PROGRESS, loan3.getStatus());

        Assert.assertEquals(3, helper.numLoans());
        Assert.assertEquals(3, helper.numLoansByEntity("entityId1"));
        Assert.assertEquals(3, helper.numLoansByWorker("workerIdLDR1"));

        Loan loan4 = baseballCards.lendCard("LOANID4", "entityId1", "C203", "workerIdLDR1",
                DateUtils.createLocalDate("01-04-2025"), DateUtils.createLocalDate("01-05-2025"));
        Assert.assertEquals(LoanStatus.IN_PROGRESS, loan4.getStatus());
        Assert.assertEquals(4, helper.numLoans());
        Assert.assertEquals(4, helper.numLoansByEntity("entityId1"));
        Assert.assertEquals(4, helper.numLoansByWorker("workerIdLDR1"));

        Assert.assertThrows(CatalogedCardAlreadyLoanedException.class, () ->
                baseballCards.lendCard("LOANID2", "entityId2", "C154", "workerIdLDR2",
                DateUtils.createLocalDate("01-04-2025"), DateUtils.createLocalDate("01-05-2025")));

        Loan loan5 = baseballCards.lendCard("LOANID5", "entityId2", "C153", "workerIdLDR1",
                DateUtils.createLocalDate("01-04-2025"), DateUtils.createLocalDate("01-05-2025"));
        Assert.assertEquals(LoanStatus.IN_PROGRESS, loan5.getStatus());
        Assert.assertEquals(5, helper.numLoans());
        Assert.assertEquals(4, helper.numLoansByEntity("entityId1"));
        Assert.assertEquals(1, helper.numLoansByEntity("entityId2"));
        Assert.assertEquals(5, helper.numLoansByWorker("workerIdLDR1"));
        Assert.assertEquals(0, helper.numLoansByWorker("workerIdLDR2"));

        Loan loan6 = baseballCards.lendCard("LOANID6", "entityId2", "C152", "workerIdLDR2",
                DateUtils.createLocalDate("01-04-2025"), DateUtils.createLocalDate("01-05-2025"));
        Assert.assertEquals(LoanStatus.IN_PROGRESS, loan6.getStatus());
        Assert.assertEquals(6, helper.numLoans());
        Assert.assertEquals(4, helper.numLoansByEntity("entityId1"));
        Assert.assertEquals(2, helper.numLoansByEntity("entityId2"));

        Assert.assertEquals(5, helper.numLoansByWorker("workerIdLDR1"));
        Assert.assertEquals(1, helper.numLoansByWorker("workerIdLDR2"));

        Loan loan7 = baseballCards.lendCard("LOANID7", "entityId2", "C151", "workerIdLDR2",
                DateUtils.createLocalDate("01-04-2025"), DateUtils.createLocalDate("01-05-2025"));
        Assert.assertEquals(LoanStatus.IN_PROGRESS, loan7.getStatus());
        Assert.assertEquals(7, helper.numLoans());
        Assert.assertEquals(4, helper.numLoansByEntity("entityId1"));
        Assert.assertEquals(3, helper.numLoansByEntity("entityId2"));
        Assert.assertEquals(5, helper.numLoansByWorker("workerIdLDR1"));
        Assert.assertEquals(2, helper.numLoansByWorker("workerIdLDR2"));

        baseballCards.addWorker("NO_LENDER", "Pepe", "Otilio", WorkerRole.AUCTIONEER);

        Assert.assertThrows(WorkerNotAllowedException.class, () ->
                baseballCards.lendCard("LOANID7", "entityId2", "C155", "NO_LENDER",
                DateUtils.createLocalDate("01-04-2025"), DateUtils.createLocalDate("01-05-2025")));


    }

    @Test
    public void lendCardTest2() throws DSException {
        storeCardTest();

        Assert.assertEquals(203, helper.numStoredCards());
        int storedCards = helper.numStoredCards();

        for (int i = 0; i < storedCards; i++) {
            baseballCards.catalogCard("workerId1");
        }

        Assert.assertEquals(storedCards, helper.numCatalogedCards());

        for (int i = 1; i <= MAX_LOANS_BY_ENTITY; i++) {
            baseballCards.lendCard("LOANID"+i, "entityId2", CardsDataHelper.generateCardId(i), "workerIdLDR2",
                    DateUtils.createLocalDate("01-04-2025"), DateUtils.createLocalDate("01-05-2025"));
        }
    }

    @Test
    public void lendCardTest3() throws DSException {
        lendCardTest2();

        Assert.assertEquals(MAX_LOANS_BY_ENTITY, helper.numLoansByEntity("entityId2"));

        Assert.assertThrows(MaximumNumberOfLoansException.class, () ->
                baseballCards.lendCard("LOANID51", "entityId2", "C051", "workerIdLDR2",
                        DateUtils.createLocalDate("01-04-2025"), DateUtils.createLocalDate("01-05-2025")));

    }


    @Test
    public void giveBackCardTest() throws DSException {
        lendCardTest();

        Assert.assertThrows(LoanNotFoundException.class, () ->
                baseballCards.givebackCard("XXXX",  DateUtils.createLocalDate("10-11-2024")));

        Loan loan1 = baseballCards.givebackCard("LOANID1",
                DateUtils.createLocalDate("02-05-2025"));
        Assert.assertEquals(LoanStatus.DELAYED, loan1.getStatus());
        Assert.assertEquals("entityId1", loan1.getEntity().getEntityId());

        Loan loan2 = baseballCards.givebackCard("LOANID3",
                DateUtils.createLocalDate("02-05-2025"));
        Assert.assertEquals(LoanStatus.DELAYED, loan2.getStatus());
        Assert.assertEquals("entityId1", loan2.getEntity().getEntityId());

        Loan loan3 = baseballCards.givebackCard("LOANID4",
                DateUtils.createLocalDate("03-04-2025"));
        Assert.assertEquals(LoanStatus.COMPLETED, loan3.getStatus());
        Assert.assertEquals("entityId1", loan3.getEntity().getEntityId());


        Assert.assertEquals(3, helper.numClosedLoansByEntity("entityId1"));
        Assert.assertEquals(3, helper.numClosedLoansByEntity("entityId1"));

    }


    @Test
    public void timeToBeCatalogedTest() throws DSException {
        Assert.assertThrows(StoredCardNotFoundException.class, () ->
                baseballCards.timeToBeCataloged("XXXX",  12, 3));

        storeCardTest();

        Assert.assertThrows(InvalidLotPreparationTimeException.class, () ->
                baseballCards.timeToBeCataloged("C202",  -1, 3));

        Assert.assertThrows(InvalidCatalogTimeException.class, () ->
                baseballCards.timeToBeCataloged("C202",  12, -3));

        Assert.assertThrows(InvalidCatalogTimeException.class, () ->
                baseballCards.timeToBeCataloged("C202",  12, -3));

        int t = baseballCards.timeToBeCataloged("C154",  8, 12);
        Assert.assertEquals(100, t);
    }

    @Test
    public void getAllCardsByPlayerTest() throws DSException {
        catalogCardTest();

        Assert.assertThrows(NoCardException.class, () ->
                baseballCards.getAllCardsByPlayer("XXXXX"));


        Assert.assertEquals(3, helper.numCatalogedCardsByPlayer("Spencer Torkelson"));
        Assert.assertEquals(1, helper.numCatalogedCardsByPlayer("Adley Rutschman"));

        // Always with the full name
        Iterator<CatalogedCard> it1 = baseballCards.getAllCardsByPlayer("Spencer Torkelson");
        Assert.assertEquals("C202", it1.next().getCardId());
        Assert.assertEquals("C152", it1.next().getCardId());
        Assert.assertEquals("C154", it1.next().getCardId());
        Assert.assertFalse(it1.hasNext());

        // Always with the full name
        Iterator<CatalogedCard> it2 = baseballCards.getAllCardsByPlayer("Adley Rutschman");
        Assert.assertEquals("C203", it2.next().getCardId());
        Assert.assertFalse(it2.hasNext());
    }

    @Test
    public void getAllCardsByCollection() throws DSException {
        catalogCardTest();

        Assert.assertThrows(NoCardException.class, () ->
                baseballCards.getAllCardsByCollection("XXXXX"));

        Assert.assertEquals(6, helper.numCatalogedCardsByCollection("Topps Chrome"));
        Assert.assertEquals(2, helper.numCatalogedCardsByCollection("Donruss"));

        // Always with the full name
        Iterator<CatalogedCard> it1 = baseballCards.getAllCardsByCollection("Topps Chrome");
        Assert.assertEquals("C201", it1.next().getCardId());
        Assert.assertEquals("C203", it1.next().getCardId());
        Assert.assertEquals("C151", it1.next().getCardId());
        Assert.assertEquals("C152", it1.next().getCardId());
        Assert.assertEquals("C153", it1.next().getCardId());
        Assert.assertEquals("C155", it1.next().getCardId());
        Assert.assertFalse(it1.hasNext());

        // Always with the full name
        Iterator<CatalogedCard> it2 = baseballCards.getAllCardsByCollection("Donruss");
        Assert.assertEquals("C202", it2.next().getCardId());
        Assert.assertEquals("C154", it2.next().getCardId());
        Assert.assertFalse(it2.hasNext());
    }

    @Test
    public void getAllLoansByEntityTest() throws DSException {
        giveBackCardTest();
        Assert.assertThrows(NoLoanException.class, () ->
                baseballCards.getAllLoansByEntity("entityId4"));

        Assert.assertEquals(4, helper.numLoansByEntity("entityId1"));
        Iterator<Loan> it1 = baseballCards.getAllLoansByEntity("entityId1");
        Assert.assertEquals("LOANID1", it1.next().getLoanId());
        Assert.assertEquals("LOANID2", it1.next().getLoanId());
        Assert.assertEquals("LOANID3", it1.next().getLoanId());
        Assert.assertEquals("LOANID4", it1.next().getLoanId());
        Assert.assertFalse(it1.hasNext());

        Assert.assertEquals(3, helper.numLoansByEntity("entityId2"));
        Iterator<Loan> it2 = baseballCards.getAllLoansByEntity("entityId2");
        Assert.assertEquals("LOANID5", it2.next().getLoanId());
        Assert.assertEquals("LOANID6", it2.next().getLoanId());
        Assert.assertEquals("LOANID7", it2.next().getLoanId());
        Assert.assertFalse(it2.hasNext());
    }

    @Test
    public void getAllLoansByStateTest() throws DSException {

        giveBackCardTest();

        Iterator<Loan> it1 = baseballCards.getAllLoansByState("entityId2", LoanStatus.IN_PROGRESS);

        Loan loan1 = it1.next();
        Assert.assertEquals("LOANID5", loan1.getLoanId());
        Assert.assertEquals(LoanStatus.IN_PROGRESS, loan1.getStatus());

        Loan loan2 = it1.next();
        Assert.assertEquals("LOANID6", loan2.getLoanId());
        Assert.assertEquals(LoanStatus.IN_PROGRESS, loan2.getStatus());

        Loan loan3 = it1.next();
        Assert.assertEquals("LOANID7", loan3.getLoanId());
        Assert.assertEquals(LoanStatus.IN_PROGRESS, loan3.getStatus());
        Assert.assertFalse(it1.hasNext());

        Iterator<Loan> it2 = baseballCards.getAllLoansByState("entityId1", LoanStatus.DELAYED);
        Loan loan4 = it2.next();
        Assert.assertEquals("LOANID1", loan4.getLoanId());
        Assert.assertEquals(LoanStatus.DELAYED, loan4.getStatus());
        Loan loan5 = it2.next();
        Assert.assertEquals("LOANID3", loan5.getLoanId());
        Assert.assertEquals(LoanStatus.DELAYED, loan5.getStatus());
        Assert.assertFalse(it2.hasNext());



    }

    @Test
    public void getEntityTheMostTest() throws DSException {
        Assert.assertThrows(NoEntityException.class, () ->
                baseballCards.getEntityTheMost());

        lendCardTest2();

        Entity entity = baseballCards.getEntityTheMost();
        Assert.assertEquals("entityId2", entity.getEntityId());
        Assert.assertEquals(100, helper.numLoansByEntity("entityId2"));
    }

    @Test
    public void getMostShownCard() throws DSException {
        Assert.assertThrows(NoCardException.class, () ->
                baseballCards.getMostShownCard());


        giveBackCardTest();
        CatalogedCard mostShownCard = baseballCards.getMostShownCard();
        Assert.assertEquals("C154", mostShownCard.getCardId());
        Assert.assertEquals(1, helper.numLoansByCard("C154"));

        Loan loan8 = baseballCards.lendCard("LOANID8", "entityId1", "C203", "workerIdLDR2",
                DateUtils.createLocalDate("03-05-2025"), DateUtils.createLocalDate("15-05-2025"));
        Assert.assertEquals(LoanStatus.IN_PROGRESS, loan8.getStatus());
        loan8 = baseballCards.givebackCard("LOANID8", DateUtils.createLocalDate("10-05-2025"));

        mostShownCard = baseballCards.getMostShownCard();
        Assert.assertEquals(2, helper.numLoansByCard("C203"));
        Assert.assertEquals("C203", mostShownCard.getCardId());

        Loan loan10 = baseballCards.lendCard("LOANID10", "entityId1", "C202", "workerIdLDR2",
                DateUtils.createLocalDate("03-05-2025"), DateUtils.createLocalDate("15-05-2025"));
        Assert.assertEquals(LoanStatus.IN_PROGRESS, loan10.getStatus());
        loan10 = baseballCards.givebackCard("LOANID10", DateUtils.createLocalDate("10-05-2025"));

        Loan loan20 = baseballCards.lendCard("LOANID20", "entityId2", "C202", "workerIdLDR2",
                DateUtils.createLocalDate("23-05-2025"), DateUtils.createLocalDate("29-05-2025"));
        Assert.assertEquals(LoanStatus.IN_PROGRESS, loan20.getStatus());
        loan20 = baseballCards.givebackCard("LOANID20", DateUtils.createLocalDate("28-05-2025"));

        mostShownCard = baseballCards.getMostShownCard();
        Assert.assertEquals(3, helper.numLoansByCard("C202"));

        Assert.assertEquals("C202", mostShownCard.getCardId());

    }
}