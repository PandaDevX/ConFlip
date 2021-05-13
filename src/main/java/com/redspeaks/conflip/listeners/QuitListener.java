package com.redspeaks.conflip.listeners;

import com.redspeaks.conflip.lib.coinflip.CoinFlipPlayer;
import com.redspeaks.conflip.lib.coinflip.CoinUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        CoinFlipPlayer player = CoinUtil.getPlayer(e.getPlayer());
        if(!player.isBetting()) return;
        player.getPartner().sendMessage("&7Bet has been cancelled");
        player.endBet();
    }
}
