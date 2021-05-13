package com.redspeaks.conflip.lib.coinflip;

import com.redspeaks.conflip.CoinFlip;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.*;
import java.util.stream.Collectors;

public class CoinUtil {

    public final static Set<String> readyPlayers = new HashSet<>();
    public final static HashMap<String, String> partnerMap = new HashMap<>();
    public final static HashMap<String, Reward> rewardMap = new HashMap<>();
    public final static HashMap<String, Double> betMap = new HashMap<>();
    private final static Economy economy = CoinFlip.getInstance().getEconomy();

    public static CoinFlipPlayer getPlayer(OfflinePlayer player) {
        return new CoinFlipPlayer() {
            @Override
            public CoinFlipPlayer getPartner() {
                return getPlayer(Bukkit.getOfflinePlayer(UUID.fromString(partnerMap.get(getUniqueId()))));
            }

            @Override
            public boolean isBetting() {
                return hasPartner();
            }

            @Override
            public String getUniqueId() {
                return player.getUniqueId().toString();
            }

            @Override
            public boolean isReady() {
                if(!hasPartner()) return false;
                return readyPlayers.contains(getUniqueId());
            }

            @Override
            public boolean hasPartner() {
                return partnerMap.containsKey(getUniqueId());
            }

            @Override
            public boolean hasReward() {
                return rewardMap.containsKey(getUniqueId());
            }

            @Override
            public Player asPlayer() {
                return player.getPlayer();
            }

            @Override
            public void sendMessage(String message) {
                if(asPlayer() == null) return;
                asPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }

            @Override
            public void setReady(boolean value) {
                if(!hasPartner()) return;
                if(value) {
                    if(readyPlayers.contains(getUniqueId())) return;
                    readyPlayers.add(getUniqueId());
                } else {
                    if(!readyPlayers.contains(getUniqueId())) return;
                    readyPlayers.remove(getUniqueId());
                }
            }

            @Override
            public Reward getReward() {
                return rewardMap.get(getUniqueId());
            }

            @Override
            public void giveItems(ItemStack... itemStacks) {
                asPlayer().getInventory().addItem(itemStacks);
                asPlayer().updateInventory();
            }

            @Override
            public void wage(CoinFlipPlayer target) {
                partnerMap.put(getUniqueId(), target.getUniqueId());
                partnerMap.put(target.getUniqueId(), getUniqueId());
            }

            @Override
            public void openInventory(Inventory inventory) {
                asPlayer().openInventory(inventory);
            }

            @Override
            public void endBet() {
                try {
                    Bet myBet = getCurrentBet();
                    Bet myPartnerBet = getPartner().getCurrentBet();
                    if(myBet.isEmpty()) {
                        giveItems(myBet.getBetItems().toArray(new ItemStack[0]));
                        economy.depositPlayer(asPlayer(), myBet.getBetAmount());
                        betMap.remove(getUniqueId());
                    }
                    if(myPartnerBet.isEmpty()) {
                        getPartner().giveItems(myPartnerBet.getBetItems().toArray(new ItemStack[0]));
                        economy.depositPlayer(getPartner().asPlayer(), myPartnerBet.getBetAmount());
                        betMap.remove(getPartner().getUniqueId());
                    }
                    asPlayer().closeInventory();
                    getPartner().asPlayer().closeInventory();
                    partnerMap.remove(getPartner().getUniqueId());
                }catch (NullPointerException ignored) {

                }
                partnerMap.remove(getUniqueId());
            }

            @Override
            public void addBet(double amount) {
                EconomyResponse response = economy.withdrawPlayer(asPlayer(), amount);
                if(!response.transactionSuccess()) {
                    return;
                }
                betMap.put(getUniqueId(), getBet() + amount);
            }

            @Override
            public void addBet(ItemStack item) {
                CoinFlipPlayer partner = getPartner();
                Inventory inventory1 = getBettingInventory();
                Inventory inventory2 = partner.getBettingInventory();
                InventoryUtil.addItem(inventory1, item);
                InventoryUtil.addItemRight(inventory2, item);
                endBet();
            }

            @Override
            public void removeBet(double amount) {
                double bet = getBet();
                if(bet == 0)return;
                if(amount > bet) {
                    economy.depositPlayer(asPlayer(), bet);
                    bet = 0;
                } else {
                    economy.depositPlayer(asPlayer(), amount);
                    bet -= amount;
                }
                betMap.put(getUniqueId(), bet);
            }

            @Override
            public double getBet() {
                return betMap.getOrDefault(getUniqueId(), 0D);
            }

            @Override
            public Inventory getBettingInventory() {
                return asPlayer().getOpenInventory().getTopInventory();
            }

            @Override
            public Bet getCurrentBet() {
                return new Bet() {
                    @Override
                    public List<ItemStack> getBetItems() {
                        return Arrays.stream(getLeft()).filter(i -> getBettingInventory().getItem(i) != null)
                                .mapToObj(i -> getBettingInventory().getItem(i)).collect(Collectors.toList());
                    }

                    @Override
                    public double getBetAmount() {
                        return betMap.get(getUniqueId());
                    }
                };
            }

            @Override
            public boolean claimReward() {
                if(!rewardMap.containsKey(getUniqueId())) return false;
                Reward reward = rewardMap.get(getUniqueId());
                if(reward.isEmpty()) {
                    return false;
                }
                economy.depositPlayer(asPlayer(), reward.moneyReward());
                giveItems(reward.itemRewards());
                rewardMap.remove(getUniqueId());
                return true;
            }
        };
    }

    public static int[] getLeft() {
        return new int[] {9, 10, 11, 12, 18, 19, 20, 21};
    }

    public static int[] getRight() {
        return new int[] {14,15,16,17,23,24,25,26};
    }
}
