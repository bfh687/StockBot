package bot.embeds;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import net.dv8tion.jda.api.EmbedBuilder;
import yahoofinance.Stock;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;
import yahoofinance.quotes.stock.StockQuote;

public class StockInfoEmbed extends EmbedBuilder {

	public StockInfoEmbed(Stock stock) throws IOException {
		super();
		init(stock);
	}
	
	private void init(Stock stock) throws IOException {
		StockQuote quote = stock.getQuote();
		
		String name = stock.getName();
		String ticker = stock.getQuote().getSymbol();
		
		double price = quote.getPrice().doubleValue();
		
		double openPrice = quote.getOpen().doubleValue();
		double lastClose = quote.getPreviousClose().doubleValue();

		double dayLow = quote.getDayLow().doubleValue();
		double dayHigh = quote.getDayHigh().doubleValue();
		
		double percentChange = quote.getChangeInPercent().doubleValue();
		double change = quote.getChange().doubleValue();
		
		this.setTitle("$" + ticker + " - " + name);
		this.setFooter("Graph represents stock market data from the past year.");
		this.setImage("attachment://graph.png");
		
		this.addField("Price", price + "", true);
		this.addField("Last Close", lastClose + "", true);
		this.addField("Daily Change", change + "/" + percentChange + "%", true);
		
		this.addField("Open", openPrice + "", true);
		this.addField("Day Low", dayLow + "", true);
		this.addField("Day High", dayHigh + "", true);
		
		List<HistoricalQuote> list = stock.getHistory(Interval.DAILY);
		if (list.get(0).getOpen().doubleValue() > list.get(list.size() - 1).getClose().doubleValue()) {
			this.setColor(new Color(0xec4738));
		} else {
			this.setColor(new Color(0x05cc05));
		}
	}
	
	public byte[] createGraph(Stock stock) throws IOException {		
		BufferedImage img = new BufferedImage(240, 150, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) img.getGraphics();
		g.setColor(Color.decode("#32353b"));
		g.fillRect(0, 0, 240, 150);
		
		List<HistoricalQuote> list = stock.getHistory(Interval.DAILY);
		int size = list.size();
		
		double min = Integer.MAX_VALUE;
		double max = Integer.MIN_VALUE;
		for (int i = 0; i < size; i++) {
			if (list.get(i).getOpen().doubleValue() < min)
				min = list.get(i).getOpen().doubleValue();
			if (list.get(i).getOpen().doubleValue() > max)
				max = list.get(i).getOpen().doubleValue();
		}

		double open = list.get(0).getOpen().doubleValue();
		double close;
		
		if (list.get(0).getOpen().doubleValue() > list.get(list.size() - 1).getClose().doubleValue()) {
			g.setColor(Color.decode("#ec4738"));
		} else {
			g.setColor(new Color(0x05cc05));
		}
		
		g.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		for (int i = 0; i < size; i++) {
			int x = (int) (i * (240.0 / size));
			int x2 = (int) ((i + 1) * (240.0 / size));
			
			close = list.get(i).getClose().doubleValue();

			g.drawLine(x, 135 - (int) ((open - min) / (max - min) * 120), x2, 135 - (int) ((close - min) / (max - min) * 120));
			
			open = close;
		}

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ImageIO.write(img, "png", stream);
		
		return stream.toByteArray();
	}
}