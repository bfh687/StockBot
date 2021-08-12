package bot.commands.stock;

import java.util.HashSet;

import bot.driver.StockBot;
import database.UserDB;
import net.dv8tion.jda.api.entities.Message.MentionType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import yahoofinance.YahooFinance;

public class StockBuyCommand extends ListenerAdapter {
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		
		String msg = event.getMessage().getContentRaw();
		String[] args = msg.split("\\s+");
		
		if (!args[0].equals(StockBot.COMMAND_PREFIX + "buy") || args.length != 3)
			return;
		
		long ID = event.getAuthor().getIdLong();
		String ticker = args[1];
		
		int amount;
		try {
			YahooFinance.get(ticker);
			amount = Integer.parseInt(args[2]);
			UserDB.buyStock(ticker, amount, ID);
			event.getMessage().reply("You have bought " + amount + " of " + ticker.toUpperCase() + ". Your new balance is **$" + UserDB.getBalance(ID) + "**.").allowedMentions(new HashSet<MentionType>()).queue();

		} catch (Exception e) {
			event.getMessage().reply("Invalid stock or amount. See *!help* for usage.").queue();
		}
	}
}