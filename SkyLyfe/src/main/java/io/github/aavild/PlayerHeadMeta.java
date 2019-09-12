package io.github.aavild;


import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.Serializable;
import java.util.*;

public class PlayerHeadMeta implements Serializable {
    private HashMap<Map<String, Object>, Map<String, Object>> skullitem;
    UUID player;
    PlayerHeadMeta(ItemStack skullitem, UUID player)
    {
        //Assigns the player
        this.player = player;


        Map<String, Object> serializedItemStack, serializedItemMeta; //creates a map of 2 types, serializedItemStack, serializedItemMeta
        HashMap<Map<String, Object>, Map<String, Object>> serializedMap = new HashMap<Map<String, Object>, Map<String, Object>>(); //This is for the maps to combine later

        serializedItemMeta = (skullitem.hasItemMeta()) ? skullitem.getItemMeta().serialize() : null; //defines the itemmeta as a map for serialization
        skullitem.setItemMeta(null); //clears itemmeta for better serialization
        serializedItemStack = skullitem.serialize(); //serializes the itemstack

        serializedMap.put(serializedItemStack, serializedItemMeta); //here the 2 maps are combined as 1
        this.skullitem = serializedMap; //assigns to the class
    }
    ItemStack GetSkull()
    {
        Map.Entry<Map<String, Object>, Map<String, Object>> serializedItemStack = skullitem.entrySet().iterator().next(); //converts from map to entry

        ItemStack itemStack = ItemStack.deserialize(serializedItemStack.getKey()); //deserializes the itemstack
        if (serializedItemStack.getValue() != null) { //if the entry contains a itemMeta
            ItemMeta itemMeta = (ItemMeta)ConfigurationSerialization.deserializeObject(serializedItemStack.getValue(), ConfigurationSerialization.getClassByAlias("ItemMeta")); //deserializes the itemmeta
            itemStack.setItemMeta(itemMeta); //assigns the itemmeta to the itemstack
        }
        return itemStack;
    }
}
