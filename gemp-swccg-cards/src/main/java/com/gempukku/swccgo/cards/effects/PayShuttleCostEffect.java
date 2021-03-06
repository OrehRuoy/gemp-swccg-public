package com.gempukku.swccgo.cards.effects;

import com.gempukku.swccgo.game.PhysicalCard;
import com.gempukku.swccgo.game.SwccgGame;
import com.gempukku.swccgo.logic.actions.SubAction;
import com.gempukku.swccgo.logic.effects.UseForceEffect;
import com.gempukku.swccgo.logic.timing.AbstractSubActionEffect;
import com.gempukku.swccgo.logic.timing.Action;

/**
 * An effect that pays the cost for a character or vehicle to be shuttled.
 */
public class PayShuttleCostEffect extends AbstractSubActionEffect {
    private String _playerId;
    private PhysicalCard _cardToMove;
    private PhysicalCard _moveFrom;
    private PhysicalCard _moveTo;
    private float _changeInCost;

    /**
     * Creates an effect that pays the cost for a character to vehicle be shuttled.
     * @param action the action performing this effect
     * @param playerId the player to pay the cost
     * @param cardToMove the card to move
     * @param moveFrom the location to shuttle from (or location the starship is at if shuttling from a starship)
     * @param moveTo the location to shuttle to (or location the starship is at if shuttling to a starship)
     * @param changeInCost change in amount of Force (can be positive or negative) required
     */
    public PayShuttleCostEffect(Action action, String playerId, PhysicalCard cardToMove, PhysicalCard moveFrom, PhysicalCard moveTo, float changeInCost) {
        super(action);
        _playerId = playerId;
        _cardToMove = cardToMove;
        _moveFrom = moveFrom;
        _moveTo = moveTo;
        _changeInCost = changeInCost;
    }

    @Override
    public boolean isPlayableInFull(SwccgGame game) {
        return true;
    }

    @Override
    protected SubAction getSubAction(SwccgGame game) {
        SubAction subAction = new SubAction(_action);

        float moveCost = game.getModifiersQuerying().getShuttleCost(game.getGameState(), _cardToMove, _moveFrom, _moveTo, _changeInCost);
        if (moveCost > 0) {
            subAction.appendEffect(new UseForceEffect(subAction, _playerId, moveCost));
        }

        return subAction;
    }

    @Override
    protected boolean wasActionCarriedOut() {
        return true;
    }
}
