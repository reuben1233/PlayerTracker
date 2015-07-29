package runner.events;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftSound;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import runner.Runner;
import runner.Runner.GameState;
import tracker.TrackerTask;
import utils.PacketUtils;

public class Events implements Listener{
	@EventHandler
    public void onSnowballHit(EntityDamageByEntityEvent e) {
        Entity damaged = e.getEntity();
        Entity damageEntity = e.getDamager();
 
        if(damaged instanceof Player)
        if(damageEntity instanceof Snowball)
        {
            Snowball snowball = (Snowball)damageEntity;
            LivingEntity entityThrower = (LivingEntity) snowball.getShooter();
            if(entityThrower instanceof Player)
            {
                Player playerHit = (Player)damaged;
                playerHit.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
                
            }
        }
    }
	
	@EventHandler
    public void onNpcClick(EntityDamageByEntityEvent e) {
		Player player = (Player) e.getDamager();
	    if(e.getEntity().getPassenger().getCustomName().equals("§eJumper")){
			Runner.archer.remove(player.getName());
			Runner.frosty.remove(player.getName());
			Runner.jumper.add(player.getName());
			player.playSound(player.getLocation(), Sound.ORB_PICKUP, 2.0F, 1.0F);
			player.sendMessage("");
			player.sendMessage("");
			player.sendMessage("");
			player.sendMessage("§2§l§m=============================================");
			player.sendMessage("§aKit - §f§lJumper");
			player.sendMessage(" §7Leap to avoid falling to your death!");
			player.sendMessage("");
			player.sendMessage("§f§lLeaper");
			player.sendMessage(" §eRight-Click §7with an axe to §aLeap");
			player.sendMessage("§2§l§m=============================================");
			player.sendMessage("§9Kit> §7You equipped §e§lJumper Kit§7.");
	    } else if(e.getEntity().getPassenger().getCustomName().equals("§aArcher §f(§c2000 Gems§f)")){
			Runner.jumper.remove(player.getName());
			Runner.frosty.remove(player.getName());
			Runner.archer.add(player.getName());
			player.playSound(player.getLocation(), Sound.ORB_PICKUP, 2.0F, 1.0F);
			player.sendMessage("");
			player.sendMessage("");
			player.sendMessage("");
			player.sendMessage("§2§l§m=============================================");
			player.sendMessage("§aKit - §f§lArcher");
			player.sendMessage(" §7Fire arrows to cause blocks to fall!");
			player.sendMessage("");
			player.sendMessage("§f§lQuickshot");
			player.sendMessage(" §eLeft-Click §7with Bow to §aQuickshot");
			player.sendMessage("§2§l§m=============================================");
			player.sendMessage("§9Kit> §7You equipped §a§lArcher Kit§7.");
	    } else if(e.getEntity().getPassenger().getCustomName().equals("§aFrosty §f(§c5000 Gems§f)")){
			Runner.jumper.remove(player.getName());
			Runner.archer.remove(player.getName());
			Runner.frosty.add(player.getName());
			player.playSound(player.getLocation(), Sound.ORB_PICKUP, 2.0F, 1.0F);
			player.sendMessage("");
			player.sendMessage("");
			player.sendMessage("");
			player.sendMessage("§2§l§m=============================================");
			player.sendMessage("§aKit - §f§lFrosty");
			player.sendMessage(" §7Slow enemies to send them to their death!");
			player.sendMessage("");
			player.sendMessage("§f§lFrost Balls");
			player.sendMessage(" §7Receive 1 Snowball every 0.5 seconds. Maximum of 16.");
			player.sendMessage("§2§l§m=============================================");
			player.sendMessage("§9Kit> §7You equipped §a§lFrosty Kit§7.");
	    }
    }
	
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
	public void onPlayerJoin(PlayerJoinEvent event)
	{		
		Player p = event.getPlayer();
		
		if(Runner.hasGameState(Runner.GameState.INGAME) || Runner.hasGameState(Runner.GameState.INGAMEWAIT)) {

			p.setHealth(20);
			p.setFoodLevel(20);
			
			p.teleport(Bukkit.getWorld(Runner.selectedMap).getSpawnLocation());
			
			Runner.alive.remove(p.getName());
			Runner.spec.add(p.getName());
			
			p.setAllowFlight(true);
			p.setFlying(true);
			
			ItemStack compass = new ItemStack(Material.COMPASS);
		    ItemMeta compass1 = (ItemMeta) compass.getItemMeta();
		    compass1.setDisplayName("§aPlayer Tracker");
		    compass.setItemMeta(compass1);
			p.getInventory().setItem(0, compass);
			new TrackerTask (p).runTaskTimer(Bukkit.getPluginManager().getPlugin("Game"), 0L, 5L);
			
			for(Player on : Bukkit.getOnlinePlayers())
				on.hidePlayer(p);
	
			p.sendMessage("§9Condition> §7You are now invisible.");
		}
	}
		
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{	
		Runner.dead.remove(event.getPlayer().getName());
		Runner.alive.remove(event.getPlayer().getName());
	}
	
