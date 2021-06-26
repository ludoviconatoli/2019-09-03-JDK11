package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public List<Food> listAllFoods(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_code"),
							res.getString("display_name")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> listAllTypePortions(){
		String sql = "SELECT DISTINCT p.portion_display_name FROM portions p" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<String> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new String(res.getString("p.portion_display_name")));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			Collections.sort(list);
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public void getVertici(Map<String, String> mappa, double calorie){
		String sql = "SELECT DISTINCT p.portion_display_name FROM portions p WHERE p.calories < ?" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setDouble(1, calorie);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				if(!mappa.containsKey(res.getString("p.portion_display_name"))) {
					mappa.put(res.getString("p.portion_display_name"), res.getString("p.portion_display_name"));
				}
				
			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public List<Adiacenza> getArchi(Map<String, String> mappa){
		String sql ="SELECT p1.portion_display_name as po1, p2.portion_display_name as po2, COUNT(*) AS peso "
				+ "FROM portions p1, portions p2 "
				+ "WHERE p1.portion_display_name < p2.portion_display_name AND p1.food_code = p2.food_code "
				+ "GROUP BY p1.portion_display_name, p2.portion_display_name";
		
		List<Adiacenza> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;

			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				if(mappa.containsKey(res.getString("po1")) && mappa.containsKey(res.getString("po2"))) {
					Adiacenza a = new Adiacenza(res.getString("po1"), res.getString("po2"), res.getDouble("peso"));
					
					result.add(a);
				}
			}
			
			conn.close();
			return result;

		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
