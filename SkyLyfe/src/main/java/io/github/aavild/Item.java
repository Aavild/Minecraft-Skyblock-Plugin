package io.github.aavild;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class Item {
    ItemStack item;
    int placement;
    public Item(Material material, String name, List<String> description, int row, int column)
    {
        item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        placement = row * 9 + column - 10;
        if (itemMeta == null)
        {
            Bukkit.getServer().broadcastMessage("An erorr occured");
        }
        itemMeta.setDisplayName(name);
        itemMeta.setLore(description);
        item.setItemMeta(itemMeta);
    }
}
