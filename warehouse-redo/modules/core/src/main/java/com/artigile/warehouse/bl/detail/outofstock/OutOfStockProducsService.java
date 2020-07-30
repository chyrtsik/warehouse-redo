package com.artigile.warehouse.bl.detail.outofstock;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.utils.dto.details.outofstock.OutOfStockProductTO;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.ReturningWork;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for calculating out of stock items.
 */
@Transactional(rollbackFor = BusinessException.class)
public class OutOfStockProducsService {

    private SessionFactory sessionFactory;

    public OutOfStockProducsService(){
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<OutOfStockProductTO> getProductsToBeOutOfStockSoon() {
        return sessionFactory.getCurrentSession().doReturningWork(new ReturningWork<List<OutOfStockProductTO>>() {
            @Override
            public List<OutOfStockProductTO> execute(Connection connection) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = connection.prepareStatement(STOCK_DEPLETING_QUERY);
                    rs = ps.executeQuery();
                    List<OutOfStockProductTO> results = new ArrayList<>();
                    while (rs.next()){
                        results.add(new OutOfStockProductTO(
                                rs.getLong("product_id"),
                                rs.getString("product_name"),
                                rs.getLong("available"),
                                rs.getDouble("enough_for_months"),
                                rs.getLong("count_to_order"),
                                rs.getLong("ordered")
                        ));
                    }
                    return results;
                } finally {
                    if (rs != null ) rs.close();
                    if (ps != null)  ps.close();
                }
            }
        });
    }

        /*
    select product_id, product_name, available, enough_for_months, need_next_month * 3 as count_to_order, ordered from
(
	select
		d.product_id,
		d.product_name,
		p6.cnt as part_6_months,
		coalesce(p1.cnt, 0) as past_month,
		ceil((coalesce(p1.cnt, 0)-p6.cnt)/6*7+p6.cnt) as need_next_month,
		d.available as available,
		round(d.available / ceil((coalesce(p1.cnt, 0)-p6.cnt)/6*7+p6.cnt),1) as enough_for_months,
		o.ordered
	from
	(
		select db.id as product_id, db.name as product_name, sum(wb.amount - wb.reservedCount) as available
		from detailbatch db, warehousebatch wb
		where db.id = wb.detailBatch_id
		group by db.id, db.name
	) d
	join
	(
		SELECT oi.detailBatch_id as product_id, sum(oi.amount) / 6 as cnt
		FROM warehouse.orderlist o, warehouse.orderitem oi
		where o.id = oi.order_id
		  and not oi.deleted
		  and datediff(now(), o.createDate) <= 180
		  group by oi.detailBatch_id
	) p6 on d.product_id = p6.product_id
	left join
	(
		SELECT oi.detailBatch_id as product_id, sum(oi.amount) as cnt
		FROM warehouse.orderlist o, warehouse.orderitem oi
		where o.id = oi.order_id
		  and not oi.deleted
		  and datediff(now(), o.createDate) <= 30
		  group by oi.detailBatch_id
	) p1 on p6.product_id = p1.product_id
	left join
	(
		SELECT wni.detailBatch_id as product_id, sum(pi.amount) as ordered
		FROM warehouse.purchase p, warehouse.purchaseitem pi, warehouse.wareneeditem wni
		where p.state in ('WAITING', 'SHIPPED', 'IN_POSTING')
		  and p.id = pi.purchase_id
		  and pi.wareNeedItem_id = wni.id
		group by wni.detailBatch_id
	) o on d.product_id = o.product_id
) s
where enough_for_months is not null
  and enough_for_months between 0 and 6
order by enough_for_months
;


     */

    private final String STOCK_DEPLETING_QUERY = "select product_id, product_name, available, enough_for_months, need_next_month * 3 as count_to_order, ordered from \n" +
            "(\n" +
            "\tselect \n" +
            "\t\td.product_id, \n" +
            "\t\td.product_name, \n" +
            "\t\tp6.cnt as part_6_months, \n" +
            "\t\tcoalesce(p1.cnt, 0) as past_month,\n" +
            "\t\tceil((coalesce(p1.cnt, 0)-p6.cnt)/6*7+p6.cnt) as need_next_month,\n" +
            "\t\td.available as available,\n" +
            "\t\tround(d.available / ceil((coalesce(p1.cnt, 0)-p6.cnt)/6*7+p6.cnt),1) as enough_for_months,\n" +
            "\t\to.ordered\n" +
            "\tfrom \n" +
            "\t(\n" +
            "\t\tselect db.id as product_id, db.name as product_name, sum(wb.amount - wb.reservedCount) as available \n" +
            "\t\tfrom detailbatch db, warehousebatch wb \n" +
            "\t\twhere db.id = wb.detailBatch_id\n" +
            "\t\tgroup by db.id, db.name\n" +
            "\t) d \n" +
            "\tjoin\n" +
            "\t(\n" +
            "\t\tSELECT oi.detailBatch_id as product_id, sum(oi.amount) / 6 as cnt\n" +
            "\t\tFROM warehouse.orderlist o, warehouse.orderitem oi\n" +
            "\t\twhere o.id = oi.order_id\n" +
            "\t\t  and not oi.deleted\n" +
            "\t\t  and datediff(now(), o.createDate) <= 180\n" +
            "\t\t  group by oi.detailBatch_id\n" +
            "\t) p6 on d.product_id = p6.product_id\n" +
            "\tleft join \n" +
            "\t(\n" +
            "\t\tSELECT oi.detailBatch_id as product_id, sum(oi.amount) as cnt\n" +
            "\t\tFROM warehouse.orderlist o, warehouse.orderitem oi\n" +
            "\t\twhere o.id = oi.order_id\n" +
            "\t\t  and not oi.deleted\n" +
            "\t\t  and datediff(now(), o.createDate) <= 30\n" +
            "\t\t  group by oi.detailBatch_id\n" +
            "\t) p1 on p6.product_id = p1.product_id\n" +
            "\tleft join \n" +
            "\t(\n" +
            "\t\tSELECT wni.detailBatch_id as product_id, sum(pi.amount) as ordered\n" +
            "\t\tFROM warehouse.purchase p, warehouse.purchaseitem pi, warehouse.wareneeditem wni\n" +
            "\t\twhere p.state in ('WAITING', 'SHIPPED', 'IN_POSTING') \n" +
            "\t\t  and p.id = pi.purchase_id \n" +
            "\t\t  and pi.wareNeedItem_id = wni.id\n" +
            "\t\tgroup by wni.detailBatch_id\n" +
            "\t) o on d.product_id = o.product_id\n" +
            ") s\n" +
            "where enough_for_months is not null \n" +
            "and enough_for_months between 0 and 6\n" +
            "order by enough_for_months\n" +
            ";\n";

}
