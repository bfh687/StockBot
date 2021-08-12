package bot.commands.stock;

import java.io.IOException;

import bot.driver.StockBot;
import bot.embeds.StockInfoEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class StockInfoCommand extends ListenerAdapter {
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		
		String[] args = event.getMessage().getContentRaw().split("\\s+");

		if (args.length != 1 || !args[0].startsWith(StockBot.STOCK_PREFIX))
			return;
		
		String msg = event.getMessage().getContentRaw();
		String ticker = msg.substring(1, msg.length());

		try {
			Stock stock = YahooFinance.get(ticker);
			StockInfoEmbed embed = new StockInfoEmbed(stock);
			event.getChannel().sendFile(embed.createGraph(stock), "graph.png").setEmbeds(embed.build()).queue();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}