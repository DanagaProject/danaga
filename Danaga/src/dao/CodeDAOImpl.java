package dao;

import dto.Code;
import exception.DatabaseException;
import util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 공통 코드 DAO 구현체
 */
public class CodeDAOImpl implements CodeDAO {

    private static CodeDAO instance = new CodeDAOImpl();

    private CodeDAOImpl() {
    }

    public static CodeDAO getInstance() {
        return instance;
    }

    /**
     * ResultSet -> Code 객체 변환 (공통)
     */
    private Code mapResultSet(ResultSet rs) throws SQLException {
        return new Code(
                rs.getInt("code_id"),
                rs.getString("name"),
                rs.getString("created_at"),
                rs.getString("group_name")
        );
    }

    /**
     * 전체 코드 목록 조회
     */
    @Override
    public List<Code> selectAll() throws DatabaseException {
        String sql = "SELECT code_id, name, created_at, group_name FROM code ORDER BY code_id";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Code> list = new ArrayList<>();

        try {
            con = DBUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }

            return list;

        } catch (SQLException e) {
            throw new DatabaseException("코드 전체 조회 중 DB 오류 발생: " + e.getMessage());
        } finally {
            DBUtil.close(con, pstmt, rs);
        }
    }

    /**
     * 그룹명으로 코드 목록 조회
     * - 예) "item_condition" -> 상(1), 중(2), 하(3)
     */
    @Override
    public List<Code> selectByGroupName(String groupName) throws DatabaseException {
        String sql = "SELECT code_id, name, created_at, group_name FROM code WHERE group_name = ? ORDER BY code_id";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Code> list = new ArrayList<>();

        try {
            con = DBUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, groupName);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }

            return list;

        } catch (SQLException e) {
            throw new DatabaseException("그룹별 코드 조회 중 DB 오류 발생: " + e.getMessage());
        } finally {
            DBUtil.close(con, pstmt, rs);
        }
    }

    /**
     * 코드 ID로 단건 조회
     */
    @Override
    public Code selectByCodeId(int codeId) throws DatabaseException {
        String sql = "SELECT code_id, name, created_at, group_name FROM code WHERE code_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, codeId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }

            return null;

        } catch (SQLException e) {
            throw new DatabaseException("코드 ID 조회 중 DB 오류 발생: " + e.getMessage());
        } finally {
            DBUtil.close(con, pstmt, rs);
        }
    }
}
