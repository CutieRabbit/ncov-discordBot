package ncov.bot.main;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import ncov.bot.command.NormalCommand;
import ncov.bot.function.DataBase;
import ncov.bot.function.dxyCrawler;

public class Main {

	public static DiscordApi api;
	public static String prefix = "<";
	
	public static void main(String[] args) {

		try {
			
			File file = new File("token.txt");
			
			if(!file.exists()) {
				file.createNewFile();
				System.out.println("請先開啟token.txt，並且將token輸入至txt內");
				return;
			}
			
			Scanner cin = new Scanner(file);
			String token = cin.nextLine();
			api = new DiscordApiBuilder().setToken(token).login().join();
			
			cin.close();
			
			Timer dxyRefresh = new Timer();
			dxyRefresh.schedule(new dxyCrawler(), 60000);
			
			api.addMessageCreateListener(new NormalCommand());
			
			DataBase.alertChannelLode();
			DataBase.publishLode();
			
			System.out.println("執行中");
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
