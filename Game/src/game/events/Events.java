package game.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Events implements Listener{
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{	
		event.setJoinMessage("§8Join> §7" + event.getPlayer().getName());
		
		Player p = event.getPlayer();
		
		p.teleport(Bukkit.getWorld("world").getSpawnLocation());
		
		p.getInventory().clear();
	}
	
	  @EventHandler
      public void onEntityExplode(EntityExplodeEvent e) {
              for (Block b : e.blockList()) {
                      final BlockState state = b.getState();
                     
                      b.setType(Material.AIR); // Stop item drops from spawning.
                     
                      int delay = 20;
                     
                      if ((b.getType() == Material.SAND) || (b.getType() == Material.GRAVEL) || (b.getType() == Material.SIGN)) {
                              delay += 1;
                      }
                     
                      Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("Game"), new Runnable() {
                              public void run() {
                                      state.update(true, false);
                              }
                      }, delay);
              }
	  }
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e)
	{
		if(e.getPlayer().getGameMode() != GameMode.CREATIVE)
		{
			e.setCancelled(true);
		}
		}
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if(event.getSpawnReason() == SpawnReason.NATURAL)
		event.setCancelled(true);
	}
	
	 @EventHandler
	 public void onEntityCombust(EntityCombustEvent event){
	 event.setCancelled(true);	  
	 }
	 
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		event.setQuitMessage("§8Quit> §7" + event.getPlayer().getName());
			
		event.getPlayer().getInventory().clear();
			
		event.getPlayer().showPlayer(event.getPlayer());
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e)
	{
	   if(e.getPlayer().getGameMode() != GameMode.CREATIVE) {
        e.setCancelled(true);
	}
	   
   
	}
}
