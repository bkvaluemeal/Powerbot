package net.bkvaluemeal.powerbot.scripts.scavenger.tasks;

import java.util.ArrayList;

import net.bkvaluemeal.powerbot.scripts.scavenger.Scavenger;
import net.bkvaluemeal.powerbot.util.Task;

import org.powerbot.script.lang.Filter;
import org.powerbot.script.methods.MethodContext;
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
		
		while(ctx.movement.isReachable(ctx.players.local().getLocation(), item.getLocation())
				&& item.isValid()) {
			
			/* Override if stuck */
			if(System.currentTimeMillis() - startTime >= 10000) {
				break;
			}
			
			if(item.isOnScreen()) {
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
