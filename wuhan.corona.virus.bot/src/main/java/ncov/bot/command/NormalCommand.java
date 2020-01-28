package ncov.bot.command;

import java.awt.Color;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import ncov.bot.exception.NoPermissionException;
import ncov.bot.function.DataBase;
import ncov.bot.main.Main;
import ncov.bot.util.ChannelData;

public class NormalCommand implements MessageCreateListener{

	@Override
	public void onMessageCreate(MessageCreateEvent event) {
		
		Server server = event.getServer().get();
		TextChannel channel = event.getChannel();
		Message message = event.getMessage();
		MessageAuthor author = message.getAuthor();
		String content = message.getContent();
		long channelID = channel.getId();
		long serverID = server.getId();
		
		if(author.isYourself()) return;
		if(event.getServer() == null) return;
		
		String prefix = Main.prefix;
		
		try {
		
			if(content.equals(prefix + "addChannel")) {
				if(!author.isBotOwner()) throw new NoPermissionException(channel);
				ChannelData data = new ChannelData(serverID, channelID);
				if(!DataBase.alertChannel.contains(data)) {
					DataBase.alertChannel.add(data);
				}
				EmbedBuilder embed = new EmbedBuilder();
				embed.setTitle("設定成功!");
				embed.setDescription("已設定此頻道推播新聞。");
				embed.setColor(Color.green);
				channel.sendMessage(embed);
				DataBase.alertChannelSave();
			}
			
			if(content.equals(prefix + "removeChannel")) {
				if(!author.isBotOwner()) throw new NoPermissionException(channel);
				ChannelData data = new ChannelData(serverID, channelID);
				if(DataBase.alertChannel.contains(data)) {
					DataBase.alertChannel.remove(data);
				}
				EmbedBuilder embed = new EmbedBuilder();
				embed.setTitle("設定成功!");
				embed.setDescription("已關閉此頻道推播新聞的功能。");
				embed.setColor(Color.green);
				channel.sendMessage(embed);
				DataBase.alertChannelSave();
			}
		
		}catch(NoPermissionException e) {
			e.printStackTrace();
		}
		
	}

}
