/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.database.update.java;

import com.artigile.warehouse.utils.StringUtils;
import com.googlecode.flyway.core.migration.java.JavaMigration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Valery Barysok, 10/1/11
 */

public class V1_0_3_18__RegexpReplace implements JavaMigration {

    private static final String sqlCount = "select count(*) from ContractorProduct";
    private static final long limit = 1000L;
    private static final String sqlContractorProductList = "select id, name from ContractorProduct LIMIT ? OFFSET ?";
    private static final String sqlContractorProductUpdate = "update ContractorProduct set simplifiedName=? where id=?";

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        // retrieve total count of ContractorProduct
        long cnt = jdbcTemplate.query(sqlCount, new ResultSetExtractor<Long>() {
            @Override
            public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
                return rs.first() ? rs.getLong(1) : 0L;
            }
        });

        for (long i = 0L; i < cnt; i += limit) {
            final long offset = i;
            // retrieve next part of ContractorProducts
            final List<ContractorProduct> contractorProducts = jdbcTemplate.query(sqlContractorProductList,
                new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setLong(1, limit);
                        ps.setLong(2, offset);
                    }
                }, new RowMapper<ContractorProduct>() {
                    @Override
                    public ContractorProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new ContractorProduct(rs.getLong("id"), rs.getString("name"));
                    }
                }
            );

            // update simplifiedName for ContractorProduct
            jdbcTemplate.batchUpdate(sqlContractorProductUpdate, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ContractorProduct contractorProduct = contractorProducts.get(i);
                    ps.setString(1, StringUtils.simplifyName(contractorProduct.getName()));
                    ps.setLong(2, contractorProduct.getId());
                }

                @Override
                public int getBatchSize() {
                    return contractorProducts.size();
                }
            });
        }
    }

    private static class ContractorProduct {
        private long id;
        private String name;

        private ContractorProduct(long id, String name) {
            this.id = id;
            this.name = name;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
