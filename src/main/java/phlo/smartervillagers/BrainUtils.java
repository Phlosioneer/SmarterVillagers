package phlo.smartervillagers;

import java.util.HashSet;
import java.util.Set;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegistryManager;

public class BrainUtils {

	@ObjectHolder("minecraft:idle")
	private static Activity idleActivity = null;

	private BrainUtils() {}

	public static void registerAll(Brain<VillagerEntity> brain, VillagerData data, float movementSpeed, Set<Activity> defaultActivities) {
		HashSet<Activity> activities = new HashSet<>();
		activities.addAll(brain.getSchedule().field_221387_e.keySet());
		activities.addAll(defaultActivities);
		activities.add(idleActivity);
		for (Activity activity : activities) {
			TaskEntry entry = (TaskEntry) RegistryManager.ACTIVE.getRegistry(SmarterVillagers.TASKS).getValue(activity.getRegistryName());
			if (entry == null) {
				SmarterVillagers.LOGGER.error("Unable to locate tasks for activity ", activity);
				SmarterVillagers.LOGGER.info("Using Activity.IDLE instead");
				brain.registerActivity(activity, ImmutableList.of());
			} else {
				brain.registerActivity(activity, entry.getTasks(data, movementSpeed));
			}
		}
	}
}
