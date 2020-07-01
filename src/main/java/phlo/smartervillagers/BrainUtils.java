package phlo.smartervillagers;

import java.util.Set;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.merchant.villager.VillagerData;

public class BrainUtils {

	private BrainUtils() {}

	public static void registerAll(Brain<?> brain, VillagerData data, float movementSpeed) {
		Set<Activity> scheduledActivites = brain.getSchedule().field_221387_e.keySet();
	}
}
