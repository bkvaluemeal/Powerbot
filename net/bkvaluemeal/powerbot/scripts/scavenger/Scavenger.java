package net.bkvaluemeal.powerbot.scripts.scavenger;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import net.bkvaluemeal.powerbot.scripts.scavenger.tasks.*;
import net.bkvaluemeal.powerbot.util.Task;

import org.powerbot.event.PaintListener;
import org.powerbot.script.Manifest;
import org.powerbot.script.PollingScript;
import org.powerbot.script.util.Random;

@Manifest(name = "Scavenger", authors = {"bkvaluemeal"}, description = "Picks up dropped items and banks them")

public class Scavenger extends PollingScript implements PaintListener {
	
	private final BufferedImage MW2Scavenger = downloadImage("http://static2.wikia.nocookie.net/__cb20120121172947/callofduty/images/thumb/3/30/ScavengePro.png/100px-ScavengePro.png");
	private ArrayList<Task> taskList = new ArrayList<>();
	private final long startTime = System.currentTimeMillis();
	private long runTime = 0L;
	private int count = 0;
	private int lastCount = 0;
	public static int profit = 0;
	public static String status = "Starting up";
	
	@Override
	public void start() {
		Scavenge.badItems.add(15412);	// Super large egg
		Scavenge.badItems.add(1944);	// Eggs
		taskList.add(new Scavenge(ctx));
		taskList.add(new ToBank(ctx));
		taskList.add(new Bank(ctx));
	}
	
	@Override
	public int poll() {
		for (Task task : taskList) {
			if(task.validate()) {
				task.execute();
				return 0;
			}
		}
		return Random.nextInt(250, 375);
	}
	
	private String time() {
		runTime = System.currentTimeMillis() - startTime;
		
		long second = (runTime / 1000) % 60;
		long minute = (runTime / (1000 * 60)) % 60;
		long hour = (runTime / (1000 * 60 * 60)) % 24;
		
		return String.format("%02d:%02d:%02d", hour, minute, second);
	}
	
	private int count() {
		int inventory = ctx.backpack.select().count();
		
		if(inventory == 0) {
			lastCount = 0;
		}
		
		count = (inventory - lastCount) + count;
		lastCount = inventory;
		
		return count;
	}
	
	private String profit() {
		if(status.equals("Updating profit")) {
			return "";
		}
		
		if(profit == 0) {
			return profit + " gp";
		} else {
			return "~" + profit + " gp";
		}
	}
	
	@Override
	public void repaint(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		
		Point mouse = ctx.mouse.getLocation();
		
		/* Menu */
		g.setColor(Color.DARK_GRAY);
		g.fillRoundRect(5, 5, 250, 89, 10, 10);
		g.setColor(Color.WHITE);
		g.drawRoundRect(5, 5, 250, 89, 10, 10);
		g.drawImage(MW2Scavenger, 2, 2, null);
		g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		g.drawString("Time: " + time(), 100, 25);
		g.drawString("Count: " + count(), 100, 40);
		g.drawString("Profit: " + profit(), 100, 55);
		g.drawString("Status: " + status, 100, 85);
		
		/* Mouse */
		g.drawLine(mouse.x, mouse.y - 5, mouse.x, mouse.y + 5);
		g.drawLine(mouse.x - 5, mouse.y, mouse.x + 5, mouse.y);
	}
	
}
