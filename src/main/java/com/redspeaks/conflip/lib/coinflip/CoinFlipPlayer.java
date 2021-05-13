package com.redspeaks.conflip.lib.coinflip;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface CoinFlipPlayer {

    CoinFlipPlayer getPartner();
    String getUniqueId();
    boolean isReady();
    boolean hasPartner();
    boolean hasReward();
    boolean isBetting();
    Player asPlayer();
    void sendMessage(String message);
    void setReady(boolean value);
    void giveItems(ItemStack... itemStacks);
    void wage(CoinFlipPlayer target);
    void openInventory(Inventory inventory);
    void endBet();
    void addBet(double amount);
    void addBet(ItemStack itemStack);
    void removeBet(double amount);
    boolean claimReward();
    double getBet();
    Inventory getBettingInventory();
    Reward getReward();
    Bet getCurrentBet();

}
