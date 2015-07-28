package spleef;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class Spleef
{
	public static ArrayList<String> alive = new ArrayList<String>();
	public static ArrayList<String> dead = new ArrayList<String>();
	public static ArrayList<String> spec = new ArrayList<String>();
	public static ArrayList<String> players = new ArrayList<String>();
	public static ArrayList<String> snowballer = new ArrayList<String>();
	public static ArrayList<String> brawler = new ArrayList<String>();
	public static ArrayList<String> archer = new ArrayList<String>();
	public static ArrayList<String> none = new ArrayList<String>();
	
	public static int gameID;
	public static int globalChatSecs = 0;
	public static int seconds = 60;
	public static boolean update = false;
	public static boolean chatMuted = false;
	public static int secs;
	public static int mins;
	public static int hrs;
	public static int waitingRoomTimer = 0;
	public static int timerSecs = 0;
	public static int secondSecs = 0;
	public static boolean autoStart = true;
	public static boolean testing = false;
	
	public static Player publicPl;
	
	public static HashMap<String, String> team = new HashMap<String, String>();
    public static HashMap<Player, Integer> cooldownTime;
    public static HashMap<Player, BukkitRunnable> cooldownTask;
    
    public static String customMapName = "Spleef";
	public static String customMapCreator = "OlympusDev";
	
	public static String selectedMap = "spleef";
	
	public static GameState state = GameState.WAITING;
	
	public static Inventory kits;
	
	public static Inventory kits(Player p)
	{
		kits = Bukkit.createInventory(null, 9, "§8Kit Selector");
		
		ItemStack snowballer = new ItemStack(Material.SNOW_BALL, 1);
		ItemStack brawler = new ItemStack(Material.IRON_AXE, 1);
		ItemStack archer = new ItemStack(Material.BOW, 1);
		
		ItemMeta snowballer1 = (ItemMeta) snowballer.getItemMeta();
		ItemMeta brawler1 = (ItemMeta) brawler.getItemMeta();
		ItemMeta archer1 = (ItemMeta) archer.getItemMeta();
		
		snowballer1.setDisplayName("§a§lSnowballer");
		brawler1.setDisplayName("§a§lBrawler");
		archer1.setDisplayName("§a§lArcher");
		
		snowballer.setItemMeta(snowballer1);
		brawler.setItemMeta(brawler1);
		archer.setItemMeta(archer1);
		
		kits.setItem(2, snowballer);
		kits.setItem(4, brawler);
		kits.setItem(6, archer);
		
		return kits;
	}
	
	public static void setGameState(GameState state)
	{
		Spleef.state = state;
	}
	
	public static boolean hasGameState(GameState gstate)
	{
		if(state == gstate)
			return true;
		else
			return false;
	}
	
	public static enum GameState
	{
		WAITING, STARTING, INGAMEWAIT, INGAME, ENDGAME, ENDING, STOPPED;
	}
	
	public boolean deleteWorld(File path) {
	      if(path.exists()) {
	          File files[] = path.listFiles();
	          for(int i=0; i<files.length; i++) {
	              if(files[i].isDirectory()) {
	                  deleteWorld(files[i]);
	              } else {
	                  files[i].delete();
	              }
	          }
	      }
	      return(path.delete());
	}
	
	public void unloadWorld(World world) {
	    if(!world.equals(null)) {
	        Bukkit.getServer().unloadWorld(world, false);
	    }
	}
	
	public void copyWorld(File source, File target){
	    try {
	        ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
	        if(!ignore.contains(source.getName())) {
	            if(source.isDirectory()) {
	                if(!target.exists())
	                target.mkdirs();
	                String files[] = source.list();
	                for (String file : files) {
	                    File srcFile = new File(source, file);
	                    File destFile = new File(target, file);
	                    copyWorld(srcFile, destFile);
	                }
	            } else {
	                InputStream in = new FileInputStream(source);
	                OutputStream out = new FileOutputStream(target);
	                byte[] buffer = new byte[1024];
	                int length;
	                while ((length = in.read(buffer)) > 0)
	                    out.write(buffer, 0, length);
	                in.close();
	                out.close();
	            }
	        }
	    } catch (IOException e) {
	 
	    }
	}
 }