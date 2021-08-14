package database;

import java.io.IOException;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import bot.config.Config;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class UserDB {
	
	public static final String URI = Config.get("DATABASE_URI");
	
	private static MongoClient client;
	private static MongoDatabase db;
	private static MongoCollection<Document> col;
	
	// static init block for static acess to the db
	static {
		client = MongoClients.create(URI);
		db = client.getDatabase("MongoDB");
		col = db.getCollection("UserData");
	}
	
	// creates a new user
	public static void createUser(long ID) {
		if (exists(ID))
			throw new IllegalStateException("User already exists.");

		Document doc = new Document("_id", ID);
		doc.append("balance", 25000d);
		doc.append("portfolio", null);
		col.insertOne(doc);
	}
	
	// gets balance
	public static double getBalance(long ID) {
		return getUser(ID).getDouble("balance");
	}
	
	// sets balance of user
	public static void setBalance(long ID, double balance) {
		Document doc = getUser(ID);
		
		if (!exists(ID))
			throw new IllegalStateException("User does not exist.");
		
		Bson newValue = new Document("balance", balance);
		Bson updateOperation = new Document("$set", newValue);
		col.findOneAndUpdate(doc, updateOperation);
	}
	
	// buys the stock
	public static void buyStock(String ticker, int amount, long ID) {
		Stock stock;
		double price;
		
		try {
			stock = YahooFinance.get(ticker);
			price = stock.getQuote().getPrice().doubleValue();
		} catch (IOException e) {
			return;
		}

		double currBalance = getBalance(ID);
		double stockCost = price * amount;
		double newBalance = currBalance - stockCost;
		
		if (newBalance < 0.0)
			throw new IllegalStateException("Not enough money to cover the transaction.");
		
		setBalance(ID, newBalance);
		
		Document doc = (Document) getUser(ID);
		Document innerDoc = (Document) doc.get("portfolio");
		
		if (innerDoc == null) {
			innerDoc = new Document(ticker, amount);
		} else {
			if (!innerDoc.containsKey(ticker)) {
				innerDoc.append(ticker, amount);
			} else {
				innerDoc.append(ticker, amount + innerDoc.getDouble(ticker));
			}
		}
			
		col.findOneAndUpdate(new BasicDBObject("_id", ID), 
			new BasicDBObject("$set", new BasicDBObject("portfolio", innerDoc)));
	}
	
	public static Document getPortfolio(long ID) {
		Document doc = (Document) getUser(ID);
		Document innerDoc = (Document) doc.get("portfolio");
		return innerDoc;
	}
	
	// sells the specified amount of the given stock
	public static void sellStock(String ticker, int amount, long ID) {
		Document doc = (Document) getUser(ID);
		Document innerDoc = (Document) doc.get("portfolio");
		
		if (innerDoc == null || !innerDoc.containsKey(ticker))
			throw new IllegalStateException("No stock to sell.");
		
		if (amount > innerDoc.getDouble(ticker))
			throw new IllegalStateException("Not enough stock to sell.");
		
		Stock stock;
		double price;
		try {
			stock = YahooFinance.get(ticker);
			price = stock.getQuote().getPrice().doubleValue();
		} catch (IOException e) {
			return;
		}
		
		// calculates new stock amount
		double currAmount = innerDoc.getDouble(ticker);
		double newAmount = currAmount - amount;
		
		// sets new balance and new user stock amounts
		setBalance(ID, getBalance(ID) + (amount * price));
		innerDoc.replace(ticker, newAmount);
			
		col.findOneAndUpdate(new BasicDBObject("_id", ID), 
			new BasicDBObject("$set", new BasicDBObject("portfolio", innerDoc)));
	}
	
	public static void sellStock(String ticker, long ID) {
		Document doc = (Document) getUser(ID);
		Document innerDoc = (Document) doc.get("portfolio");

		if (innerDoc == null || !innerDoc.containsKey(ticker))
			throw new IllegalStateException("No stock to sell.");
		
		Stock stock;
		double price;
		try {
			stock = YahooFinance.get(ticker);
			price = stock.getQuote().getPrice().doubleValue();
		} catch (IOException e) {
			return;
		}
		
		int amount = innerDoc.getInteger(ticker);
		setBalance(ID, getBalance(ID) + (amount * price));
		innerDoc.remove(ticker);
			
		col.findOneAndUpdate(new BasicDBObject("_id", ID), 
			new BasicDBObject("$set", new BasicDBObject("portfolio", innerDoc)));
	}
	
	public static void sellAllStock(long ID) {
		Document doc = (Document) getUser(ID);
		Document innerDoc = (Document) doc.get("portfolio");

		if (innerDoc == null)
			throw new IllegalStateException("No stock to sell.");
		
		for (String ticker: innerDoc.keySet()) {
			sellStock(ticker, ID);
		}
	}
	
	public static void clearDB() {
		col.deleteMany(new BasicDBObject());
	}
	
	private static boolean exists(long ID) {
		return getUser(ID) != null;
	}

	private static Document getUser(long ID) {
		return col.find(new Document("_id", ID)).first();
	}
}