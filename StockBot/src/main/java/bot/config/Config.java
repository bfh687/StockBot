package bot.config;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
	public static String get(String key) {
		return Dotenv.load().get(key);
	}
}