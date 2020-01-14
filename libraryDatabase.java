import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Arrays;
import java.util.List;

public class Assignment9 {

	public String dbURL1 = "jdbc:oracle:thin:SYSTEM/PASSWORD@localhost:1521:xe";
 
	public JFrame frmJdbc;
	public JTextArea textArea;
	private JTextField txtName;
	private JTextField textField_1;
	public JLabel lblId;
	
	public String[] tables = new String[]{"reservation","fine","book","publisher","borrows","bookClub","member"};
    public String memberName;
    public int memberAge;
    public int memberID = 12309352;
	public String query;
	
	public String[] createTables = {"CREATE TABLE member(\r\n" + 
			"    mem_id VARCHAR(25) PRIMARY KEY,\r\n" + 
			"    mem_name VARCHAR(25) NOT NULL,\r\n" + 
			"    mem_age INTEGER CHECK (mem_age > -1)\r\n" + 
			"    ) ",
			"CREATE TABLE publisher(\r\n" + 
			"    pub_name VARCHAR(25) PRIMARY KEY,\r\n" + 
			"    pub_location VARCHAR(25)\r\n" + 
			"    ) ",
			"CREATE TABLE book(\r\n" + 
			"    isbn VARCHAR(15) PRIMARY KEY,\r\n" + 
			"    b_author VARCHAR(25) NOT NULL,\r\n" + 
			"    b_title VARCHAR(25) NOT NULL,\r\n" + 
			"    b_genre VARCHAR(25) NOT NULL,\r\n" + 
			"    b_count INTEGER CHECK (b_count > -1),\r\n" + 
			"    b_minAge INTEGER,\r\n" + 
			"    pub_name VARCHAR(25) REFERENCES publisher(pub_name) ON DELETE CASCADE,\r\n" + 
			"    pub_location VARCHAR(25) NOT NULL,\r\n" + 
			"    pub_date DATE\r\n" + 
			"    ) ",
			"CREATE TABLE reservation(\r\n" + 
			"    isbn VARCHAR(15) REFERENCES book(isbn) ON DELETE CASCADE,\r\n" + 
			"    mem_id VARCHAR(25) REFERENCES member(mem_id) ON DELETE CASCADE\r\n" + 
			"    ) " + 
			"    ",
			"CREATE TABLE borrows(\r\n" + 
			"    b_date DATE NOT NULL,\r\n" + 
			"    d_date DATE NOT NULL,\r\n" + 
			"    r_date DATE NOT NULL\r\n" + 
			"    ) ",
			"CREATE TABLE fine(\r\n" + 
			"    mem_id VARCHAR(25) REFERENCES member(mem_id) ON DELETE CASCADE,\r\n" + 
			"    isbn VARCHAR(25) REFERENCES book(isbn) ON DELETE CASCADE,\r\n" + 
			"    f_rate NUMBER(5,2),\r\n" + 
			"    /* r_date DATE, */\r\n" + 
			"    /* d_date DATE NOT NULL, */\r\n" + 
			"    f_amount FLOAT \r\n" + 
			"    ) ",
			"CREATE TABLE bookClub(\r\n" + 
			"    start_date DATE,\r\n" + 
			"    end_date DATE,\r\n" + 
			"    mem_name VARCHAR(25) NOT NULL,\r\n" + 
			"    b_title VARCHAR(25),\r\n" + 
			"    meeting_date VARCHAR(25)\r\n" + 
			"    )"}; 
	public String[] tempInsert = 
			{" member(mem_id, mem_name, mem_age) " , " (123, 'John', 25) " , 
			" member(mem_id, mem_name, mem_age) " , " (456, 'Sue', 13) " , 
			" member(mem_id, mem_name, mem_age) " , " (789, 'Mary', 65) " , 
			" member(mem_id, mem_name, mem_age) " , " (101, 'Albert', 5) " , 
			" member(mem_id, mem_name, mem_age) " , " (098, 'Jim', NULL) " , 
			" publisher(pub_name, pub_location) " , " ('Scholastic','New York') " , 
			" publisher(pub_name, pub_location) " , " ('Gollancz','Moscow') " , 
			" publisher(pub_name, pub_location) " , " ('Bantam Spectra','New York') " , 
			" publisher(pub_name, pub_location) " , " ('George Allen Unwin','London') " , 
			" book(isbn, b_author, b_title, b_genre, b_count, b_minAge, pub_name, pub_location, pub_date) " , " (1111, 'Edith Wharton', 'House of Mirth', 'novel', 5, 0,'Scholastic','New York',TO_DATE('1905/10/14','yyyy/mm/dd')) " , 
			" book(isbn, b_author, b_title, b_genre, b_count, b_minAge, pub_name, pub_location, pub_date) " , " (2222, 'Dmitry Glukhovsky', 'Metro 2033', 'science fiction novel', 2, 13,'Gollancz','Moscow',TO_DATE('2010/03/18','yyyy/mm/dd')) " , 
			" book(isbn, b_author, b_title, b_genre, b_count, b_minAge, pub_name, pub_location, pub_date) " , " (3333, 'J.R.R Toilken', 'The Hobbit', 'fantasy novel', 10, 0,'George Allen Unwin','London',TO_DATE('1937/09/21','yyyy/mm/dd')) " , 
			" book(isbn, b_author, b_title, b_genre, b_count, b_minAge, pub_name, pub_location, pub_date) " , " (4444, 'George R. R. Martin', 'A Game of Thrones', 'fantasy novel', 3, 0,'Scholastic','New York',TO_DATE('1996/08/01','yyyy/mm/dd')) " , 
			" fine(mem_id, f_rate, f_amount) " , " (123, 1.025, 143.00) " , 
			" fine(mem_id, f_rate, f_amount) " , " (456, 1.025, 0.00) " , 
			" bookClub(mem_name, b_title, start_date, end_date, meeting_date) " , " ('John', 'The Life of Pie', TO_DATE('2018/06/12','YYYY/MM/DD'), TO_DATE('2019/06/14','YYYY/MM/DD'), 'THU') " , 
			" bookClub(mem_name, b_title, start_date, end_date, meeting_date) " , " ('Sue', 'The Hobbit', TO_DATE('2019/12/01', 'YYYY/MM/DD'), TO_DATE('2021/01/23','YYYY/MM/DD'), 'WED') " , 
			" bookClub(mem_name, b_title, start_date, end_date, meeting_date) " , " ('Mary', 'The Hobbit', TO_DATE('2020/03/20','YYYY/MM/DD'), TO_DATE('2022/09/19','YYYY/MM/DD'), 'FRI') " , 
			" bookClub(mem_name, b_title, start_date, end_date, meeting_date) " , " ('Albert', 'The Hobbit', TO_DATE('2019/10/10', 'YYYY/MM/DD'), TO_DATE('2020/06/09','YYYY/MM/DD'), 'SUN') "};
	
