package uoc.ds.pr;


import uoc.ds.pr.enums.WorkerRole;

public class FactoryBaseballsCards {


    public static BaseballCardsPR3 getBaseballCards() throws Exception {
        BaseballCardsPR3 baseballCards;
        baseballCards = new BaseballCardsPR3Impl();

        ///
        /// ENTITIES
        ///
        baseballCards.addEntity("entityId1", "NeoTech Industries", "Future Avenue, 404");
        baseballCards.addEntity("entityId2", "Quantum Dynamics", "Schrödinger’s Cat Street, 1");
        baseballCards.addEntity("entityId3", "Eclipse Syndicate", "Secret Passage, N/A");
        baseballCards.addEntity("entityId4", "Orion Enterprises", "Lunar Orbit, Sector 7G");

        ///
        /// WORKERS
        ///
        baseballCards.addWorker("workerId1", "Pepe", "Gotera", WorkerRole.CATALOGER);
        baseballCards.addWorker("workerId2", "Otilio", "Botched Repairs", WorkerRole.CATALOGER);
        baseballCards.addWorker("workerId3", "Manolo", "y Compañía", WorkerRole.CATALOGER);
        baseballCards.addWorker("workerId4", "Benito", "y Compañía", WorkerRole.CATALOGER);

        baseballCards.addWorker("workerIdLDR1", "Pepón", "Plisa", WorkerRole.LENDER);
        baseballCards.addWorker("workerIdLDR2", "Joana", "López", WorkerRole.LENDER);

        return baseballCards;
    }



}