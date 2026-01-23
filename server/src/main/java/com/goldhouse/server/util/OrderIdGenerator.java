package com.goldhouse.server.util;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class OrderIdGenerator extends SequenceStyleGenerator {

    public static final String prefix = "ORD";
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        Connection connection = null;
        try {
            connection = session.getJdbcConnectionAccess().obtainConnection();
            Statement statement = connection.createStatement();
            // Assuming sequence name is 'order_id_seq'. Ensure this exists in Postgres.
            ResultSet rs = statement.executeQuery("SELECT nextval('order_id_seq')");
            if (rs.next()) {
                int id = rs.getInt(1);
                return prefix + id;
            }
        } catch (Exception e) {
            // Fallback or error handling
            e.printStackTrace();
        }
        return null; // Or throw exception
    }
}
