package io.github.aavild;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Schematic implements Serializable {
    private String[][][] blockdata;
    private int[] home = new int[5];
    private int[] size = new int[3];
    Schematic(Block[][][] Schematic, Location Home)
    {
        size[0] = Schematic.length;
        size[1] = Schematic[0].length;
        size[2] = Schematic[0][0].length;
        home[0] = Home.getBlockX() - Schematic[0][0][0].getX();
        home[1] = Home.getBlockY() - Schematic[0][0][0].getY();
        home[2] = Home.getBlockZ() - Schematic[0][0][0].getZ();
        home[3] = (int) Home.getYaw();
        home[4] = (int) Home.getPitch();
        blockdata = new String[Schematic.length][Schematic[0].length][Schematic[0][0].length];
        for (int i = 0; i < size[0]; i++)
        {
            for (int i2 = 0; i2 < size[1]; i2++)
            {
                for (int i3 = 0; i3 < size[2]; i3++)
                {
                    blockdata[i][i2][i3] = Schematic[i][i2][i3].getBlockData().getAsString();
                    if (Schematic[i][i2][i3].getType().equals(Material.CHEST))
                    {
                        SerializeInventory(((Container) Schematic[i][i2][i3].getState()).getInventory().getContents());
                    }
                }
            }
        }
    }
    Location CreateIsland(World skyworld, Location centerLocation, SkyLyfeMain main)
    {
        size[0] = blockdata.length;
        size[1] = blockdata[0].length;
        size[2] = blockdata[0][0].length;
        int containerCounter = 0;
        List<ItemStack[]> inventories = toInventory();
        for (int i = 0; i < size[0]; i++)
        {
            for (int i2 = 0; i2 < size[1]; i2++)
            {
                for (int i3 = 0; i3 < size[2]; i3++)
                {
                    Block block = new Location(skyworld, i - size[0] / 2 + centerLocation.getBlockX(), i2 - size[1] / 2 + centerLocation.getBlockY(), i3 - size[2] / 2 + centerLocation.getBlockZ()).getBlock();

                    block.setBlockData(main.getServer().createBlockData(blockdata[i][i2][i3]));
                    if (block.getType().equals(Material.CHEST))
                    {
                        Container c = (Container) block.getState();
                        c.getInventory().setContents(inventories.get(containerCounter));
                        containerCounter++;
                    }
                }
            }
        }
        return new Location(skyworld, home[0] + centerLocation.getBlockX() - size[0] / 2 + 0.5,
                home[1] + centerLocation.getBlockY() - size[1] / 2,
                home[2] + centerLocation.getBlockZ() - size[2] / 2 + 0.5,
                home[3], home[4]);
    }


    private List<List<Map<String, Map<String, Object>>>> inventories = new ArrayList<List<Map<String, Map<String, Object>>>>();
    private void SerializeInventory(ItemStack[] itemStacks)
    {
        List<Map<String, Map<String, Object>>> inventory = new ArrayList<Map<String, Map<String, Object>>>();
        for (ItemStack Itemstack : itemStacks)
        {
            Map<String, Map<String, Object>> serializableMap = new HashMap<String, Map<String, Object>>();
            ItemStack itemStack;
            if (Itemstack == null)
                itemStack = new ItemStack(Material.AIR);
            else
                itemStack = Itemstack.clone();
            serializableMap.put("itemMeta", itemStack.hasItemMeta() ? itemStack.getItemMeta().serialize() : null);
            itemStack.setItemMeta(null);
            serializableMap.put("itemStack", itemStack.serialize());
            inventory.add(serializableMap);
        }
        inventories.add(inventory);
    }
    private List<ItemStack[]> toInventory() {
        List<ItemStack[]> deserialized = new ArrayList<ItemStack[]>();
        for (List<Map<String, Map<String, Object>>> inventory : inventories)
        {
            ItemStack[] deserializedInven = new ItemStack[27];
            for (Map<String, Map<String, Object>> serializableMap : inventory)
            {
                ItemStack itemStack = ItemStack.deserialize(serializableMap.get( "itemStack"));
                if (serializableMap.get("itemMeta") != null) itemStack.setItemMeta((ItemMeta) ConfigurationSerialization.deserializeObject(serializableMap.get("itemMeta"), ConfigurationSerialization.getClassByAlias("ItemMeta")));
                deserializedInven[inventory.indexOf(serializableMap)] = itemStack;
            }
            deserialized.add(deserializedInven);
        }
        return deserialized;
    }

}
