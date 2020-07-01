package phlo.smartervillagers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.Dynamic;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.schedule.Schedule;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.nbt.CompoundNBT;
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

	protected static ImmutableList<SensorType<? extends Sensor<? super VillagerEntity>>> SENSOR_TYPES = ImmutableList.of(
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
	private static Schedule BABY_SCHEDULE = null;

	@ObjectHolder("minecraft:villager_default")
	private static Schedule NORMAL_SCHEDULE = null;

	@ObjectHolder("minecraft:core")
	private static Activity CORE_ACTIVITY = null;

	@ObjectHolder("minecraft:idle")
	private static Activity IDLE_ACTIVITY = null;

	@SuppressWarnings("unchecked")
	public SmarterVillagerEntity(VillagerEntity original) {
		super((EntityType<VillagerEntity>) original.getType(), original.world, original.getVillagerData().getType());
		SmarterVillagers.LOGGER.debug("SmarterVillagerEntity:79");
		setVillagerData(original.getVillagerData());
		SmarterVillagers.LOGGER.debug("SmarterVillagerEntity:81");
		CompoundNBT data = new CompoundNBT();
		SmarterVillagers.LOGGER.debug("SmarterVillagerEntity:83");
		original.writeWithoutTypeId(data);
		SmarterVillagers.LOGGER.debug("SmarterVillagerEntity:85");
		read(data);
		SmarterVillagers.LOGGER.debug("SmarterVillagerEntity:87");
	}

	@Override
	protected Brain<?> createBrain(Dynamic<?> dynamicIn) {
		Brain<VillagerEntity> brain = new Brain<VillagerEntity>(MEMORY_TYPES, SENSOR_TYPES, dynamicIn);
		initBrain(brain);
		return brain;
	}

	@Override
	public void resetBrain(ServerWorld serverWorldIn) {
		@SuppressWarnings("unchecked")
		Brain<VillagerEntity> brain = (Brain<VillagerEntity>) this.brain;
		brain.stopAllTasks(serverWorldIn, this);
		brain = brain.copy();
		this.brain = brain;
		initBrain(brain);
	}

	protected void initBrain(Brain<VillagerEntity> brain) {
		float walkSpeed = (float) this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue();
		if (isChild()) {
			brain.setSchedule(BABY_SCHEDULE);
		} else {
			brain.setSchedule(NORMAL_SCHEDULE);
		}
		ImmutableSet<Activity> defaultActivities = ImmutableSet.of(CORE_ACTIVITY);
		brain.setDefaultActivities(defaultActivities);
		brain.setFallbackActivity(IDLE_ACTIVITY);
		BrainUtils.registerAll(brain, getVillagerData(), walkSpeed, defaultActivities);
	}
}
