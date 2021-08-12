package bot.stock;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;

public class StockMarket {
	
	private boolean isOpen;
	private JDA jda;
	
	public StockMarket(JDA jda) {
		this.jda = jda;
		setMarketStatus();
		setOnlineStatus();
	}
	
	public boolean isOpen() {
		return isOpen;
	}
	
	public void monitor() {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date date = null;
		try {
			date = formatter.parse(nextOpen());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		int dayInterval = 1000 * 60 * 60 * 24;
	    Timer openTimer = new Timer();
	    openTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				Calendar calendar = Calendar.getInstance();
				int day = calendar.get(Calendar.DAY_OF_WEEK);
				
				if (day != Calendar.SATURDAY && day != Calendar.SUNDAY) {
					isOpen = true;
					setOnlineStatus();
				}
			}
	    	
	    }, date, dayInterval);

	    date = null;
		try {
			date = formatter.parse(nextClose());
		} catch (ParseException e) {
			e.printStackTrace();
		}

	    Timer closeTimer = new Timer();
	    closeTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				Calendar calendar = Calendar.getInstance();
				int day = calendar.get(Calendar.DAY_OF_WEEK);
				
				if (day != Calendar.SATURDAY && day != Calendar.SUNDAY) {
					isOpen = false;
					setOnlineStatus();
				}
			}
	    	
	    }, date, dayInterval);
	}

	private String nextOpen() {
		Calendar c = Calendar.getInstance();
		
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		if (hour > 6 || (hour == 6 && minute > 30))
			c.add(Calendar.DAY_OF_YEAR, 1);
		
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);

		return year + "-" + month + "-" + day + " 6:30:00";
	}
	
	private String nextClose() {
		Calendar c = Calendar.getInstance();
		
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if (hour > 12)
			c.add(Calendar.DAY_OF_YEAR, 1);
		
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);

		return year + "-" + month + "-" + day + " 1:00:00";
	}
	
	private void setMarketStatus() {
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		if ((hour < 6 || (hour == 6 && minute < 30)) || hour > 12) {
			isOpen = false;
		} else {
			isOpen = true;
		}
	}
	
	private void setOnlineStatus() {
		if (isOpen) 
			jda.getPresence().setStatus(OnlineStatus.ONLINE);
		else
			jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
	}
}