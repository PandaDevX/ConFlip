package com.redspeaks.conflip.lib.coinflip;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.OptionalInt;

public class InventoryUtil {

    public static boolean isOpen(Inventory inventory) {
        return Arrays.stream(CoinUtil.getLeft()).anyMatch(i -> inventory.getItem(i) != null);
    }

    public static void removeIfAny(CoinFlipPlayer owner, int slot) {
        int conversion = convert(slot);
        if(conversion != -1) {
            if (owner.getBettingInventory().getItem(slot) != null) {
                owner.asPlayer().getInventory().addItem(owner.getBettingInventory().getItem(slot));
                owner.getPartner().getBettingInventory().setItem(CoinUtil.getRight()[conversion], null);
                owner.getBettingInventory().setItem(slot, null);
            }
        }
    }

    public static int convert(int number) {
        int index = -1;
        for(int i = 0; i < CoinUtil.getLeft().length; i++) {
            if (number == CoinUtil.getLeft()[i]) {
                index = i;
            }
        }
        return index;
    }

    public static void addItem(Inventory inventory, ItemStack itemStack) {
        if(!isOpen(inventory)) return;
        OptionalInt firstEmpty = Arrays.stream(CoinUtil.getLeft()).filter(i -> inventory.getItem(i) != null).findFirst();
        firstEmpty.ifPresent(i -> inventory.setItem(i, itemStack));
    }

    public static void addItemRight(Inventory inventory, ItemStack itemStack) {
        if(!isOpen(inventory)) return;
        OptionalInt firstEmpty = Arrays.stream(CoinUtil.getRight()).filter(i -> inventory.getItem(i) != null).findFirst();
        firstEmpty.ifPresent(i -> inventory.setItem(i, itemStack));
    }
}
