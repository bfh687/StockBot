package bot.embeds;

import java.awt.Color;
import java.io.IOException;
import org.bson.Document;

import database.UserDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class PortfolioEmbed extends EmbedBuilder {
	
	public PortfolioEmbed(User user) {
		super();
		init(user);
	}
	
	private void init(User user) {
		
		// total value
		// quant and price of each stock
		
		long ID = user.getIdLong();
		
		Document doc = UserDB.getPortfolio(ID);
		int size = doc.size();
		
		int pageCap = 1;
		if (pageCap % 5 == 0) 
			pageCap = size / 5;
		else
			pageCap = size / 5 + 1;

		this.setAuthor((user.getName() + "'s portfolio").toLowerCase(), user.getAvatarUrl(), user.getAvatarUrl());
		this.setFooter("Showing page " + 1 + " of " + pageCap);
		this.setDescription("**Total Equity: $" + equity(doc) + "**");
		
		int counter = 0;
		for (String ticker : doc.keySet()) {
			if (counter == 5)
				break;
			Stock stock;
			try {
				stock = YahooFinance.get(ticker);
				addField(ticker.toUpperCase().replace("-USD", ""), "$" + String.format("%.2f", stock.getQuote().getPrice().doubleValue()), true);
				addField("Shares", doc.getDouble(ticker) + "", true);
				addField("Equity", "$" + String.format("%.2f", (doc.getDouble(ticker) * stock.getQuote().getPrice().doubleValue())), true);
			} catch (IOException e) {
				e.printStackTrace();
			}
			counter++;
		}
		setColor(new Color(0x05cc05));
	}
	
	private double equity(Document doc) {
		double equity = 0;
		for (String ticker : doc.keySet()) {
			Stock stock = null;
			try {
				stock = YahooFinance.get(ticker);
			} catch (IOException e) {
				e.printStackTrace();
			}
			equity += (doc.getDouble(ticker) * stock.getQuote().getPrice().doubleValue());
		}
		return equity;
	}
		
}