package bot.embeds;

import java.awt.Color;
import net.dv8tion.jda.api.EmbedBuilder;

public class HelpEmbed extends EmbedBuilder {
	
	public HelpEmbed() {
		super();
		init();
	}
	
	private void init() {
		
		setTitle("StockBot Information");
		
		setDescription("Hello! I am StockBot, a Discord paper trading simulation bot. Here are a list of commands to this get you started.");

		addField("Stock Commands", "**$ticker** - Gets the given stock's information.\n" + 
								   "**!buy ticker amount** - Buys the specified amount of the given stock.\n" +
									    "**!sell ticker amount** - Sells the specified amount of the given stock.\n" + 
									    "**!sell ticker all** - Sells the maximum amount of the given stock.\n", false);
		
		addField("User Commands", "**!setup** - Creates your user profile with $25000.\n" +
									"**!balance** - Gets your current balance.\n" +
									   "**!balance @user** - Gets the current balance of the given user.\n" +
								       "**!portfolio** - Shows your stock portfolio.\n", false);
		
		addField("Utility Commands", "**!about** - Gives information about StockBot.\n" + 
									      "**!help** - Gets information about StockBot command usage.\n" +
									      "**!ban @user** - Bans the mentioned user (@admin only).\n" + 
								          "**!clear** - Removes all messages less than 2 weeks old (@admin only).\n", false);
							
		setColor(new Color(0x05cc05));
	}
}