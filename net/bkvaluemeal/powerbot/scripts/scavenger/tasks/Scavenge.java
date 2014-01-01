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
		
		if(item.isOnScreen()) {
			item.interact("Take");
			sleep(500);
		} else {
			ctx.movement.stepTowards(item);
			ctx.camera.turnTo(item);
		}
	}
	
}