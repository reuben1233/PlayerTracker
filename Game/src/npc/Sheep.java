package npc;

import java.lang.reflect.Field;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSheep;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_8_R3.EntitySheep;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.World;

public class Sheep extends EntitySheep{

	public Sheep(World world) {
		super(world);	
		try{
			Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
			bField.setAccessible(true);
			Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
			cField.setAccessible(true);
			 
			bField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
			bField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
			cField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
			cField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
			 
			}catch (Exception e){
			e.printStackTrace();
			}
	}
	
	@Override
	public void move(double d0, double d1, double d2) {
		return;
	}
	
	@Override
	public void g(double x, double y, double z) {
		Vector vector = this.getBukkitEntity().getVelocity();
		super.g(vector.getX(), vector.getY(), vector.getZ());
	}

	public static org.bukkit.entity.Sheep spawn(Location location) {
		World mcWorld = (World) ((CraftWorld) location.getWorld()).getHandle();
		final Sheep customEntity = new Sheep(
				mcWorld);
		customEntity.setLocation(location.getX(), location.getY(),
				location.getZ(), location.getYaw(), location.getPitch());
		((CraftLivingEntity) customEntity.getBukkitEntity())
				.setRemoveWhenFarAway(false);
		mcWorld.addEntity(customEntity, SpawnReason.CUSTOM);
		return (CraftSheep) customEntity.getBukkitEntity();
	}
}
