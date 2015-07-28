package spleef.runnables;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import spleef.Spleef;
import spleef.Spleef.GameState;

public class onWin extends BukkitRunnable{

	@Override
	public void run() {
		

		if(Spleef.alive.size() <= 1 && Spleef.testing == false && Spleef.hasGameState(GameState.INGAME))
		{
		 Firework f = (Firework) Bukkit.getWorld("spleef").spawn(Bukkit.getWorld("spleef").getSpawnLocation().add(Math.random() * 160.0D - 80.0D, 10.0D + Math.random() * 20.0D, Math.random() * 160.0D - 80.0D), Firework.class);
           
           FireworkMeta fm = f.getFireworkMeta();
           fm.addEffect(FireworkEffect.builder()
                           .flicker(false)
                            .trail(false)
                           .with(FireworkEffect.Type.BALL_LARGE)
                           .withColor(Color.RED)
                           .build());
           fm.setPower(0);
           f.setFireworkMeta(fm);
		}
	}

}
