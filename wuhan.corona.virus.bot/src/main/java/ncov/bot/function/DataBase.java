package ncov.bot.function;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

import ncov.bot.util.Data;

public class DataBase {
	
	public static Map<Long,Data> dataMap = new HashMap<Long,Data>();
	public static Map<Long,Boolean> published = new HashMap<>();
	public static List<Long> alertChannel = new ArrayList<>();
	public static List<Long> timeList = new ArrayList<>();
	public static List<Long> removeAlertChannelTemp = new ArrayList<>();

	public static void publishLode() {
		try {
			File file = new File("Published.txt");
			if(!file.exists()) file.createNewFile();
			Scanner cin = new Scanner(file);
			while(cin.hasNext()) {
				long time = cin.nextLong();
				System.out.println(time);
				published.put(time, true);
			}
			cin.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void publishSave() {
		try {
			File file = new File("Published.txt");
			PrintWriter pw = new PrintWriter(file);
			SortedSet<Long> keys = new TreeSet<>(published.keySet());
			for(Long time : keys) {
				pw.println(time);
			}
			pw.flush();
			pw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void alertChannelLode() {
		try {
			File file = new File("AlertChannel.txt");
			if(!file.exists()) file.createNewFile();
			Scanner cin = new Scanner(file);
			while(cin.hasNext()) {
				Long channelID = cin.nextLong();
				alertChannel.add(channelID);
			}
			cin.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void alertChannelSave() {
		try {
			File file = new File("AlertChannel.txt");
			PrintWriter pw = new PrintWriter(file);
			for(long channelID : removeAlertChannelTemp) {
				alertChannel.remove(channelID);
			}
			for(long channelID : alertChannel) {
				pw.println(channelID);
			}
			pw.flush();
			pw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
