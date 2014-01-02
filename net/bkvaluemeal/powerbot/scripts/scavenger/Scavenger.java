package net.bkvaluemeal.powerbot.scripts.scavenger;

import java.util.ArrayList;

import net.bkvaluemeal.powerbot.scripts.scavenger.tasks.*;
import net.bkvaluemeal.powerbot.util.Task;

import org.powerbot.script.Manifest;
import org.powerbot.script.PollingScript;
import org.powerbot.script.util.Random;

@Manifest(name = "Scavenger", authors = {"bkvaluemeal"}, description = "Picks up dropped items and banks them")

public class Scavenger extends PollingScript {
	
	private ArrayList<Task> taskList = new ArrayList<>();
	
	@Override
	public void start() {
		Scavenge.badItems.add(15412);
		taskList.add(new Scavenge(ctx));
		taskList.add(new ToBank(ctx));
		taskList.add(new Bank(ctx));
	}
	
	@Override
	public int poll() {
		for (Task task : taskList) {
			if(task.validate()) {
				task.execute();
				return Random.nextInt(250, 375);
			}
		}
		return 0;
	}
	
}
