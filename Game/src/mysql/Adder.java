package mysql;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Adder extends BukkitRunnable{
    List<String> player = MySQL.getGame();
	@Override
	public void run() {
		for(Player p : Bukkit.getOnlinePlayers())
		if(player.contains(p.getUniqueId().toString())){
			MySQL.insertGame(p.getUniqueId().toString(), 0);
		}
	}
}
