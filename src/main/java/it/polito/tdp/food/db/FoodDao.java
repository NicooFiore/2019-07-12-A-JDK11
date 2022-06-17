package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Coppia;
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
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
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
	public void creaIdMap(int max,Map<Integer,Food> idMap){
		String sql = "SELECT f.food_code,f.display_name,COUNT(*) AS c\n"
				+ "FROM `portion` p,food f\n"
				+ "WHERE p.food_code=f.food_code \n"
				+ "GROUP BY f.food_code\n"
				+ "HAVING c<=?" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, max);
			
			
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
				Food f=new Food(res.getInt("food_code"),
							res.getString("display_name")
							);
				idMap.put(f.getFood_code(), f);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			

		} catch (SQLException e) {
			e.printStackTrace();
			
		}

	}
	public List<Coppia> getAllCoppie(Integer max,Map<Integer,Food> idMap){
		String sql = "SELECT fc1.food_code,fc2.food_code,AVG(cc.condiment_calories) "
				+ "FROM food_condiment fc1,food_condiment fc2,condiment cc "
				+ "WHERE fc1.food_code IN (SELECT distinct f.food_code FROM `portion` p,food f WHERE p.food_code=f.food_code "
				+ "GROUP BY f.food_code "
				+ "HAVING COUNT(*)<=?) AND fc2.food_code IN (SELECT distinct f.food_code FROM `portion` p,food f WHERE p.food_code=f.food_code "
				+ "GROUP BY f.food_code "
				+ "HAVING COUNT(*)<=?) AND fc1.food_code>fc2.food_code AND fc1.condiment_code=fc2.condiment_code AND cc.condiment_code=fc1.condiment_code "
				+ "group BY fc1.food_code,fc2.food_code " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, max);
			st.setInt(2, max);
			
			List<Coppia> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
				
				Food f1=idMap.get(res.getInt("fc1.food_code"));
				Food f2=idMap.get(res.getInt("fc2.food_code"));
				double peso=res.getDouble("AVG(cc.condiment_calories)");
				list.add(new Coppia(f1, f2, peso));
				
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