	@EventHandler
    public void onLeap(PlayerInteractEvent event) {
   
        final Player p = event.getPlayer();  
		
        if (event.getAction() == Action.RIGHT_CLICK_AIR && p.getItemInHand().getType() == Material.IRON_AXE && Runner.cooldownTime.containsKey(p)|| event.getAction() == Action.RIGHT_CLICK_BLOCK && p.getItemInHand().getType() == Material.IRON_AXE && Runner.cooldownTime.containsKey(p) && Runner.alive.contains(p)) {
        	if(Runner.cooldownTime.get(p) != 1) {
				p.sendMessage("§9Recharge> §7You cannot use §aLeap§7 for §a" + Runner.cooldownTime.get(p) + " §aSeconds§7.");
        	}	
        	if(Runner.cooldownTime.get(p) == 1) {
				p.sendMessage("§9Recharge> §7You cannot use §aLeap§7 for §a" + Runner.cooldownTime.get(p) + " §aSecond§7.");
        	}	
    }
        
		if(event.getAction() == Action.RIGHT_CLICK_AIR && p.getItemInHand().getType() == Material.IRON_AXE && Runner.hasGameState(GameState.INGAME) && p.getLocation().getY() < 24 && !Runner.cooldownTime.containsKey(p) || event.getAction() == Action.RIGHT_CLICK_BLOCK && p.getItemInHand().getType() == Material.IRON_AXE && Runner.hasGameState(GameState.INGAME) && p.getLocation().getY() < 24 && !Runner.cooldownTime.containsKey(p)) {
			
	        event.setCancelled(true);
	        Vector v = p.getLocation().getDirection().multiply(1).setY(1);
	        p.setVelocity(v);
	        p.getWorld().playEffect(p.getLocation(), Effect.BLAZE_SHOOT, 0);

	        Runner.cooldownTime.put(p, 5);
	        Runner.cooldownTask.put(p, new BukkitRunnable() {
                public void run() {
                	Runner.cooldownTime.put(p, Runner.cooldownTime.get(p) - 1);
                     if (Runner.cooldownTime.get(p) == 0 && Runner.alive.contains(p.getName())) {
                         	 PacketUtils.sendActionBar(p, "§fLeap §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌ §f" + Runner.cooldownTime.get(p) + ".0 Seconds");
                    	     p.sendMessage("§9Recharge> §7You can now use §aLeap§7.");
                    	     Runner.cooldownTime.remove(p);
                    	     Runner.cooldownTask.remove(p);
                             cancel();
                }
                }});
	            Runner.cooldownTask.get(p).runTaskTimer(Bukkit.getPluginManager().getPlugin("Game"), 20, 20);
		}}
	
