package com.tweetapp.webservices.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.tweetapp.webservices.service.RegistrationService;

public class Main {
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	static RegistrationService registerService = new RegistrationService();

	public static void menu() throws NumberFormatException, IOException {
		System.out.println("Enter your choice :\n1. Register\n2. Login\n3. Forgot Password\n4. Exit\n");
		int choice = Integer.parseInt(br.readLine());

		switch (choice) {
		case 1:
			registerUser();
			break;
		case 2:
			loginUser();
			break;
		case 3:
			forgotPassord();
			break;
		case 4:
			System.exit(0);
		default:
			menu();
			break;
		}

	}

	private static void registerUser() throws NumberFormatException, IOException {
		try {
			registerService.registerUser();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		menu();
	}

	private static void loginUser() throws IOException {
		registerService.loginUser();
	}

	private static void forgotPassord() throws NumberFormatException, IOException {
		registerService.forgotPassord();
		menu();
	}

	public static void main(String[] args) throws IOException {
		menu();

	}

}
