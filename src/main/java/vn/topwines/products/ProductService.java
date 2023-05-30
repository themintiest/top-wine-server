package vn.topwines.products;

import io.quarkus.vertx.ConsumeEvent;
import io.vertx.core.eventbus.Message;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import vn.topwines.brands.BrandService;
import vn.topwines.brands.entity.Brand;
import vn.topwines.categories.CategoryService;
import vn.topwines.categories.domain.CategorySimple;
import vn.topwines.categories.entity.Category;
import vn.topwines.categories.mapper.CategoryMapper;
import vn.topwines.common.constant.EventCode;
import vn.topwines.core.query.PageRequest;
import vn.topwines.core.query.Pageable;
import vn.topwines.core.repository.specification.Specification;
import vn.topwines.core.utils.CollectionUtils;
import vn.topwines.core.utils.PageUtils;
import vn.topwines.exception.BadRequestException;
import vn.topwines.grapes.GrapeService;
import vn.topwines.grapes.entity.Grape;
import vn.topwines.nations.NationService;
import vn.topwines.nations.entity.Nation;
import vn.topwines.product_type.ProductTypeService;
import vn.topwines.product_type.entity.ProductType;
import vn.topwines.products.constant.UpdateQuantityType;
import vn.topwines.products.domain.CreateProductRQ;
import vn.topwines.products.domain.GetProductRQ;
import vn.topwines.products.domain.ProductDetailRS;
import vn.topwines.products.domain.ProductRS;
import vn.topwines.products.domain.ProductSimpleRS;
import vn.topwines.products.domain.UpdateProductQuantityMessage;
import vn.topwines.products.domain.UpdateProductRQ;
import vn.topwines.products.entity.Product;
import vn.topwines.products.entity.ProductCategory;
import vn.topwines.products.entity.ProductImage;
import vn.topwines.products.mappers.ProductMapper;
import vn.topwines.products.repository.ProductRepository;
import vn.topwines.products.specification.ProductSpecifications;
import vn.topwines.regions.RegionService;
import vn.topwines.regions.entity.Region;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static vn.topwines.core.utils.CodeGenerator.generateObjectCode;

