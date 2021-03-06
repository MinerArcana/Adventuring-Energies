package com.minercana.adventuringenergies.data;

import com.google.common.collect.ImmutableList;
import com.minercana.adventuringenergies.AdventuringEnergies;
import com.minercana.adventuringenergies.blocks.AdventuringEnergiesBlocks;
import com.minercana.adventuringenergies.items.AdventuringEnergiesItems;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.data.loot.ChestLootTables;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AdventuringEnergiesLootTableProvider extends LootTableProvider {
    public AdventuringEnergiesLootTableProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return ImmutableList.of(Pair.of(AdventuringEnergiesBlockLootTables::new, LootParameterSets.BLOCK), Pair.of(AdventuringEnergiesChestLootTables::new, LootParameterSets.CHEST));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, @Nonnull ValidationTracker validationtracker) {
        map.forEach((resourceLocation, lootTable) -> LootTableManager.validateLootTable(validationtracker, resourceLocation, lootTable));
    }

    public static class AdventuringEnergiesBlockLootTables extends BlockLootTables {

        @Override
        @Nonnull
        protected Iterable<Block> getKnownBlocks() {
            return AdventuringEnergiesBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
        }

        @Override
        protected void addTables() {
            registerLootTable(AdventuringEnergiesBlocks.GOLDEN_ALTAR.get(), dropping(AdventuringEnergiesItems.GOLDEN_ALTAR.get()));
        }
    }

    public static class AdventuringEnergiesChestLootTables extends ChestLootTables {
        @Override
        public void accept(@Nonnull BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
            createInjectPool(consumer, "simple_dungeon", LootTable.builder()
                    .addLootPool(LootPool.builder()
                            .rolls(BinomialRange.of(1, .1f))
                            .addEntry(ItemLootEntry.builder(AdventuringEnergiesItems.AMULET_OF_RECOVERY.get()))));
        }

        public void createInjectPool(BiConsumer<ResourceLocation, LootTable.Builder> consumer, String name, LootTable.Builder builder) {
            consumer.accept(new ResourceLocation(AdventuringEnergies.MOD_ID, "inject/chests/" + name), builder);
        }
    }
}
