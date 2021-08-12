package bot.driver;

import java.util.EnumSet;
import javax.security.auth.login.LoginException;

import bot.commands.stock.BalanceCommand;
import bot.commands.stock.MarketCommand;
import bot.commands.stock.PortfolioCommand;
import bot.commands.stock.SetupCommand;
import bot.commands.stock.StockBuyCommand;
import bot.commands.stock.StockInfoCommand;
import bot.commands.stock.StockSellCommand;
import bot.commands.utility.AboutCommand;
import bot.commands.utility.HelpCommand;
import bot.commands.utility.PurgeCommand;
import bot.config.Config;
import bot.stock.StockMarket;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class StockBot {
	
	public static final String TOKEN = Config.get("TOKEN");
	public static final String COMMAND_PREFIX = "!";
	public static final String STOCK_PREFIX = "$";
	
	public StockBot() throws LoginException {
		JDABuilder builder = JDABuilder.createDefault(TOKEN);

		builder.enableIntents(EnumSet.allOf(GatewayIntent.class));
		builder.addEventListeners(new BalanceCommand(), new PurgeCommand(), new PortfolioCommand(), new StockSellCommand(), new StockInfoCommand(), new AboutCommand(), new HelpCommand(), new SetupCommand(), new StockBuyCommand(), new MarketCommand());
		
		JDA jda = builder.build();
		
		StockMarket market = new StockMarket(jda);
		market.monitor();
	}
	
	public static void main(String[] args) throws Exception {
		try {
			new StockBot();
		} catch (LoginException e) {
			e.printStackTrace();
		}
	}
}