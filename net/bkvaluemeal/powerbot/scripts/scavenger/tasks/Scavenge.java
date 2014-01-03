package net.bkvaluemeal.powerbot.scripts.scavenger.tasks;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.bkvaluemeal.powerbot.scripts.scavenger.Scavenger;
import net.bkvaluemeal.powerbot.util.Task;

import org.powerbot.script.lang.Filter;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.Random;
import org.powerbot.script.wrappers.GroundItem;

public class Scavenge extends Task {
	
	public static ArrayList<Integer> badItems = new ArrayList<>();
	
	public Scavenge(MethodContext ctx) {
		super(ctx);
	}
	
	@Override
	public boolean validate() {
		return ctx.backpack.select().count() < 28
				&& ctx.players.local().getAnimation() == -1
				&& !ctx.players.local().isInCombat();
	}
	
	@Override
	public void execute() {
		Scavenger.status = "Scavenging";
		GroundItem item = ctx.groundItems.select(new Filter<GroundItem>() {
			
			@Override
			public boolean accept(GroundItem i) {
				for(int badItem : badItems) {
					if(i.getId() == badItem) {
						return false;
					}
				}
				return true;
			}
			
		}).nearest().poll();
		long startTime = System.currentTimeMillis();
		
		Logger.getGlobal().log(Level.INFO, item.getName()
				+ " ID: " + item.getId()
				+ " " + item.getLocation()
				+ " " + ctx.players.local().getLocation().distanceTo(item.getLocation()));
		
		if(item.getId() == -1) {
			Scavenger.status = "Teleporting";
			
			if(ctx.widgets.get(1465).getComponent(10).interact("Teleport")) {
				sleep(Random.nextInt(1000, 2000));
				if(ctx.widgets.get(1092).getComponent(47).interact("Teleport")) {
					sleep(Random.nextInt(18000, 20000));
					item = ctx.groundItems.select().nearest().poll();
				}
			}
		}
		
		while(ctx.movement.isReachable(ctx.players.local().getLocation(), item.getLocation())
				&& item.isValid()) {
			
			/* Override if stuck */
			if(System.currentTimeMillis() - startTime >= 10000) {
				break;
			}
			
			if(ctx.players.local().getLocation().distanceTo(item.getLocation()) <= 5.0D && 
					item.isOnScreen()) {
				if(item.interact("Take")) {
					sleep(500);
					while(ctx.players.local().isInMotion()) {
						sleep(500);
					}
				}
			} else {
				if(ctx.movement.stepTowards(item)) {
					sleep(500);
					while(ctx.players.local().isInMotion()) {
						sleep(500);
					}
				}
				ctx.camera.turnTo(item);
			}
		}
	}
	
}
