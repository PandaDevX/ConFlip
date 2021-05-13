package com.redspeaks.conflip.commands;

import com.redspeaks.conflip.CoinFlip;
import com.redspeaks.conflip.lib.coinflip.CoinFlipPlayer;
import com.redspeaks.conflip.lib.coinflip.CoinUtil;
import com.redspeaks.conflip.lib.coinflip.Reward;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class ICFCommand implements CommandExecutor, TabCompleter {

    private final Economy economy = CoinFlip.getInstance().getEconomy();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to do that");
            return true;
        }
        CoinFlipPlayer player = CoinUtil.getPlayer((Player)sender);
        if(args.length >= 1) {
            if(args[0].equalsIgnoreCase("reward")) {
                if(!player.hasReward()) {
                    player.sendMessage("&cYou have no existing reward");
                    return true;
                }
                boolean result = player.claimReward();
                if(!result) {
                    player.sendMessage("&7No reward to claim");
                    return true;
                }
                player.sendMessage("&aSuccessfully claimed");
                return true;
            }
            if(args[0].equalsIgnoreCase("wage")) {
                if(args.length < 2) {
                    player.sendMessage("&7Correct Usage: &b/icf wage <player>");
                    return true;
                }
                if(args[1].equalsIgnoreCase(player.asPlayer().getName())) {
                    player.sendMessage("&7You cannot target yourself");
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if(target == null) {
                    player.sendMessage("&7You can't choose offline player");
                    return true;
                }
                CoinFlipPlayer targetPlayer = CoinUtil.getPlayer(target);
                if(targetPlayer.isBetting()) {
                    player.sendMessage("&7Target player is already betting");
                    return true;
                }
                player.wage(targetPlayer);
                return true;
            }
            player.sendMessage("&7Unknown Command");
            return true;
        }
        player.sendMessage("&7Unknown Command");
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length == 1) {
            return Arrays.asList("reward", "wage") ;
        }
        return null;
    }
}