			public List<String> insertTables = Arrays.asList(tempInsert);
	
	 public String query1 = "SELECT *\r\n" + 
	 		"FROM book\r\n" + 
	 		"INNER JOIN publisher ON\r\n" + 
	 		"	publisher.pub_location = book.pub_location ";
	 
	 public String query2 = "SELECT publisher.pub_name, COUNT(book.pub_name)\r\n" + 
	 		"FROM book,publisher\r\n" + 
	 		"WHERE book.pub_name = publisher.pub_name\r\n" + 
	 		"AND book.pub_location = publisher.pub_location\r\n" + 
	 		"GROUP BY publisher.pub_name ";
	 
	 public String query3 = "SELECT AVG(member.mem_age) AS AverageAgeOfTheHobbitBookClub\r\n" + 
	 		"FROM bookClub, member\r\n" + 
	 		"WHERE bookClub.b_title = 'The Hobbit'\r\n" + 
	 		"AND bookClub.mem_name = member.mem_name ";
	 
	 public String query4 = "SELECT member.mem_name AS SafeMembers\r\n" + 
	 		"FROM member\r\n" + 
	 		"	WHERE NOT EXISTS\r\n" + 
	 		"	(SELECT *\r\n" + 
	 		" 	FROM fine\r\n" + 
	 		" 	WHERE fine.mem_id = member.mem_id) ";
	 
