package system_directories;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
	
	private Connection connection;
	private Scanner scanner;

	public Patient(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}

	public void addPatient() {

		//This is to get values when the driver class execute if a new patient detail is to add in db
		System.out.println("Enter patient name: ");
		String name = scanner.next();
		System.out.println("Enter patient age: ");
		int age = scanner.nextInt();
		System.out.println("Enter patient gender: ");
		String gender = scanner.next();

		try {
			String query = "INSERT into patient(patient_name, patient_age, patient_gender) values(?, ?, ?)";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, name);
			preparedStatement.setInt(2, age);
			preparedStatement.setString(3, gender);

			int affectedRows = preparedStatement.executeUpdate();
			if(affectedRows > 0) {
				System.out.println("Patient added successfully");
			}else {
				System.out.println("Failed to add patient");
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void viewPatients() {
		String query = "Select * from patient";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			System.out.println("+------------+--------------+-----+--------+");
			System.out.println("| Patient ID | Patient Name | Age | Gender |");
			System.out.println("+------------+--------------+-----+--------+");
			while(resultSet.next()) {
				int id = resultSet.getInt("patient_id");
				String name = resultSet.getString("patient_name");
				int age = resultSet.getInt("patient_age");
				String gender = resultSet.getString("patient_gender");
				System.out.printf("|%-12s|%-14s|%-5s|%-8s|\n",id, name, age, gender );
				System.out.println("+------------+--------------+-----+--------+");
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public boolean checkPatient(int id) {
		String query = "Select * from patient where patient_id = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return true;
			}
			else {
				return false;
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
}
