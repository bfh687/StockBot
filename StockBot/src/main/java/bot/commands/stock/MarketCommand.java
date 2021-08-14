package bot.commands.stock;

import bot.driver.StockBot;
import bot.stock.StockMarket;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MarketCommand extends ListenerAdapter {
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		
		String msg = event.getMessage().getContentRaw();
        if (!msg.equals(StockBot.COMMAND_PREFIX + "market"))
        	return;
        
        String marketStatus = null;
        if (StockMarket.isOpen()) {
        	marketStatus = "open";
        } else {
        	marketStatus = "closed";
        }
        
        event.getMessage().reply("The market is currently " + marketStatus + ".").queue();
	}
}