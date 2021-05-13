package com.redspeaks.conflip;

import com.redspeaks.conflip.commands.ICFCommand;
import com.redspeaks.conflip.lib.coinflip.CoinUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class CoinFlip extends JavaPlugin {

    private static Economy econ = null;
    private static CoinFlip instance = null;

    @Override
    public void onEnable() {
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        instance = this;

        PluginCommand command = getCommand("icf");
        assert command != null;
        command.setExecutor(new ICFCommand());
        command.setTabCompleter(new ICFCommand());
        super.onEnable();
    }

    @Override
    public void onDisable() {
        CoinUtil.readyPlayers.clear();
        CoinUtil.partnerMap.clear();
        CoinUtil.rewardMap.clear();
        CoinUtil.betMap.clear();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public Economy getEconomy() {
        return econ;
    }

    public static CoinFlip getInstance() {
        return instance;
    }
}
