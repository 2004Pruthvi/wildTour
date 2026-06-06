package com.wild_tour.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Connector {
	public static Connection requestConnection() {
		Connection con = null;
		// This will create a file named "wildlife.db" in your user directory or project folder.
		String url = "jdbc:sqlite:wildlife.db";
		
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection(url);
			
			// Initialize tables if they don't exist
			createTablesIfNotExist(con);
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return con;
	}

	private static void createTablesIfNotExist(Connection con) {
		try (Statement stmt = con.createStatement()) {
			// Create User table
			String userTable = "CREATE TABLE IF NOT EXISTS user (" +
				"user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"user_name TEXT," +
				"email TEXT UNIQUE," +
				"password TEXT," +
				"phone INTEGER," +
				"address TEXT" +
				");";
			stmt.execute(userTable);

			// Create safari table
			String safariTable = "CREATE TABLE IF NOT EXISTS safari (" +
				"safari_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"name TEXT," +
				"image_url TEXT," +
				"price_per_seat REAL," +
				"description TEXT" +
				");";
			stmt.execute(safariTable);

			// Create stay table
			String stayTable = "CREATE TABLE IF NOT EXISTS stay (" +
				"stay_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"name TEXT," +
				"image_url TEXT," +
				"price_per_night REAL," +
				"description TEXT" +
				");";
			stmt.execute(stayTable);

			// Create packages table
			String packagesTable = "CREATE TABLE IF NOT EXISTS packages (" +
				"package_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"name TEXT," +
				"image_url TEXT," +
				"price REAL," +
				"description TEXT" +
				");";
			stmt.execute(packagesTable);

			// Create guides table
			String guidesTable = "CREATE TABLE IF NOT EXISTS guides (" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"name TEXT," +
				"bio TEXT," +
				"price REAL," +
				"image TEXT" +
				");";
			stmt.execute(guidesTable);

			// Create booking table
			String bookingTable = "CREATE TABLE IF NOT EXISTS booking (" +
				"booking_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"user_id INTEGER," +
				"tourist_name TEXT," +
				"item_type TEXT," +
				"item_name TEXT," +
				"item_image TEXT," +
				"num_persons INTEGER," +
				"total_price REAL," +
				"payment_mode TEXT," +
				"status TEXT DEFAULT 'Confirmed'," +
				"booking_date TEXT," +
				"from_date TEXT," +
				"to_date TEXT," +
				"booked_date TEXT," +
				"FOREIGN KEY(user_id) REFERENCES user(user_id)" +
				");";
			stmt.execute(bookingTable);

			// Insert initial data for safari
			stmt.execute("INSERT OR IGNORE INTO safari (safari_id, name, image_url, price_per_seat, description) VALUES (1, 'Jeep Safari', 'https://jagatsinghhotels.com/media/jsHotel/Experiences/jeep-safari-in-kerala-banner.jpg', 800.0, 'Experience thrilling off-road adventures through the wild.')");
			stmt.execute("INSERT OR IGNORE INTO safari (safari_id, name, image_url, price_per_seat, description) VALUES (3, 'Elephant Safari', 'https://www.sollunaresort.com/images/safari/elephant-ride.jpg', 1000.0, 'Ride majestic elephants and observe wildlife from a unique perspective.')");
			stmt.execute("INSERT OR IGNORE INTO safari (safari_id, name, image_url, price_per_seat, description) VALUES (4, 'Boat Safari', 'https://www.heritagetoursandsafaris.com/wp-content/uploads/2022/06/IMG_9180-1024x683.jpg', 500.0, 'Enjoy a peaceful ride on the water while spotting wildlife.')");

			// Insert initial data for packages
			stmt.execute("INSERT OR IGNORE INTO packages (package_id, name, image_url, price, description) VALUES (1, 'Maharaja Package', 'https://cdn.junglelodges.com/uploads/2023/02/WhatsApp-Image-2023-02-18-at-4.09.49-PM-1_555x306_acf_cropped-1.jpeg', 15999.0, '')");
			stmt.execute("INSERT OR IGNORE INTO packages (package_id, name, image_url, price, description) VALUES (2, 'Viceroy Package', 'https://cdn.junglelodges.com/uploads/2023/02/WhatsApp-Image-2023-02-18-at-4.45.04-PM_555x306_acf_cropped.jpeg', 14042.0, '')");
			stmt.execute("INSERT OR IGNORE INTO packages (package_id, name, image_url, price, description) VALUES (3, 'Kabini Tent Package', 'https://cdn.junglelodges.com/uploads/2020/03/1-37.jpg', 12136.0, '')");
			stmt.execute("INSERT OR IGNORE INTO packages (package_id, name, image_url, price, description) VALUES (4, 'Dormitory Package', 'https://cdn.junglelodges.com/uploads/2022/06/WhatsApp-Image-2022-06-04-at-12.23.57-PM_555x306_acf_cropped.jpeg', 8776.0, '')");

			// Insert initial data for stay
			stmt.execute("INSERT OR IGNORE INTO stay (stay_id, name, image_url, price_per_night, description) VALUES (1, 'Evolve Back, Kabini', 'https://dynamic-media-cdn.tripadvisor.com/media/photo-o/13/9f/58/38/pool-reserve-private.jpg?w=900&h=500&s=1', 5000.0, 'Luxury stay near Kabini River. Peaceful and scenic environment.')");
			stmt.execute("INSERT OR IGNORE INTO stay (stay_id, name, image_url, price_per_night, description) VALUES (2, 'The Serai Kabini', 'https://assets.simplotel.com/simplotel/image/upload/x_276,y_45,w_488,h_275,r_0,c_crop,q_90,fl_progressive/w_900,f_auto,c_fit/the-serai-kabini/Waterfront_Villa_at_The_Serai_Kabini,_Stay_At_Kabini,_Riverside_Resort_in_Kabini_1?1743465600076', 8500.0, 'Waterfront villa offering calm and cozy vibes amidst nature.')");

			// Insert initial data for guides
			stmt.execute("INSERT OR IGNORE INTO guides (id, name, bio, price, image) VALUES (1, 'Educational Talks', 'Listen to zookeepers share fun facts and stories. Watch live feeding sessions too!', 1200.0, 'https://travelogyindia.b-cdn.net/blog/wp-content/uploads/2016/07/Wildlife-Travel-Guide.jpg')");

			// Admin dummy user
			String adminCheck = "INSERT OR IGNORE INTO user (user_id, user_name, email, password, phone, address) " +
								"VALUES (100, 'Admin', 'admin@wildtour.com', 'admin', 1234567890, 'System');";
			stmt.execute(adminCheck);

		} catch (SQLException e) {
			System.err.println("Error initializing SQLite tables: " + e.getMessage());
		}
	}
}


