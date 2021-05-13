package com.redspeaks.conflip.lib.coinflip;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface Bet {

    List<ItemStack> getBetItems();
    double getBetAmount();
    default boolean isEmpty() {
        return getBetItems().isEmpty() && getBetAmount() == 0;
    }
}
