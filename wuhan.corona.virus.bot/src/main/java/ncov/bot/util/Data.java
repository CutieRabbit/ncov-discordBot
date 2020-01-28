package ncov.bot.util;

public class Data {
	
	String title;
	String context;
	String time;
	String sourceURL;
	String infoSource;
	String ID;
	
	public Data(String title, String context, String time, String sourceURL, String infoSource, String ID) {
		this.title = title;
		this.context = context;
		this.time = time;
		this.sourceURL = sourceURL;
		this.infoSource = infoSource;
		this.ID = ID;
	}
	
	public String getInfoSource() {
		return infoSource;
	}
	
	public String getID() {
		return ID;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getContext() {
		return context;
	}
	
	public String getTime() {
		return time;
	}
	
	public String getSourceURL() {
		return sourceURL;
	}

}
