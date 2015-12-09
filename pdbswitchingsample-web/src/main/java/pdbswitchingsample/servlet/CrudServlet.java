package pdbswitchingsample.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class PDBSwitchSampleServlet
 */
@WebServlet("/crudservlet")
public class CrudServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CrudServlet() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void init() {
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String jdbc_jndi = "jdbc/appjdbc";
		InitialContext ctx;
		Connection conn = null;

		String ope = request.getParameter("operation");
		String out = "";
		String records = "";
		
		try {
			ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup(jdbc_jndi);
//			conn = ((WLDataSource)ds).getConnection(connectionLabel);
			conn = ds.getConnection();

			switch (ope) {
			case "create":
				createtable(conn);
				out = "successfully created table mytable";
				break;
			case "insert":
				inserttable(conn, request.getParameter("data"));
				out = "successfully inserted into table mytable";
				break;

			case "drop":
				droptable(conn);
				out = "successfully dropped table mytable";
				break;

			case "select":
				records = selecttable(conn);
				System.out.println("successfully selected table mytable");
				out = "successfully selected table mytable<br/>" + out;
				break;

			default:
				break;
			}

		} catch (NamingException e1) {
			e1.printStackTrace();
			out = e1.getStackTrace().toString();
		} catch (SQLException e) {
			e.printStackTrace();
			out = e.getStackTrace().toString();
		} finally {
			try {
				conn.close();
				System.out.println("connection closed");
			} catch (SQLException e) {
				e.printStackTrace();
				out = e.getStackTrace().toString();
			}
		}
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		writer.println("<html>");
		writer.println("<head><title>pdbswitching</title></head>");
		writer.println("<body>");
		writer.println("<p>" + out + "</p>");
		writer.println("<p id=\"records\">" + records + "</p>");
		writer.println("<a href=\"index.html\">Go back to index page</a>");
		writer.println("</html>");
		writer.close();
	}

	private void createtable(Connection conn) throws SQLException {
		String sql = "create table mytable ( data varchar2(100) )";

		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
		} finally {
			ps.close();
		}
	}

	private void droptable(Connection conn) throws SQLException {
		String sql = "drop table mytable";

		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
		} finally {
			ps.close();
		}
	}

	private void inserttable(Connection conn, String data) throws SQLException {
		String sql = "insert into mytable ( data ) values ('" + data + "')";

		PreparedStatement ps = null;
		int cnt = 0;
		try {
			ps = conn.prepareStatement(sql);
			cnt = ps.executeUpdate();
		} finally {
			ps.close();
		}
	}

	private String selecttable(Connection conn) throws SQLException {
		String sql = "select data from mytable";
		StringBuffer data = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				data.append(rs.getString("data") + "<br/>");
			}
		} finally {
			rs.close();
			ps.close();
		}
		return data.toString();
	}

}
