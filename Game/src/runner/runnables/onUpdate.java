package runner.runnables;

import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
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
			
			if(Runner.seconds > 1)
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

				Scoreboard sc = Bukkit.getScoreboardManager().getNewScoreboard();
				Objective obj1 = sc.registerNewObjective("stats", "dummy");
				obj1.setDisplayName("§6§lRUNNER");
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
			
			Runner.globalChatSecs = (int) Runner.seconds;
			
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
				
				PacketUtils.sendActionBar(p, "§fGame Start §c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Runner.seconds + ".0 Seconds");
		        
				}
				}
			else if(Runner.seconds == 11)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
				PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Runner.seconds + ".0 Seconds");
				}
			}
			else if(Runner.seconds == 10)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
					PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Runner.seconds + ".0 Seconds");
				}
			}
			else if(Runner.seconds == 9)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
					PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Runner.seconds + ".0 Seconds");					}
			    }
			else if(Runner.seconds == 8)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
					PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Runner.seconds + ".0 Seconds");							}
			}
			else if(Runner.seconds == 7)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
				PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Runner.seconds + ".0 Seconds");							}
			}
			else if(Runner.seconds == 6)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
				PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Runner.seconds + ".0 Seconds");							}
			}
			else if(Runner.seconds == 5)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
					PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Runner.seconds + ".0 Seconds");							}
			}
			else if(Runner.seconds == 4)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
				PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Runner.seconds + ".0 Seconds");							}
			}
			else if(Runner.seconds == 3)
			{	for(Player p : Bukkit.getOnlinePlayers())
				{
				PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Runner.seconds + ".0 Seconds");							}
			}
			else if(Runner.seconds == 2)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
				PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌ §f" + Runner.seconds + ".0 Seconds");							}
			}
			else if(Runner.seconds == 1)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
				PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌ §f" + Runner.seconds + ".0 Second");							}
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
				PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌ §f" + Runner.seconds + ".0 Seconds");	
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
			obj.setDisplayName("§§lRUNNER");
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
	    				PacketUtils.sendActionBar(p, "§fLeap §c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Runner.cooldownTime.get(p) + ".0 Seconds");
	            	}	
	            	if(Runner.cooldownTime.get(p) == 4) {
	    				PacketUtils.sendActionBar(p, "§fLeap §a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Runner.cooldownTime.get(p) + ".0 Seconds");
	            	}	
	            	if(Runner.cooldownTime.get(p) == 3) {
	    				PacketUtils.sendActionBar(p, "§fLeap §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Runner.cooldownTime.get(p) + ".0 Seconds");
	            	}	
	            	if(Runner.cooldownTime.get(p) == 2) {
	    				PacketUtils.sendActionBar(p, "§fLeap §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Runner.cooldownTime.get(p) + ".0 Seconds");
	            	}	
	            	if(Runner.cooldownTime.get(p) == 1) {
	    				PacketUtils.sendActionBar(p, "§fLeap §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌ §f" + Runner.cooldownTime.get(p) + ".0 Seconds");
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
                    Firework f = (Firework) p.getWorld().spawn(p.getWorld().getSpawnLocation().add(Math.random() * 160.0D - 80.0D, 10.0D + Math.random() * 20.0D, Math.random() * 160.0D - 80.0D), Firework.class);
                    FireworkMeta fm = f.getFireworkMeta();                    
                    fm.addEffect(FireworkEffect.builder()
                                    .flicker(false)
                                    .with(FireworkEffect.Type.BALL_LARGE)
                                    .trail(false)
                                    .build());
					}
				}
				else
				{
	             Game.randomint = 1;
	             
                 Game.Spleef();
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
         Game.randomint = 1;
         
         Game.Spleef();
		}
	}	
		
	}

}
