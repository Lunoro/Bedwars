package de.lunoro.bedwars.shopinventory.item;

import de.lunoro.bedwars.builder.ItemBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ItemNode {

    private final ItemStack rootItemStack;
    private final List<ItemStack> childrenList;

    public ItemNode(Material rootNodeItem, String name) {
        this.rootItemStack = new ItemBuilder(rootNodeItem).setName(name).toItemStack();
        childrenList = new ArrayList<>();
    }

    public void addChildren(Material item, String name) {
        childrenList.add(new ItemBuilder(item).setName(name).toItemStack());
    }
}
