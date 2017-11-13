package be.aga.dominionSimulator.cards;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomCost;
import be.aga.dominionSimulator.DomEngine;
import be.aga.dominionSimulator.enums.DomCardName;
import be.aga.dominionSimulator.enums.DomCardType;
import be.aga.dominionSimulator.enums.DomPlayStrategy;

import java.util.ArrayList;
import java.util.Collections;

public class Cursed_GoldCard extends DomCard {

    public Cursed_GoldCard() {
        super(DomCardName.Cursed_Gold);
    }

    @Override
    public void play() {
        owner.addAvailableCoins(3);
        owner.gain(DomCardName.Curse);
    }

    @Override
    public int getTrashPriority() {
        if (owner.getCurrentGame().countInSupply(DomCardName.Curse)==0)
            return DomCardName.Gold.getTrashPriority();
        return super.getTrashPriority();
    }

    @Override
    public boolean wantsToBePlayed() {
        if (owner.getCurrentGame().countInSupply(DomCardName.Curse)==0)
            return true;
        if (owner.wantsToGainOrKeep(DomCardName.Province)
                && owner.getTotalPotentialCurrency().compareTo(DomCardName.Province.getCost(owner.getCurrentGame()))<0
                && owner.getTotalPotentialCurrency().add(new DomCost(3,0)).compareTo(DomCardName.Province.getCost(owner.getCurrentGame()))>=0)
            return true;
        if (owner.wantsToGainOrKeep(DomCardName.Colony)
                && owner.getTotalPotentialCurrency().compareTo(DomCardName.Colony.getCost(owner.getCurrentGame()))<0
                && owner.getTotalPotentialCurrency().add(new DomCost(3,0)).compareTo(DomCardName.Colony.getCost(owner.getCurrentGame()))>=0)
            return true;
        return false;
    }
}