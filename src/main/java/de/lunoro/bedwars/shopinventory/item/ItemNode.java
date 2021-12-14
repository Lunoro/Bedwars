package de.lunoro.bedwars.shopinventory.item;

import de.lunoro.bedwars.builder.ItemBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class ItemNode {

    private ItemStack item;
    private final List<ItemNode> childrenList;
    private final int index, price;
    private final String name;
    private final Material priceMaterial;

    public ItemNode(Material material, String name, Material priceMaterial, int price, int amount, int index, List<ItemNode> childrenList) {
        this.item = new ItemBuilder(material)
                .setName(name.replace("%p", String.valueOf(price)))
                .setAmount(1)
                .toItemStack();
        this.name = name;
        this.price = price;
        this.index = index;
        this.childrenList = childrenList;
        this.priceMaterial = priceMaterial;
    }

    public void addLore(String description) {
        this.item = new ItemBuilder(item).setLore(description.replace("%p", String.valueOf(price))).toItemStack();
    }

    public void addEnchantment(String enchantment, int enchantmentLevel) {
        if (enchantment == null || enchantmentLevel == 0) {
            return;
        }
        this.item = new ItemBuilder(item).addUnsafeEnchantment(Enchantment.getByKey(NamespacedKey.minecraft(enchantment)), enchantmentLevel).toItemStack();
    }
}