	@EventHandler
    public void onQuickshot(PlayerInteractEvent event) {
   
        final Player p = event.getPlayer();  
		
        if (event.getAction() == Action.LEFT_CLICK_AIR && p.getItemInHand().getType() == Material.BOW && Runner.cooldownTime.containsKey(p)|| event.getAction() == Action.LEFT_CLICK_BLOCK && p.getItemInHand().getType() == Material.BOW && Runner.cooldownTime.containsKey(p) && Runner.alive.contains(p)) {
        	if(Runner.cooldownTime.get(p) != 1) {
				p.sendMessage("§9Recharge> §7You cannot use §aQuickshot§7 for §a" + Runner.cooldownTime.get(p) + " §aSeconds§7.");
        	}	
        	if(Runner.cooldownTime.get(p) == 1) {
				p.sendMessage("§9Recharge> §7You cannot use §aQuickshot§7 for §a" + Runner.cooldownTime.get(p) + " §aSecond§7.");
        	}	
    }
        
		if(event.getAction() == Action.LEFT_CLICK_AIR && p.getItemInHand().getType() == Material.BOW && Runner.hasGameState(GameState.INGAME) && !Runner.cooldownTime.containsKey(p) || event.getAction() == Action.LEFT_CLICK_BLOCK && p.getItemInHand().getType() == Material.BOW && Runner.hasGameState(GameState.INGAME) && !Runner.cooldownTime.containsKey(p)) {
			
			Player player = event.getPlayer();
	        event.setCancelled(true);
	        Arrow arrow = (Arrow)player.launchProjectile(Arrow.class);
	        arrow.setVelocity(player.getLocation().getDirection().multiply(3));


	        Runner.cooldownTime.put(p, 5);
	        Runner.cooldownTask.put(p, new BukkitRunnable() {
                public void run() {
                	Runner.cooldownTime.put(p, Runner.cooldownTime.get(p) - 1);
                     if (Runner.cooldownTime.get(p) == 0 && Runner.alive.contains(p.getName())) {
                         	 PacketUtils.sendActionBar(p, "§fQuickshot §a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌§a▌ §f" + Runner.cooldownTime.get(p) + ".0 Seconds");
                    	     p.sendMessage("§9Recharge> §7You can now use §aQuickshot§7.");
                    	     Runner.cooldownTime.remove(p);
                    	     Runner.cooldownTask.remove(p);
                             cancel();
                }
                }});
	            Runner.cooldownTask.get(p).runTaskTimer(Bukkit.getPluginManager().getPlugin("Game"), 20, 20);
		}}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e)
	{
		e.setDeathMessage(null);
		
		e.getDrops().clear();
		
		
		if(Runner.alive.contains(e.getEntity().getName()))
		{
			Player player = e.getEntity();
			
			player.setHealth(20);
			player.setFoodLevel(20);
			
			player.teleport(Bukkit.getWorld(Runner.selectedMap).getSpawnLocation());
			
			Runner.alive.remove(player.getName());
			Runner.dead.add(player.getName());
			Runner.cooldownTime.remove(player.getName());
			
			player.setAllowFlight(true);
			player.setFlying(true);
			player.getInventory().clear();
			
			ItemStack compass = new ItemStack(Material.COMPASS);
		    ItemMeta compass1 = (ItemMeta) compass.getItemMeta();
		    compass1.setDisplayName("§aPlayer Tracker");
		    compass.setItemMeta(compass1);
			player.getInventory().setItem(0, compass);
			new TrackerTask (player).runTaskTimer((Plugin) this, 0L, 5L);
			
			for(Player on : Bukkit.getOnlinePlayers())
				on.hidePlayer(player);
			
			player.sendMessage("§9Condition> §7You are now invisible.");
		}
	}
	
	  @EventHandler
	  public void BlockForm(EntityChangeBlockEvent event)
	  {
	    BlockSmash(event.getEntity());
	    
	    event.setCancelled(true);
	  }
	  
	  @SuppressWarnings("deprecation")
	public void BlockSmash(Entity ent)
	  {
	    if (!(ent instanceof FallingBlock)) {
	      return;
	    }
	    FallingBlock block = (FallingBlock)ent;
	    
	    int id = block.getBlockId();
	    if ((id == 35) || (id == 159)) {
	      id = 152;
	    }
	    block.getWorld().playEffect(block.getLocation(), org.bukkit.Effect.STEP_SOUND, id);
	    
	    ent.remove();
	  }

	@EventHandler
	public void onPlayerTakeDamage(EntityDamageEvent e)
	{		
	  e.setCancelled(true);
  	  Player p = (Player) e.getEntity();
			if (e.getEntityType() == EntityType.PLAYER) {
			if (e.getCause() == DamageCause.VOID) {
					if(Runner.hasGameState(Runner.GameState.INGAME) & Runner.alive.contains(p.getName()) && Runner.alive.size() != 1) {
					Bukkit.broadcastMessage("§9Death> §e" + p.getName() + "§7 was killed by §eVoid§7.");
					p.setAllowFlight(true);
					p.setFlying(true);
					p.getInventory().clear();
					Runner.dead.add(p.getName());
					Runner.alive.remove(p.getName());
					ItemStack compass = new ItemStack(Material.COMPASS);
				    ItemMeta compass1 = (ItemMeta) compass.getItemMeta();
				    compass1.setDisplayName("§aPlayer Tracker");
				    compass.setItemMeta(compass1);
					p.getInventory().setItem(0, compass);
					new TrackerTask (p).runTaskTimer(Bukkit.getPluginManager().getPlugin("Game"), 0L, 5L);
					}
					
					if(!Runner.hasGameState(Runner.GameState.INGAME)  || !Runner.hasGameState(Runner.GameState.INGAMEWAIT)) {
						p.teleport(Bukkit.getWorld(Runner.selectedMap).getSpawnLocation());
					}
					
					if(Runner.alive.size() == 1 && Runner.alive.contains(p.getName()) && Runner.testing == false) {
						p.setAllowFlight(true);
						p.setFlying(true);
						p.getInventory().clear();
					}
			
			for(String deads : Runner.dead)
			{
				Player pl = Bukkit.getPlayerExact(deads);
				
				pl.hidePlayer(p);
			}
		}
	}
}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e)
	{
        Player p = e.getPlayer();
  		
        if(Runner.team.containsKey(p.getName())) {
  		e.setFormat("§e" + p.getName() + " §f" + e.getMessage());
        }
  		
        if(!Runner.team.containsKey(p.getName())) {
  		e.setFormat("§7" + p.getName() + " §f" + e.getMessage());
        }
        
        if(Runner.dead.contains(p.getName())) {
  		e.setFormat("§7Dead §e" + p.getName() + " §f" + e.getMessage());
        }
        
		if(Runner.chatMuted && !p.isOp())
		{
			e.setCancelled(true);
			e.getPlayer().sendMessage("§9Chat> §7Chat is silenced for §a" + Runner.globalChatSecs + "§a Seconds§7.");
		}
		}
	
	@EventHandler
	public void onPlayerDeath(EntityDamageEvent e)
	{
		if(e.getEntity() instanceof Player && Runner.hasGameState(GameState.INGAME))
		{
			Player p = (Player) e.getEntity();
			
			if(p.getHealth() == 0)
			{
				p.setHealth(20);
				p.setFlying(true);
				Runner.dead.add(p.getName());
				Runner.alive.remove(p.getName());
				
				for(String deads : Runner.dead)
				{
					Player pl = Bukkit.getPlayerExact(deads);
					
					pl.hidePlayer(p);
				}
			}
		}
		else if(!Runner.hasGameState(Runner.GameState.INGAME) && e.getEntity() instanceof Player && e.getCause() != DamageCause.PROJECTILE)
		{
			e.setCancelled(true);
		}
	}
}
