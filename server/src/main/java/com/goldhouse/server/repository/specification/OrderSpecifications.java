package com.goldhouse.server.repository.specification;

import com.goldhouse.server.model.Customer;
import com.goldhouse.server.model.Order;
import com.goldhouse.server.model.OrderStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class OrderSpecifications {

    public static Specification<Order> filterOrders(String orderId, String customerName, Long phoneNumber, OrderStatus orderStatus) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by Order ID (Exact or Like match - using Exact match since IDs are usually specific)
            if (StringUtils.hasText(orderId)) {
                predicates.add(criteriaBuilder.equal(root.get("id"), orderId));
            }

            // Filter by Order Status
            if (orderStatus != null) {
                predicates.add(criteriaBuilder.equal(root.get("orderStatus"), orderStatus));
            }

            // Join with Customer for customer filters
            if (StringUtils.hasText(customerName) || phoneNumber != null) {
                Join<Order, Customer> customerJoin = root.join("customer");

                if (StringUtils.hasText(customerName)) {
                    // Case-insensitive partial match for name
                    predicates.add(criteriaBuilder.like(
                            criteriaBuilder.lower(customerJoin.get("name")),
                            "%" + customerName.toLowerCase() + "%"
                    ));
                }

                if (phoneNumber != null) {
                    predicates.add(criteriaBuilder.equal(customerJoin.get("phoneNumber"), phoneNumber));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}