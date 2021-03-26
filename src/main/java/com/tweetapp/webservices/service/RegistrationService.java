package com.tweetapp.webservices.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.regex.Pattern;

import com.tweetapp.webservices.dao.RegisterDAO;
import com.tweetapp.webservices.exception.InvalidDateException;
import com.tweetapp.webservices.model.UserModel;

public class RegistrationService {

	static RegisterDAO registerDAO = new RegisterDAO();
	LoginService loginService = new LoginService();
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	public void registerUser() throws IOException {
		LocalDate localDate = null;
		String firstName;
		//String middleName;
		String lastName;
		String email;
		String gender;
		String password;
		System.out.println("Please enter your details:");
		do {
			System.out.println("Enter your First Name:");
			firstName = br.readLine().trim();
		} while (firstName.isEmpty());
//		System.out.println("Enter your Middle Name:");
//		middleName = br.readLine().trim();
		do {
			System.out.println("Enter your Last Name:");
			lastName = br.readLine().trim();
		} while (lastName.isEmpty());
		do {
			System.out.println("Enter Your Gender:");
			gender = br.readLine().trim();
		} while (gender.isEmpty());
		System.out.println("Enter Date Of Birth(dd-MM-yyyy):");
		String date = br.readLine().trim();
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			localDate = LocalDate.parse(date, formatter);
		} catch (DateTimeParseException e) {
			new InvalidDateException("Invalid Date");
		}
		do {
			System.out.println("Enter your EmailId:");
			email = br.readLine().trim();
		} while (!validateEmail(email));
		do {
			System.out.println("Enter your Password:");
			password = br.readLine().trim();
		} while (password.isEmpty());

		UserModel userModel = new UserModel(firstName, lastName, gender, localDate, email, password);
		if (registerDAO.registerUser(userModel)) {
			System.out.println("Registered Scucessfully");
			System.out.println("Press Enter to got menu");
			br.readLine();
		} else {
			System.out.println("Registeration Fail :");
			System.out.println("Press Enter to got menu");
			br.readLine();
		}
	}

	public void loginUser() throws IOException {
		System.out.println("Enter Your Email-Id");
		String email = br.readLine().trim();
		System.out.println("Enter Your Password");
		String password = br.readLine().trim();

		if (registerDAO.loginUser(email, password)) {
			loginService.loginMenu(email);
		} else {
			System.out.println("Wrong Credentails");
		}
	}

	public boolean validateEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";

		Pattern pattern = Pattern.compile(emailRegex);
		if (Objects.isNull(email)) {
			System.out.println("Not a Valid  null Email");
			return false;
		} else if (pattern.matcher(email).matches() && registerDAO.isExistingEmail(email)) {
			return true;
		} else if (pattern.matcher(email).matches() && !registerDAO.isExistingEmail(email)) {
			System.out.println("Email already exits");
			return false;
		} else {
			System.out.println("Not a Valid Email");
			return false;
		}
	}

	public void forgotPassord() throws IOException {
		LocalDate localDate = null;
		System.out.println("Enter Your First Name :");
		String firstName = br.readLine();
		System.out.println("Enter Your Last Name");
		String lastName = br.readLine();
		System.out.println("Enter Your Email");
		String email = br.readLine();
		System.out.println("Enter Your DOB");
		String date = br.readLine();
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			localDate = LocalDate.parse(date, formatter);
		} catch (DateTimeParseException e) {
			new InvalidDateException("Invalid Date");
		}
		if (registerDAO.forgotPassword(firstName, lastName, email, localDate)) {
			br.readLine();
		} else {
			System.out.println("No Such User");
			System.out.println("Press Enter to got menu");
			br.readLine();
		}
	}

	public static void resetPassword(String email) throws IOException {
		System.out.println("Enter New Password");
		String newPassword = br.readLine();
		if (registerDAO.restPassword(newPassword, email)) {
			System.out.println("Password Changed");
		}
	}
}
