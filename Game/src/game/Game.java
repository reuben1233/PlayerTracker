package game;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import mysql.MySQL;
import net.minecraft.server.v1_8_R3.EntitySheep;
import net.minecraft.server.v1_8_R3.EntitySkeleton;
import npc.NMSUtils;
import runner.Runner;
import runner.Runner.GameState;
import spleef.Spleef;

public class Game extends JavaPlugin {

	   public static Random r = new Random();
	   public static int randomint = r.nextInt(2); 
    public void onEnable(){
    	MySQL.openConnection();
    	getCommand("game").setExecutor((CommandExecutor) new game.commands.Commands());
    	
    if(randomint == 0){
        Runner();
    }
    else if(randomint == 1) {
        Spleef();
    }
}
    public static void Runner(){
		HandlerList.unregisterAll();
		Bukkit.getScheduler().cancelAllTasks();
    	registerEvents(Bukkit.getPluginManager().getPlugin("Game"), new game.events.Events());
    	new mysql.Adder().runTaskTimer(Bukkit.getPluginManager().getPlugin("Game"), 0L, 20L);
    	registerEvents(Bukkit.getPluginManager().getPlugin("Game"), new runner.events.Events());
    	registerEvents(Bukkit.getPluginManager().getPlugin("Game"), new runner.scoreboard.LobbyScoreboard());
    	new runner.runnables.onUpdate().runTaskTimer(Bukkit.getPluginManager().getPlugin("Game"), 0L, 20L);
    	new runner.runnables.onRun().runTaskTimer(Bukkit.getPluginManager().getPlugin("Game"), 0L, 1L);
    	new runner.runnables.Snowball().runTaskTimer(Bukkit.getPluginManager().getPlugin("Game"), 0L, 20L);
    	new runner.runnables.Arrow().runTaskTimer(Bukkit.getPluginManager().getPlugin("Game"), 0L, 40L);
		
	    Runner.cooldownTime = new HashMap<Player, Integer>();
	    Runner.cooldownTask = new HashMap<Player, BukkitRunnable>();
		
		new NMSUtils().registerEntity("Skeleton", 51, EntitySkeleton.class, npc.Skeleton.class);
		new NMSUtils().registerEntity("Sheep", 91, EntitySheep.class, npc.Sheep.class);
		
		for(Entity e : Bukkit.getWorld("world").getEntities()){
			if(!(e instanceof Player)){
				e.remove();
			}
		}
		 for(Player p : Bukkit.getOnlinePlayers())
			{
			p.getInventory().clear();
			p.playSound(p.getLocation(), Sound.NOTE_PLING, 100, 1);
			World w = Bukkit.getWorld(Runner.selectedMap);
			p.teleport(Bukkit.getWorld("world").getSpawnLocation());
			
			Runner.cooldownTime.remove(p);
			Runner.team.clear();
			p.getInventory().clear();
			p.setAllowFlight(false);
			p.showPlayer(p);
			
			for(String ded : Runner.dead)
			{
				Player ps = Bukkit.getPlayerExact(ded);
				p.showPlayer(ps);
				ps.showPlayer(p);
				
				if(ded != null)
				{	
					p.showPlayer(ps);
					ps.showPlayer(p);
					ps.teleport(Bukkit.getWorld("world").getSpawnLocation());
				}
			}
			
			p.setFlying(false);
			
			Runner.dead.clear();
			Runner.alive.clear();
			
			Runner.jumper.clear();
			Runner.archer.clear();
			Runner.frosty.clear();
			
			Bukkit.unloadWorld(w, false);
			
			Runner.seconds = 60;
		
			}
		    Runner.setGameState(GameState.WAITING);
			
	        ArmorStand am = (ArmorStand) Bukkit.getWorld("world").spawn(new Location(Bukkit.getWorld("world"), -17, 90, 4), ArmorStand.class);
	        am.setVisible(false);
	        am.setCustomName("§eJumper");
	        am.setCustomNameVisible(true);
	        am.setGravity(false);
	        am.setSmall(true);
	        
	        ArmorStand am11 = (ArmorStand) Bukkit.getWorld("world").spawn(new Location(Bukkit.getWorld("world"), -17, 90, 0), ArmorStand.class);
	        am11.setVisible(false);
	        am11.setCustomName("§aArcher §f(§c2000 Gems§f)");
	        am11.setCustomNameVisible(true);
	        am11.setGravity(false);
	        am11.setSmall(true);
	        
	        ArmorStand am111 = (ArmorStand) Bukkit.getWorld("world").spawn(new Location(Bukkit.getWorld("world"), -17, 90, -4), ArmorStand.class);
	        am111.setVisible(false);
	        am111.setCustomName("§aFrosty §f(§c5000 Gems§f)");
	        am111.setCustomNameVisible(true);
	        am111.setGravity(false);
	        am111.setSmall(true);
	        
	        ArmorStand am1111 = (ArmorStand) Bukkit.getWorld("world").spawn(new Location(Bukkit.getWorld("world"), 18, 102, 0), ArmorStand.class);
	        am1111.setVisible(false);
	        am1111.setCustomName("§e§lPlayers Team");
	        am1111.setCustomNameVisible(true);
	        am1111.setGravity(false);
	        am1111.setSmall(true);
	        
			final org.bukkit.entity.Skeleton z = npc.Skeleton.spawn(new Location(Bukkit.getWorld("world"), -17, 102.5, 4));
			z.setCustomName("");
			z.setCustomNameVisible(false);
			z.getEquipment().setItemInHand(new ItemStack(Material.IRON_AXE));
			z.setPassenger(am);
			
			final org.bukkit.entity.Skeleton z1 = npc.Skeleton.spawn(new Location(Bukkit.getWorld("world"), -17, 102.5, 0));
			z1.setCustomName("");
			z1.setCustomNameVisible(false);
			z1.getEquipment().setItemInHand(new ItemStack(Material.BOW));
			z1.setPassenger(am11);
			
			final org.bukkit.entity.Skeleton z11 = npc.Skeleton.spawn(new Location(Bukkit.getWorld("world"), -17, 102.5, -4));
			z11.setCustomName("");
			z11.setCustomNameVisible(false);
			z11.getEquipment().setItemInHand(new ItemStack(Material.SNOW_BALL));
			z11.setPassenger(am111);
			
			final org.bukkit.entity.Sheep s = npc.Sheep.spawn(new Location(Bukkit.getWorld("world"), 18, 102.5, 0));
			s.setColor(DyeColor.YELLOW);
			s.setCustomName("");
			s.setCustomNameVisible(false);
			s.setPassenger(am1111);
			
			runner.scoreboard.LobbyScoreboard.players.clear();
    }
    
