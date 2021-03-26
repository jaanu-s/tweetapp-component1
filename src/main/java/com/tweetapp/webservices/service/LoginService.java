package com.tweetapp.webservices.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import com.tweetapp.webservices.dao.LoginDAO;

public class LoginService {

	private final static Logger LOGGER = Logger.getLogger(LoginService.class);
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	LoginDAO loginDAO = new LoginDAO();

	/**
	 * This is where we shows our menu to user to proceed further.
	 * 
	 * @param email
	 * @throws IOException
	 */
	public void loginMenu(String email) throws IOException {

		System.out.println(
				"Your Choices:\n1.> Change Password\n" + "2.> Post a tweet\n3.> View Post\n4.> View All\n5.> logout");
		int choices = Integer.parseInt(br.readLine());

		switch (choices) {
		case 1:
			changePassword(email);
			break;
		case 2:
			postTweet(email);
			break;
		case 3:
			viewTweet(email);
			break;
		case 4:
			viewAllTweet(email);
			break;
		case 5:
			logout(email);
			break;
		default:
			System.out.println("Not a Valid selection \n Press enter key return to Menu");
			br.readLine();
			loginMenu(email);
			break;
		}
	}

	private void logout(String email) {
		if (loginDAO.logout(email)) {
			System.out.println("Successfully logged out.");
			System.exit(0);
		}
	}

	private void viewAllTweet(String email) throws IOException {
		loginDAO.viewAllTweet();
		System.out.println("Press enter key return to Menu");
		br.readLine();
		loginMenu(email);
	}

	private void viewTweet(String email) throws IOException {
		loginDAO.viewTweet(email);
		System.out.println("Press enter key return to Menu");
		br.readLine();
		loginMenu(email);
	}

	private void postTweet(String email) throws IOException {
		System.out.println("Post something on your mind");
		String tweet = br.readLine();
		loginDAO.postTweet(email, tweet);
		System.out.println("Press enter key return to Menu");
		br.readLine();
		loginMenu(email);

	}

	private void changePassword(String email) throws IOException {

		LOGGER.info("Inside change password method for user-id: {}" + email);
		System.out.println("Current Password:");
		String currentPassword = br.readLine();
		System.out.println("New Password: ");
		String newPassword = br.readLine();
		System.out.println("Re-enter new Password: ");
		String confirmPassword = br.readLine();

		if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
			System.out.println("Fields cannot be empty");
		} else if (currentPassword.equals(newPassword)) {
			System.out.println("Please don't use your existing password");
		} else if (!newPassword.equals(confirmPassword)) {
			System.out.println("Re-enter your new password correctly");
		} else {
			loginDAO.changePassword(email, currentPassword, newPassword);
		}
		System.out.println("Press enter key return to Menu");
		br.readLine();
		loginMenu(email);
	}
}
