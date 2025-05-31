package uoc.ds.pr.repository;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.BaseballCards;
import uoc.ds.pr.BaseballCardsHelper;
import uoc.ds.pr.model.CatalogedCard;
import uoc.ds.pr.model.Player;
import uoc.ds.pr.util.DSLinkedList;

public class PlayerRepository {
    private BaseballCards baseballCards;
    private BaseballCardsHelper helper;
    protected DSLinkedList<Player> players;

    public PlayerRepository(BaseballCards baseballCards) {
        this.baseballCards = baseballCards;
        this.helper = baseballCards.getBaseballCardsHelper();
        this.players = new DSLinkedList<>(Player.CMP_ID);
    }

    public void addCatalogedCard(CatalogedCard catalogedCard) {
        Player player = helper.getPlayer(catalogedCard.getPlayer());
        if (player == null) {
            player = new Player(catalogedCard.getPlayer());
            players.insertEnd(player);
        }

        player.addCataloguedCard(catalogedCard);

    }

    public int numCatalogedCards(String player) {
        Player thePlayer = getPlayer(player);

        return (thePlayer!=null?thePlayer.numCatalogedCards():0);
    }

    public Player getPlayer(String id) {
        return players.get(new Player(id));
    }

    public Iterator<CatalogedCard> getCatalogedCards(String playerName) {
        Iterator<CatalogedCard> it = null;
        Player player = getPlayer(playerName);
        if (player != null) {
            it = player.catalogedCards();
        }
        return it;
    }
}
