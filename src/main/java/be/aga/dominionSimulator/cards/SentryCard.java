package be.aga.dominionSimulator.cards;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.enums.DomCardName;
import be.aga.dominionSimulator.enums.DomCardType;

import java.util.ArrayList;
import java.util.Collections;

public class SentryCard extends DomCard {
    public SentryCard() {
      super( DomCardName.Sentry);
    }

    public void play() {
        owner.addActions(1);
        owner.drawCards(1);
        owner.setNeedsToUpdate();
        ArrayList<DomCard> theTopCards = owner.revealTopCards(2);
        Collections.sort(theTopCards, SORT_FOR_DISCARDING);
        if (theTopCards.isEmpty())
            return;
        if (owner.isHumanOrPossessedByHuman()) {
            ArrayList<DomCardName> theChosenCards = new ArrayList<DomCardName>();
            owner.getEngine().getGameFrame().askToSelectCards("Choose to trash" , theTopCards, theChosenCards, 0);
            while (!theChosenCards.isEmpty()) {
                DomCard theCardToTrash = null;
                for (DomCard theCard : theTopCards) {
                    if (theCard.getName() == theChosenCards.get(0))
                        theCardToTrash = theCard;
                }
                theChosenCards.remove(0);
                owner.trash(theTopCards.remove(theTopCards.indexOf(theCardToTrash)));
            }
            if (theTopCards.isEmpty())
                return;
            theChosenCards = new ArrayList<DomCardName>();
            owner.getEngine().getGameFrame().askToSelectCards("Choose to discard" , theTopCards, theChosenCards, 0);
            while (!theChosenCards.isEmpty()) {
                DomCard theCardToDiscard= null;
                for (DomCard theCard : theTopCards) {
                    if (theCard.getName() == theChosenCards.get(0))
                        theCardToDiscard = theCard;
                }
                theChosenCards.remove(0);
                owner.discard(theTopCards.remove(theTopCards.indexOf(theCardToDiscard)));
            }
            if (theTopCards.isEmpty())
                return;
            if (theTopCards.size()==1) {
              owner.putOnTopOfDeck(theTopCards.remove(0));
              return;
            }
            theChosenCards = new ArrayList<DomCardName>();
            owner.getEngine().getGameFrame().askToSelectCards("<html>Choose <u>order</u> (first card = top card)</html>" , theTopCards, theChosenCards, 2);
            if (theTopCards.get(1).getName()==theChosenCards.get(1)) {
                owner.putOnTopOfDeck(theTopCards.get(1));
                owner.putOnTopOfDeck(theTopCards.get(0));
            } else {
                owner.putOnTopOfDeck(theTopCards.get(0));
                owner.putOnTopOfDeck(theTopCards.get(1));
            }
        } else {
            while (!theTopCards.isEmpty()) {
                if (theTopCards.get(0).getTrashPriority() <= DomCardName.Copper.getTrashPriority()) {
                    owner.trash(theTopCards.remove(0));
                } else {
                    if (theTopCards.get(0).getDiscardPriority(1) <= DomCardName.Silver.getDiscardPriority(1)) {
                        owner.discard(theTopCards.remove(0));
                    } else {
                        if (theTopCards.get(0).hasCardType(DomCardType.Action) && owner.getNextActionToPlay() != null && owner.getNextActionToPlay().hasCardType(DomCardType.Card_Advantage) && owner.getNextActionToPlay().hasCardType(DomCardType.Terminal) && owner.getActionsLeft() == 1)
                            owner.discard(theTopCards.remove(0));
                        else
                            owner.putOnTopOfDeck(theTopCards.remove(0));
                    }
                }
            }
        }
    }

    @Override
    public int getPlayPriority() {
        if (owner.getKnownTopCards()>1)
            return 2;
        return super.getPlayPriority();
    }
}