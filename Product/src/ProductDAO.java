import java.sql.*;
import java.util.*;
import java.awt.*;

public class ProductDAO {

	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/mydb?characterEncoding=UTF-8&serverTimezone=UTC";
	String id = "root";
	String pwd = "1234";

	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	Vector<String> items = null; // 콤보박스 아이템 관리를 위한 벡터
	String sql = "";

	public ProductDAO() {
		
		
	} //ProductDAO()

	public ArrayList<Product> getAll() throws SQLException {
		connectDB();
		sql = "SELECT * FROM product";
		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();	
		
		// 전체 검색 데이터를 전달
		ArrayList<Product> datas = new ArrayList<Product>();

		// 관리번호 콤보박스 데이터
		items = new Vector<String>();
		items.add("전체");
		
		while (rs.next()) {
			Product p = new Product();
			p.setPrcode(rs.getInt("prcode"));
			p.setPrname(rs.getString("prname"));
			p.setPrice(rs.getInt("price"));
			p.setManufacture(rs.getString("manufacture"));
			datas.add(p);
			items.add(String.valueOf(rs.getInt("prcode")));
			System.out.println(p);
		} //while
			
		return datas;
		
	} // getAll()
	
	

	public void connectDB() throws SQLException {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, id, pwd); // db연결
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	} // connectDB()

	public void closeDB() {
		try {
			pstmt.close();
			rs.close();
			conn.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	} // closeDB()

	public Product getProduct(int prcode) throws SQLException {
		connectDB();
		sql = sql = "SELECT * FROM product WHERE prcode = ?";
		Product p = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, prcode);
			rs = pstmt.executeQuery();	
			rs.next();
			p = new Product();
			p.setPrcode(rs.getInt("prcode"));
			p.setPrname(rs.getString("prname"));
			p.setPrice(rs.getInt("price"));
			p.setManufacture(rs.getString("manufacture"));	
		} catch(Exception e) {
			e.getStackTrace();
		}
		return p;
	} //getProduct()
	
	public boolean newProduct(Product product) throws SQLException {
		connectDB();
		String sql = "INSERT INTO product VALUES (?,?,?,?)";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, product.getPrcode());
			pstmt.setString(2, product.getPrname());
			pstmt.setInt(3, product.getPrice());
			pstmt.setString(4, product.getManufacture());
			pstmt.executeUpdate();
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	} //newProduct()
	
	public boolean delProduct(int prcode) throws SQLException {
		connectDB();
		sql = "DELETE FROM product WHERE prcode = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, prcode);
			pstmt.executeUpdate();
			return true;
		} catch(Exception e) {
			e.getStackTrace();
			return false;
		}
	} //delProduct()
	
	public boolean updateProduct(Product product) throws SQLException {
		connectDB();
		sql = "UPDATE product SET prname = ?, price = ?, manufacture = ? WHERE prcode = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, product.getPrname());
			pstmt.setInt(2, product.getPrice());
			pstmt.setString(3, product.getManufacture());
			pstmt.setInt(4, product.getPrcode());
			pstmt.executeUpdate();
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace(); 
	        return false;
	    }
	} //updateProduct()
	
	public Vector<String> getItems() {
		   return items;
	} //getItems()
	
	
} // ProductDAO()

