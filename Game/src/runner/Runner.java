package runner;

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
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@SuppressWarnings("deprecation")
public class Runner{
	
	public static ArrayList<String> alive = new ArrayList<String>();
	public static ArrayList<String> dead = new ArrayList<String>();
	public static ArrayList<String> spec = new ArrayList<String>();
	public static ArrayList<String> players = new ArrayList<String>();
	public static ArrayList<String> jumper = new ArrayList<String>();
	public static ArrayList<String> archer = new ArrayList<String>();
	public static ArrayList<String> frosty = new ArrayList<String>();
	public static ArrayList<String> none = new ArrayList<String>();
	public static HashMap<Block, Long> _blocks = new HashMap<>();
	
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
	
	public Player publicPl;
	
	public static HashMap<String, String> team = new HashMap<String, String>();
    public static HashMap<Player, Integer> cooldownTime;
    public static HashMap<Player, BukkitRunnable> cooldownTask;
	
	public static String customMapName = "Runner";
	public static String customMapCreator = "OlympusDev";
	
	public static String selectedMap ="runner";
	
	public static GameState state = GameState.WAITING;
	
	public static void AddBlock(Block block)
	  {
	    if ((block == null) || (block.getTypeId() == 0) || (block.getTypeId() == 7) || (block.isLiquid())) {
	      return;
	    }
	    if (block.getRelative(org.bukkit.block.BlockFace.UP).getTypeId() != 0) {
	      return;
	    }
	    if (Runner._blocks.containsKey(block)) {
	      return;
	    }
	    Runner._blocks.put(block, Long.valueOf(System.currentTimeMillis()));
	  }
	
	public static void setGameState(GameState state)
	{
		Runner.state = state;
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