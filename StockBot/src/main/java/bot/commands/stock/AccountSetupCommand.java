package bot.commands.stock;

import bot.driver.StockBot;
import database.UserDB;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AccountSetupCommand extends ListenerAdapter {
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		
		String msg = event.getMessage().getContentRaw();

		if (!msg.equals(StockBot.COMMAND_PREFIX + "setup"))
			return;
		
		long ID = event.getMember().getIdLong();
		
		try {
			UserDB.createUser(ID);
			event.getMessage().reply("Your account has been created.").queue();
		} catch (IllegalStateException e) {
			event.getMessage().reply("Account already exists.").queue();;
		}
	}
}