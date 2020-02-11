package wuhan.corona.virus.bot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Debug {
	public static void main(String[] args) {
		String url = "https://zh.wikipedia.org/zh-tw/2019%E6%96%B0%E5%9E%8B%E5%86%A0%E7%8B%80%E7%97%85%E6%AF%92%E7%96%AB%E6%83%85";
		try {
			
			Document doc = Jsoup.connect(url).get();
			
			int chinaConfirm = 0;
			int chinaDead = 0;
			int taiwanConfirm = 0;
			for(int i = 0; i < 20; i++) {
				for (int j = 0; j < 20; j++) {
					String country = doc.select(String.format(
							"#mw-content-text > div > table.infobox.vevent > tbody > tr:nth-child(%d) > td > table > tbody > tr:nth-child(%d) > td:nth-child(1) > a",
							i, j)).text();
					String confirm = doc.select(String.format(
							"#mw-content-text > div > table.infobox.vevent > tbody > tr:nth-child(%d) > td > table > tbody > tr:nth-child(%d) > td:nth-child(2)",
							i, j)).text();
					String death = doc.select(String.format(
							"#mw-content-text > div > table.infobox.vevent > tbody > tr:nth-child(%d) > td > table > tbody > tr:nth-child(%d) > td:nth-child(3)",
							i, j)).text();
					confirm = confirm.replaceAll(",", "");
					death = death.replaceAll(",", "");
					if (country.equals("")) continue;
					if (country.contains("中國大陸")) {
						chinaConfirm = Integer.parseInt(confirm);
						chinaDead = Integer.parseInt(death);
					}
					if (country.contains("臺灣")) {
						taiwanConfirm = Integer.parseInt(confirm);
					}
				}
			}
			System.out.println(chinaConfirm);
			System.out.println(chinaDead);
			System.out.println(taiwanConfirm);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
