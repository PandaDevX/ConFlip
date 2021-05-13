package com.redspeaks.conflip.listeners;

import com.redspeaks.conflip.lib.coinflip.CoinFlipPlayer;
import com.redspeaks.conflip.lib.coinflip.CoinUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class CloseInventoryListener implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        CoinFlipPlayer player = CoinUtil.getPlayer((Player)e.getPlayer());
        if(!player.isBetting()) return;
        player.sendMessage("&7You cancelled betting");
        player.getPartner().sendMessage("&7Other party cancelled betting");
        player.endBet();
    }
}
