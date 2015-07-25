package mysql;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Adder extends BukkitRunnable{
	@Override
	public void run() {
		for(Player p : Bukkit.getOnlinePlayers()){
			if(!MySQL.getColumn("player").contains(p.getUniqueId().toString())){
			MySQL.insertGame(p.getUniqueId().toString(), 0);
		}
		}
	}
}
