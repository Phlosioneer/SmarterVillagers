package phlo.smartervillagers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.schedule.Schedule;
import net.minecraft.entity.ai.brain.task.VillagerTasks;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("smartervillagers")
@Mod.EventBusSubscriber
public class SmarterVillagers {

	// Directly reference a log4j logger.
	private static final Logger LOGGER = LogManager.getLogger();

	public SmarterVillagers() {
		// Register the setup method for modloading
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		modBus.addListener(this::setup);
		modBus.addListener(this::loadingComplete);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup(final FMLCommonSetupEvent event) {
		if (!LOGGER.isDebugEnabled()) {
			if (!LOGGER.isErrorEnabled()) {
				throw new RuntimeException("Invalid logger: " + LOGGER);
			} else {
				LOGGER.error("Debug logging not enabled");
			}
		} else {
			LOGGER.debug("Debug logging enabled");
		}
	}

	private void loadingComplete(final FMLLoadCompleteEvent event) {
		// LOGGER.debug("Scheduling recipes");
		// DeferredWorkQueue.runLater(SigilScience::registerBrewingRecipes);
	}

	// You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
	// Event bus for receiving Registry Events)
	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void onCreateRegistries(final RegistryEvent.NewRegistry event) {
			@SuppressWarnings("unchecked")
			IForgeRegistry<TaskEntry> taskReg = (IForgeRegistry<TaskEntry>) new RegistryBuilder<>()
					.setDefaultKey(new ResourceLocation("minecraft:idle"))
					.disableSaving()
					.disableSync()
					.addCallback(new TaskRegistryCallbacks())
					.create();

			// Register vanilla stuff.
			taskReg.registerAll(
					new TaskEntry((d, s) -> VillagerTasks.core(d.getProfession(), s)).setRegistryName("minecraft:core"),
					new TaskEntry((d, s) -> VillagerTasks.work(d.getProfession(), s)).setRegistryName("minecraft:work"),
					new TaskEntry((d, s) -> VillagerTasks.play(s)).setRegistryName("minecraft:play"),
					new TaskEntry((d, s) -> VillagerTasks.rest(d.getProfession(), s)).setRegistryName("minecraft:rest"),
					new TaskEntry((d, s) -> VillagerTasks.meet(d.getProfession(), s)).setRegistryName("minecraft:meet"),
					new TaskEntry((d, s) -> VillagerTasks.idle(d.getProfession(), s)).setRegistryName("minecraft:idle"),
					new TaskEntry((d, s) -> VillagerTasks.panic(d.getProfession(), s)).setRegistryName("minecraft:panic"),
					new TaskEntry((d, s) -> VillagerTasks.preRaid(d.getProfession(), s)).setRegistryName("minecraft:pre_raid"),
					new TaskEntry((d, s) -> VillagerTasks.raid(d.getProfession(), s)).setRegistryName("minecraft:raid"),
					new TaskEntry((d, s) -> VillagerTasks.hide(d.getProfession(), s)).setRegistryName("minecraft:hide"));
		}

		@SubscribeEvent
		public static void onActivityRegistry(final RegistryEvent.Register<Activity> event) {
			// event.getRegistry().register(SMART_REST);
		}

		@SubscribeEvent
		public static void onScheduleRegistry(final RegistryEvent.Register<Schedule> event) {
			// event.getRegistry().register(SMARTER_VILLAGER_DEFAULT);
		}
	}

	@Mod.EventBusSubscriber({Dist.DEDICATED_SERVER})
	public static class GameEvents {
		@SubscribeEvent
		public static void onEntitySpawn(final EntityJoinWorldEvent event) {
			if (event.getEntity().getClass() != VillagerEntity.class) {
				return;
			}
			LOGGER.debug("Normal villager spawn blocked.");
			event.setCanceled(true);

		}
	}
}
