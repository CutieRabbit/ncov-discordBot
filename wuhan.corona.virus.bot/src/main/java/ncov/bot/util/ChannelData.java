package ncov.bot.util;

public class ChannelData {
	
	Long server, channelID;
	
	public ChannelData(Long server, Long channelID) {
		this.server = server;
		this.channelID = channelID;
	}
	
	public Long getServerID() {
		return server;
	}
	
	public Long getChannelID() {
		return channelID;
	}

}
