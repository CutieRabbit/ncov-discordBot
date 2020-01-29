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

import ncov.bot.util.ChannelData;
import ncov.bot.util.Data;

public class DataBase {
	
	public static Map<Long,Data> dataMap = new HashMap<Long,Data>();
	public static Map<Long,Boolean> published = new HashMap<>();
	public static List<ChannelData> alertChannel = new ArrayList<>();
	public static List<Long> timeList = new ArrayList<>();
	public static List<ChannelData> removeAlertChannelTemp = new ArrayList<>();

	public static void publishLode() {
		try {
			File file = new File("Published.txt");
			if(!file.exists()) file.createNewFile();
			Scanner cin = new Scanner(file);
			while(cin.hasNext()) {
				long time = cin.nextLong();
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
				Long serverID = cin.nextLong();
				Long channelID = cin.nextLong();
				ChannelData data = new ChannelData(serverID, channelID);
				alertChannel.add(data);
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
			for(ChannelData channelData : removeAlertChannelTemp) {
				alertChannel.remove(channelData);
			}
			for(ChannelData data : alertChannel) {
				pw.println(data.getServerID() + " " + data.getChannelID());
			}
			pw.flush();
			pw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
