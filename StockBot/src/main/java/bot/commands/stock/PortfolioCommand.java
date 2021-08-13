package bot.commands.stock;

import java.util.HashSet;

import org.bson.Document;

import bot.driver.StockBot;
import bot.embeds.PortfolioEmbed;
import database.UserDB;
import net.dv8tion.jda.api.entities.Message.MentionType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class PortfolioCommand extends ListenerAdapter {
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String msg = event.getMessage().getContentRaw();
		String[] args = msg.split("\\s+");
		
		if (!args[0].equals(StockBot.COMMAND_PREFIX + "portfolio") && !args[0].equals(StockBot.COMMAND_PREFIX + "port"))
			return;
		
		if (args.length > 3)
			return;
		
		if (args.length == 1) {
			try {
				event.getChannel().sendMessageEmbeds(new PortfolioEmbed(event.getAuthor(), 1).build()).queue();
			} catch (Exception e) {
				event.getMessage().reply("You have not yet set up your user profile.").queue();
			}
		} else if (args.length == 2) {
			if (event.getMessage().getMentionedUsers().size() == 1) {
				try {
					event.getChannel().sendMessageEmbeds(new PortfolioEmbed(event.getMessage().getMentionedUsers().get(0), 1).build()).queue();
				} catch (Exception e) {
					event.getMessage().reply("<@" + event.getMessage().getMentionedUsers().get(0).getIdLong() + "> has not yet set up their user profile.").allowedMentions(new HashSet<MentionType>()).queue();
				}
			} else {
				
				String page = args[1];
				boolean isValid;
				int pageNum = 1;
				
				try {
					pageNum = Integer.parseInt(page);
					isValid = true;
				} catch (NumberFormatException e) {
					isValid = false;
				}
				
				if (!isValid) {
					try {
						Stock stock = YahooFinance.get(args[1]);
						if (stock != null) {
							String ticker = stock.getQuote().getSymbol().toLowerCase();
							long ID = event.getAuthor().getIdLong();
							Document port = UserDB.getPortfolio(ID);
							if (port.containsKey(ticker)) {
								event.getChannel().sendMessageEmbeds(new PortfolioEmbed(event.getAuthor(), stock).build()).queue();
							} else {
								event.getMessage().reply("You do not own any $" + stock.getQuote().getSymbol() + ".").queue();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					event.getChannel().sendMessageEmbeds(new PortfolioEmbed(event.getAuthor(), pageNum).build()).queue();
				}
			}
		} else if (args.length == 3) {
			if (!event.getMessage().getMentionedUsers().isEmpty()) {
				try {
					String page = args[2];
					int pageNum = Integer.parseInt(page);
					event.getChannel().sendMessageEmbeds(new PortfolioEmbed(event.getAuthor(), pageNum).build()).queue();
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
}