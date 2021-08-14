package bot.driver;

import java.util.EnumSet;
import javax.security.auth.login.LoginException;

import bot.commands.stock.BalanceCommand;
import bot.commands.stock.MarketCommand;
import bot.commands.stock.PortfolioCommand;
import bot.commands.admin.PurgeCommand;
import bot.commands.admin.PurgeDatabaseCommand;
import bot.commands.info.AboutCommand;
import bot.commands.info.HelpCommand;
import bot.commands.stock.AccountSetupCommand;
import bot.commands.stock.StockBuyCommand;
import bot.commands.stock.StockInfoCommand;
import bot.commands.stock.StockSellCommand;
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
		builder.addEventListeners(new BalanceCommand(), new PurgeCommand(), new PortfolioCommand(), new StockSellCommand(), 
				 				  new StockInfoCommand(), new AboutCommand(), new HelpCommand(), new AccountSetupCommand(), 
				 				  new StockBuyCommand(), new MarketCommand(), new PurgeDatabaseCommand());
		
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