package runner.runnables;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import runner.Runner;

public class Arrow extends BukkitRunnable{

	@Override
	public void run() {
		for(Player p : Bukkit.getOnlinePlayers()) {
    		if(Runner.hasGameState(Runner.GameState.INGAME) && Runner.archer.contains(p.getName()) && Runner.alive.contains(p.getName())) {
				ItemStack arrow = new ItemStack(Material.ARROW);
			    ItemMeta arrow1 = (ItemMeta) arrow.getItemMeta();
			    arrow1.setDisplayName("§fArrow");
			    arrow.setItemMeta(arrow1);
				ItemMeta meta1 = arrow.getItemMeta();
				meta1.spigot().setUnbreakable(true);
				arrow.setItemMeta(meta1);
				if(!p.getInventory().containsAtLeast(arrow, 2)) {
				p.getInventory().addItem(arrow);
				}
    		}
		}
	}
}
