package de.lunoro.bedwars.shopinventory.item;

import de.lunoro.bedwars.builder.ItemBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class ItemNode {

    private final ItemStack item;
    private final List<ItemNode> childrenList;
    private final int index;
    private final int price;
    private final Material priceMaterial;

    public ItemNode(Material material, String name, String description, Material priceMaterial, int price, int index, List<ItemNode> childrenList) {
        final String nameString = name.replace("%price%", String.valueOf(price).replace("%priceItem%", name));
        if (description.equals("")) {
            this.item = new ItemBuilder(material)
                    .setName(nameString)
                    .toItemStack();
        } else {
            this.item = new ItemBuilder(material)
                    .setName(nameString)
                    .setLore(description.replace("%price%", String.valueOf(price)).replace("%priceItem%", name))
                    .toItemStack();
        }
        this.price = price;
        this.index = index;
        this.childrenList = childrenList;
        this.priceMaterial = priceMaterial;
    }
}
