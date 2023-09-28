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
		String userString = sc.nextLine();
		
		
		try (Connection con = DriverManager.getConnection(url, user, password)) {
			
			PreparedStatement ps = con.prepareStatement("select c.country_id, c.name, r.name, c2.name"
					+ " from countries c"
					+ " join regions r on c.region_id = r.region_id"
					+ " join continents c2 on c2.continent_id = r.continent_id"
					+ " where c.name like ? " 
					+ " order by c.name; ");
			ps.setString(1, "%" + userString + "%");
		
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				int id = rs.getInt("c.country_id");
				String country_name = rs.getString("c.name");
				String region_name = rs.getString("r.name");
				String continent_name = rs.getString("c2.name");
				
				System.out.println(id + " | " + country_name + " | "  + region_name + " | "  + continent_name);
				System.out.println("----------------------------------------------------------------");

			}
			
			System.out.println("Which country do you want (write the id)?");
			int userSelection = Integer.parseInt(sc.nextLine());
			
			PreparedStatement psLanguage = con.prepareStatement(
					"select l.language"
					+ " from countries c "
					+ " join country_languages cl on cl.country_id = c.country_id"
					+ " join languages l on l.language_id = cl.language_id"
					+ " where c.country_id  = ?"
					+ " group by l.language; "
					);
			
			psLanguage.setInt(1, userSelection);
			
			PreparedStatement psStat = con.prepareStatement(""
					+ "SELECT cs.*"
					+ " FROM countries c"
					+ " JOIN country_stats cs ON cs.country_id = c.country_id"
					+ " WHERE c.country_id = ?"
					+ " AND cs.year = (SELECT MAX(year) FROM country_stats WHERE country_id = ?);");
			
			psStat.setInt(1, userSelection);
			psStat.setInt(2, userSelection);
			
			
			ResultSet rsLanguage = psLanguage.executeQuery();
			ResultSet rsStat = psStat.executeQuery(); 
			
			System.out.println("In this country the following languages are spoken: ");
			while (rsLanguage.next()) {
				String language = rsLanguage.getString("l.language");
				System.out.println(language);
			}
			
			while (rsStat.next()) {
				long population = rsStat.getLong("population"); 
				long gdp = rsStat.getLong("gdp");
				
				System.out.println("Population: " + population);
				System.out.println("Gdp: " + gdp);
			}
			
			
			
			
		} catch (Exception e) {
			
			System.out.println("Errore di connessione: " + e.getMessage());
		}
		
		System.out.println("\n----------------------------------\n");
		System.out.println("The end");
	}
}

