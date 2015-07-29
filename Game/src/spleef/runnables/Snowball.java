package spleef.runnables;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import spleef.Spleef;
import spleef.Spleef.GameState;

public class Snowball extends BukkitRunnable{

	@Override
	public void run() {
		for(Player p : Bukkit.getOnlinePlayers()) {
    		if(Spleef.hasGameState(GameState.INGAME) && Spleef.snowballer.contains(p.getName()) && Spleef.alive.contains(p.getName())) {
				ItemStack snowball = new ItemStack(Material.SNOW_BALL);
			    ItemMeta snowball1 = (ItemMeta) snowball.getItemMeta();
			    snowball1.setDisplayName("§fSnowball");
			    snowball.setItemMeta(snowball1);
				ItemMeta meta1 = snowball.getItemMeta();
				meta1.spigot().setUnbreakable(true);
				snowball.setItemMeta(meta1);
				if(!p.getInventory().containsAtLeast(snowball, 16)) {
				p.getInventory().addItem(snowball);
				}
    		}
	    }
	}
}
