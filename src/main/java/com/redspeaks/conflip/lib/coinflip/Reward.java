package com.redspeaks.conflip.lib.coinflip;

import org.bukkit.inventory.ItemStack;

public interface Reward {

    ItemStack[] itemRewards();
    double moneyReward();
    boolean isEmpty();

}
