package mysql;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Adder extends BukkitRunnable{
	@Override
	public void run() {
		final List<String> temp = MySQL.getColumn("player");
		if(!temp.isEmpty()){
		for(Player p : Bukkit.getOnlinePlayers()){
			if(!temp.contains(p))
			MySQL.insertGame(p.getUniqueId().toString(), 0);
		}
		}
	}
}
