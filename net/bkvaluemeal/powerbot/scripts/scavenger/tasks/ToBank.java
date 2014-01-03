package net.bkvaluemeal.powerbot.scripts.scavenger.tasks;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.wrappers.Locatable;

import net.bkvaluemeal.powerbot.scripts.scavenger.Scavenger;
import net.bkvaluemeal.powerbot.util.Task;

public class ToBank extends Task {
	
	public ToBank(MethodContext ctx) {
		super(ctx);
	}
	
	@Override
	public boolean validate() {
		return ctx.backpack.select().count() == 28
				&& ctx.players.local().getAnimation() == -1
				&& !ctx.players.local().isInCombat()
				&& !ctx.bank.isOnScreen();
	}
	
	@Override
	public void execute() {
		Scavenger.status = "Walking to bank";
		Locatable bank = ctx.bank.getNearest();
		
		if(ctx.movement.stepTowards(bank)) {
			sleep(500);
			while(ctx.players.local().isInMotion()) {
				sleep(500);
			}
		}
	}
	
}
