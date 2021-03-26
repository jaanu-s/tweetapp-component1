package com.tweetapp.webservices.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class JDBCConnection {

	private final static Logger LOGGER = Logger.getLogger(JDBCConnection.class);

	public static Connection getJdbcConnection() {
		Properties props = new Properties();
		try {
			FileInputStream in = new FileInputStream(
					"C:\\Users\\Pooja Sai\\Practice\\tweetapp-web-services\\src\\main\\resources\\db.properties");
			props.load(in);
			in.close();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
		Connection con = null;
		try {
			Class.forName(props.getProperty("jdbc.driver"));
			String url = props.getProperty("jdbc.url");
			String username = props.getProperty("jdbc.username");
			String password = props.getProperty("jdbc.password");
			con = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.error(e.getMessage());
		}
		return con;
	}
}
