package bot.commands.utility;

import bot.driver.StockBot;
import bot.embeds.AboutEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AboutCommand extends ListenerAdapter {
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		
		String msg = event.getMessage().getContentRaw();
		if (!msg.equals(StockBot.COMMAND_PREFIX + "about"))
			return;
		
		event.getChannel().sendMessageEmbeds(new AboutEmbed().build()).queue();
	}
}