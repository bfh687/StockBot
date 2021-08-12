package bot.embeds;

import java.awt.Color;
import net.dv8tion.jda.api.EmbedBuilder;

public class AboutEmbed extends EmbedBuilder {
	
	public AboutEmbed() {
		super();
		init();
	}
	
	private void init() {
		setTitle("About StockBot");
		setDescription("StockBot is a Discord paper trading simulation bot. The bot uses real data taken from " + 
					   "Yahoo Finance to let users buy and sell stocks.");				
		setColor(new Color(0x05cc05));
	}
}