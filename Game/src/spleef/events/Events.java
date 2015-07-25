package spleef.events;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftSound;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import spleef.Spleef;
import spleef.Spleef.GameState;
import utils.PacketUtils;

public class Events implements Listener{
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Snowball || entity instanceof Arrow) {
            Location loc = entity.getLocation();
            Vector vec = entity.getVelocity();
            Location loc2 = new Location(loc.getWorld(), loc.getX()+vec.getX(), loc.getY()+vec.getY(), loc.getZ()+vec.getZ());
            loc2.getBlock().setType(Material.AIR);
            entity.remove();
            
            try
			 {
		        for(Sound sound : Sound.values()) {
		            Field f = CraftSound.class.getDeclaredField("sounds");
		            f.setAccessible(true);
		           
		            String[] sounds = (String[]) f.get(null);
		            Method getBlock = CraftBlock.class.getDeclaredMethod("getNMSBlock");
		            getBlock.setAccessible(true);
		            Object nmsBlock = getBlock.invoke(loc2.getBlock());
		            net.minecraft.server.v1_8_R3.Block block = (net.minecraft.server.v1_8_R3.Block) nmsBlock;
		 
		            if(block.stepSound.getBreakSound()
		                    .equals(sounds[sound.ordinal()])) {
		            	loc2.getBlock().getWorld().playSound(loc2.getBlock().getLocation(), sound, 10, 10);
		            }
		        }
			 }
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }
        }
        }
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e)
	{
		if(!Spleef.hasGameState(GameState.INGAME) && e.getPlayer().getGameMode() != GameMode.CREATIVE || Spleef.dead.contains(e.getPlayer().getName()) || Spleef.spec.contains(e.getPlayer().getName()))
		{
			e.setCancelled(true);
		}
		}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e)
	   {
	   if(e.getPlayer().getGameMode() != GameMode.CREATIVE) {
        e.setCancelled(true);
	   }
	}
	
	@EventHandler
    public void onLeap(PlayerInteractEvent event) {
   
        final Player p = event.getPlayer();  
		
        if (event.getAction() == Action.RIGHT_CLICK_AIR && p.getItemInHand().getType() == Material.IRON_AXE && Spleef.cooldownTime.containsKey(p)|| event.getAction() == Action.RIGHT_CLICK_BLOCK && p.getItemInHand().getType() == Material.IRON_AXE && Spleef.cooldownTime.containsKey(p) && Spleef.alive.contains(p)) {
        	if(Spleef.cooldownTime.get(p) != 1) {
				p.sendMessage("§9Recharge> §7You cannot use §aLeap§7 for §a" + Spleef.cooldownTime.get(p) + " §aSeconds§7.");
        	}	
        	if(Spleef.cooldownTime.get(p) == 1) {
				p.sendMessage("§9Recharge> §7You cannot use §aLeap§7 for §a" + Spleef.cooldownTime.get(p) + " §aSecond§7.");
        	}	
    }
        
		if(event.getAction() == Action.RIGHT_CLICK_AIR && p.getItemInHand().getType() == Material.IRON_AXE && Spleef.hasGameState(GameState.INGAME) && p.getLocation().getY() < 11 && !Spleef.cooldownTime.containsKey(p) || event.getAction() == Action.RIGHT_CLICK_BLOCK && p.getItemInHand().getType() == Material.IRON_AXE && Spleef.hasGameState(GameState.INGAME) && p.getLocation().getY() < 11 && !Spleef.cooldownTime.containsKey(p)) {
			
	        event.setCancelled(true);
	        Vector v = p.getLocation().getDirection().multiply(1).setY(1);
	        p.setVelocity(v);
	        p.getWorld().playEffect(p.getLocation(), Effect.BLAZE_SHOOT, 0);

	        Spleef.cooldownTime.put(p, 5);
	        Spleef.cooldownTask.put(p, new BukkitRunnable() {
                public void run() {
                	Spleef.cooldownTime.put(p, Spleef.cooldownTime.get(p) - 1);
                     if (Spleef.cooldownTime.get(p) == 0 && Spleef.alive.contains(p.getName())) {
                    	     PacketUtils.sendActionBar(p, "§fLeap §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌ §f" + Spleef.cooldownTime.get(p) + " Seconds");
                    	     p.sendMessage("§9Recharge> §7You can now use §aLeap§7.");
                    	     Spleef.cooldownTime.remove(p);
                    	     Spleef.cooldownTask.remove(p);
                             cancel();
                }
                }});
	            Spleef.cooldownTask.get(p).runTaskTimer(Bukkit.getPluginManager().getPlugin("Game"), 20, 20);
		}}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e)
	{
		if(!Spleef.hasGameState(GameState.INGAME) && e.getPlayer().getGameMode() != GameMode.CREATIVE || Spleef.dead.contains(e.getPlayer().getName()))
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		 Player p = event.getPlayer();
		 
		if(p.getItemInHand().getType() == Material.COMPASS && p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§aKit Selector"))
	  	{    
		if((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK))
		{
			p.openInventory(Spleef.kits(p));
		}
	  }
		
		if(Spleef.hasGameState(GameState.INGAMEWAIT) || Spleef.dead.contains(event.getPlayer()) || Spleef.alive.size() <= 1)
		{
			event.setCancelled(true);
		}
	
		if(event.getAction() == Action.LEFT_CLICK_BLOCK && Spleef.hasGameState(GameState.INGAME) && !Spleef.dead.contains(event.getPlayer().getName()) && Spleef.alive.size() > 1)
		{
				    event.getClickedBlock().setType(Material.AIR);
				 
					 try
					 {
				        for(Sound sound : Sound.values()) {
				            Field f = CraftSound.class.getDeclaredField("sounds");
				            f.setAccessible(true);
				           
				            String[] sounds = (String[]) f.get(null);
				            Method getBlock = CraftBlock.class.getDeclaredMethod("getNMSBlock");
				            getBlock.setAccessible(true);
				            Object nmsBlock = getBlock.invoke(event.getClickedBlock());
				            net.minecraft.server.v1_8_R3.Block block = (net.minecraft.server.v1_8_R3.Block) nmsBlock;
				 
				            if(block.stepSound.getBreakSound()
				                    .equals(sounds[sound.ordinal()])) {
				                event.getClickedBlock().getWorld().playSound(event.getClickedBlock().getLocation(), sound, 10, 10);
				            }
				        }
					 }
					 catch (Exception e)
					 {
						 e.printStackTrace();
					 }
					 
					if(Spleef.hasGameState(GameState.INGAME) && Spleef.alive.contains(event.getPlayer().getName()))
					{
					   event.getPlayer().setFoodLevel(event.getPlayer().getFoodLevel() + 1);
					}

					if(Spleef.snowballer.contains(event.getPlayer().getName())) {
					final ItemStack snowball = new ItemStack(Material.SNOW_BALL);
				    ItemMeta snowball1 = (ItemMeta) snowball.getItemMeta();
				    snowball1.setDisplayName("Snowball");
				    snowball.setItemMeta(snowball1);
				    if(!event.getPlayer().getInventory().containsAtLeast(snowball, 16)) {
					event.getPlayer().getInventory().addItem(snowball);
				    }
			}
	}}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{		
		Player p = event.getPlayer();
		
		if(Spleef.hasGameState(GameState.WAITING) || Spleef.hasGameState(GameState.STARTING)) {
			ItemStack kits = new ItemStack(Material.COMPASS);
		    ItemMeta kits1 = (ItemMeta) kits.getItemMeta();
			kits1.setDisplayName("§aKit Selector");
			kits.setItemMeta(kits1);
			p.getInventory().clear();
			p.getInventory().setItem(4, kits);
		}
		
		if(Spleef.hasGameState(GameState.INGAME) || Spleef.hasGameState(GameState.INGAMEWAIT)) {
			
			p.setHealth(20);
			p.setFoodLevel(20);
			
			p.getInventory().clear();
			
			p.teleport(Bukkit.getWorld(Spleef.selectedMap).getSpawnLocation());
			
			Spleef.alive.remove(p.getName());
			Spleef.spec.add(p.getName());
			
			p.setAllowFlight(true);
			p.setFlying(true);
			
			for(Player on : Bukkit.getOnlinePlayers())
				on.hidePlayer(p);
	
			p.sendMessage("§9Condition> §7You are now invisible.");
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		Spleef.dead.remove(event.getPlayer().getName());
		Spleef.alive.remove(event.getPlayer().getName());
	}
	
	@EventHandler
	  public void onInventoryClick(InventoryClickEvent e)
	  {
  	  Player player = (Player) e.getWhoClicked();
  	  
  	  if (e.isCancelled()) return;
  	  else if(e.getCurrentItem().getItemMeta().getDisplayName() == "§a§lSnowballer") {
			{
				Spleef.brawler.remove(player.getName());
				Spleef.archer.remove(player.getName());
				Spleef.snowballer.add(player.getName());
				player.playSound(player.getLocation(), Sound.ORB_PICKUP, 2.0F, 1.0F);
				player.sendMessage("§2§l§m=============================================");
				player.sendMessage("§aKit - §f§lSnowballer");
				player.sendMessage(" §7Throw snowballs to break blocks!");
				player.sendMessage("");
				player.sendMessage(" §7Receives 1 Snowball when you punch blocks!");
				player.sendMessage("");
				player.sendMessage("§2§l§m=============================================");
			}
          e.setCancelled(true);
          player.closeInventory();
  	  }
  	 else if(e.getCurrentItem().getItemMeta().getDisplayName() == "§a§lBrawler"){
			{
				Spleef.snowballer.remove(player.getName());
				Spleef.archer.remove(player.getName());
				Spleef.brawler.add(player.getName());
				player.playSound(player.getLocation(), Sound.ORB_PICKUP, 2.0F, 1.0F);
				player.sendMessage("§2§l§m=============================================");
				player.sendMessage("§aKit - §f§lBrawler");
				player.sendMessage(" §7Very leap. Much Wow.");
				player.sendMessage("");
				player.sendMessage(" §eRight-Click §7with an axe to §aLeap");
				player.sendMessage("");
				player.sendMessage("§2§l§m=============================================");
			}
            e.setCancelled(true);
            player.closeInventory();
        }
  	 else if(e.getCurrentItem().getItemMeta().getDisplayName() == "§a§lArcher"){
			{
				Spleef.snowballer.remove(player.getName());
				Spleef.brawler.remove(player.getName());
				Spleef.archer.add(player.getName());
				player.playSound(player.getLocation(), Sound.ORB_PICKUP, 2.0F, 1.0F);
				player.sendMessage("§2§l§m=============================================");
				player.sendMessage("§aKit - §f§lArcher");
				player.sendMessage(" §7Shoot Arrows to break blocks!");
				player.sendMessage("");
				player.sendMessage(" §7Receives 1 Arrow every 2 seconds. Maximum of 2.");
				player.sendMessage("");
				player.sendMessage("§2§l§m=============================================");
			}
                e.setCancelled(true);
                player.closeInventory();
        }
	  }

	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e)
	{
		e.setDeathMessage(null);
		
		e.getDrops().clear();
		
		if(Spleef.alive.contains(e.getEntity().getName()))
		{
			Player player = e.getEntity();
			if(Spleef.alive.contains(e.getEntity().getName()))
			{
			player.setHealth(20);
			player.setFoodLevel(20);
			
			player.teleport(Bukkit.getWorld(Spleef.selectedMap).getSpawnLocation());
			
			Spleef.alive.remove(player.getName());
			Spleef.dead.add(player.getName());
			
			player.setAllowFlight(true);
			player.setFlying(true);
			
			for(Player on : Bukkit.getOnlinePlayers())
				on.hidePlayer(player);
			String k = e.getEntity().getKiller().getName();
			e.setDeathMessage("§9Death> §e" + player.getName() + "§7 was killed by §e" + k + "§7.");
			player.sendMessage("§9Condition> §7You are now invisible.");
		}
		}
	}
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if(event.getSpawnReason() == SpawnReason.NATURAL)
		event.setCancelled(true);
	}
	
	 @EventHandler(priority = EventPriority.HIGH)
	 public void onFallDamage(EntityDamageEvent event){
	 if(event.getEntity() instanceof Player && event.getCause() == DamageCause.FALL)
	 event.setCancelled(true);
	 }
	
	@EventHandler
	public void onPlayerTakeDamage(EntityDamageEvent e)
	{
		 	
	  	  Player p = (Player) e.getEntity();
				if (e.getEntityType() == EntityType.PLAYER) {
					if(!Spleef.hasGameState(GameState.INGAME) || Spleef.dead.contains(p)) {
						e.setCancelled(true);
					}
				if (e.getCause() == DamageCause.VOID) {
					e.setCancelled(true);
					if(Spleef.hasGameState(GameState.INGAME) & Spleef.alive.contains(p.getName()) && Spleef.alive.size() != 1) {
					Bukkit.broadcastMessage("§9Death> §e" + p.getName() + "§7 was killed by §eVoid§7.");
					p.setAllowFlight(true);
					p.setFlying(true);
					p.getInventory().clear();
					Spleef.dead.add(p.getName());
					Spleef.alive.remove(p.getName());
					}
					if(!Spleef.hasGameState(GameState.INGAME)  || !Spleef.hasGameState(GameState.INGAMEWAIT) || Spleef.alive.size() != 1) {
						p.teleport(p.getWorld().getSpawnLocation());
					}
					if(Spleef.alive.size() == 1 && Spleef.alive.contains(p.getName())) {
						p.setAllowFlight(true);
						p.setFlying(true);
						p.getInventory().clear();
						Spleef.dead.add(p.getName());
						Spleef.alive.remove(p.getName());
					}
				    
			for(String ingame : Spleef.alive)
			{
				Player pl = Bukkit.getPlayerExact(ingame);
				
				pl.hidePlayer(p);
			}
			
			for(String deads : Spleef.dead)
			{
				Player pl = Bukkit.getPlayerExact(deads);
				
				pl.hidePlayer(p);
			}
	       }
		}
			
		if(Spleef.hasGameState(GameState.INGAMEWAIT) || Spleef.hasGameState(GameState.WAITING) || Spleef.hasGameState(GameState.STARTING) || Spleef.hasGameState(GameState.ENDING) || Spleef.dead.contains(e.getEntity().getName()))
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e)
	{
        Player p = e.getPlayer();
  		
        if(Spleef.team.containsKey(p.getName())) {
  		e.setFormat("§e" + p.getName() + " §f" + e.getMessage());
        }
  		
        if(!Spleef.team.containsKey(p.getName())) {
  		e.setFormat("§7" + p.getName() + " §f" + e.getMessage());
        }
        
        if(Spleef.dead.contains(p.getName())) {
  		e.setFormat("§7Dead §e" + p.getName() + " §f" + e.getMessage());
        }
        
		if(Spleef.chatMuted && !p.isOp())
		{
			e.setCancelled(true);
			e.getPlayer().sendMessage("§9Chat> §7Chat is silenced for §a" + Spleef.globalChatSecs + "§a Seconds§7.");
		}
		}
	
	@EventHandler
	public void onPlayerDamagedByPlayer(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player && Spleef.hasGameState(GameState.INGAME))
		{
			String k = e.getDamager().getName();
			if(Spleef.dead.contains(k)) {
				e.setCancelled(true);
			}
		}
	}


	@EventHandler
	public void onPlayerDeath(EntityDamageEvent e)
	{
		if(e.getEntity() instanceof Player && Spleef.hasGameState(GameState.INGAME))
		{
			Player p = (Player) e.getEntity();
			
			if(p.getHealth() == 0 && Spleef.alive.contains(p.getName()))
			{
				p.setHealth(20);
				p.setFlying(true);
				Spleef.dead.add(p.getName());
				Spleef.alive.remove(p.getName());
				
				for(String ingame : Spleef.alive)
				{
					Player pl = Bukkit.getPlayerExact(ingame);
					
					pl.hidePlayer(p);
				}
				
				for(String deads : Spleef.dead)
				{
					Player pl = Bukkit.getPlayerExact(deads);
					
					pl.hidePlayer(p);
				}
			}
		}
		else if(!Spleef.hasGameState(GameState.INGAME) || !(e.getEntity() instanceof Player || Spleef.dead.contains(e.getEntity().getName())))
		{
			e.setCancelled(true);
		}
	}
}
