package vn.topwines.categories.entity;

import lombok.Getter;
import lombok.Setter;
import vn.topwines.core.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String code;
    private String keywords;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "text")
    private String image;

    @Column(name = "seo_title")
    private String seoTitle;

    @Column(name = "seo_description", columnDefinition = "text")
    private String seoDescription;

    @Column(name = "robot_tag")
    private String robotTag;

    @Column(name = "canonical", columnDefinition = "text")
    private String canonical;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Category parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Category> children;
}
