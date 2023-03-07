package vn.topwines.booking.entity;

import lombok.Getter;
import lombok.Setter;
import vn.topwines.products.entity.Product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "booking_products")
public class BookingProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "product_discount")
    private BigDecimal productDiscount;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_image", columnDefinition = "text")
    private String productImage;

    @Column(name = "product_description", columnDefinition = "text")
    private String productDescription;

    @Column(name = "product_price")
    private BigDecimal productPrice;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "product_created_year")
    private Integer productCreatedYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", referencedColumnName = "id")
    private Booking booking;
}
