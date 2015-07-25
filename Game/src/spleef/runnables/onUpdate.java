package spleef.runnables;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.ArmorStand;
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
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import ca.wacos.nametagedit.NametagAPI;
import game.Game;
import net.minecraft.server.v1_8_R3.EntitySheep;
import net.minecraft.server.v1_8_R3.EntitySkeleton;
import npc.NMSUtils;
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
			
			ScoreboardManager manager = Bukkit.getScoreboardManager();
			Scoreboard snowball = manager.getNewScoreboard();
			Objective obj = snowball.registerNewObjective("stats", "dummy");
			obj.setDisplayName("§a§lWaiting for Players");
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			
			Score s1 = obj.getScore("§1");
			Score s2 = obj.getScore("§e§lPlayers");
			Score s3 = obj.getScore("" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers());
			Score s4 = obj.getScore("§2");
			Score s5 = obj.getScore("§7§lKit");
			Score s6 = obj.getScore("Snowballer");
			Score s7 = obj.getScore("");
			Score s8 = obj.getScore("§b§lPlaying");
			Score s9 = obj.getScore("Spleef");
			
			for(Player p : Bukkit.getOnlinePlayers()) {
			if(Spleef.team.containsKey(p.getName()))
			{
				s5 = obj.getScore("§e§lKit");
			}
	        else if(!Spleef.team.containsKey(p.getName())){
		        s5 = obj.getScore("§7§lKit");
		    }
			}
			
			s1.setScore(9);
			s2.setScore(8);
			s3.setScore(7);
			s4.setScore(6);
			s5.setScore(5);
			s6.setScore(4);
			s7.setScore(3);
			s8.setScore(2);
			s9.setScore(1);
			
			Scoreboard brawl = manager.getNewScoreboard();
			obj = brawl.registerNewObjective("stats", "dummy");
			obj.setDisplayName("§a§lWaiting for Players");
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			
			Score s10 = obj.getScore("§1");
			Score s20 = obj.getScore("§e§lPlayers");
			Score s30 = obj.getScore("" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers());
			Score s40 = obj.getScore("§2");
			Score s50 = obj.getScore("§7§lKit");
			Score s60 = obj.getScore("Brawler");
			Score s70 = obj.getScore("");
			Score s80 = obj.getScore("§b§lPlaying");
			Score s90 = obj.getScore("Spleef");
			
			for(Player p : Bukkit.getOnlinePlayers()) {
			if(Spleef.team.containsKey(p.getName()))
			{
				s50 = obj.getScore("§e§lKit");
			}
	        else if(!Spleef.team.containsKey(p.getName())){
		        s50 = obj.getScore("§7§lKit");
		    }
			}
			
			s10.setScore(9);
			s20.setScore(8);
			s30.setScore(7);
			s40.setScore(6);
			s50.setScore(5);
			s60.setScore(4);
			s70.setScore(3);
			s80.setScore(2);
			s90.setScore(1);
			
			Scoreboard arch = manager.getNewScoreboard();
			obj = arch.registerNewObjective("stats", "dummy");
			obj.setDisplayName("§a§lWaiting for Players");
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			
			Score s100 = obj.getScore("§1");
			Score s200 = obj.getScore("§e§lPlayers");
			Score s300 = obj.getScore("" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers());
			Score s400 = obj.getScore("§2");
			Score s500 = obj.getScore("§7§lKit");
			Score s600 = obj.getScore("Archer");
			Score s700 = obj.getScore("");
			Score s800 = obj.getScore("§b§lPlaying");
			Score s900 = obj.getScore("Spleef");
			
			for(Player p : Bukkit.getOnlinePlayers()) {
			if(Spleef.team.containsKey(p.getName()))
			{
				s500 = obj.getScore("§e§lKit");
			}
	        else if(!Spleef.team.containsKey(p.getName())){
		        s500 = obj.getScore("§7§lKit");
		    }
			}
			
			s100.setScore(9);
			s200.setScore(8);
			s300.setScore(7);
			s400.setScore(6);
			s500.setScore(5);
			s600.setScore(4);
			s700.setScore(3);
			s800.setScore(2);
			s900.setScore(1);
			
			Scoreboard non = manager.getNewScoreboard();
			obj = non.registerNewObjective("stats", "dummy");
			obj.setDisplayName("§a§lWaiting for Players");
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			
			Score s1000 = obj.getScore("§1");
			Score s2000 = obj.getScore("§e§lPlayers");
			Score s3000 = obj.getScore("" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers());
			Score s4000 = obj.getScore("§2");
			Score s5000 = obj.getScore("§7§lKit");
			Score s6000 = obj.getScore("None");
			Score s7000 = obj.getScore("");
			Score s8000 = obj.getScore("§b§lPlaying");
			Score s9000 = obj.getScore("Spleef");
			
			for(Player p : Bukkit.getOnlinePlayers()) {
			if(Spleef.team.containsKey(p.getName()))
			{
				s5000 = obj.getScore("§e§lKit");
			}
	        else if(!Spleef.team.containsKey(p.getName())){
		        s5000 = obj.getScore("§7§lKit");
		    }
			}
			
			s1000.setScore(9);
			s2000.setScore(8);
			s3000.setScore(7);
			s4000.setScore(6);
			s5000.setScore(5);
			s6000.setScore(4);
			s7000.setScore(3);
			s8000.setScore(2);
			s9000.setScore(1);
			
			for(String none: Spleef.none)
			{
				Player p1 = Bukkit.getPlayerExact(none);
			    p1.setScoreboard(non);
			}
			for(String snowballer : Spleef.snowballer)
			{
				Player p1 = Bukkit.getPlayerExact(snowballer);
				p1.setScoreboard(snowball);
			}
		
			for(String brawler : Spleef.brawler)
			{
				Player p1 = Bukkit.getPlayerExact(brawler);
				p1.setScoreboard(brawl);
			}
			
			for(String archer: Spleef.archer)
			{
				Player p1 = Bukkit.getPlayerExact(archer);
				p1.setScoreboard(arch);
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
			
			ScoreboardManager manager = Bukkit.getScoreboardManager();
			Scoreboard snowball = manager.getNewScoreboard();
			Objective obj = snowball.registerNewObjective("stats", "dummy");
			obj.setDisplayName("§f§lStarting in §a§l" + Spleef.seconds + " Seconds");
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			
			Score s1 = obj.getScore("§1");
			Score s2 = obj.getScore("§e§lPlayers");
			Score s3 = obj.getScore("" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers());
			Score s4 = obj.getScore("§2");
			Score s5 = obj.getScore("§7§lKit");
			Score s6 = obj.getScore("Snowballer");
			Score s7 = obj.getScore("");
			Score s8 = obj.getScore("§b§lPlaying");
			Score s9 = obj.getScore("Spleef");
			
			for(Player p : Bukkit.getOnlinePlayers()) {
			if(Spleef.team.containsKey(p.getName()))
			{
				s5 = obj.getScore("§e§lKit");
			}
	        else if(!Spleef.team.containsKey(p.getName())){
		        s5 = obj.getScore("§7§lKit");
		    }
			}
			
			s1.setScore(9);
			s2.setScore(8);
			s3.setScore(7);
			s4.setScore(6);
			s5.setScore(5);
			s6.setScore(4);
			s7.setScore(3);
			s8.setScore(2);
			s9.setScore(1);
			
			Scoreboard brawl = manager.getNewScoreboard();
			obj = brawl.registerNewObjective("stats", "dummy");
			obj.setDisplayName("§f§lStarting in §a§l" + Spleef.seconds + " Seconds");
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			
			Score s10 = obj.getScore("§1");
			Score s20 = obj.getScore("§e§lPlayers");
			Score s30 = obj.getScore("" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers());
			Score s40 = obj.getScore("§2");
			Score s50 = obj.getScore("§7§lKit");
			Score s60 = obj.getScore("Brawler");
			Score s70 = obj.getScore("");
			Score s80 = obj.getScore("§b§lPlaying");
			Score s90 = obj.getScore("Spleef");
			
			for(Player p : Bukkit.getOnlinePlayers()) {
			if(Spleef.team.containsKey(p.getName()))
			{
				s50 = obj.getScore("§e§lKit");
			}
	        else if(!Spleef.team.containsKey(p.getName())){
		        s50 = obj.getScore("§7§lKit");
		    }
			}
			
			s10.setScore(9);
			s20.setScore(8);
			s30.setScore(7);
			s40.setScore(6);
			s50.setScore(5);
			s60.setScore(4);
			s70.setScore(3);
			s80.setScore(2);
			s90.setScore(1);
			
			Scoreboard arch = manager.getNewScoreboard();
			obj = arch.registerNewObjective("stats", "dummy");
			obj.setDisplayName("§f§lStarting in §a§l" + Spleef.seconds + " Seconds");
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			
			Score s100 = obj.getScore("§1");
			Score s200 = obj.getScore("§e§lPlayers");
			Score s300 = obj.getScore("" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers());
			Score s400 = obj.getScore("§2");
			Score s500 = obj.getScore("§7§lKit");
			Score s600 = obj.getScore("Archer");
			Score s700 = obj.getScore("");
			Score s800 = obj.getScore("§b§lPlaying");
			Score s900 = obj.getScore("Spleef");
			
			for(Player p : Bukkit.getOnlinePlayers()) {
			if(Spleef.team.containsKey(p.getName()))
			{
				s500 = obj.getScore("§e§lKit");
			}
	        else if(!Spleef.team.containsKey(p.getName())){
		        s500 = obj.getScore("§7§lKit");
		    }
			}
			
			s100.setScore(9);
			s200.setScore(8);
			s300.setScore(7);
			s400.setScore(6);
			s500.setScore(5);
			s600.setScore(4);
			s700.setScore(3);
			s800.setScore(2);
			s900.setScore(1);
			
			for(String snowballer : Spleef.snowballer)
			{
				Player p1 = Bukkit.getPlayerExact(snowballer);
				p1.setScoreboard(snowball);
			}
		
			for(String brawler : Spleef.brawler)
			{
				Player p1 = Bukkit.getPlayerExact(brawler);
				p1.setScoreboard(brawl);
			}
			
			for(String archer: Spleef.archer)
			{
				Player p1 = Bukkit.getPlayerExact(archer);
				p1.setScoreboard(arch);
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

				Scoreboard sc = manager.getNewScoreboard();
				obj.unregister();
				obj = sc.registerNewObjective("stats", "dummy");
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
					Spleef.setGameState(GameState.WAITING);
					World w = Bukkit.getWorld(Spleef.selectedMap);
					for(Player p : Bukkit.getOnlinePlayers())
					{
					p.teleport(Bukkit.getWorld("world").getSpawnLocation());
					p.getInventory().clear();
					p.setAllowFlight(false);
					
					for(int i = 0; i < Spleef.dead.size(); i++)
					{
						Player pl = Bukkit.getPlayerExact(Spleef.dead.get(i));
						
						p.showPlayer(pl);
					}
					
					for(int i = 0; i < Spleef.spec.size(); i++)
					{
						Player pl = Bukkit.getPlayerExact(Spleef.spec.get(i));
						
						p.showPlayer(pl);
					}
					}
				
					Bukkit.unloadWorld(w, false);
					
					Spleef.dead.clear();
					Spleef.alive.clear();
					Spleef.spec.clear();
					
					WorldCreator wc = new WorldCreator(Spleef.selectedMap);
					wc.createWorld();
					
					w.setAutoSave(false);
					//for(Chunk c : w.getLoadedChunks())
					//{
					//	c.unload(false, false);
					//}
					//RegionFileCache.a();
					//deleteWorld(new File(Bukkit.getWorld(selectedMap).getName() + "_clone"));
					Spleef.seconds = 60;
					
					Spleef.secs = 0;
					Spleef.mins = 0;
					Spleef.hrs = 0;
					Spleef.timerSecs = 0;
					Spleef.secondSecs = 0;
					
					HandlerList.unregisterAll();
					Bukkit.getScheduler().cancelAllTasks();
					Game.registerEvents(Bukkit.getPluginManager().getPlugin("Game"), new runner.events.Events());
					Game.registerEvents(Bukkit.getPluginManager().getPlugin("Game"), new game.events.Events());
			    	new runner.runnables.onUpdate().runTaskTimer(Bukkit.getPluginManager().getPlugin("Game"), 0L, 20L);
			    	new runner.runnables.onRun().runTaskTimer(Bukkit.getPluginManager().getPlugin("Game"), 0L, 1L);
			    	new runner.runnables.Snowball().runTaskTimer(Bukkit.getPluginManager().getPlugin("Game"), 0L, 20L);
			    	new runner.runnables.Arrow().runTaskTimer(Bukkit.getPluginManager().getPlugin("Game"), 0L, 40L);
					
				    Spleef.cooldownTime = new HashMap<Player, Integer>();
				    Spleef.cooldownTask = new HashMap<Player, BukkitRunnable>();
					
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
						World w1 = Bukkit.getWorld(Spleef.selectedMap);
						p1.teleport(Bukkit.getWorld("world").getSpawnLocation());
						
						Spleef.cooldownTime.remove(p1);
						Spleef.team.clear();
						p1.getInventory().clear();
						p1.setAllowFlight(false);
						p1.showPlayer(p1);
						
						for(String ded : Spleef.dead)
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
						
						Spleef.snowballer.clear();
						Spleef.archer.clear();
						Spleef.brawler.clear();
						
						Bukkit.unloadWorld(w1, false);
						
						Spleef.seconds = 60;
					
						}
					    Spleef.setGameState(Spleef.GameState.WAITING);
						
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
			for(Player p : Bukkit.getOnlinePlayers())
			{
			p.getInventory().clear();
			p.playSound(p.getLocation(), Sound.NOTE_PLING, 100, 1);
			World w = Bukkit.getWorld(Spleef.selectedMap);
			p.teleport(Bukkit.getWorld("world").getSpawnLocation());
			
			Spleef.team.remove(p.getName());
			p.getInventory().clear();
			p.setAllowFlight(false);
			p.showPlayer(p);
			
			for(String ded : Spleef.dead)
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
			
			p.setFlying(false);
			
			Spleef.dead.clear();
			Spleef.alive.clear();
		
			Bukkit.unloadWorld(w, false);
			
			Spleef.seconds = 60;
			}
			Spleef.setGameState(GameState.WAITING);
			
			Spleef.secs = 0;
			Spleef.mins = 0;
			Spleef.hrs = 0;
			Spleef.timerSecs = 0;
			Spleef.secondSecs = 0;	
			
			HandlerList.unregisterAll();
			Bukkit.getScheduler().cancelAllTasks();
			Game.registerEvents(Bukkit.getPluginManager().getPlugin("Game"), new runner.events.Events());
			Game.registerEvents(Bukkit.getPluginManager().getPlugin("Game"), new game.events.Events());
	    	new runner.runnables.onUpdate().runTaskTimer(Bukkit.getPluginManager().getPlugin("Game"), 0L, 20L);
	    	new runner.runnables.onRun().runTaskTimer(Bukkit.getPluginManager().getPlugin("Game"), 0L, 1L);
	    	new runner.runnables.Snowball().runTaskTimer(Bukkit.getPluginManager().getPlugin("Game"), 0L, 20L);
	    	new runner.runnables.Arrow().runTaskTimer(Bukkit.getPluginManager().getPlugin("Game"), 0L, 40L);
			
		    Spleef.cooldownTime = new HashMap<Player, Integer>();
		    Spleef.cooldownTask = new HashMap<Player, BukkitRunnable>();
			
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
				World w = Bukkit.getWorld(Spleef.selectedMap);
				p1.teleport(Bukkit.getWorld("world").getSpawnLocation());
				
				Spleef.cooldownTime.remove(p1);
				Spleef.team.clear();
				p1.getInventory().clear();
				p1.setAllowFlight(false);
				p1.showPlayer(p1);
				
				for(String ded : Spleef.dead)
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
				
				Spleef.snowballer.clear();
				Spleef.archer.clear();
				Spleef.brawler.clear();
				
				Bukkit.unloadWorld(w, false);
				
				Spleef.seconds = 60;
			
				}
			    Spleef.setGameState(Spleef.GameState.WAITING);
				
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
