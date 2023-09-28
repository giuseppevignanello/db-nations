package org.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import com.mysql.cj.xdevapi.PreparableStatement;

public class Main {
	public static void main(String[] args) {
		
		final String url = "jdbc:mysql://localhost:3306/db-nation";
		final String user = "root";
		final String password = "root";
		Scanner sc = new Scanner(System.in);
		System.out.println("Search a country");
		String userString = "'%" + sc.nextLine() + "%'";
		
		
		try (Connection con = DriverManager.getConnection(url, user, password)) {
			
			PreparedStatement ps = con.prepareStatement("select c.country_id, c.name, r.name, c2.name"
					+ " from countries c"
					+ " join regions r on c.region_id = r.region_id"
					+ " join continents c2 on c2.continent_id = r.continent_id"
					+ " where c.name like " + userString 
					+ " order by c.name ");
		
			
			ResultSet rs = ps.executeQuery();
			System.out.println(userString);
			
			while(rs.next()) {
				int id = rs.getInt("c.country_id");
				String country_name = rs.getString("c.name");
				String region_name = rs.getString("r.name");
				String continent_name = rs.getString("c2.name");
				
				System.out.println(id + " | " + country_name + " | "  + region_name + " | "  + continent_name);
				System.out.println("----------------------------------------------------------------");

			}
		} catch (Exception e) {
			
			System.out.println("Errore di connessione: " + e.getMessage());
		}
		
		System.out.println("\n----------------------------------\n");
		System.out.println("The end");
	}
}

