## StockBot
StockBot is a Java-based Discord bot built using the [Java Discord API](https://github.com/DV8FromTheWorld/JDA) wrapper. Users of the bot can simulate stock trading on the New York Stock Exchange with imaginary funds. User data is stored in the cloud using MongoDB Atlas as a database. Upon user account creation, users are given $25,000 USD to play with.

## Usage
Environment variables are stored in a .env file, which is hidden on GitHub but found in the local directory. The .env file holds the following variables where TOKEN is the Discord bot's token and DATABASE_URI is the URI of the MongoDB Atlas cluster.

```
TOKEN=PLACEHOLDER_TOKEN
DATABASE_URI=PLACEHOLDER_MONGODB_TOKEN
```

## Commands
The following commands allow the user to simulate stock trading, as well as several account management commands (!balance, !portfolio, etc.). Admins can also use additional utility commands for managing the server. 



````
Trade Commands
  $ticker - Gets the given stock's information.
  !buy ticker amount - Buys the specified amount of the given stock.
  !sell ticker amount - Sells the specified amount of the given stock.
  !sell ticker all - Sells the maximum amount of the given stock.
  
User Commands
  !setup - Creates your user profile with $25000.
  !balance - Gets your current balance.
  !balance @user - Gets the current balance of the given user.
  !portfolio [page-number] - Shows your stock portfolio.
  !portfolio @user [page-number] - Shows then mentioned user's stock portfolio.
  
Utility Commands
  !about - Gives information about StockBot.
  !help - Gets information about StockBot command usage.
  !ban @user - Bans the mentioned user (@admin only).
  !clear - Removes all messages less than 2 weeks old (@admin only).
````

