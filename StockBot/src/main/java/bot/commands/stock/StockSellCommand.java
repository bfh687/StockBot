package bot.commands.stock;

import java.util.HashSet;

import bot.driver.StockBot;
import bot.stock.StockMarket;
import database.UserDB;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.MentionType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class StockSellCommand extends ListenerAdapter {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		
		String msg = event.getMessage().getContentRaw();
		String[] args = msg.split("\\s+");
		
		if (!args[0].equals(StockBot.COMMAND_PREFIX + "sell") || args.length > 3 || args.length < 2)
			return;
		
		if (!StockMarket.isOpen()) {
			event.getMessage().reply("The market is currently closed.").queue();
			return;
		}
		
		long ID = event.getAuthor().getIdLong();
		Message message = event.getMessage();
		
		if (args[1].equals("all") && args.length == 2) {
			UserDB.sellAllStock(ID);
			message.reply("You have sold all of your stocks. Your new balance is **$" + UserDB.getBalance(ID) + "**.").allowedMentions(new HashSet<MentionType>()).queue();
		} else if (args.length == 3) {
			String ticker = args[1];
			if (args[2].equals("all")) {
				UserDB.sellStock(ticker, ID);
				message.reply("You have sold all of your " + ticker.toUpperCase() + " stock. Your new balance is **$" + UserDB.getBalance(ID) + "**.").allowedMentions(new HashSet<MentionType>()).queue();
			} else {
				try {
					int amount = Integer.parseInt(args[2]);
					UserDB.sellStock(ticker, amount, ID);
					message.reply("You have sold " + amount + " of your " + ticker.toUpperCase() + " stock(s). Your new balance is **$" + UserDB.getBalance(ID) + "**.").allowedMentions(new HashSet<MentionType>()).queue();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}