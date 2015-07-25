package runner.runnables;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import runner.Runner;
import runner.Runner.GameState;

public class onRun extends BukkitRunnable{

	@SuppressWarnings({ "rawtypes", "deprecation" })
	@Override
	public void run() {
		Bukkit.getWorld("world").setStorm(false);
    	Bukkit.getWorld("world").setThundering(false);
    	
    	
    		
    		if(Runner.hasGameState(GameState.INGAME)) {
    			
    			 for(Player p : Bukkit.getOnlinePlayers()) {	 
    				 double xMod = p.getLocation().getX() % 1.0D;
    			      if (p.getLocation().getX() < 0.0D) {
    			        xMod += 1.0D;
    			      }
    			      double zMod = p.getLocation().getZ() % 1.0D;
    			      if (p.getLocation().getZ() < 0.0D) {
    			        zMod += 1.0D;
    			      }
    			      int xMin = 0;
    			      @SuppressWarnings("unused")
					  int xMax = 0;
    			      int zMin = 0;
    			      int zMax = 0;
    			      
    			      if (xMod < 0.3D) xMin = -1;
    			      if (xMod > 0.7D) { xMax = 1;
    			      }
    			      if (zMod < 0.3D) zMin = -1;
    			      if (zMod > 0.7D) { zMax = 1;
    			      }
    			      int x = xMin;x++;
    			      xMod = p.getLocation().getX() % 1.0D;
    			      if (p.getLocation().getX() < 0.0D) {
    			          xMod += 1.0D;
    			        }
    			        zMod = p.getLocation().getZ() % 1.0D;
    			        if (p.getLocation().getZ() < 0.0D) {
    			          zMod += 1.0D;
    			        }
    			        xMin = 0;
    			        xMax = 0;
    			        zMin = 0;
    			        zMax = 0;
    			        
    			        if (xMod < 0.3D) xMin = -1;
    			        if (xMod > 0.7D) { xMax = 1;
    			        }
    			        if (zMod < 0.3D) zMin = -1;
    			        if (zMod > 0.7D) { zMax = 1;
    			        }
    			        x = xMin;
    			        for (int z = zMin; z <= zMax; z++)
    			        {
    			        	Runner.AddBlock(p.getLocation().add(x, -0.5D, z).getBlock());
    			        }
    			        x++;   
		        	}







					HashSet<Block> readd = new HashSet<>();
    			    Object blockIterator = Runner._blocks.keySet().iterator();
    			    int id;
    			    while (((Iterator)blockIterator).hasNext())
    			    {
    			      Block block = (Block)((Iterator)blockIterator).next();

    			        ((Iterator)blockIterator).remove();
    			        

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
    			        block.setType(Material.AIR);
						@SuppressWarnings("unused")
						FallingBlock ent = block.getWorld().spawnFallingBlock(block.getLocation(), id, data);
    			      }
    			    
    			    for (Block block : readd)
    			    {
    			    	Runner._blocks.put(block, Long.valueOf(System.currentTimeMillis()));
    			    }

    		       }
	         }

}
