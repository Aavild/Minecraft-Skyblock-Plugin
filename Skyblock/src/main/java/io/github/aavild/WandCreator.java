package io.github.aavild;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class WandCreator {
    ItemStack CreateWand()
    {
        ItemStack wand = new ItemStack(Material.COOKIE);
        ItemMeta itemMeta = wand.getItemMeta();
        itemMeta.setDisplayName(ChatColor.DARK_PURPLE + "Admin Wand");
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GRAY + "Left click to mark one location");
        lore.add(ChatColor.GRAY + "Right click to mark a second location");
        lore.add(ChatColor.GRAY + "Then type /is schematic to create a custom schematic");
        lore.add(ChatColor.GRAY + "The position of you when you type /is schematic will be the /is home as standard");
        itemMeta.setLore(lore);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        wand.setItemMeta(itemMeta);
        return wand;
    }
}
