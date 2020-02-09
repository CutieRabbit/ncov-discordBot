package wuhan.corona.virus.bot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Debug {
	public static void main(String[] args) {
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
			System.out.println(chinaConfirm);
			System.out.println(chinaDead);
			System.out.println(taiwanConfirm);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
