package bot.embeds;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import database.UserDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class PortfolioEmbed extends EmbedBuilder {
	
	public PortfolioEmbed(User user, int pageNum) {
		super();
		init(user, pageNum);
	}
	
	public PortfolioEmbed(User user, Stock stock) {
		super();
		initStock(user, stock);
	}
	
	private void init(User user, int pageNum) {
		long ID = user.getIdLong();
		
		Document doc = UserDB.getPortfolio(ID);
		int size = doc.size();
		
		int pageCap = 1;
		if (pageCap % 5 == 0) 
			pageCap = size / 5;
		else
			pageCap = size / 5 + 1;

		this.setAuthor((user.getName() + "'s portfolio").toLowerCase(), user.getAvatarUrl(), user.getAvatarUrl());
		this.setFooter("Showing page " + pageNum + " of " + pageCap);
		this.setDescription("**==========================\nTotal Equity: $" + String.format("%.2f", equity(doc)) + "\n==========================**");
		
		int counter = (pageNum - 1) * 5;
		int limit = counter + 5;
		
		List<String> list = new ArrayList<String>(doc.keySet());
		for (int i = counter; i < Math.min(limit, list.size()); i++) {
			Stock stock;
			try {
				String ticker = list.get(i).toLowerCase();
				stock = YahooFinance.get(ticker);
				if (stock != null) {
					addField(ticker.toUpperCase().replace("-USD", ""), "$" + String.format("%.2f", stock.getQuote().getPrice().doubleValue()), true);
					addField("Shares", doc.getDouble(ticker) + "", true);
					addField("Equity", "$" + String.format("%.2f", (doc.getDouble(ticker) * stock.getQuote().getPrice().doubleValue())), true);
				} else {
					addField("", "", true);
					addField("", "", true);
					addField("", "", true);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		setColor(new Color(0x05cc05));
	}
	
	private void initStock(User user, Stock stock) {
		String ticker = stock.getQuote().getSymbol().toLowerCase();
		Document doc = UserDB.getPortfolio(user.getIdLong());
		setAuthor((user.getName() + "'s $" + stock.getQuote().getSymbol() + " Stock"), user.getAvatarUrl(), user.getAvatarUrl());
		addField(ticker.toUpperCase().replace("-USD", ""), "$" + String.format("%.2f", stock.getQuote().getPrice().doubleValue()), true);
		addField("Shares", doc.getDouble(ticker) + "", true);
		addField("Equity", "$" + String.format("%.2f", (doc.getDouble(ticker) * stock.getQuote().getPrice().doubleValue())), true);
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