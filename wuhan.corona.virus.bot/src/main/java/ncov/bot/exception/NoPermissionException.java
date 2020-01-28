package ncov.bot.exception;

import java.awt.Color;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;

public class NoPermissionException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoPermissionException(TextChannel channel) {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("你沒有權限使用此指令");
		embed.setColor(Color.RED);
	}
	
}
