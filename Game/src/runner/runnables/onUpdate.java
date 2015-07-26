package runner.runnables;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import ca.wacos.nametagedit.NametagAPI;
import game.Game;
import runner.Runner;
import runner.Runner.GameState;
import spleef.Spleef;
import utils.PacketUtils;

public class onUpdate extends BukkitRunnable{

	@Override
	public void run() {
		if(Runner.hasGameState(Runner.GameState.WAITING))
		{
			if(Bukkit.getOnlinePlayers().size() >= 2 && Runner.autoStart == true){
				Runner.setGameState(GameState.STARTING);
				Runner.seconds = 30;
			}
			
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(!Runner.team.containsKey(p)){
                      NametagAPI.setPrefix(p.getName(), "&7");
				    }
				}
			
			if(!Runner.alive.isEmpty() || !Runner.dead.isEmpty())
			{
				Runner.alive.clear();
				Runner.dead.clear();
			}
			
			for(Player p : Bukkit.getOnlinePlayers())
			{ 
				p.setHealth(20);
				p.setFoodLevel(20);
			}
			
			if(Runner.waitingRoomTimer != 15)
			{
				Runner.waitingRoomTimer++;
			}
			else
			{
				Runner.waitingRoomTimer = 0;
			}
		}
		else if(Runner.hasGameState(GameState.STARTING))
		{
			for(Player p : Bukkit.getOnlinePlayers())
			{ 
				p.setHealth(20);
				p.setFoodLevel(20);
			}
			
			if(Bukkit.getOnlinePlayers().size() < 2 && Runner.testing == false || Runner.autoStart == false && Runner.testing == false){
				Runner.setGameState(GameState.WAITING);
				Bukkit.broadcastMessage("§c§lStopping Runner.");
			}
			
			for(Player p : Bukkit.getOnlinePlayers())
			{
				if(!Runner.team.containsKey(p.getName()))
				{
					Runner.team.put(p.getName(), "Players Team");
					
					p.sendMessage("§9Team> §7You have joined §e§lPlayers Team§7.");
					
					NametagAPI.setPrefix(p.getName(), "&e");
				}
				
				if(NametagAPI.getPrefix(p.getName()) != "&e") {
					NametagAPI.setPrefix(p.getName(), "&e");
				}
			}
			for(Player p : Bukkit.getOnlinePlayers())
			{
				p.setHealth(20);
				p.setFoodLevel(20);
				
				if(!Runner.jumper.contains(p.getName()) && !Runner.archer.contains(p.getName()) && !Runner.frosty.contains(p.getName())) {
					Runner.jumper.add(p.getName());
				}
			}
			
			if(Runner.seconds <= 10 && Runner.seconds >= 1)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
					p.playSound(p.getLocation(), Sound.NOTE_PLING, 100, 1);
				}
			}
			
			if(Runner.seconds > 0)
			{
				Runner.seconds--;
			}
			else
			{
				//copyWorld(Bukkit.getWorld(selectedMap).getWorldFolder(), new File(Bukkit.getWorld(selectedMap).getWorldFolder() + "_clone"));
				WorldCreator wc = new WorldCreator(Runner.selectedMap);
				wc.createWorld();
				World cw = Bukkit.getWorld(Runner.selectedMap);
				cw.setAutoSave(false);
				for(Player p : Bukkit.getOnlinePlayers())
				{
				p.teleport(cw.getSpawnLocation());
				p.getInventory().clear();
				Runner.alive.add(p.getName());
				}
				Runner.setGameState(GameState.INGAMEWAIT);
				Runner.seconds = 12;
				Runner.update = false;

				Scoreboard sc = Bukkit.getScoreboardManager().getNewScoreboard();
				Objective obj1 = sc.registerNewObjective("stats", "dummy");
				obj1.setDisplayName("§b§lRunner");
				obj1.setDisplaySlot(DisplaySlot.SIDEBAR);
					
				sc.getObjective("stats").getScore("§e").setScore(15);
				sc.getObjective("stats").getScore("§aPlayers Alive").setScore(14);
				sc.getObjective("stats").getScore("" + Runner.alive.size()).setScore(13);
				sc.getObjective("stats").getScore("§b").setScore(12);
				sc.getObjective("stats").getScore("§cPlayers Dead").setScore(11);
				sc.getObjective("stats").getScore("" + Runner.dead.size()).setScore(10);
				
				for(Player p : Bukkit.getOnlinePlayers())
					p.setScoreboard(sc);
				
				Runner.chatMuted = true;
			}
		}
		else if(Runner.hasGameState(GameState.INGAMEWAIT))
		{
			for(Player p : Bukkit.getOnlinePlayers())
			{
			p.setHealth(20);
			p.setFoodLevel(20);
			}
			
			Runner.globalChatSecs = Runner.seconds;
			
			if(Runner.seconds == 12)
			{
				Bukkit.broadcastMessage("§2§l§m=============================================");
				Bukkit.broadcastMessage("§aGame - §e§lRunner");
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(" Right click the axe to Leap!");
				Bukkit.broadcastMessage(" Blocks break under your feet!");
				Bukkit.broadcastMessage(" Last player alive wins!");
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage("§aMap - §e§l" + Runner.customMapName + " §7created by §e§l" + Runner.customMapCreator);
				Bukkit.broadcastMessage("§2§l§m=============================================");
				for(Player p : Bukkit.getOnlinePlayers()){
				p.playSound(p.getLocation(), Sound.LEVEL_UP, 100, 1);
				
				PacketUtils.sendActionBar(p, "§fGame Start §c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Runner.seconds + " Seconds");
		        
				}
				}
			else if(Runner.seconds == 11)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
				PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Runner.seconds + " Seconds");
				}
			}
			else if(Runner.seconds == 10)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
					PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Runner.seconds + " Seconds");
				}
			}
			else if(Runner.seconds == 9)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
					PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Runner.seconds + " Seconds");					}
			    }
			else if(Runner.seconds == 8)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
					PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Runner.seconds + " Seconds");							}
			}
			else if(Runner.seconds == 7)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
				PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Runner.seconds + " Seconds");							}
			}
			else if(Runner.seconds == 6)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
				PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Runner.seconds + " Seconds");							}
			}
			else if(Runner.seconds == 5)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
					PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Runner.seconds + " Seconds");							}
			}
			else if(Runner.seconds == 4)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
				PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Runner.seconds + " Seconds");							}
			}
			else if(Runner.seconds == 3)
			{	for(Player p : Bukkit.getOnlinePlayers())
				{
				PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Runner.seconds + " Seconds");							}
			}
			else if(Runner.seconds == 2)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
				PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌ §f" + Runner.seconds + " Seconds");							}
			}
			else if(Runner.seconds == 1)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
				PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌ §f" + Runner.seconds + " Second");							}
			}
			
			if(Runner.seconds != 0)
			{
				Runner.seconds--;
				for(Player p : Bukkit.getOnlinePlayers())
				{
				p.playSound(p.getLocation(), Sound.NOTE_STICKS, 100, 1);
				}
			}
			else
			{
				Runner.setGameState(GameState.INGAME);
				for(Player p : Bukkit.getOnlinePlayers())
				{
				PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌ §f" + Runner.seconds + " Seconds");	
				p.playSound(p.getLocation(), Sound.NOTE_PLING, 100, 1);
				p.sendMessage("§9Chat> §7Chat is no longer silenced.");
				
				if(Runner.jumper.contains(p.getName())) {
					ItemStack axe = new ItemStack(Material.IRON_AXE);
				    ItemMeta axe1 = (ItemMeta) axe.getItemMeta();
				    axe1.setDisplayName("§fIron Axe");
				    axe.setItemMeta(axe1);
					ItemMeta meta = axe.getItemMeta();
					meta.spigot().setUnbreakable(true);
					axe.setItemMeta(meta);
					p.getInventory().addItem(axe);
			   } else if(Runner.archer.contains(p.getName())) {
						ItemStack bow = new ItemStack(Material.BOW);
					    ItemMeta bow1 = (ItemMeta) bow.getItemMeta();
					    bow1.setDisplayName("§fBow");
					    bow.setItemMeta(bow1);
						ItemMeta meta = bow.getItemMeta();
						meta.spigot().setUnbreakable(true);
						bow.setItemMeta(meta);
						p.getInventory().addItem(bow);
					}
				}
				Runner.seconds = 10;
				Runner.chatMuted = false;
			}
}

        else if(Runner.hasGameState(GameState.INGAME))
		{	
			for(Player p : Bukkit.getOnlinePlayers())
			{ 
				p.setHealth(20);
				p.setFoodLevel(20);
			}
			
        	ScoreboardManager manager = Bukkit.getScoreboardManager();
			Scoreboard sc = manager.getNewScoreboard();
			Objective obj = sc.registerNewObjective("stats", "dummy");
			obj.setDisplayName("§b§lRunner");
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			
			if(Runner.alive.size() < 14) {	
			sc.getObjective("stats").getScore("§e").setScore(15);
			for(int i = 0; i < Runner.alive.size(); i++)
			{
			Player pl = Bukkit.getPlayerExact(Runner.alive.get(i));
			sc.getObjective("stats").getScore("§a" + pl.getName()).setScore(i+15-Runner.alive.size());
			}
			for(int i = 0; i < Runner.dead.size(); i++)
			{
			Player pl = Bukkit.getPlayerExact(Runner.dead.get(i));
			sc.getObjective("stats").getScore("§7" + pl.getName()).setScore(i+15 - Runner.dead.size() - Runner.alive.size());
			}
			} else {
				sc.getObjective("stats").getScore("§e").setScore(15);
				sc.getObjective("stats").getScore("§aPlayers Alive").setScore(14);
				sc.getObjective("stats").getScore("" + Runner.alive.size()).setScore(13);
				sc.getObjective("stats").getScore("§b").setScore(12);
				sc.getObjective("stats").getScore("§cPlayers Dead").setScore(11);
				sc.getObjective("stats").getScore("" + Runner.dead.size()).setScore(10);
			}
			
			for(Player p : Bukkit.getOnlinePlayers())
			{
			p.setScoreboard(sc);
			
			  if (Runner.cooldownTime.containsKey(p) & p.getItemInHand().getType() == Material.IRON_AXE && Runner.alive.contains(p.getName())) {
	            	if(Runner.cooldownTime.get(p) == 5) {
	    				PacketUtils.sendActionBar(p, "§fLeap §c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Runner. cooldownTime.get(p) + " Seconds");
	            	}	
	            	if(Runner.cooldownTime.get(p) == 4) {
	    				PacketUtils.sendActionBar(p, "§fLeap §a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Runner.cooldownTime.get(p) + " Seconds");
	            	}	
	            	if(Runner.cooldownTime.get(p) == 3) {
	    				PacketUtils.sendActionBar(p, "§fLeap §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Runner.cooldownTime.get(p) + " Seconds");
	            	}	
	            	if(Runner.cooldownTime.get(p) == 2) {
	    				PacketUtils.sendActionBar(p, "§fLeap §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Runner.cooldownTime.get(p) + " Seconds");
	            	}	
	            	if(Runner.cooldownTime.get(p) == 1) {
	    				PacketUtils.sendActionBar(p, "§fLeap §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌ §f" + Runner.cooldownTime.get(p) + " Seconds");
	            	}	
	        }
				}
			
			if(Runner.alive.size() <= 1 && Runner.testing == false)
			{
				if(Runner.seconds != 0)
				{
					Runner.seconds--;
					
					for(String a : Runner.alive)
					{
				    Player p = Bukkit.getPlayerExact(a);
                    Firework f = (Firework) p.getWorld().spawn(new Location(p.getWorld(), 12.588, 0.9486, 404.568), Firework.class);
                    
                    FireworkMeta fm = f.getFireworkMeta();
                    fm.addEffect(FireworkEffect.builder()
                                    .flicker(false)
                                    .trail(true)
                                    .withColor(Color.AQUA)
                                    .build());
                    fm.setPower(1);
                    f.setFireworkMeta(fm);
					}
				}
				else
				{
					Runner.setGameState(GameState.WAITING);
					World w = Bukkit.getWorld(Runner.selectedMap);
					for(Player p : Bukkit.getOnlinePlayers())
					{
					p.teleport(Bukkit.getWorld("world").getSpawnLocation());
					p.getInventory().clear();
					p.setAllowFlight(false);
					
					for(int i = 0; i < Runner.dead.size(); i++)
					{
						Player pl = Bukkit.getPlayerExact(Runner.dead.get(i));
						
						p.showPlayer(pl);
					}
					
					for(int i = 0; i < Runner.spec.size(); i++)
					{
						Player pl = Bukkit.getPlayerExact(Runner.spec.get(i));
						
						p.showPlayer(pl);
					}
					}
					
					Bukkit.unloadWorld(w, false);
					
					Runner.dead.clear();
					Runner.alive.clear();
					Runner.spec.clear();
					Runner.cooldownTime.clear();
					
					WorldCreator wc = new WorldCreator(Runner.selectedMap);
					wc.createWorld();
					
					w.setAutoSave(false);
					//for(Chunk c : w.getLoadedChunks())
					//{
					//	c.unload(false, false);
					//}
					//RegionFileCache.a();
					//deleteWorld(new File(Bukkit.getWorld(selectedMap).getName() + "_clone"));
					Runner.seconds = 60;
					
					Runner.secs = 0;
					Runner.mins = 0;
					Runner.hrs = 0;
					Runner.timerSecs = 0;
					Runner.secondSecs = 0;
					
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
					World w1 = Bukkit.getWorld(Spleef.selectedMap);
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
					
					Bukkit.unloadWorld(w1, false);
					
					Spleef.seconds = 60;
					}
					Spleef.setGameState(Spleef.GameState.WAITING);
				}
				
				}

			if(Runner.alive.size() + Runner.dead.size() == 1 && Runner.hasGameState(GameState.INGAME) && Runner.testing == false) {
				
				Bukkit.broadcastMessage("§c§lStopping Runner.");
				Runner.setGameState(GameState.STOPPED);
			}
			
			if(Runner.alive.size() == 0 && Runner.seconds == 9 & Runner.hasGameState(GameState.INGAME)) {
				Runner.setGameState(GameState.STOPPED);
			}
			if(Runner.alive.size() + Runner.dead.size() == 2 && Runner.seconds == 9 && !(Runner.alive.size() == 0) & Runner.hasGameState(GameState.INGAME))
				{
					Bukkit.broadcastMessage("§2§l§m=============================================");
					Bukkit.broadcastMessage("§aGame - §e§lRunner");
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage("§c§l1st Place §f- " + Runner.alive.get(0));
					Bukkit.broadcastMessage("§6§l2nd Place §f- " + Runner.dead.get(Runner.dead.size() - 1));
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage("§aMap - §e§l" + Runner.customMapName + " §7created by §e§l" + Runner.customMapCreator);
					Bukkit.broadcastMessage("§2§l§m=============================================");
				    for(Player p :Bukkit.getOnlinePlayers()) {
					PacketUtils.sendTitle(p, "§e" + Runner.alive.get(0), "§ewon the game", 20, 200, 20);
					  }
				}
			if(Runner.alive.size() + Runner.dead.size() >= 3 && Runner.seconds == 9 && !(Runner.alive.size() == 0) & Runner.hasGameState(GameState.INGAME))
			{
				Bukkit.broadcastMessage("§2§l§m=============================================");
				Bukkit.broadcastMessage("§aGame - §e§lRunner");
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage("§c§l1st Place §f- " + Runner.alive.get(0));
				Bukkit.broadcastMessage("§6§l2nd Place §f- " + Runner.dead.get(Runner.dead.size() - 1));
				Bukkit.broadcastMessage("§e§l3rd Place §f- " + Runner.dead.get(Runner.dead.size() - 2));
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage("§aMap - §e§l" + Runner.customMapName + " §7created by §e§l" + Runner.customMapCreator);
				Bukkit.broadcastMessage("§2§l§m=============================================");
				  for(Player p :Bukkit.getOnlinePlayers()) {
				  PacketUtils.sendTitle(p, "§e" + Runner.alive.get(0), "§ewon the game", 20, 200, 20);
				  }
			}


	 if(Runner.hasGameState(GameState.STOPPED))
		{
			for(Player p : Bukkit.getOnlinePlayers())
			{
			p.getInventory().clear();
			p.playSound(p.getLocation(), Sound.NOTE_PLING, 100, 1);
			World w = Bukkit.getWorld(Runner.selectedMap);
			p.teleport(Bukkit.getWorld("world").getSpawnLocation());
			
			Runner.team.remove(p.getName());
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
			
			p.setFlying(false);
			
			Runner.	dead.clear();
			Runner.	alive.clear();
			Runner.	spec.clear();
			
		
			Bukkit.unloadWorld(w, false);
			
			Runner.	seconds = 60;
			}
			Runner.	setGameState(GameState.WAITING);
			
			Runner.secs = 0;
			Runner.mins = 0;
			Runner.hrs = 0;
			Runner.timerSecs = 0;
			Runner.secondSecs = 0;
			
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
	}	
		
	}

}
