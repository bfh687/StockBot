package bot.commands.utility;

import bot.driver.StockBot;
import bot.embeds.HelpEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HelpCommand extends ListenerAdapter {
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		
		String msg = event.getMessage().getContentRaw();
		if (!msg.equals(StockBot.COMMAND_PREFIX + "help"))
			return;
		
		event.getChannel().sendMessageEmbeds(new HelpEmbed().build()).queue();
	}
}