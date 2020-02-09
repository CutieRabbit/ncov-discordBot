package ncov.bot.function;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.TimerTask;

import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.luhuiguo.chinese.ChineseUtils;

import ncov.bot.main.Main;
import ncov.bot.util.ChannelData;
import ncov.bot.util.Data;

public class dxyCrawlerNews extends TimerTask {

	public void run() {
		refresh();
		alert();
		infoUpdate();
	}

	public static String url = "https://3g.dxy.cn/newh5/view/pneumonia_timeline?whichFrom=dxy";

	public void refresh() {

		try {

			Document doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
			Elements scriptElements = doc.select("#getTimelineService");

			String rawData = scriptElements.toString();
			int startIndex = 0, endIndex = 0;

			for (int i = 0; i < rawData.length(); i++) {
				if (rawData.charAt(i) == '[')
					startIndex = i;
				if (rawData.charAt(i) == ']')
					endIndex = i;
			}

			rawData = rawData.substring(startIndex, endIndex + 1);

			JsonElement json = new JsonParser().parse(rawData);
			JsonArray array = json.getAsJsonArray();

			for (int i = 0; i < array.size(); i++) {
				JsonObject object = array.get(i).getAsJsonObject();
//				System.out.println(object.toString());
				JsonElement titleElement = object.get("title");
				JsonElement summaryElement = object.get("summary");
				JsonElement pubDateElement = object.get("pubDate");
//				JsonElement pubDateStr = object.get("pubDateStr");
				JsonElement IDElement = object.get("id");
				JsonElement sourceURLElement = object.get("sourceUrl");
				JsonElement infoSourceElement = object.get("infoSource");

				long date = pubDateElement.getAsLong();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				String rawTitle = titleElement.getAsString();
				String rawSummaryElement = summaryElement.getAsString().replaceAll("\n", "");
//				String rawPubDateStrElement = pubDateStr.getAsString();

				String ID = IDElement.getAsString();
				String sourceURL = sourceURLElement.getAsString();
				String infoSource = infoSourceElement.getAsString();
				String formatTime = sdf.format(new Date(date));

				String traditionalTitle = ChineseUtils.toTraditional(rawTitle);
				String traditionalSummary = ChineseUtils.toTraditional(rawSummaryElement);
//				String traditionalPubDateStrElement = ChineseUtils.toTraditional(rawPubDateStrElement);

				Data data = new Data(traditionalTitle, traditionalSummary, formatTime, sourceURL, infoSource, ID);

				if (!DataBase.dataMap.containsKey(date) && !DataBase.published.containsKey(date)) {
					DataBase.dataMap.put(date, data);
					DataBase.published.put(date, false);
					DataBase.timeList.add(date);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void alert() {
		Collections.sort(DataBase.timeList);
		for (long time : DataBase.timeList) {

			Data value = DataBase.dataMap.get(time);
			EmbedBuilder embed = new EmbedBuilder();
			String title = value.getTitle();
			String url = value.getSourceURL();
			String description = value.getContext();
			String info = value.getInfoSource();
			String timeString = value.getTime();

			embed.setTitle(title);
			embed.setUrl(url);
			embed.setDescription(description);
			embed.setFooter(timeString);
			embed.setAuthor(info);

			if (DataBase.published.get(time) == true)
				continue;

			for (ChannelData data : DataBase.alertChannel) {
				try {
					Server server = Main.api.getServerById(data.getServerID()).get();
					ServerChannel channel = server.getChannelById(data.getChannelID()).get();
					TextChannel textChannel = channel.asTextChannel().get();
					textChannel.sendMessage(embed);
				} catch (NoSuchElementException e) {
					DataBase.removeAlertChannelTemp.add(data);
				}
			}

			DataBase.published.put(time, true);
		}
		DataBase.publishSave();
		DataBase.alertChannelSave();
	}
	
	public void infoUpdate() {
		String url = "https://en.wikipedia.org/wiki/2019%E2%80%9320_Wuhan_coronavirus_outbreak";
		try {
			
			Document doc = Jsoup.connect(url).get();
			
			int chinaConfirm = 0;
			int chinaDead = 0;
			int taiwanConfirm = 0;
			for(int i = 0; i < 20; i++) {
				for (int j = 0; j < 20; j++) {
					String country = doc.select(String.format(
							"#mw-content-text > div > table:nth-child(%d) > tbody > tr:nth-child(%d) > td:nth-child(1)",
							i, j)).text();
					String confirm = doc.select(String.format(
							"#mw-content-text > div > table:nth-child(%d) > tbody > tr:nth-child(%d) > td:nth-child(2)",
							i, j)).text();
					String death = doc.select(String.format(
							"#mw-content-text > div > table:nth-child(%d) > tbody > tr:nth-child(%d) > td:nth-child(3)",
							i, j)).text();
					confirm = confirm.replaceAll(",", "");
					death = death.replaceAll(",", "");
					if (country.equals("")) continue;
					if (country.contains("Mainland China")) {
						chinaConfirm = Integer.parseInt(confirm);
						chinaDead = Integer.parseInt(death);
					}
					if (country.contains("Taiwan")) {
						taiwanConfirm = Integer.parseInt(confirm);
					}
				}
			}
			
			long channel1 = 671705344233177089L;
			long channel2 = 671706318540636200L;
			long channel3 = 671705470628659201L;
			
			Server server = Main.api.getServerById(671605920333299712L).get();
			
			ServerVoiceChannel ch1 = server.getVoiceChannelById(channel1).get();
			ServerVoiceChannel ch2 = server.getVoiceChannelById(channel2).get();
			ServerVoiceChannel ch3 = server.getVoiceChannelById(channel3).get();

			ch1.updateName(String.format("中國確診：%d例", chinaConfirm));
			ch2.updateName(String.format("中國死亡：%d例", chinaDead));
			ch3.updateName(String.format("臺灣確診：%d例", taiwanConfirm));
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
