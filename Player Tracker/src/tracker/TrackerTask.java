package tracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import utils.PacketUtils;
import utils.UtilMath;

public class TrackerTask extends BukkitRunnable implements Listener{
	
   private Player player;
   
   public TrackerTask (Player player) {
	   this.player = player;
   }
   
   String message = null;
 	
List<Player> players = new ArrayList<> ();
   @Override
   public void run() {
	   Boolean perms = Bukkit.getPluginManager().getPlugin("PlayerTracker").getConfig().getBoolean("permissions");
	  if(player.hasPermission("tracker.use") || perms == false){
 	for (Player p : player.getWorld().getPlayers()) {
		if(!p.getUniqueId().equals(player.getUniqueId())){
			players.add(p);
		}
	} 
	
	      Collections.sort (players, new TrackerCompare (player));
	      Player nearest = null;
	      try{
		  nearest = players.get(0);
	      } catch(IndexOutOfBoundsException ignore){
	    	  
	      }    
	      
	      if(nearest != null)
		  message = "§f§lNearest Player: §e" + nearest.getName() + "   §f§lDistance: §e" + UtilMath.trim(1, nearest.getLocation().distance(player.getLocation())) + "   §f§lHeight: §e" + UtilMath.trim(1, nearest.getLocation().getY() - player.getLocation().getY());
	      if(nearest == null)
	    	  message = "";
	   if(message.equals(null))
		   message = "";
	   
	   if(player.getItemInHand().getType() == Material.COMPASS) {
		 PacketUtils.sendActionBar(player, "" + message);
	   }
	   
	   try {
		   player.setCompassTarget(nearest != null ? nearest.getLocation() : null);
	   } catch(NullPointerException ignore){
		   
	   }
	   
	   players.clear();
   }
	  
   }
   
}