@ApplicationScoped
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final RegionService regionService;
    private final GrapeService grapeService;
    private final BrandService brandService;
    private final NationService nationService;
    private final ProductTypeService productTypeService;

    @Transactional
    public ProductDetailRS createProduct(CreateProductRQ createProductRQ) {
        String code = StringUtils.isNotBlank(createProductRQ.getCode())
                ? createProductRQ.getCode()
                : generateObjectCode(createProductRQ.getName());
        if (productRepository.exists("code", code)) {
            throw new BadRequestException("Mã sản phẩm đã tồn tại");
        }
        createProductRQ.setCode(code);
        Product product = ProductMapper.INSTANCE.mapCreateProductRQToProduct(createProductRQ);
        if (CollectionUtils.isNotEmpty(createProductRQ.getImages())) {
            List<ProductImage> productImages = new ArrayList<>(createProductRQ.getImages().size());
            for (CreateProductRQ.ProductImage image : createProductRQ.getImages()) {
                ProductImage productImage = new ProductImage();
                productImage.setProduct(product);
                productImage.setImage(image.getImage());
                productImages.add(productImage);
            }
            product.setProductImages(productImages);
        }
        if (CollectionUtils.isNotEmpty(createProductRQ.getCategoryIds())) {
            List<Category> categories = categoryService.getActiveByIdIn(createProductRQ.getCategoryIds());
            List<ProductCategory> productCategories = new ArrayList<>(categories.size());
            for (Category category : categories) {
                ProductCategory productCategory = new ProductCategory();
                productCategory.setProduct(product);
                productCategory.setCategory(category);
                productCategories.add(productCategory);
            }
            product.setProductCategories(productCategories);
        }

        if (createProductRQ.getRegionId() != null) {
            Region region = regionService.getActiveById(createProductRQ.getRegionId());
            product.setRegion(region);
        }

        if (createProductRQ.getGrapeId() != null) {
            Grape grape = grapeService.getActiveById(createProductRQ.getGrapeId());
            product.setGrape(grape);
        }

        if (createProductRQ.getBrandId() != null) {
            Brand brand = brandService.getActiveById(createProductRQ.getBrandId());
            product.setBrand(brand);
        }

        if (createProductRQ.getNationId() != null) {
            Nation nation = nationService.getActiveById(createProductRQ.getNationId());
            product.setNation(nation);
        }
        if (createProductRQ.getProductTypeId() != null) {
            ProductType productType = productTypeService.getActiveById(createProductRQ.getProductTypeId());
            product.setProductType(productType);
        }

        ProductDetailRS productDetailRS = ProductMapper.INSTANCE.mapProductToProductDetailRS(productRepository.save(product));
        List<CategorySimple> categorySimpleList = product.getProductCategories().stream()
                .map(ProductCategory::getCategory)
                .map(CategoryMapper.INSTANCE::mapFromCategoryToCategoryAdminSimple)
                .collect(Collectors.toList());
        productDetailRS.setCategories(categorySimpleList);
        return productDetailRS;
    }

    @Transactional
    public ProductDetailRS updateProduct(Long id, UpdateProductRQ updateProductRQ) {
        if (StringUtils.isNotBlank(updateProductRQ.getCode()) && productRepository.existByCodeAndIgnoreId(updateProductRQ.getCode(), id)) {
            throw new BadRequestException("Mã sản phẩm đã tồn tại");
        }
        Product product = getById(id);
        ProductMapper.INSTANCE.merge(product, updateProductRQ);
        if (updateProductRQ.getImages() != null) {
            mergeImages(updateProductRQ, product);
        }
        if (updateProductRQ.getCategoryIds() != null) {
            mergeCategories(updateProductRQ, product);
        }

        if (updateProductRQ.getRegionId() != null) {
            Region region = regionService.getActiveById(updateProductRQ.getRegionId());
            product.setRegion(region);
        } else {
            product.setRegion(null);
        }

        if (updateProductRQ.getGrapeId() != null) {
            Grape grape = grapeService.getActiveById(updateProductRQ.getGrapeId());
            product.setGrape(grape);
        } else {
            product.setGrape(null);
        }

        if (updateProductRQ.getBrandId() != null) {
            Brand brand = brandService.getActiveById(updateProductRQ.getBrandId());
            product.setBrand(brand);
        } else {
            product.setBrand(null);
        }

        if (updateProductRQ.getNationId() != null) {
            Nation nation = nationService.getActiveById(updateProductRQ.getNationId());
            product.setNation(nation);
        } else {
            product.setNation(null);
        }

        if (updateProductRQ.getProductTypeId() != null) {
            ProductType productType = productTypeService.getActiveById(updateProductRQ.getNationId());
            product.setProductType(productType);
        } else {
            product.setProductType(null);
        }

        ProductDetailRS productDetailRS = ProductMapper.INSTANCE.mapProductToProductDetailRS(productRepository.save(product));
        List<CategorySimple> categorySimpleList = product.getProductCategories().stream()
                .map(ProductCategory::getCategory)
                .map(CategoryMapper.INSTANCE::mapFromCategoryToCategoryAdminSimple)
                .collect(Collectors.toList());
        productDetailRS.setCategories(categorySimpleList);
        return productDetailRS;
    }

    public void deleteProduct(Long id) {
        Product product = getById(id);
        product.setIsDeleted(true);
        productRepository.save(product);
    }

    public Pageable<ProductRS> getProducts(GetProductRQ getProductRQ) {
        PageRequest pageRequest = PageUtils.createPageRequest(getProductRQ);
        Specification<Product> specification = ProductSpecifications.builder()
                .isDeleted(getProductRQ.getIsDeleted())
                .withNameLike(getProductRQ.getName())
                .withProductCode(getProductRQ.getProductCode())
                .withCode(getProductRQ.getCode())
                .withCategoryId(getProductRQ.getCategoryId())
                .withRegionId(getProductRQ.getRegionId())
                .withGrapeId(getProductRQ.getGrapeId())
                .withBrandId(getProductRQ.getBrandId())
                .withNationId(getProductRQ.getNationId())
                .withMinPrice(getProductRQ.getMinPrice())
                .withMaxPrice(getProductRQ.getMaxPrice())
                .build();
        Pageable<Product> page = productRepository.findAll(specification, pageRequest);
        return PageUtils.createPageResponse(page, ProductMapper.INSTANCE::mapProductToProductRS);
    }

    public Pageable<ProductSimpleRS> getActiveProducts(GetProductRQ getProductRQ) {
        PageRequest pageRequest = PageUtils.createPageRequest(getProductRQ);
        Specification<Product> specification = ProductSpecifications.builder()
                .isDeleted(false)
                .withNameLike(getProductRQ.getName())
                .withCode(getProductRQ.getCode())
                .withProductCode(getProductRQ.getProductCode())
                .withCategoryId(getProductRQ.getCategoryId())
                .withRegionId(getProductRQ.getRegionId())
                .withGrapeId(getProductRQ.getGrapeId())
                .withBrandId(getProductRQ.getBrandId())
                .withNationId(getProductRQ.getNationId())
                .withMinPrice(getProductRQ.getMinPrice())
                .withMaxPrice(getProductRQ.getMaxPrice())
                .build();
        Pageable<Product> page = productRepository.findAll(specification, pageRequest);
        return PageUtils.createPageResponse(page, ProductMapper.INSTANCE::mapProductToProductSimpleRS);
    }

    public ProductDetailRS getProductDetailById(Long id) {
        Product product = getById(id);
        ProductDetailRS productDetailRS = ProductMapper.INSTANCE.mapProductToProductDetailRS(product);
        List<CategorySimple> categorySimpleList = product.getProductCategories().stream()
                .map(ProductCategory::getCategory)
                .map(CategoryMapper.INSTANCE::mapFromCategoryToCategoryAdminSimple)
                .collect(Collectors.toList());
        productDetailRS.setCategories(categorySimpleList);
        return productDetailRS;
    }

    public ProductDetailRS getProductDetailByCode(String code) {
        Product product = getByCodeAndIsDeleted(code, false);
        ProductDetailRS productDetailRS = ProductMapper.INSTANCE.mapProductToProductDetailRS(product);
        List<CategorySimple> categorySimpleList = product.getProductCategories().stream()
                .map(ProductCategory::getCategory)
                .map(CategoryMapper.INSTANCE::mapFromCategoryToCategoryAdminSimple)
                .collect(Collectors.toList());
        productDetailRS.setCategories(categorySimpleList);
        return productDetailRS;
    }

    private void mergeCategories(UpdateProductRQ updateProductRQ, Product product) {
        Map<Long, ProductCategory> oldProductCategoryMap = product.getProductCategories().stream()
                .collect(Collectors.toMap(item -> item.getCategory().getId(), Function.identity()));
        List<ProductCategory> productCategories = new ArrayList<>();
        List<Category> categories = categoryService.getActiveByIdIn(updateProductRQ.getCategoryIds());
        for (Category category : categories) {
            if (oldProductCategoryMap.containsKey(category.getId())) {
                productCategories.add(oldProductCategoryMap.get(category.getId()));
                continue;
            }
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProduct(product);
            productCategory.setCategory(category);
            productCategories.add(productCategory);
        }
        product.getProductCategories().clear();
        product.getProductCategories().addAll(productCategories);
    }

    private void mergeImages(UpdateProductRQ updateProductRQ, Product product) {
        Map<Long, ProductImage> existed = product.getProductImages().stream()
                .collect(Collectors.toMap(ProductImage::getId, Function.identity()));
        List<ProductImage> productImages = new ArrayList<>(updateProductRQ.getImages().size());
        for (UpdateProductRQ.ProductImage image : updateProductRQ.getImages()) {
            if (image.getId() == null) {
                ProductImage productImage = new ProductImage();
                productImage.setProduct(product);
                productImage.setImage(image.getImage());
                productImages.add(productImage);
            } else if (existed.containsKey(image.getId())) {
                ProductImage productImage = existed.get(image.getId());
                productImage.setImage(image.getImage());
                productImages.add(productImage);
                existed.remove(image.getId());
            }
        }
        product.getProductImages().clear();
        product.getProductImages().addAll(productImages);
    }

    public Product getAvailableById(Long productId, Integer quantity) {
        return productRepository.findAvailableById(productId, quantity)
                .orElseThrow(() -> new BadRequestException("Sản phẩm không tồn tại hoặc đã hết hàng"));
    }

    @Transactional
    @ActivateRequestContext
    @ConsumeEvent(value = EventCode.UPDATE_PRODUCT_QUANTITY, blocking = true)
    public void updateProductQuantity(Message<UpdateProductQuantityMessage> message) {
        UpdateProductQuantityMessage body = message.body();
        boolean isDecrease = UpdateQuantityType.DECREASE.equalsIgnoreCase(body.getType());
        Set<Long> productIds = body.getItems().stream()
                .map(UpdateProductQuantityMessage.Item::getProductId)
                .collect(Collectors.toSet());
        Map<Long, Product> productMap = productRepository.findByIdIn(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        for (UpdateProductQuantityMessage.Item item : body.getItems()) {
            Product product = productMap.get(item.getProductId());
            if (product == null) {
                continue;
            }
            product.setQuantity(isDecrease ? product.getQuantity() - item.getQuantity() : product.getQuantity() + item.getQuantity());
            product.setSold(isDecrease ? product.getSold() + item.getQuantity() : product.getSold() - item.getQuantity());
            productRepository.save(product);
        }
    }

    private Product getById(Long id) {
        return productRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Sản phẩm không tồn tại"));
    }

    private Product getByCodeAndIsDeleted(String code, Boolean isDeleted) {
        return productRepository.findByCodeAndIsDeleted(code, isDeleted)
                .orElseThrow(() -> new EntityNotFoundException("Sản phẩm không tồn tại"));
    }
}