	 public String query5 = "SELECT pub_location, COUNT(pub_location)\r\n" + 
	 		"FROM book\r\n" + 
	 		"GROUP BY pub_location\r\n" + 
	 		"ORDER BY pub_location ";
	 
	 public String query6 = "SELECT mem_name, mem_age\r\n" + 
	 		"FROM member\r\n" + 
	 		"WHERE mem_age BETWEEN 13 AND 18 ";
	 
	 public String query7 = "SELECT SUM(f_amount), MAX(f_amount), MIN(f_amount) AS FineInfo\r\n" + 
	 		"FROM fine ";
	 
			
	/**
	 * @wbp.parser.entryPoint
	 */
	public Assignment9() {
		initialize();
	}
    /**
     * @wbp.parser.entryPoint
     */
    public static void main(String[] args) {
    	
    	EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Assignment9 window = new Assignment9();
					window.frmJdbc.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
 
        
        }	
        
    public void sqlStatement(String operation) {
    	Connection conn1 = null;

        try {
            Class.forName("oracle.jdbc.OracleDriver");		
			conn1 = DriverManager.getConnection(dbURL1);
            if (conn1 != null) {
                textArea.append("Connected with connection #1 \n");
            }
            
            
			try (Statement stmt = conn1.createStatement()) {
			
			if (operation.equals("drop")) {
				for (int i = 0; i < tables.length; i++) {
					query = "DROP TABLE " + tables[i] + " "; 
					stmt.executeUpdate(query);
					textArea.append("Dropped table: " + tables[i] + " \n");
				}
			}
			
			else if (operation.equals("create")) {
				for (int i = 0; i < createTables.length; i++) {
					query = " " + createTables[i] + " ";
					stmt.executeUpdate(query);
					textArea.append("Created Table: " + tables[i] + " \n");
				}
			}
			
			else if (operation.equals("insert")) {
				for (int i = 0; i < insertTables.size(); i += 2) {
					query = "INSERT INTO " + insertTables.get(i) + " VALUES" + insertTables.get(i+1) + " " ;
					stmt.executeUpdate(query);
					textArea.append("Inserted " + insertTables.get(i) + " with values: " + insertTables.get(i+1) + " \n");
				}
			}
			
		
			
			} catch (SQLException e) {
				textArea.setText("");
				textArea.append("SQLException Error" + e.getErrorCode());
			}
			
        } catch (ClassNotFoundException ex) {
        	ex.printStackTrace(); 
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn1 != null && !conn1.isClosed()) {
                    conn1.close();
                }
     
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    
    public void insertMember(String mname, int age, int id) {
    	Connection conn1 = null;

        try {
            Class.forName("oracle.jdbc.OracleDriver");
			
			conn1 = DriverManager.getConnection(dbURL1);
            if (conn1 != null) {
                //textArea.append("Connected with connection #1 \n");
            }
            
            
			try (PreparedStatement pstmt = conn1.prepareStatement(" INSERT INTO MEMBER (mem_id, mem_name, mem_age) VALUES (?, ?, ?) ")) {
			pstmt.setString(2, mname);
			pstmt.setInt(1, id);
			pstmt.setInt(3, age);
			query = " INSERT INTO MEMBER (mem_id, mem_name, mem_age) VALUES (" + id + ", '" + mname + "', " + age + ") ";
			pstmt.executeUpdate();
			//stmt.executeUpdate(query);
			textArea.append("Added member " + mname + " to database \n");
			lblId.setText("ID: " + id);
			
			} catch (SQLException e) {
				textArea.setText("");
				textArea.append("SQLException Error" + e.getErrorCode());
			}
			
        } catch (ClassNotFoundException ex) {
        	ex.printStackTrace(); 
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn1 != null && !conn1.isClosed()) {
                    conn1.close();
                }
     
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
     
    /**
     * @wbp.parser.entryPoint
     */
    public void initialize() {
    	frmJdbc = new JFrame();
    	frmJdbc.setTitle("Library Database");
		frmJdbc.setBounds(100, 100, 800, 600);
		frmJdbc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmJdbc.getContentPane().setLayout(null);
		
		JButton btnDropTables = new JButton("Drop Tables");
		btnDropTables.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			textArea.setText("");
			sqlStatement("drop");
			}
		});
		btnDropTables.setBounds(15, 16, 145, 145);
		frmJdbc.getContentPane().add(btnDropTables);
	
