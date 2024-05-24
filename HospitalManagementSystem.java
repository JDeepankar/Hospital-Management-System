package system_directories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalManagementSystem {
	
	private static final String url = "jdbc:postgresql://localhost/HotelManagement";
	private static final String username = "postgres";
	private static final String password = "Mother@@95";

	public static void main (String[]args) {
		@SuppressWarnings("resource")
		Scanner scanner  = new Scanner (System.in);
	try
	{
		Connection connection = DriverManager.getConnection(url, username, password);
		if (connection != null) {
			System.out.println("Successfully connected to database");
			Patient patient = new Patient(connection, scanner);
			Doctor doctor = new Doctor(connection);
			while(true) {
				System.out.println("Welcome to Hospital Management System");
				System.out.println("Please select your query - ");
				System.out.println("1. Add Patient \n2. View Patients \n3. View Doctor \n4. Book Appointments \n5. Exit");
				int choice = scanner.nextInt();
				switch (choice) {
				case 1: {
					patient.addPatient();
					System.out.println();
					break;
				}
				case 2: {
					patient.viewPatients();
					System.out.println();
					break;
				}
				case 3: {
					doctor.viewDoctor();
					System.out.println();
					break;
				}
				case 4: {
					bookAppointment(connection, scanner, patient, doctor);
					System.out.println();
					break;
				}
				case 5: {
					System.out.println("Thankyou for using this system!");
					return;
				}
				default:
					System.out.println("Please enter a valid number!!!");
					System.out.println();
					break;
				}
			}
		} else {
			System.out.println("Taking longer to connect to database please try again later");
		}
	}catch(SQLException e)
	{
		e.printStackTrace();
	}

	}

	public static void bookAppointment(Connection connection, Scanner scanner, Patient patient, Doctor doctor) {
		System.out.println("Please enter patient id: ");
		int pId  = scanner.nextInt();
		System.out.println("Please enter doctor id: ");
		int dId = scanner.nextInt();
		System.out.println("Please enter appointment date in following format(yyyy-mm-dd): ");
		String appointmentDate = scanner.next();
		if(patient.checkPatient(pId) && doctor.checkDoctor(dId)) {
			if(checkDoctorAvailability(dId, appointmentDate, connection)) {
				String query = "INSERT into Appointment(patient_id, doctor_id, appointment_date) values(?, ?, ?)";
				try {
					PreparedStatement preparedStatement = connection.prepareStatement(query);
					preparedStatement.setInt(1, pId);
					preparedStatement.setInt(2, dId);
					preparedStatement.setString(3, appointmentDate);

					int affectedRows = preparedStatement.executeUpdate();
					if(affectedRows > 0) {
						System.out.println("Appointment booked successfully");
					}else {
						System.out.println("Failed to book appointment, please try later!!");
					}
				} catch (SQLException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}else {
			System.out.println("Please check the id's and enter the valid input!!");
		}

	}

	public static boolean checkDoctorAvailability(int id, String date, Connection connection) {
		String query  = "Select count(*) from appointment where doctor_id = ? and appointment_date = ?";
		try {
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		preparedStatement.setInt(1, id);
		preparedStatement.setString(2, date);
		ResultSet resultSet = preparedStatement.executeQuery();
		if(resultSet.next()) {
			int count  = resultSet.getInt(1);
			if(count==0) {
				return true;
			}
			else {
				return false;
			}
		}
		}catch(SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
}
