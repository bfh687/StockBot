package bot.commands.stock;

import java.util.HashSet;

import bot.driver.StockBot;
import database.UserDB;
import net.dv8tion.jda.api.entities.Message.MentionType;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BalanceCommand extends ListenerAdapter {
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		
		String[] args = event.getMessage().getContentRaw().split("\\s+");
        if (!args[0].equals(StockBot.COMMAND_PREFIX + "balance"))
        	return;
        if (event.getMessage().getMentionedMembers().size() > 1)
        	return;
		
        MessageChannel channel = event.getChannel();
        long ID = event.getAuthor().getIdLong();
        String discordID = "<@" + ID + ">";
        
        if (event.getMessage().getMentionedMembers().size() == 1) {
        	ID = event.getMessage().getMentionedMembers().get(0).getIdLong();
            discordID = "<@" + ID + ">";
        	try {
	        	channel.sendMessage(discordID + "'s balance: $" + UserDB.getBalance(ID)).allowedMentions(new HashSet<MentionType>()).queue();
	        } catch (NullPointerException e) {
	        	channel.sendMessage(discordID + " has not yet set up a user profile.").allowedMentions(new HashSet<MentionType>()).queue();
	        }
        } else {
        	try {
	        	channel.sendMessage(discordID + "'s balance: $" + UserDB.getBalance(ID)).allowedMentions(new HashSet<MentionType>()).queue();
	        } catch (NullPointerException e) {
	        	event.getMessage().reply("You have not yet set up a user profile. To get started, use *!setup*.").allowedMentions(new HashSet<MentionType>()).queue();
	        }
        }
	}
}