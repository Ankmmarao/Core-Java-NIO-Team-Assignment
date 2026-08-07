package com.iispl.daoimpl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.iispl.dao.AccountDAO;
import com.iispl.enums.AccountStatus;
import com.iispl.enums.AccountType;
import com.iispl.model.Account;

public class AccountDAOImpl implements AccountDAO {

	public static final String CHECK_ACCOUNT = "select accountnumber from Account where Acchount=?";
	public static final String DEBIT_ACCOUNT = "UPDATE Account SET balance = balance - ? WHERE accountnumber = ?";

	public static final String CREDIT_ACCOUNT = "UPDATE Account SET balance = balance + ? WHERE accountnumber = ?";

	@Override
	public boolean isAccountExist(Connection con, String accountNumber) throws Exception {

		try (PreparedStatement stmt = con.prepareStatement(CHECK_ACCOUNT)) {
			stmt.setString(1, accountNumber);

			ResultSet rs = stmt.executeQuery();

			return rs.next();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;

	}

	public static final String GET_ACCOUNT = "select * from account where accountNumber = ?";

	@Override
	public Account getAccount(Connection con, String accountNumber) throws Exception {

		try (PreparedStatement stmt = con.prepareStatement(GET_ACCOUNT)) {

			stmt.setString(1, accountNumber);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {

				String customerName = rs.getString("customerName");

				String accNumber = rs.getString("accountNumber");

				AccountType accountType = AccountType.valueOf(rs.getString("accountType"));

				BigDecimal accountmoney = rs.getBigDecimal("accountMoney");

				AccountStatus accountStatus = AccountStatus.valueOf(rs.getString("accountStatus"));

				return new Account(customerName, accNumber, accountType, accountmoney, accountStatus);

			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	public static final String UPDATE_BALANCE = "update account set accountMoney = ? where accountNumber = ?";

	@Override
	public boolean updateBalance(Connection con, String accountNumber, BigDecimal acccountMoney) throws Exception {
		
		try(PreparedStatement stmt = con.prepareStatement(UPDATE_BALANCE))
				{
					stmt.setBigDecimal(1, acccountMoney);
					stmt.setString(1, accountNumber);
					
					int rowUpdated  = stmt.executeUpdate();
						
						if(rowUpdated > 0 ) {
							System.out.println("Updated successfully");
							return true;
						}else {
							System.out.println("Updated failed");
						}
						
				}
				catch(SQLException e) {
					System.out.println(e.getMessage());
				}
		
		
		return false;
	}

	@Override
	public boolean debitAccount(Connection con, String accountNumber,BigDecimal amount) throws Exception {

	    PreparedStatement ps =
	            con.prepareStatement(DEBIT_ACCOUNT);

	    ps.setBigDecimal(1, amount);
	    ps.setString(2, accountNumber);

	    int rows = ps.executeUpdate();

	    return rows > 0;
	}

	@Override
	public boolean creditAccount(Connection con, String accountNumber, BigDecimal amount) throws Exception {
		PreparedStatement ps=con.prepareStatement(CREDIT_ACCOUNT);
		ps.setBigDecimal(1, amount);
		ps.setString(2, accountNumber);
		int rows =ps.executeUpdate();
		
		// TODO Auto-generated method stub
		return rows>0;
	}
}
