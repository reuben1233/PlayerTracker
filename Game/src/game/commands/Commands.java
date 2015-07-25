package game.commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import game.Game;
import net.minecraft.server.v1_8_R3.EntitySheep;
import net.minecraft.server.v1_8_R3.EntitySkeleton;
import npc.NMSUtils;
import runner.Runner;
import runner.Runner.GameState;
import spleef.Spleef;

public class Commands implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player) sender;

			if(cmd.getName().equalsIgnoreCase("game"))
			{
			if(args[0].equalsIgnoreCase("set"))
				if(args[1].equalsIgnoreCase("Runner"))
				{
					HandlerList.unregisterAll();
					Bukkit.getScheduler().cancelAllTasks();
					Game.registerEvents(Bukkit.getPluginManager().getPlugin("Game"), new runner.events.Events());
					Game.registerEvents(Bukkit.getPluginManager().getPlugin("Game"), new game.events.Events());
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
					 for(Player p1 : Bukkit.getOnlinePlayers())
						{
						p1.getInventory().clear();
						p1.playSound(p1.getLocation(), Sound.NOTE_PLING, 100, 1);
						World w = Bukkit.getWorld(Runner.selectedMap);
						p1.teleport(Bukkit.getWorld("world").getSpawnLocation());
						
						Runner.cooldownTime.remove(p1);
						Runner.team.clear();
						p1.getInventory().clear();
						p1.setAllowFlight(false);
						p1.showPlayer(p1);
						
						for(String ded : Runner.dead)
						{
							Player ps = Bukkit.getPlayerExact(ded);
							p1.showPlayer(ps);
							ps.showPlayer(p1);
							
							if(ded != null)
							{	
								p1.showPlayer(ps);
								ps.showPlayer(p1);
								ps.teleport(Bukkit.getWorld("world").getSpawnLocation());
							}
						}
						
						p1.setFlying(false);
						
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
					
					Bukkit.broadcastMessage("§b§l" + p.getName() + " has changed game to Runner.");
				}
				else if(args[1].equalsIgnoreCase("Spleef"))
				{
					HandlerList.unregisterAll();
					Bukkit.getScheduler().cancelAllTasks();
					Game.registerEvents(Bukkit.getPluginManager().getPlugin("Game"), new spleef.events.Events());
					Game.registerEvents(Bukkit.getPluginManager().getPlugin("Game"), new game.events.Events());
					new spleef.runnables.onUpdate().runTaskTimer(Bukkit.getPluginManager().getPlugin("Game"), 0L, 20L);
					new spleef.runnables.onRun().runTaskTimer(Bukkit.getPluginManager().getPlugin("Game"), 0L, 40L);
					
					for(Entity e : Bukkit.getWorld("world").getEntities()){
						if(!(e instanceof Player)){
							e.remove();
						}
					}
					
				    Spleef.cooldownTime = new HashMap<Player, Integer>();
				    Spleef.cooldownTask = new HashMap<Player, BukkitRunnable>();
					
					for(Player p1 : Bukkit.getOnlinePlayers())
					{
					p1.getInventory().clear();
					p1.playSound(p1.getLocation(), Sound.NOTE_PLING, 100, 1);
					World w = Bukkit.getWorld(Spleef.selectedMap);
					p1.teleport(Bukkit.getWorld("world").getSpawnLocation());
					Spleef.team.clear();
					p1.getInventory().clear();
					p1.setAllowFlight(false);
					p1.showPlayer(p1);
					
					ItemStack kits = new ItemStack(Material.COMPASS);
				    ItemMeta kits1 = (ItemMeta) kits.getItemMeta();
					kits1.setDisplayName("§aKit Selector");
					kits.setItemMeta(kits1);
					p1.getInventory().clear();
					p1.getInventory().setItem(4, kits);
					
					Spleef. cooldownTime.clear();
					
					for(String ded :Spleef. dead)
					{
						Player ps = Bukkit.getPlayerExact(ded);
						p1.showPlayer(ps);
						ps.showPlayer(p1);
						
						if(ded != null)
						{	
							p1.showPlayer(ps);
							ps.showPlayer(p1);
							ps.teleport(Bukkit.getWorld("world").getSpawnLocation());
						}
					}
					
					p1.setFlying(false);
					
					Spleef.dead.clear();
					Spleef.alive.clear();
					
					Bukkit.unloadWorld(w, false);
					
					Spleef.seconds = 60;
					Spleef.setGameState(Spleef.GameState.WAITING);
					Bukkit.broadcastMessage("§b§l" + p.getName() + " has changed game to Spleef.");
					}
				}
			}
			}
		  Player p = (Player) sender;
			if(args[0].equalsIgnoreCase("start"))
			{
				if(Game.randomint == 0) {
				if(Runner.hasGameState(Runner.GameState.WAITING))
				{
					Runner.setGameState(GameState.STARTING);
					Runner.seconds = 10;
					Runner.autoStart = true;
					Bukkit.broadcastMessage("§b§l" + p.getName() + " has started the game.");
					p.playSound(p.getLocation(), Sound.NOTE_PLING, 100, 1);
				}
				else if(Runner.hasGameState(GameState.STARTING) && Runner.seconds > 3)
				{
					Runner.seconds = 3;
					Runner.autoStart = true;
					Bukkit.broadcastMessage("§b§l" + p.getName() + " has started the game.");
				}
				else
				{
					p.sendMessage("§9Game> §7Game has already started.");
				}
				} else if(Game.randomint == 1) {
					if(Spleef.hasGameState(Spleef.GameState.WAITING))
					{
						Spleef.setGameState(Spleef.GameState.STARTING);
						Spleef.seconds = 10;
						Bukkit.broadcastMessage("§b§l" + p.getName() + " has started the game.");
						p.playSound(p.getLocation(), Sound.NOTE_PLING, 100, 1);
					}
					else if(Runner.hasGameState(GameState.STARTING) && Runner.seconds > 3)
					{
						Spleef.seconds = 3;
						Bukkit.broadcastMessage("§b§l" + p.getName() + " has started the game.");
					}
					else
					{
						p.sendMessage("§9Game> §7Game has already started.");
					}
					}
			}
			if(args[0].equalsIgnoreCase("stop"))
			{
				if(Game.randomint == 0) {
				if(Runner.hasGameState(GameState.INGAME))
				{
					Bukkit.broadcastMessage("§b§l" + p.getName() + " has stopped the game.");
					p.playSound(p.getLocation(), Sound.NOTE_PLING, 100, 1);
					
					for(Player player : Bukkit.getOnlinePlayers())
					{
					player.getInventory().clear();
					player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 1);
					World w = Bukkit.getWorld(Runner.selectedMap);
					player.teleport(Bukkit.getWorld("world").getSpawnLocation());
					
					Runner.team.clear();
					Runner.jumper.clear();
					Runner.archer.clear();
					Runner.frosty.clear();
					
					
					player.getInventory().clear();
					player.setAllowFlight(false);
					player.showPlayer(player);
					
					for(String ded : Runner.dead)
					{
						Player ps = Bukkit.getPlayerExact(ded);
						player.showPlayer(ps);
						ps.showPlayer(player);
						
						if(ded != null)
						{	
							player.showPlayer(ps);
							ps.showPlayer(player);
							ps.teleport(Bukkit.getWorld("world").getSpawnLocation());
						}
					}
					
					for(String spc : Runner.spec)
					{
						Player ps = Bukkit.getPlayerExact(spc);
						p.showPlayer(ps);
						ps.showPlayer(p);
						
						if(spc != null)
						{	
							p.showPlayer(ps);
							ps.showPlayer(p);
							ps.teleport(Bukkit.getWorld("world").getSpawnLocation());
						}	
						
					}
					
					player.setFlying(false);
					
					Runner.dead.clear();
					Runner.alive.clear();
					Runner.spec.clear();
					
					Bukkit.unloadWorld(w, false);
					
					Runner.seconds = 60;
					
					}
					Runner.setGameState(GameState.WAITING);
					
					HandlerList.unregisterAll();
					Bukkit.getScheduler().cancelAllTasks();
					Game.registerEvents(Bukkit.getPluginManager().getPlugin("Game"), new spleef.events.Events());
					Game.registerEvents(Bukkit.getPluginManager().getPlugin("Game"), new game.events.Events());
					new spleef.runnables.onUpdate().runTaskTimer(Bukkit.getPluginManager().getPlugin("Game"), 0L, 20L);
					new spleef.runnables.onRun().runTaskTimer(Bukkit.getPluginManager().getPlugin("Game"), 0L, 40L);
					
					for(Entity e : Bukkit.getWorld("world").getEntities()){
						if(!(e instanceof Player)){
							e.remove();
						}
					}
					
				    Spleef.cooldownTime = new HashMap<Player, Integer>();
				    Spleef.cooldownTask = new HashMap<Player, BukkitRunnable>();
					
					for(Player p1 : Bukkit.getOnlinePlayers())
					{
					p1.getInventory().clear();
					p1.playSound(p1.getLocation(), Sound.NOTE_PLING, 100, 1);
					World w = Bukkit.getWorld(Spleef.selectedMap);
					p1.teleport(Bukkit.getWorld("world").getSpawnLocation());
					Spleef.team.clear();
					p1.getInventory().clear();
					p1.setAllowFlight(false);
					p1.showPlayer(p1);
					
					ItemStack kits = new ItemStack(Material.COMPASS);
				    ItemMeta kits1 = (ItemMeta) kits.getItemMeta();
					kits1.setDisplayName("§aKit Selector");
					kits.setItemMeta(kits1);
					p1.getInventory().clear();
					p1.getInventory().setItem(4, kits);
					
					Spleef. cooldownTime.clear();
					
					for(String ded :Spleef. dead)
					{
						Player ps = Bukkit.getPlayerExact(ded);
						p1.showPlayer(ps);
						ps.showPlayer(p1);
						
						if(ded != null)
						{	
							p1.showPlayer(ps);
							ps.showPlayer(p1);
							ps.teleport(Bukkit.getWorld("world").getSpawnLocation());
						}
					}
					
					p1.setFlying(false);
					
					Spleef.dead.clear();
					Spleef.alive.clear();
					
					Bukkit.unloadWorld(w, false);
					
					Spleef.seconds = 60;
					}
					Spleef.setGameState(Spleef.GameState.WAITING);
				}
				} else if(Game.randomint == 1) {
					if(Spleef.hasGameState(Spleef.GameState.INGAME))
					{
						Bukkit.broadcastMessage("§b§l" + p.getName() + " has stopped the game.");
						p.playSound(p.getLocation(), Sound.NOTE_PLING, 100, 1);
						
						for(Player player : Bukkit.getOnlinePlayers())
						{
						player.getInventory().clear();
						player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 1);
						World w = Bukkit.getWorld(Spleef.selectedMap);
						player.teleport(Bukkit.getWorld("world").getSpawnLocation());
						
						Spleef.team.clear();
						Spleef.brawler.clear();
						Spleef.archer.clear();
						Spleef.snowballer.clear();
						
						
						player.getInventory().clear();
						player.setAllowFlight(false);
						player.showPlayer(player);
						
						for(String ded : Spleef.dead)
						{
							Player ps = Bukkit.getPlayerExact(ded);
							player.showPlayer(ps);
							ps.showPlayer(player);
							
							if(ded != null)
							{	
								player.showPlayer(ps);
								ps.showPlayer(player);
								ps.teleport(Bukkit.getWorld("world").getSpawnLocation());
							}
						}
						
						for(String spc : Spleef.spec)
						{
							Player ps = Bukkit.getPlayerExact(spc);
							p.showPlayer(ps);
							ps.showPlayer(p);
							
							if(spc != null)
							{	
								p.showPlayer(ps);
								ps.showPlayer(p);
								ps.teleport(Bukkit.getWorld("world").getSpawnLocation());
							}	
							
						}
						
						player.setFlying(false);
						
						Spleef.dead.clear();
						Spleef.alive.clear();
						Spleef.spec.clear();
						
						Bukkit.unloadWorld(w, false);
						
						Spleef.seconds = 60;
						
						}
						Spleef.setGameState(Spleef.GameState.WAITING);
						
						HandlerList.unregisterAll();
						Bukkit.getScheduler().cancelAllTasks();
						Game.registerEvents(Bukkit.getPluginManager().getPlugin("Game"), new runner.events.Events());
						Game.registerEvents(Bukkit.getPluginManager().getPlugin("Game"), new game.events.Events());
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
						 for(Player p1 : Bukkit.getOnlinePlayers())
							{
							p1.getInventory().clear();
							p1.playSound(p1.getLocation(), Sound.NOTE_PLING, 100, 1);
							World w = Bukkit.getWorld(Runner.selectedMap);
							p1.teleport(Bukkit.getWorld("world").getSpawnLocation());
							
							Runner.cooldownTime.remove(p1);
							Runner.team.clear();
							p1.getInventory().clear();
							p1.setAllowFlight(false);
							p1.showPlayer(p1);
							
							for(String ded : Runner.dead)
							{
								Player ps = Bukkit.getPlayerExact(ded);
								p1.showPlayer(ps);
								ps.showPlayer(p1);
								
								if(ded != null)
								{	
									p1.showPlayer(ps);
									ps.showPlayer(p1);
									ps.teleport(Bukkit.getWorld("world").getSpawnLocation());
								}
							}
							
							p1.setFlying(false);
							
							Runner.dead.clear();
							Runner.alive.clear();
							
							Runner.jumper.clear();
							Runner.archer.clear();
							Runner.frosty.clear();
							
							Bukkit.unloadWorld(w, false);
							
							Runner.seconds = 60;
						
							}
						    Runner.setGameState(Runner.GameState.WAITING);
							
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
					}
				}
			   }
			if(args[0].equalsIgnoreCase("edit"))
				{
					if(Runner.hasGameState(GameState.WAITING) || Runner.hasGameState(GameState.STARTING)) {
						WorldCreator wc = new WorldCreator(Runner.selectedMap);
						wc.createWorld();
						World cw = Bukkit.getWorld(Runner.selectedMap);
						p.teleport(cw.getSpawnLocation());
						p.getInventory().clear();
						p.setGameMode(GameMode.CREATIVE);
						p.setAllowFlight(true);
					}
			    }
				if(args[0].equalsIgnoreCase("done"))
				{
					if(Runner.hasGameState(GameState.WAITING) || Runner.hasGameState(GameState.STARTING)) {
					p.teleport(Bukkit.getWorld("world").getSpawnLocation());
					p.getInventory().clear();
					p.setGameMode(GameMode.SURVIVAL);
					p.setAllowFlight(false);
					p.getInventory().clear();
					}
			    }
			    if(args[0].equalsIgnoreCase("autostart"))
				{
					if(Runner.hasGameState(GameState.WAITING) || Runner.hasGameState(GameState.STARTING)) {
                    if(Runner.autoStart == true) {
                    	Runner.autoStart = false;
                    	Bukkit.broadcastMessage("§c§lAuto-start disabled!");
                    }
                    else if(Runner.autoStart == false) {
                    	Runner.autoStart = true;
                    	Bukkit.broadcastMessage("§a§lAuto-start enabled!");
                    }
					}
			    }
				if(args[0].equalsIgnoreCase("testing"))
				{
					if(Runner.hasGameState(GameState.WAITING) || Runner.hasGameState(GameState.STARTING)) {
                    if(Runner.testing == false) {
                    	Runner.testing = true;
                    	Bukkit.broadcastMessage("§a§lTesting enabled!");
                    }
                    else if(Runner.testing == true) {
                    	Runner.testing = false;
                    	Bukkit.broadcastMessage("§c§lTesting disabled!");
                    }
					}
			    }
		return false;
			
   }

}