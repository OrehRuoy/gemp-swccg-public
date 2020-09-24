package com.gempukku.swccgo.cards.set6.dark;

import com.gempukku.swccgo.cards.AbstractAlien;
import com.gempukku.swccgo.cards.GameConditions;
import com.gempukku.swccgo.cards.effects.usage.OncePerPhaseEffect;
import com.gempukku.swccgo.common.*;
import com.gempukku.swccgo.filters.Filter;
import com.gempukku.swccgo.filters.Filters;
import com.gempukku.swccgo.game.PhysicalCard;
import com.gempukku.swccgo.game.SwccgGame;
import com.gempukku.swccgo.logic.GameUtils;
import com.gempukku.swccgo.logic.TriggerConditions;
import com.gempukku.swccgo.logic.actions.OptionalGameTextTriggerAction;
import com.gempukku.swccgo.logic.actions.TopLevelGameTextAction;
import com.gempukku.swccgo.logic.effects.*;
import com.gempukku.swccgo.logic.effects.choose.ChooseCardOnTableEffect;
import com.gempukku.swccgo.logic.modifiers.*;
import com.gempukku.swccgo.logic.timing.Action;
import com.gempukku.swccgo.logic.timing.EffectResult;
import com.gempukku.swccgo.logic.timing.results.HitResult;
import com.gempukku.swccgo.logic.effects.FireWeaponEffect;
import com.gempukku.swccgo.logic.effects.AddUntilEndOfWeaponFiringModifierEffect;

import java.util.Collections;
import java.util.List;


/**
 * Set: Jabba's Palace
 * Type: Character
 * Subtype: Alien
 * Title: Weequay Marksman
 */
public class Card6_132 extends AbstractAlien {
    public Card6_132() {
        super(Side.DARK, 2, 4, 2, 1, 3, Title.Weequay_Marksman, Uniqueness.RESTRICTED_3);
        setLore("Patient and quiet. Jabba uses many of his Weequay henchmen as assassins. Use womp rats for target practice during their religious ceremonies.");
        setGameText("Deploys only on Tatooine. May fire one weapon during your control phase (at double use of Force). May use 2 Force to 'assassinate' any character 'hit' by Weequay Marksman (victim is immediately lost)");
        addIcons(Icon.WARRIOR);
        addKeywords(Keyword.ASSASSIN);
    }

   // @Override
  //  protected List<Modifier> getGameTextWhileActiveInPlayModifiers(SwccgGame game, final PhysicalCard self) {
     //   List<Modifier> modifiers = new LinkedList<Modifier>();
     //   return modifiers;
 //   }

    @Override
    protected Filter getGameTextValidDeployTargetFilter(SwccgGame game, PhysicalCard self, PlayCardOptionId playCardOptionId, boolean asReact) {
        return Filters.Deploys_on_Tatooine;
    }

    @Override
    protected List<OptionalGameTextTriggerAction> getGameTextOptionalAfterTriggers(final String playerId, SwccgGame game, EffectResult effectResult, final PhysicalCard self, int gameTextSourceCardId) {
        Filter otherCharacterPresent = Filters.and(Filters.other(self), Filters.character, Filters.present(self));
        TargetingReason targetingReason = TargetingReason.TO_BE_LOST;

        // Check condition(s)
        if (TriggerConditions.justHitBy(game, effectResult, Filters.and(Filters.opponents(self), Filters.character), self)
                && GameConditions.canUseForce(game, playerId, 2)) {
            PhysicalCard cardHit = ((HitResult) effectResult).getCardHit();
            if (GameConditions.canTarget(game, self, targetingReason, cardHit)) {

                final OptionalGameTextTriggerAction action = new OptionalGameTextTriggerAction(self, gameTextSourceCardId);
                action.setText("Pay 2 force to assassinate " + GameUtils.getFullName(cardHit));
                // Choose target(s)
                action.appendTargeting(
                        new TargetCardOnTableEffect(action, playerId, "Choose character", targetingReason, cardHit) {
                            @Override
                            protected void cardTargeted(final int targetGroupId, PhysicalCard cardTargeted) {
                                action.addAnimationGroup(cardTargeted);
                                // Allow response(s)
                                action.allowResponses("Pay 2 force to assassinate " + GameUtils.getCardLink(cardTargeted),
                                        new RespondableEffect(action) {
                                            @Override
                                            protected void performActionResults(Action targetingAction) {
                                                // Get the targeted card(s) from the action using the targetGroupId.
                                                // This needs to be done in case the target(s) were changed during the responses.
                                                PhysicalCard cardToOperateOn = targetingAction.getPrimaryTargetCard(targetGroupId);

                                                // Pay cost(s)
                                                action.appendCost(
                                                        new UseForceEffect(action, playerId, 2));
                                                // Perform result(s)
                                                action.appendEffect(
                                                        new LoseCardFromTableEffect(action, cardToOperateOn));
                                            }
                                        });
                            }
                        }
                );
                return Collections.singletonList(action);

            }
        }

    return null;
    }

    @Override
    protected List<TopLevelGameTextAction> getGameTextTopLevelActions(final String playerId, SwccgGame game, final PhysicalCard self, int gameTextSourceCardId) {
        Filter weaponFilter = Filters.and(Filters.weapon, Filters.attachedTo(self), Filters.canBeFiredForFree(self, 0));

        // Check condition(s)
        if (GameConditions.isOnceDuringYourPhase(game, self, playerId, gameTextSourceCardId, Phase.CONTROL)
                && GameConditions.canSpot(game, self, weaponFilter)) {

            final TopLevelGameTextAction action = new TopLevelGameTextAction(self, gameTextSourceCardId);
            action.setText("Fire a weapon");
            // Update usage limit(s)
            action.appendUsage(
                    new OncePerPhaseEffect(action));
            // Choose target(s)
            action.appendTargeting(
                    new ChooseCardOnTableEffect(action, playerId, "Choose weapon to fire", weaponFilter) {
                        @Override
                        protected void cardSelected(final PhysicalCard weapon) {
                            action.addAnimationGroup(weapon);
                            // Allow response(s)
                            action.allowResponses("Fire " + GameUtils.getCardLink(weapon),

                                    new UnrespondableEffect(action) {
                                        @Override
                                        protected void performActionResults(Action targetingAction) {

                                            // Perform result(s)
                                            action.appendEffect(
                                                    new FireWeaponEffect(action, weapon, false, Filters.any) {


                                                        @Override
                                                        protected List<Modifier> getWeaponFiringModifiers(String playerId, SwccgGame game, PhysicalCard weapon) {
                                                            Modifier modifier = new FireWeaponCostModifier(self, weapon, 3);
                                                            return Collections.singletonList(modifier);
                                                                }
                                                            }
                                                    );
                                                }
                                               }
                                               );
                                                   }
                                                 }
                                              );
                                                  return Collections.singletonList(action);
                                              }

                                                return null;
                                              }

}
