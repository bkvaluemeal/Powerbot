package net.bkvaluemeal.powerbot.scripts.scavenger.tasks;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.GeItem;
import org.powerbot.script.wrappers.Item;

import net.bkvaluemeal.powerbot.scripts.scavenger.Scavenger;
import net.bkvaluemeal.powerbot.util.Task;

public class Bank extends Task {
	
	private GeItem item;
	
	public Bank(MethodContext ctx) {
		super(ctx);
	}
	
	@Override
	public boolean validate() {
		return ctx.backpack.select().count() == 28
				&& ctx.players.local().getAnimation() == -1
				&& !ctx.players.local().isInCombat()
				&& ctx.bank.isOnScreen()
				&& ctx.players.local().getLocation().distanceTo(ctx.bank.getNearest()) <= 2.0D;
	}
	
	@Override
	public void execute() {
		Scavenger.status = "Banking";
		Item[] inventory = ctx.backpack.getAllItems();
		
		if(ctx.bank.open()) {
			ctx.bank.depositInventory();
			ctx.bank.close();
		}
		
		for(Item i : inventory) {
			Scavenger.status = "Updating profit";
			
			try {
				item = GeItem.getProfile(i.getId());
			} catch(Exception e) {
				item = null;
			}
			
			if(item != null) {
				Scavenger.profit = item.getPrice(GeItem.PriceType.CURRENT).getPrice() + Scavenger.profit;
			}
		}
	}
	
}
