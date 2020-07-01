package phlo.smartervillagers;

import java.util.function.BiFunction;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class TaskEntry extends ForgeRegistryEntry<TaskEntry> {

	private final BiFunction<VillagerData, Float, ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>>> tasks;

	public TaskEntry(BiFunction<VillagerData, Float, ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>>> tasks) {
		this.tasks = tasks;
	}

	public ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> getTasks(VillagerData data, float movementSpeed) {
		return tasks.apply(data, movementSpeed);
	}
}
