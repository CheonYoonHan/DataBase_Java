import java.sql.*;
import java.util.*;
import java.io.*;

public class MySQLJDBC {
	static Scanner scanner = new Scanner(System.in);
	static int com;
	public static void main(String[] args) throws Exception {
		while(true) {
			CLI();
			if(com == 0) {System.out.println("프로그램을 종료합니다.");}
			else if(com == 1) {createTable();}
			else if(com == 2) {searchTitle();}
			else if(com == 3) {searchTotalnum();}
			else if(com == 4) {searchDate();}
			else{System.out.println("잘못 입력하셨습니다.");}
			
		}
	}
	public static int CLI() {
		System.out.println("==============================================");
		System.out.println("(0) 종료");
		System.out.println("(1) 릴레이션 생성 및 삽입");
		System.out.println("(2) 제목을 이용한 검색");
		System.out.println("(3) 관객수를 이용한 검색");
		System.out.println("(4) 개봉일을 이용한 검색");
		System.out.println("==============================================");
		System.out.println("원하는 번호를 입력하시오");
		com = scanner.nextInt();
		return com;
	}
	public static void createTable() throws Exception {
		BufferedReader ReadFile1 = new BufferedReader(new FileReader("C:\\Users\\201611113\\Desktop\\create_table.txt"));
		String table = ReadFile1.readLine();
		System.out.println(table);
		BufferedReader ReadFile2 = new BufferedReader(new FileReader("C:\\Users\\201611113\\Desktop\\movie_data.txt"));
		String data = ReadFile2.readLine();
		
		try {
				Connection con = getConnection();
				Statement create = con.createStatement();
				create.execute(table);
				
				while(data != null) {
					int cnt = 0;
					StringTokenizer tk = new StringTokenizer(data, "|");
					String qry = "INSERT INTO movie VALUES (";
					while(tk.hasMoreTokens()) {
						if(cnt == 5 || cnt == 6 || cnt == 7) {
							qry += tk.nextToken() + ",";
						}
						else {
							qry += "'" + tk.nextToken() + "',";
						}
						cnt++;
					}
					qry = qry.substring(0, qry.length() - 1);
					qry = qry + ")";
					
					create.executeUpdate(qry);
					data = ReadFile2.readLine();
				}
				data = ReadFile2.readLine();
		} catch (Exception e) {
			System.out.println(e);
		}
		finally {
			System.out.println("Create Table and Insert Data Complete.");
		}
	}

	public static Connection getConnection() throws Exception{
		try{
			System.out.println("Connecting to database...");

			//Register JDBC driver
			Class.forName("com.mysql.cj.jdbc.Driver");

			//Open a connection
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/movie?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "1234");
			System.out.println("Connecting Success");
			
			return conn;
		} catch (Exception e){
		    //Handle errors for Class.forName
			System.out.println(e);
		}
		
		return null;
	}
	public static void searchTitle() throws Exception{
		String search_title;
		
		try {
			Connection con = getConnection();
			scanner.nextLine();
			System.out.print("제목 입력 :");
			search_title =scanner.nextLine();
			PreparedStatement statement = con.prepareStatement("select * from movie where title like '%" + search_title + "%';");
			
			ResultSet result = statement.executeQuery();
			if(result == null) {System.out.println("검색 결과가 없습니다.");}
			
			while(result.next()) {
				System.out.println(result.getString("id") + "|" + result.getString("title") + "|" + result.getString("company") + "|" + result.getDate("releasedate") + "|" +result.getString("country") + "|" +result.getDouble("totalscreen") + "|" +result.getDouble("profit") + "|" +result.getDouble("totalnum") + "|" +result.getString("grade"));
			}
		}catch(Exception e) {
				System.out.println(e);
		}
		System.out.println();
	}
	public static void searchTotalnum() throws Exception{
		int search_totalnum;
		
		try {
			Connection con = getConnection();
			scanner.nextLine();
			System.out.print("관람객 수 입력 :");
			search_totalnum =scanner.nextInt();
			PreparedStatement statement = con.prepareStatement("select * from movie where totalnum >=" + search_totalnum + ";");
			
			ResultSet result = statement.executeQuery();
			if(result == null) {System.out.println("검색 결과가 없습니다.");}
			
			while(result.next()) {
				System.out.println(result.getString("id") + "|" + result.getString("title") + "|" + result.getString("company") + "|" + result.getDate("releasedate") + "|" +result.getString("country") + "|" +result.getDouble("totalscreen") + "|" +result.getDouble("profit") + "|" +result.getDouble("totalnum") + "|" +result.getString("grade"));
			} 
		}catch(Exception e) {
				System.out.println(e);
		}
		System.out.println();
	}
	public static void searchDate() throws Exception{
		String search_date1;
		String search_date2;
		
		try {
			Connection con = getConnection();
			scanner.nextLine();
			System.out.print("첫번째 날 입력: ");
			search_date1 =scanner.next();
			System.out.print("두번째 날 입력: ");
			search_date2 =scanner.next();
			PreparedStatement statement = con.prepareStatement("select * from movie where releasedate between date('" + search_date1 +"') and date('" + search_date2 + "')+1;");
			
			ResultSet result = statement.executeQuery();
			if(result == null) {System.out.println("검색 결과가 없습니다.");}
			
			while(result.next()) {
				System.out.println(result.getString("id") + "|" + result.getString("title") + "|" + result.getString("company") + "|" + result.getDate("releasedate") + "|" +result.getString("country") + "|" +result.getDouble("totalscreen") + "|" +result.getDouble("profit") + "|" +result.getInt("totalnum") + "|" +result.getString("grade"));
			} 
		}catch(Exception e) {
				System.out.println(e);
		}
		System.out.println();
	}
}