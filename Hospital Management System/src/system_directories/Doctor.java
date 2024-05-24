package system_directories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Doctor {
	
	private Connection connection;

	public Doctor(Connection connection) {
		this.connection = connection;
	}

	public void viewDoctor() {
		String query = "Select * from doctor";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			System.out.println("+-----------+---------------+----------------+");
			System.out.println("| Doctor ID | Doctor Name   | Specialization |");
			System.out.println("+-----------+---------------+----------------+");
			while(resultSet.next()) {
				int id = resultSet.getInt("doctor_id");
				String name = resultSet.getString("doctor_name");
				String dept = resultSet.getString("doctor_department");
				System.out.printf("|%-11s|%-15s|%-16s|\n",id, name, dept);
				System.out.println("+-----------+---------------+----------------+");
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public boolean checkDoctor(int id) {
		String query = "Select * from doctor where doctor_id = ?";
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