    public static void Spleef(){
    	HandlerList.unregisterAll();
		Bukkit.getScheduler().cancelAllTasks();
    	registerEvents(Bukkit.getPluginManager().getPlugin("Game"), new game.events.Events());
    	new mysql.Adder().runTaskTimer(Bukkit.getPluginManager().getPlugin("Game"), 0L, 20L);
    	registerEvents(Bukkit.getPluginManager().getPlugin("Game"), new spleef.events.Events());
    	registerEvents(Bukkit.getPluginManager().getPlugin("Game"), new spleef.scoreboard.LobbyScoreboard());
    	new spleef.runnables.onUpdate().runTaskTimer(Bukkit.getPluginManager().getPlugin("Game"), 0L, 20L);
    	new spleef.runnables.onRun().runTaskTimer(Bukkit.getPluginManager().getPlugin("Game"), 0L, 40L);
		
    	for(Entity e : Bukkit.getWorld("world").getEntities()){
			if(!(e instanceof Player)){
				e.remove();
			}
		}
		
	    Spleef.cooldownTime = new HashMap<Player, Integer>();
	    Spleef.cooldownTask = new HashMap<Player, BukkitRunnable>();
		
		for(Player p : Bukkit.getOnlinePlayers())
		{
		p.getInventory().clear();
		p.playSound(p.getLocation(), Sound.NOTE_PLING, 100, 1);
		World w = Bukkit.getWorld(Spleef.selectedMap);
		p.teleport(Bukkit.getWorld("world").getSpawnLocation());
		Spleef.team.clear();
		p.getInventory().clear();
		p.setAllowFlight(false);
		p.showPlayer(p);
		
		ItemStack kits = new ItemStack(Material.COMPASS);
	    ItemMeta kits1 = (ItemMeta) kits.getItemMeta();
		kits1.setDisplayName("§aKit Selector");
		kits.setItemMeta(kits1);
		p.getInventory().clear();
		p.getInventory().setItem(4, kits);
		
		Spleef. cooldownTime.clear();
		
		for(String ded :Spleef. dead)
		{
			Player ps = Bukkit.getPlayerExact(ded);
			p.showPlayer(ps);
			ps.showPlayer(p);
			
			if(ded != null)
			{	
				p.showPlayer(ps);
				ps.showPlayer(p);
				ps.teleport(Bukkit.getWorld("world").getSpawnLocation());
			}
		}
		
		p.setFlying(false);
		
		Spleef.dead.clear();
		Spleef.alive.clear();
		
		Bukkit.unloadWorld(w, false);
		
		Spleef.seconds = 60;
		}
		Spleef.setGameState(Spleef.GameState.WAITING);
		
		spleef.scoreboard.LobbyScoreboard.players.clear();
    }
 
    public static void registerEvents(org.bukkit.plugin.Plugin plugin, Listener... listeners) {
    	for (Listener listener : listeners) {
    	Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
    	}
    	}
}