		JButton btnCreateTables = new JButton("Create Tables");
		btnCreateTables.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					textArea.setText("");
					sqlStatement("create");
				
			}
		});
		btnCreateTables.setBounds(175, 16, 145, 145);
		frmJdbc.getContentPane().add(btnCreateTables);
		
		JButton btnPopulateTables = new JButton("Populate Tables");
		btnPopulateTables.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
				sqlStatement("insert");
			}
		});
		
		btnPopulateTables.setBounds(335, 16, 145, 145);
		frmJdbc.getContentPane().add(btnPopulateTables);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(15, 210, 461, 145);
		frmJdbc.getContentPane().add(scrollPane_1);
		
		textArea = new JTextArea();
		scrollPane_1.setViewportView(textArea);
		
		JLabel lblConsole = new JLabel("Console:");
		lblConsole.setBounds(15, 177, 69, 20);
		frmJdbc.getContentPane().add(lblConsole);
		
		JLabel lblInsertIntoTable = new JLabel("Add new member:");
		lblInsertIntoTable.setBounds(495, 210, 134, 20);
		frmJdbc.getContentPane().add(lblInsertIntoTable);
		
		txtName = new JTextField();
		txtName.setBounds(617, 252, 146, 26);
		frmJdbc.getContentPane().add(txtName);
		txtName.setColumns(10);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(495, 255, 69, 20);
		frmJdbc.getContentPane().add(lblName);
		
		JLabel lblAge = new JLabel("Age:");
		lblAge.setBounds(495, 314, 69, 20);
		frmJdbc.getContentPane().add(lblAge);
		
		lblId = new JLabel("ID:");
		lblId.setBounds(495, 371, 268, 20);
		frmJdbc.getContentPane().add(lblId);
		
		textField_1 = new JTextField();
		textField_1.setBounds(617, 311, 146, 26);
		frmJdbc.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setEnabled(false);
		lblNewLabel.setBounds(495, 452, 268, 20);
		frmJdbc.getContentPane().add(lblNewLabel);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				memberName = txtName.getText();
				try {
				memberAge = Integer.parseInt(textField_1.getText()); }
				catch (NumberFormatException nfe) {
					System.out.println(nfe.getMessage());
				}
				memberID = memberID + 1;
				if (memberName.equals("")) {
					lblNewLabel.setText("Please enter a valid name");
				}
				else {
					insertMember(memberName, memberAge, memberID);
					lblNewLabel.setText("Added new member");
				}
			}
		});
		btnSubmit.setBounds(495, 407, 115, 29);
		frmJdbc.getContentPane().add(btnSubmit);
		
		JLabel lblLibrary = new JLabel("Library ");
		lblLibrary.setFont(new Font("Corbel Light", Font.BOLD | Font.ITALIC, 28));
		lblLibrary.setBounds(533, 39, 101, 50);
		frmJdbc.getContentPane().add(lblLibrary);
		
		JLabel lblDatabase = new JLabel("Database");
		lblDatabase.setFont(new Font("Corbel Light", Font.BOLD | Font.ITALIC, 28));
		lblDatabase.setBounds(560, 80, 203, 56);
		frmJdbc.getContentPane().add(lblDatabase);
		
		
    }
}