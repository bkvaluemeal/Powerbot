package net.bkvaluemeal.powerbot.scripts.scavenger.tasks;

import org.powerbot.script.methods.MethodContext;

import net.bkvaluemeal.powerbot.util.Task;

public class Bank extends Task {
	
	public Bank(MethodContext ctx) {
		super(ctx);
	}
	
	@Override
	public boolean validate() {
		return ctx.backpack.select().count() == 28
				&& ctx.players.local().getAnimation() == -1
				&& !ctx.players.local().isInCombat()
				&& ctx.bank.isOnScreen();
	}
	
	@Override
	public void execute() {
		if(ctx.bank.open()) {
			ctx.bank.depositInventory();
			ctx.bank.close();
		}
	}
	
}
