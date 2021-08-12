package bot.commands.utility;

import java.util.List;

import bot.driver.StockBot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BanCommand extends ListenerAdapter {
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		
		String msg = event.getMessage().getContentRaw();
		if (!msg.equals(StockBot.COMMAND_PREFIX + "ban"))
			return;
		
		List<Member> list = event.getMessage().getMentionedMembers();
		
		if (!list.isEmpty()) {
			for (Member member : list) {
				member.ban(7).queue();
			}
		}
	}
}