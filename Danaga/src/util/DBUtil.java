package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 데이터베이스 연결 및 리소스 관리 유틸리티
 */
public class DBUtil {
    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    // static 초기화 블록: 클래스 로딩 시 db.properties 읽기
    static {
        try (InputStream input = DBUtil.class.getClassLoader()
                .getResourceAsStream("config/db.properties")) {

            if (input == null) {
                throw new IOException("db.properties 파일을 찾을 수 없습니다.");
            }

            Properties props = new Properties();
            props.load(input);

            driver = props.getProperty("driver");
            url = props.getProperty("url");
            username = props.getProperty("username");
            password = props.getProperty("password");

            // JDBC 드라이버 로딩
            Class.forName(driver);

            System.out.println("[DBUtil] DB 설정 로드 완료");

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[DBUtil] DB 설정 로드 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * DB 연결 얻기
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * 리소스 정리 (ResultSet, PreparedStatement, Connection)
     */
    public static void close(Connection con, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (pstmt != null)
                pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (con != null)
                con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 리소스 정리 (PreparedStatement, Connection만)
     */
    public static void close(Connection con, PreparedStatement pstmt) {
        close(con, pstmt, null);
    }

    /**
     * 리소스 정리 (Connection만)
     */
    public static void close(Connection con) {
        close(con, null, null);
    }

    /**
     * 트랜잭션 커밋
     */
    public static void commit(Connection con) {
        try {
            if (con != null && !con.isClosed()) {
                con.commit();
            }
        } catch (SQLException e) {
            System.err.println("[DBUtil] 커밋 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 트랜잭션 롤백
     */
    public static void rollback(Connection con) {
        try {
            if (con != null && !con.isClosed()) {
                con.rollback();
                System.out.println("[DBUtil] 트랜잭션 롤백 완료");
            }
        } catch (SQLException e) {
            System.err.println("[DBUtil] 롤백 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
