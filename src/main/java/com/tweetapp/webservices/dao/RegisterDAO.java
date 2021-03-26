package com.tweetapp.webservices.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.tweetapp.webservices.model.UserModel;
import com.tweetapp.webservices.service.RegistrationService;
import com.tweetapp.webservices.util.JDBCConnection;

public class RegisterDAO {

	public boolean registerUser(UserModel userEntity) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String insertQuery = "INSERT INTO tweet.userdetails (EmailID, FirstName, LastName, Gender, DateOfBirth, Password, UserStatus) VALUES(?,?,?,?,?,?,?)";
		try {
			connection = JDBCConnection.getJdbcConnection();
			preparedStatement = connection.prepareStatement(
					"CREATE TABLE IF NOT EXISTS tweet.userdetails (EmailID varchar(255) NOT NULL, FirstName varchar(255) NOT NULL, LastName varchar(255) NOT NULL, Gender varchar(10) NOT NULL, DateOfBirth Date NOT NULL, Password varchar(255) NOT NULL, UserStatus int, PRIMARY KEY(EmailID)) ");
			preparedStatement.execute();
			preparedStatement = connection.prepareStatement(insertQuery);
			preparedStatement.setString(1, userEntity.getEmail());
			preparedStatement.setString(2, userEntity.getFirst_name());
			// preparedStatement.setString(3, userEntity.getMiddleName());
			preparedStatement.setString(3, userEntity.getLast_name());
			preparedStatement.setString(4, userEntity.getGender());
			preparedStatement.setDate(5, Date.valueOf(userEntity.getDateOfBirth()));
			preparedStatement.setString(6, userEntity.getPassword());
			preparedStatement.setInt(7, 0);
			int x = preparedStatement.executeUpdate();
			if (x > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean loginUser(String email, String password) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String selectQuery = "SELECT * FROM tweet.userdetails WHERE EmailID=? and Password=?";
		try {
			connection = JDBCConnection.getJdbcConnection();
			preparedStatement = connection.prepareStatement(selectQuery);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, password);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				System.out.print(resultSet.getString("EmailID"));
				String updateQuery = "UPDATE tweet.userdetails SET UserStatus = 1 where EmailID =?";
				preparedStatement = connection.prepareStatement(updateQuery);
				preparedStatement.setString(1, email);
				if (preparedStatement.executeUpdate() != 0) {
					return true;
				}
			}
		} catch (SQLException e) {
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean isExistingEmail(String email) {
		String selectQuery = "select * from tweet.userdetails where EmailID=?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = JDBCConnection.getJdbcConnection();
			preparedStatement = connection.prepareStatement(
					"CREATE TABLE IF NOT EXISTS tweet.userdetails (EmailID varchar(255) NOT NULL, FirstName varchar(255) NOT NULL, LastName varchar(255) NOT NULL, Gender varchar(10) NOT NULL, DateOfBirth Date NOT NULL, Password varchar(255) NOT NULL, UserStatus int, PRIMARY KEY(EmailID)) ");
			preparedStatement.execute();
			preparedStatement = connection.prepareStatement(selectQuery);
			preparedStatement.setString(1, email);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public boolean forgotPassword(String firstName, String lastName, String email, LocalDate date) {
		String selectQuery = "select * from tweet.userdetails where FirstName = ? and LastName = ? and EmailID = ? and DateOfBirth = ?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = JDBCConnection.getJdbcConnection();
			preparedStatement = connection.prepareStatement(selectQuery);
			preparedStatement.setString(1, firstName);
			preparedStatement.setString(2, lastName);
			preparedStatement.setString(3, email);
			preparedStatement.setDate(4, Date.valueOf(date));
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				try {
					RegistrationService.resetPassword(email);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public boolean restPassword(String newPassword, String email) {
		String updateQuery = "UPDATE tweet.userdetails SET Password=? WHERE (EmailID=?);";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JDBCConnection.getJdbcConnection();
			preparedStatement = connection.prepareStatement(updateQuery);
			preparedStatement.setString(1, newPassword);
			preparedStatement.setString(2, email);
			if (preparedStatement.executeUpdate() != 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
