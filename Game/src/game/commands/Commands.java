package game.commands;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
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
					
					Bukkit.broadcastMessage("§b§l" + p.getName() + " has changed game to Runner.");
				}
				else if(args[1].equalsIgnoreCase("Spleef"))
				{
					Game.Spleef();
					
					Bukkit.broadcastMessage("§b§l" + p.getName() + " has changed game to Spleef.");
				}
			}
			}
		  Player p = (Player) sender;
			if(args[0].equalsIgnoreCase("start"))
			{
				if(Game.randomint == 0) {
				if(Runner.hasGameState(Runner.GameState.WAITING) || Runner.hasGameState(Runner.GameState.STARTING))
				{
					Runner.setGameState(GameState.STARTING);
					Runner.seconds = 0;
					Runner.autoStart = true;
					Bukkit.broadcastMessage("§b§l" + p.getName() + " has started the game.");
					p.playSound(p.getLocation(), Sound.NOTE_PLING, 100, 1);
				}
				} else if(Game.randomint == 1) {
					if(Spleef.hasGameState(Spleef.GameState.WAITING) || Spleef.hasGameState(Spleef.GameState.STARTING))
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
				if(Runner.hasGameState(GameState.INGAME) || Runner.hasGameState(GameState.INGAMEWAIT) || Runner.hasGameState(GameState.STARTING))
				{
					Bukkit.broadcastMessage("§b§l" + p.getName() + " has stopped the game.");
					p.playSound(p.getLocation(), Sound.NOTE_PLING, 100, 1);
					
					Game.Spleef();
				}
				} else if(Game.randomint == 1) {
					if(Spleef.hasGameState(Spleef.GameState.INGAME))
					{
						Bukkit.broadcastMessage("§b§l" + p.getName() + " has stopped the game.");
						p.playSound(p.getLocation(), Sound.NOTE_PLING, 100, 1);
						
						Game.Runner();
					}
				}
			   }
			    if(args[0].equalsIgnoreCase("autostart"))
				{
			    	if(Game.randomint == 0) {
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
			    	} else if(Game.randomint == 0) {
						if(Spleef.hasGameState(Spleef.GameState.WAITING) || Spleef.hasGameState(Spleef.GameState.STARTING)) {
		                    if(Spleef.autoStart == true) {
		                    	Spleef.autoStart = false;
		                    	Bukkit.broadcastMessage("§c§lAuto-start disabled!");
		                    }
		                    else if(Spleef.autoStart == false) {
		                    	Spleef.autoStart = true;
		                    	Bukkit.broadcastMessage("§a§lAuto-start enabled!");
		                    }
							}
					    	}
			    }
				if(args[0].equalsIgnoreCase("testing"))
				{
					if(Game.randomint == 0) {
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
					} else if (Game.randomint == 1){
						if(Spleef.hasGameState(Spleef.GameState.WAITING) || Spleef.hasGameState(Spleef.GameState.STARTING)) {
		                    if(Spleef.testing == false) {
		                    	Spleef.testing = true;
		                    	Bukkit.broadcastMessage("§a§lTesting enabled!");
		                    }
		                    else if(Spleef.testing == true) {
		                    	Spleef.testing = false;
		                    	Bukkit.broadcastMessage("§c§lTesting disabled!");
		                    }
							}
					}
			    }
		return false;
			
   }

}