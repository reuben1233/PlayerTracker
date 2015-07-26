package spleef.runnables;

import org.bukkit.Bukkit;
import org.bukkit.Color;
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
import spleef.Spleef;
import spleef.Spleef.GameState;
import utils.PacketUtils;

public class onUpdate extends BukkitRunnable{

	@Override
	public void run() {
		if(Spleef.hasGameState(GameState.WAITING))
		{
			
			if(Bukkit.getOnlinePlayers().size() >= 2){
				Spleef.setGameState(GameState.STARTING);
				Spleef.seconds = 30;
			}
			
			for(Player p : Bukkit.getOnlinePlayers()) {
			if(!Spleef.team.containsKey(p)){
              NametagAPI.setPrefix(p.getName(), "&7");
		    }
		}
			
			if(!Spleef.alive.isEmpty() || !Spleef.dead.isEmpty())
			{
				Spleef.alive.clear();
				Spleef.dead.clear();
			}
			
			for(Player p : Bukkit.getOnlinePlayers())
			{
				p.setHealth(20);
				p.setFoodLevel(20);
			}
			
			if(Spleef.waitingRoomTimer != 15)
			{
				Spleef.waitingRoomTimer++;
			}
			else
			{
				Spleef.waitingRoomTimer = 0;
			}
			
			if(Spleef.waitingRoomTimer <= 4)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
					PacketUtils.sendActionBar(p, "§f§lPlaying §c§lSpleef");
				}
			}
			else if(Spleef.waitingRoomTimer >= 5 && Spleef.waitingRoomTimer <= 9)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
					PacketUtils.sendActionBar(p, "§f§lMap §e§l" + Spleef.customMapName);
				}
			}
			else if(Spleef.waitingRoomTimer >= 10 && Spleef.waitingRoomTimer <= 14)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
					PacketUtils.sendActionBar(p, "§9§lBy " + Spleef.customMapCreator);
				}
			}
			
			for(Player p : Bukkit.getOnlinePlayers()) {
			if(!Spleef.snowballer.contains(p) || !Spleef.brawler.contains(p) || !Spleef.archer.contains(p)) {
				Spleef.none.add(p.getName());
			}
			else{
				Spleef.none.remove(p.getName());
			}
			}
			
		}
		else if(Spleef.hasGameState(GameState.STARTING))
		{
			
			if(Bukkit.getOnlinePlayers().size() < 2){
				Spleef.setGameState(GameState.WAITING);
				Bukkit.broadcastMessage("§4§lNot enough players... Stopping Spleef.");
			}
			
			for(Player p : Bukkit.getOnlinePlayers())
			{
				if(!Spleef.snowballer.contains(p.getName()) && !Spleef.brawler.contains(p.getName()) && !Spleef.archer.contains(p.getName()))
				{
					Spleef.snowballer.add(p.getName());
				}
			}
			
			for(Player p : Bukkit.getOnlinePlayers())
			{
				if(!Spleef.team.containsKey(p.getName()))
				{
					Spleef.team.put(p.getName(), "Players Team");
					
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
			}
			
			if(Spleef.seconds <= 10 && Spleef.seconds >= 1)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
					p.playSound(p.getLocation(), Sound.NOTE_PLING, 100, 1);
				}
			}
			
			if(Spleef.seconds > 0)
			{
				Spleef.seconds--;
			}
			else
			{
				//copyWorld(Bukkit.getWorld(selectedMap).getWorldFolder(), new File(Bukkit.getWorld(selectedMap).getWorldFolder() + "_clone"));
				WorldCreator wc = new WorldCreator(Spleef.selectedMap);
				wc.createWorld();
				World cw = Bukkit.getWorld(Spleef.selectedMap);
				cw.setAutoSave(false);
				for(Player p : Bukkit.getOnlinePlayers())
				{
				p.teleport(cw.getSpawnLocation());
				p.getInventory().clear();
				Spleef.alive.add(p.getName());
				}
				Spleef.setGameState(GameState.INGAMEWAIT);
				Spleef.seconds = 12;
				Spleef.update = false;

				Scoreboard sc = Bukkit.getScoreboardManager().getNewScoreboard();
				Objective obj = sc.registerNewObjective("stats", "dummy");
				obj.setDisplayName("§b§lSpleef");
				obj.setDisplaySlot(DisplaySlot.SIDEBAR);
					
				sc.getObjective("stats").getScore("§e").setScore(15);
				sc.getObjective("stats").getScore("§aPlayers Alive").setScore(14);
				sc.getObjective("stats").getScore("" + Spleef.alive.size()).setScore(13);
				sc.getObjective("stats").getScore("§b").setScore(12);
				sc.getObjective("stats").getScore("§cPlayers Dead").setScore(11);
				sc.getObjective("stats").getScore("" + Spleef.dead.size()).setScore(10);
				
				for(Player p : Bukkit.getOnlinePlayers())
					p.setScoreboard(sc);
				
				Spleef.chatMuted = true;
			}
		}
		else if(Spleef.hasGameState(GameState.INGAMEWAIT))
		{
			for(Player p : Bukkit.getOnlinePlayers())
			{
			p.setHealth(20);
			p.setFoodLevel(20);
			}
			
			Spleef.globalChatSecs = Spleef.seconds;
			
			if(Spleef.seconds == 12)
			{
				Bukkit.broadcastMessage("§2§l§m=============================================");
				Bukkit.broadcastMessage("§aGame - §e§lSpleef");
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(" Free for all fight to the death!");
				Bukkit.broadcastMessage(" Break blocks to restore hunger!");
				Bukkit.broadcastMessage(" Last player alive wins!");
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage("§aMap - §e§l" + Spleef.customMapName + " §7created by §e§l" + Spleef.customMapCreator);
				Bukkit.broadcastMessage("§2§l§m=============================================");
				for(Player p : Bukkit.getOnlinePlayers()){
				p.playSound(p.getLocation(), Sound.LEVEL_UP, 100, 1);
				
				PacketUtils.sendActionBar(p, "§fGame Start §c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Spleef.seconds + " Seconds");
		        
				}
				}
			else if(Spleef.seconds == 11)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
				PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" +Spleef. seconds + " Seconds");
				}
			}
			else if(Spleef.seconds == 10)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
				PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" +Spleef. seconds + " Seconds");
				}
			}
			else if(Spleef.seconds == 9)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
			    PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Spleef.seconds + " Seconds");							}
			    }
			else if(Spleef.seconds == 8)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
				PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" +Spleef. seconds + " Seconds");							}
			}
			else if(Spleef.seconds == 7)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
				PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Spleef.seconds + " Seconds");							}
			}
			else if(Spleef.seconds == 6)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
				PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Spleef.seconds + " Seconds");							}
			}
			else if(Spleef.seconds == 5)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
				PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Spleef.seconds + " Seconds");							}
			}
			else if(Spleef.seconds == 4)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
				PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Spleef.seconds + " Seconds");							}
			}
			else if(Spleef.seconds == 3)
			{	for(Player p : Bukkit.getOnlinePlayers())
				{
				PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Spleef.seconds + " Seconds");							}
			}
			else if(Spleef.seconds == 2)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
				PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌ §f" + Spleef.seconds + " Seconds");							}
			}
			else if(Spleef.seconds == 1)
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
				PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌ §f" + Spleef.seconds + " Second");							}
			}
			
			if(Spleef.seconds != 0)
			{
				Spleef.seconds--;
				for(Player p : Bukkit.getOnlinePlayers())
				{
				p.playSound(p.getLocation(), Sound.NOTE_STICKS, 100, 1);
				}
			}
			else
			{
				Spleef.setGameState(GameState.INGAME);
				for(Player p : Bukkit.getOnlinePlayers())
				{
				PacketUtils.sendActionBar(p, "§fGame Start §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌ §f" + Spleef.seconds + " Seconds");	
				p.playSound(p.getLocation(), Sound.NOTE_PLING, 100, 1);
				p.sendMessage("§9Chat> §7Chat is no longer silenced.");
				
				if(Spleef.snowballer.contains(p.getName())) {
				ItemStack snowball = new ItemStack(Material.SNOW_BALL);
			    ItemMeta snowball1 = (ItemMeta) snowball.getItemMeta();
			    snowball1.setDisplayName("§fSnowball");
			    snowball.setItemMeta(snowball1);
				ItemMeta meta = snowball.getItemMeta();
				meta.spigot().setUnbreakable(true);
				snowball.setItemMeta(meta);
				p.getInventory().addItem(snowball);
				} else if(Spleef.brawler.contains(p.getName())) {
					ItemStack axe = new ItemStack(Material.IRON_AXE);
				    ItemMeta axe1 = (ItemMeta) axe.getItemMeta();
				    axe1.setDisplayName("§fIron Axe");
				    axe.setItemMeta(axe1);
					ItemMeta meta = axe.getItemMeta();
					meta.spigot().setUnbreakable(true);
					axe.setItemMeta(meta);
					p.getInventory().addItem(axe);
				} else if(Spleef.archer.contains(p.getName())) {
					ItemStack bow = new ItemStack(Material.BOW);
				    ItemMeta bow1 = (ItemMeta) bow.getItemMeta();
				    bow1.setDisplayName("§fBow");
				    bow.setItemMeta(bow1);
					ItemMeta meta = bow.getItemMeta();
					meta.spigot().setUnbreakable(true);
					bow.setItemMeta(meta);
					p.getInventory().addItem(bow);
					
					ItemStack arrow = new ItemStack(Material.ARROW);
				    ItemMeta arrow1 = (ItemMeta) arrow.getItemMeta();
				    arrow1.setDisplayName("§fArrow");
				    arrow.setItemMeta(arrow1);
					ItemMeta meta1 = arrow.getItemMeta();
					meta1.spigot().setUnbreakable(true);
					arrow.setItemMeta(meta1);
					p.getInventory().addItem(arrow);
				}
			}
				
				Spleef.seconds = 10;
				Spleef.chatMuted = false;
			}
		}

        else if(Spleef.hasGameState(GameState.INGAME))
		{	
			
			ScoreboardManager manager = Bukkit.getScoreboardManager();
			Scoreboard sc = manager.getNewScoreboard();
			Objective obj = sc.registerNewObjective("stats", "dummy");
			obj.setDisplayName("§b§lSpleef");
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			
			if(Spleef.alive.size() < 14) {	
			sc.getObjective("stats").getScore("§e").setScore(15);
			for(int i = 0; i < Spleef.alive.size(); i++)
			{
			Player pl = Bukkit.getPlayerExact(Spleef.alive.get(i));
			sc.getObjective("stats").getScore("§a" + pl.getName()).setScore(i+15-Spleef.alive.size());
			}
			for(int i = 0; i < Spleef.dead.size(); i++)
			{
			Player pl = Bukkit.getPlayerExact(Spleef.dead.get(i));
			sc.getObjective("stats").getScore("§7" + pl.getName()).setScore(i+15 - Spleef.dead.size() - Spleef.alive.size());
			}
			} else {
				sc.getObjective("stats").getScore("§e").setScore(15);
				sc.getObjective("stats").getScore("§aPlayers Alive").setScore(14);
				sc.getObjective("stats").getScore("" + Spleef.alive.size()).setScore(13);
				sc.getObjective("stats").getScore("§b").setScore(12);
				sc.getObjective("stats").getScore("§cPlayers Dead").setScore(11);
				sc.getObjective("stats").getScore("" + Spleef.dead.size()).setScore(10);
			}
			
			
			for(Player p : Bukkit.getOnlinePlayers())
			{
			p.setScoreboard(sc);
			
			  if (Spleef.cooldownTime.containsKey(p) & p.getItemInHand().getType() == Material.IRON_AXE && Spleef.alive.contains(p.getName())) {
	            	if(Spleef.cooldownTime.get(p) == 5) {
	    				PacketUtils.sendActionBar(p, "§fLeap §c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Spleef.cooldownTime.get(p) + " Seconds");
	            	}	
	            	if(Spleef.cooldownTime.get(p) == 4) {
	    				PacketUtils.sendActionBar(p, "§fLeap §a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Spleef.cooldownTime.get(p) + " Seconds");
	            	}	
	            	if(Spleef.cooldownTime.get(p) == 3) {
	    				PacketUtils.sendActionBar(p, "§fLeap §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Spleef.cooldownTime.get(p) + " Seconds");
	            	}	
	            	if(Spleef.cooldownTime.get(p) == 2) {
	    				PacketUtils.sendActionBar(p, "§fLeap §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌§c▌ §f" + Spleef.cooldownTime.get(p) + " Seconds");
	            	}	
	            	if(Spleef.cooldownTime.get(p) == 1) {
	    				PacketUtils.sendActionBar(p, "§fLeap §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§c▌§c▌§c▌§c▌ §f" + Spleef.cooldownTime.get(p) + " Seconds");
	            	}	
	        }
			}
			
			if(Spleef.alive.size() <=1)
			{
				
				if(Spleef.seconds != 0)
				{
					Spleef.seconds--;
					
					for(String a : Spleef.alive)
					{
				    Player p = Bukkit.getPlayerExact(a);
                    Firework f = (Firework) p.getWorld().spawn(p.getPlayer().getWorld().getSpawnLocation(), Firework.class);
                    
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
					Game.Runner();
					
					Game.randomint = 0;
				}
				}
			}

				if(Spleef.alive.size() + Spleef.dead.size() == 1 && Spleef.seconds == 9 & Spleef.hasGameState(GameState.INGAME)) {
					
					Bukkit.broadcastMessage("§2§l§m=============================================");
					Bukkit.broadcastMessage("§aGame - §e§lSpleef");
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage("§eNobody won the game");
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage("§aMap - §e§l" + Spleef.customMapName + " §7created by §e§l" + Spleef.customMapCreator);
					Bukkit.broadcastMessage("§2§l§m=============================================");
                   
					for(Player p :Bukkit.getOnlinePlayers()) {
						PacketUtils.sendTitle(p, "§eNobody", "§ewon the game", 20, 200, 20);
				  }
				}
				if(Spleef.alive.size() == 0 && Spleef.seconds == 9 & Spleef.hasGameState(GameState.INGAME)) {
					
					Bukkit.broadcastMessage("§2§l§m=============================================");
					Bukkit.broadcastMessage("§aGame - §e§lSpleef");
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage("§eNobody won the game");
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage("§aMap - §e§l" + Spleef.customMapName + " §7created by §e§l" + Spleef.customMapCreator);
					Bukkit.broadcastMessage("§2§l§m=============================================");
                   
					for(Player p :Bukkit.getOnlinePlayers()) {
						PacketUtils.sendTitle(p, "§eNobody", "§ewon the game", 20, 200, 20);
				  }
				}
				if(Spleef.alive.size() + Spleef.dead.size() == 2 && Spleef.seconds == 9 && !(Spleef.alive.size() == 0) & Spleef.hasGameState(GameState.INGAME))
					{
						Bukkit.broadcastMessage("§2§l§m=============================================");
						Bukkit.broadcastMessage("§aGame - §e§lSpleef");
						Bukkit.broadcastMessage("");
						Bukkit.broadcastMessage("§c§l1st Place §f- " + Spleef.alive.get(0));
						Bukkit.broadcastMessage("§6§l2nd Place §f- " + Spleef.dead.get(Spleef.dead.size() - 1));
						Bukkit.broadcastMessage("");
						Bukkit.broadcastMessage("§aMap - §e§l" + Spleef.customMapName + " §7created by §e§l" + Spleef.customMapCreator);
						Bukkit.broadcastMessage("§2§l§m=============================================");
					    for(Player p :Bukkit.getOnlinePlayers()) {
					    	PacketUtils.sendTitle(p, "§e" + Spleef.alive.get(0), "§ewon the game", 20, 200, 20);
						  }
					}
				if(Spleef.alive.size() + Spleef.dead.size() >= 3 && Spleef.seconds == 9 && !(Spleef.alive.size() == 0) & Spleef.hasGameState(GameState.INGAME))
				{
					Bukkit.broadcastMessage("§2§l§m=============================================");
					Bukkit.broadcastMessage("§aGame - §e§lSpleef");
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage("§c§l1st Place §f- " + Spleef.alive.get(0));
					Bukkit.broadcastMessage("§6§l2nd Place §f- " + Spleef.dead.get(Spleef.dead.size() - 1));
					Bukkit.broadcastMessage("§e§l3rd Place §f- " + Spleef.dead.get(Spleef.dead.size() - 2));
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage("§aMap - §e§l" + Spleef.customMapName + " §7created by §e§l" +Spleef.customMapCreator);
					Bukkit.broadcastMessage("§2§l§m=============================================");
					  for(Player p :Bukkit.getOnlinePlayers()) {
						  PacketUtils.sendTitle(p, "§e" + Spleef.alive.get(0), "§ewon the game", 20, 200, 20);
					  }
				}

	 if(Spleef.hasGameState(GameState.STOPPED))
		{
			Game.Runner();
			
			Game.randomint = 0;
		}
	}	
		
	}
