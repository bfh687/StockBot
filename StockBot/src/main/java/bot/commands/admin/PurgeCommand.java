package bot.commands.admin;

import java.util.List;

import bot.driver.StockBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PurgeCommand extends ListenerAdapter {
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (!event.getMember().getPermissions().contains(Permission.ADMINISTRATOR))
			return;
			
		String msg = event.getMessage().getContentRaw();
		if (!msg.equals(StockBot.COMMAND_PREFIX + "purge"))
			return;

		MessageChannel channel = event.getChannel();
		List<Message> messages = channel.getHistory().retrievePast(100).complete();
		while (messages.size() > 0) {
			if (messages.size() == 1) {
				messages.get(0).delete().queue();
			} else {
				channel.purgeMessages(messages);
			}
			messages = channel.getHistory().retrievePast(100).complete();
		}
	}
}