package runner.runnables;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import runner.Runner;
import runner.Runner.GameState;
import utils.UtilTime;

public class onRun extends BukkitRunnable{
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public void run() {
		Bukkit.getWorld("world").setStorm(false);
    	Bukkit.getWorld("world").setThundering(false);
    	for(Player p : Bukkit.getOnlinePlayers()){
        if(Runner.hasGameState(GameState.INGAME) && Runner.alive.contains(p.getName()) && Runner.alive.size() > 1 || Runner.hasGameState(GameState.INGAME) && Runner.alive.contains(p.getName()) && Runner.testing == true) {
        	    @SuppressWarnings("unused")
				int xMax;
        	    int x;
        	    {
        	     for(Player player : Bukkit.getOnlinePlayers()){

        	      double xMod = player.getLocation().getX() % 1.0D;
        	      if (player.getLocation().getX() < 0.0D) {
        	        xMod += 1.0D;
        	      }
        	      double zMod = player.getLocation().getZ() % 1.0D;
        	      if (player.getLocation().getZ() < 0.0D) {
        	        zMod += 1.0D;
        	      }
        	      int xMin = 0;
        	      xMax = 0;
        	      int zMin = 0;
        	      int zMax = 0;

        	      if (xMod < 0.3D) xMin = -1;
        	      if (xMod > 0.7D) xMax = 1;

        	      if (zMod < 0.3D) zMin = -1;
        	      if (zMod > 0.7D) zMax = 1;

        	      x = xMin;

        	      for (int z = zMin; z <= zMax; z++)
        	      {
        	        Runner.AddBlock(player.getLocation().add(x, -0.5D, z).getBlock());
        	      }
        	      x++;
        	    }

        	    @SuppressWarnings("rawtypes")
				HashSet readd = new HashSet();

        	    @SuppressWarnings("rawtypes")
				Iterator blockIterator = Runner._blocks.keySet().iterator();
        	    int id;
        	    while (blockIterator.hasNext())
        	    {
        	      Block block = (Block)blockIterator.next();

        	      if (UtilTime.elapsed(((Long)Runner._blocks.get(block)).longValue(), 120L))
        	      {
        	        blockIterator.remove();

        	        if (block.getTypeId() == 98)
        	        {
        	          if (block.getData() == 0)
        	          {
        	            readd.add(block);
        	            block.setData((byte)2);
        	            continue;
        	          }

        	        }

        	        if ((block.getTypeId() == 35) || (block.getTypeId() == 159))
        	        {
        	          if (block.getData() == 3)
        	          {
        	            readd.add(block);
        	            block.setData((byte)5);
        	            continue;
        	          }

        	          if (block.getData() == 5)
        	          {
        	            readd.add(block);
        	            block.setData((byte)4);
        	            continue;
        	          }

        	          if (block.getData() == 4)
        	          {
        	            readd.add(block);
        	            block.setData((byte)1);
        	            continue;
        	          }

        	          if (block.getData() == 1)
        	          {
        	            readd.add(block);
        	            block.setData((byte)14);
        	            continue;
        	          }

        	          if (block.getData() != 14)
        	          {
        	            readd.add(block);
        	            block.setData((byte)3);
        	            continue;
        	          }

        	        }

        	        id = block.getTypeId();
					byte data = block.getData();
        	        block.getLocation().getBlock().setType(Material.AIR);
        	        block.getWorld().spawnFallingBlock(block.getLocation(), id, data);
        	      }
        	    }

        	    for (Object block : readd)
        	    {
        	      Runner._blocks.put((Block)block, Long.valueOf(System.currentTimeMillis()));
        	    }	
        	    }
        }
     }
   }
}
