package com.tweetapp.webservices.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;

import com.tweetapp.webservices.util.JDBCConnection;

public class LoginDAO {

	private final static Logger LOGGER = Logger.getLogger(LoginDAO.class);

	/**
	 * This method is to change the password for any user. We we get their
	 * current password and only if it match with the existing data we will 
	 * let them to create a new password
	 * @param email
	 * @param curr_pass
	 * @param new_pass
	 */
	public void changePassword(String email, String curr_pass, String new_pass) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String query = "select * from tweet.userdetails where EmailID=? and Password=?";
		try {
			connection = JDBCConnection.getJdbcConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, curr_pass);
			ResultSet result = preparedStatement.executeQuery();
			if (result.next()) {
				String query1 = "UPDATE tweet.userdetails SET Password=? WHERE (EmailID=?);";
				preparedStatement = connection.prepareStatement(query1);
				preparedStatement.setString(1, new_pass);
				preparedStatement.setString(2, email);
				if (preparedStatement.executeUpdate() != 0) {
					System.out.println("Password Change Sucessfully");
				}
			} else {
				System.out.println("Invalid Current Password");
			}
		} catch (SQLException e) {
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.error(e.getMessage());
			}
		}
	}

	/**
	 * This method is to post a tweet if the user is logged in.
	 * @param email
	 * @param tweet
	 * @return
	 */
	public boolean postTweet(String email, String tweet) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String insertQuery = "INSERT INTO tweet.tweets (UserID , Tweets) values (?,?)";
		try {
			connection = JDBCConnection.getJdbcConnection();
			preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS tweet.tweets (UserID varchar(255), Tweets varchar(255));");
			preparedStatement.execute();
			preparedStatement = connection.prepareStatement(insertQuery);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, tweet);
			int x = preparedStatement.executeUpdate();
			if (x > 0) {
				return true;
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.error(e.getMessage());
			}
		}
		return false;
	}

	/**
	 * This method is to view all of his post only.
	 * @param email
	 */
	public void viewTweet(String email) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String query = "SELECT * FROM tweet.tweets WHERE UserID = ?";
		try {
			connection = JDBCConnection.getJdbcConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			System.out.println("All Tweet's From : " + email);
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				System.out.println("Tweet :" + result.getString(2));
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.error(e.getMessage());
			}
		}
	}
	
	/**
	 * This method is used to view all the tweet posted by all the users.
	 */
	public void viewAllTweet() {
		Connection connection = null;
		Statement selectQuery = null;
		String query = "SELECT * FROM tweet.tweets";
		List<String> tweetList = new ArrayList<>();

		try {
			connection = JDBCConnection.getJdbcConnection();
			selectQuery = connection.createStatement();
			ResultSet result = selectQuery.executeQuery(query);
			while (result.next()) {
				tweetList.add(result.getString(1));
				tweetList.add(result.getString(2));
			}
			tweetList.stream().filter(Objects::nonNull).forEach(tweet -> {
				System.out.println(tweet);
			});;
			
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.error(e.getMessage());
			}
		}
	}
	
	/**
	 * This is to logout a user.
	 * @param email
	 * @return
	 */

	public boolean logout(String email) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String updateQuery = "UPDATE tweet.userdetails SET UserStatus = FALSE where EmailID =?";
		try {
			connection = JDBCConnection.getJdbcConnection();
			preparedStatement = connection.prepareStatement(updateQuery);
			preparedStatement.setString(1, email);
			if (preparedStatement.executeUpdate() != 0) {
				return true;
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.error(e.getMessage());
			}
		}
		return false;
	}

}
