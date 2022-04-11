package com.guavapay.deliveryservice.entity;


import com.guavapay.deliveryservice.enumeration.DeliveryOrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "deliveryorders")
@Getter
@Setter
public class DeliveryOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "adress_to_id")
    private Address addressTo;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "adress_from_id")
    private Address addressFrom;

    @Column(name = "pickupDate")
    private LocalDateTime pickupDate;

    @Column(name = "deliveryDate")
    private LocalDate deliveryDate;

    @Column(name = "dateCreated")
    private LocalDateTime dateCreated;

    @Column(name = "dateUpdated")
    private LocalDateTime dateUpdated;

    @Column(name = "comment")
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DeliveryOrderStatus status;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "orderdetail_id")
    private OrderDetail orderDetail;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id")
    private User courier;

}
