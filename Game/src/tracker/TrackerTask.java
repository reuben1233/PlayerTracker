package tracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import utils.PacketUtils;

public class TrackerTask extends BukkitRunnable{
	
   private Player player;
   
   public TrackerTask (Player player) {
	   this.player = player;
   }
   
   String message = null;
 	
   @Override
   public void run() {
	List<Player> players = new ArrayList<> ();
	for (Player p : player.getWorld().getPlayers()) {
		if(!p.getUniqueId().equals(player.getUniqueId ()) && (player.canSee(p))){
			players.add(p);
		}
	}
	
	   Collections.sort (players, new TrackerCompare (player));
	   Player nearest = null;
	
	   if(players.size() > 0) {
		  nearest = players.get (0);
		  message = "§f§lNearest Player: §e" + nearest.getName() + " §f§lDistance: §e" + (int) nearest.getLocation().distance(player.getLocation());
	   }
	   if(message.equals(null))
		   message = "";
	   
	   if(player.getItemInHand().getType() == Material.COMPASS) {
		 PacketUtils.sendActionBar(player, "" + message);
	   }
	   
	   try {
		   player.setCompassTarget(nearest != null ? nearest.getLocation() : null);
	   } catch(NullPointerException ignore){
		   
	   }
   }
   
}
