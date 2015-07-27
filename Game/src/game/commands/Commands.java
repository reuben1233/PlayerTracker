package game.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import game.Game;
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
					Game.Runner();
					
					Game.randomint = 0;
					
					Bukkit.broadcastMessage("§b§l" + p.getName() + " has changed game to Runner.");
				}
				else if(args[1].equalsIgnoreCase("Spleef"))
				{
					Game.Spleef();
					
					Game.randomint = 1;
					
					Bukkit.broadcastMessage("§b§l" + p.getName() + " has changed game to Spleef.");
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
					Runner.seconds = 0;
					Runner.autoStart = true;
					Bukkit.broadcastMessage("§b§l" + p.getName() + " has started the game.");
					p.playSound(p.getLocation(), Sound.NOTE_PLING, 100, 1);
				}
				} else if(Game.randomint == 1) {
					if(Spleef.hasGameState(Spleef.GameState.WAITING))
					{
						Spleef.setGameState(Spleef.GameState.STARTING);
						Spleef.seconds = 0;
						Bukkit.broadcastMessage("§b§l" + p.getName() + " has started the game.");
						p.playSound(p.getLocation(), Sound.NOTE_PLING, 100, 1);
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
					
					Game.randomint = 1;
					
					Game.Spleef();
				}
				} else if(Game.randomint == 1) {
					if(Spleef.hasGameState(Spleef.GameState.INGAME))
					{
						Bukkit.broadcastMessage("§b§l" + p.getName() + " has stopped the game.");
						p.playSound(p.getLocation(), Sound.NOTE_PLING, 100, 1);
						
						Game.randomint = 0;
						
						Game.Runner();
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