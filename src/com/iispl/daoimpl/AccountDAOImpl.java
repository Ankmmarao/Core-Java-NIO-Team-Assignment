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

    private static final String CHECK_ACCOUNT =
            "SELECT account_number FROM account WHERE account_number = ?";

    private static final String GET_ACCOUNT =
            "SELECT * FROM account WHERE account_number = ?";

    private static final String UPDATE_BALANCE =
            "UPDATE account SET account_money = ? WHERE account_number = ?";

    private static final String DEBIT_ACCOUNT =
            "UPDATE account SET account_money = account_money - ? WHERE account_number = ?";

    private static final String CREDIT_ACCOUNT =
            "UPDATE account SET account_money = account_money + ? WHERE account_number = ?";

    @Override
    public boolean isAccountExist(Connection con, String accountNumber) throws Exception {

        try (PreparedStatement stmt = con.prepareStatement(CHECK_ACCOUNT)) {

            stmt.setString(1, accountNumber);

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public Account getAccount(Connection con, String accountNumber) throws Exception {

        try (PreparedStatement stmt = con.prepareStatement(GET_ACCOUNT)) {

            stmt.setString(1, accountNumber);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                String customerName = rs.getString("customer_name");

                String accNumber = rs.getString("account_number");

                AccountType accountType =
                        AccountType.valueOf(rs.getString("account_type"));

                BigDecimal accountMoney =
                        rs.getBigDecimal("account_money");

                AccountStatus accountStatus =
                        AccountStatus.valueOf(rs.getString("account_status"));

                return new Account(
                        customerName,
                        accNumber,
                        accountType,
                        accountMoney,
                        accountStatus);
            }

        } catch (SQLException e) {
            throw e;
        }

        return null;
    }

    @Override
    public boolean updateBalance(Connection con,
                                 String accountNumber,
                                 BigDecimal accountMoney) throws Exception {

        try (PreparedStatement stmt =
                     con.prepareStatement(UPDATE_BALANCE)) {

            stmt.setBigDecimal(1, accountMoney);
            stmt.setString(2, accountNumber);

            int rows = stmt.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public boolean debitAccount(Connection con,
                                String accountNumber,
                                BigDecimal amount) throws Exception {

        try (PreparedStatement ps =
                     con.prepareStatement(DEBIT_ACCOUNT)) {

            ps.setBigDecimal(1, amount);
            ps.setString(2, accountNumber);

            int rows = ps.executeUpdate();

            return rows > 0;
        }
    }

    @Override
    public boolean creditAccount(Connection con,
                                 String accountNumber,
                                 BigDecimal amount) throws Exception {

        try (PreparedStatement ps =
                     con.prepareStatement(CREDIT_ACCOUNT)) {

            ps.setBigDecimal(1, amount);
            ps.setString(2, accountNumber);

            int rows = ps.executeUpdate();

            return rows > 0;
        }
    }
}