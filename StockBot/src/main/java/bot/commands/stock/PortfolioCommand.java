package bot.commands.stock;

import bot.driver.StockBot;
import bot.embeds.PortfolioEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PortfolioCommand extends ListenerAdapter {
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String msg = event.getMessage().getContentRaw();
		
//		!portfolio
//		!portfolio @user
//		!portfolio ticker
//		all 3 can make an error embed

		if (!msg.equals(StockBot.COMMAND_PREFIX + "portfolio") && !msg.equals(StockBot.COMMAND_PREFIX + "port"))
			return;
		
		// long ID = event.getMember().getIdLong();
		event.getChannel().sendMessageEmbeds(new PortfolioEmbed(event.getAuthor()).build()).queue();
	}
}