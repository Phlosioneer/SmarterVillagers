package phlo.smartervillagers;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.Dynamic;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Schedule;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ObjectHolder;

public class SmarterVillagerEntity extends VillagerEntity {
	protected static ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
			// Vanilla memory modules
			MemoryModuleType.HOME,
			MemoryModuleType.JOB_SITE,
			MemoryModuleType.MEETING_POINT,
			MemoryModuleType.MOBS,
			MemoryModuleType.VISIBLE_MOBS,
			MemoryModuleType.VISIBLE_VILLAGER_BABIES,
			MemoryModuleType.NEAREST_PLAYERS,
			MemoryModuleType.NEAREST_VISIBLE_PLAYER,
			MemoryModuleType.WALK_TARGET,
			MemoryModuleType.LOOK_TARGET,
			MemoryModuleType.INTERACTION_TARGET,
			MemoryModuleType.BREED_TARGET,
			MemoryModuleType.PATH,
			MemoryModuleType.INTERACTABLE_DOORS,
			// OPENED_DOORS
			MemoryModuleType.field_225462_q,
			MemoryModuleType.NEAREST_BED,
			MemoryModuleType.HURT_BY,
			MemoryModuleType.HURT_BY_ENTITY,
			MemoryModuleType.NEAREST_HOSTILE,
			MemoryModuleType.SECONDARY_JOB_SITE,
			MemoryModuleType.HIDING_PLACE,
			MemoryModuleType.HEARD_BELL_TIME,
			MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
			MemoryModuleType.LAST_SLEPT,
			// LAST_WOKEN
			MemoryModuleType.field_226332_A_,
			MemoryModuleType.LAST_WORKED_AT_POI,
			MemoryModuleType.GOLEM_LAST_SEEN_TIME);

	protected static ImmutableList<SensorType<? extends Sensor<? super SmarterVillagerEntity>>> SENSOR_TYPES = ImmutableList.of(
			// Vanilla sensors
			SensorType.NEAREST_LIVING_ENTITIES,
			SensorType.NEAREST_PLAYERS,
			SensorType.INTERACTABLE_DOORS,
			SensorType.NEAREST_BED,
			SensorType.HURT_BY,
			SensorType.VILLAGER_HOSTILES,
			SensorType.VILLAGER_BABIES,
			SensorType.SECONDARY_POIS,
			SensorType.GOLEM_LAST_SEEN);

	@ObjectHolder("minecraft:villager_baby")
	private static final Schedule BABY_SCHEDULE = null;

	@ObjectHolder("minecraft:villager_default")
	private static final Schedule NORMAL_SCHEDULE = null;

	@SuppressWarnings("unchecked")
	public SmarterVillagerEntity(VillagerEntity original) {
		super((EntityType<VillagerEntity>) original.getType(), original.world, original.getVillagerData().getType());
	}

	@Override
	protected Brain<?> createBrain(Dynamic<?> dynamicIn) {
		Brain<SmarterVillagerEntity> brain = new Brain<SmarterVillagerEntity>(MEMORY_TYPES, SENSOR_TYPES, dynamicIn);
		initBrain(brain);
		return brain;
	}

	@Override
	public void resetBrain(ServerWorld serverWorldIn) {
		@SuppressWarnings("unchecked")
		Brain<SmarterVillagerEntity> brain = (Brain<SmarterVillagerEntity>) this.brain;
		brain.stopAllTasks(serverWorldIn, this);
		brain = brain.copy();
		this.brain = brain;
		initBrain(brain);
	}

	protected void initBrain(Brain<SmarterVillagerEntity> brain) {
		float walkSpeed = (float) this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue();
		if (isChild()) {
			brain.setSchedule(BABY_SCHEDULE);

		} else {
			brain.setSchedule(NORMAL_SCHEDULE);
		}
	}
}
