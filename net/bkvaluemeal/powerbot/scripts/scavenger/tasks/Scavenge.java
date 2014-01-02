package net.bkvaluemeal.powerbot.scripts.scavenger.tasks;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.wrappers.GroundItem;

import net.bkvaluemeal.powerbot.util.Task;

public class Scavenge extends Task {
	
	public Scavenge(MethodContext ctx) {
		super(ctx);
	}
	
	@Override
	public boolean validate() {
		return ctx.backpack.select().count() < 28
				&& ctx.players.local().getAnimation() == -1;
	}
	
	@Override
	public void execute() {
		GroundItem item = ctx.groundItems.nearest().poll();
		
		while(ctx.movement.isReachable(ctx.players.local().getLocation(), item.getLocation())
				&& item.isValid()) {
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
