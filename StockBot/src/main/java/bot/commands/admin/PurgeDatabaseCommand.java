package bot.commands.admin;

import bot.driver.StockBot;
import database.UserDB;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PurgeDatabaseCommand extends ListenerAdapter {
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (!event.getMember().getPermissions().contains(Permission.ADMINISTRATOR) || !event.getAuthor().getName().equals("blake"))
			return;
		
		String msg = event.getMessage().getContentRaw();
		if (!msg.equals(StockBot.COMMAND_PREFIX + "purge db"))
			return;
		
		UserDB.clearDB();
	}
}