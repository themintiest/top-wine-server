package vn.topwines.product_type;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import vn.topwines.core.query.PageRequest;
import vn.topwines.core.query.Pageable;
import vn.topwines.core.repository.specification.Specification;
import vn.topwines.core.utils.CodeGenerator;
import vn.topwines.core.utils.PageUtils;
import vn.topwines.exception.BadRequestException;
import vn.topwines.product_type.domain.CreateProductTypeRQ;
import vn.topwines.product_type.domain.GetProductTypeRQ;
import vn.topwines.product_type.domain.ProductTypeDetailRS;
import vn.topwines.product_type.domain.ProductTypeRS;
import vn.topwines.product_type.domain.ProductTypeSimpleRS;
import vn.topwines.product_type.domain.UpdateProductTypeRQ;
import vn.topwines.product_type.entity.ProductType;
import vn.topwines.product_type.mapper.ProductTypeMapper;
import vn.topwines.product_type.repository.ProductTypeRepository;
import vn.topwines.product_type.specification.ProductTypeSpecifications;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;

@ApplicationScoped
@RequiredArgsConstructor
public class ProductTypeService {
    
    private final ProductTypeRepository productTypeRepository;

    @Transactional
    public ProductTypeDetailRS create(CreateProductTypeRQ regionRQ) {
        String code = StringUtils.isNotBlank(regionRQ.getCode())
                ? regionRQ.getCode()
                : CodeGenerator.generateObjectCode(regionRQ.getName());
        if (productTypeRepository.existsByCode(code)) {
            throw new BadRequestException("Mã loại sản phẩm đã tồn tại");
        }
        regionRQ.setCode(code);
        ProductType productType = ProductTypeMapper.INSTANCE.mapCreateProductTypeRQToProductType(regionRQ);
        return ProductTypeMapper.INSTANCE.mapProductTypeToProductTypeDetailRS(productTypeRepository.save(productType));
    }

    @Transactional
    public ProductTypeDetailRS update(Long id, UpdateProductTypeRQ updateProductTypeRQ) {
        ProductType productType = getById(id);
        if (StringUtils.isNotBlank(updateProductTypeRQ.getCode()) && productTypeRepository.existsByCodeAndIgnoreId(updateProductTypeRQ.getCode(), productType.getId())) {
            throw new BadRequestException("Mã loại sản phẩm đã tồn tại");
        }

        ProductTypeMapper.INSTANCE.merge(productType, updateProductTypeRQ);
        return ProductTypeMapper.INSTANCE.mapProductTypeToProductTypeDetailRS(productTypeRepository.save(productType));
    }

    @Transactional
    public void delete(Long id) {
        ProductType productType = getById(id);
        productType.setIsDeleted(true);
        productTypeRepository.save(productType);
    }

    @Transactional
    public Pageable<ProductTypeRS> getProductTypes(GetProductTypeRQ getProductTypeRQ) {
        PageRequest pageRequest = PageUtils.createPageRequest(getProductTypeRQ);
        Specification<ProductType> specification = ProductTypeSpecifications.builder()
                .isDeleted(getProductTypeRQ.getIsDeleted())
                .withNameLike(getProductTypeRQ.getName())
                .withCode(getProductTypeRQ.getCode())
                .build();
        Pageable<ProductType> page = productTypeRepository.findAll(specification, pageRequest);
        return PageUtils.createPageResponse(page.getItems(), page.getTotal(), ProductTypeMapper.INSTANCE::mapProductTypeToProductTypeRS);
    }

    @Transactional
    public Pageable<ProductTypeSimpleRS> getActiveProductTypes(GetProductTypeRQ getProductTypeRQ) {
        PageRequest pageRequest = PageUtils.createPageRequest(getProductTypeRQ);
        Specification<ProductType> specification = ProductTypeSpecifications.builder()
                .isDeleted(false)
                .withCode(getProductTypeRQ.getCode())
                .withNameLike(getProductTypeRQ.getName())
                .build();
        Pageable<ProductType> page = productTypeRepository.findAll(specification, pageRequest);
        return PageUtils.createPageResponse(page.getItems(), page.getTotal(), ProductTypeMapper.INSTANCE::mapProductTypeToProductTypeSimpleRS);
    }

    public ProductTypeDetailRS getProductTypeRSById(Long id) {
        return ProductTypeMapper.INSTANCE.mapProductTypeToProductTypeDetailRS(getById(id));
    }

    public ProductTypeDetailRS getActiveProductTypeRSByCode(String code) {
        return ProductTypeMapper.INSTANCE.mapProductTypeToProductTypeDetailRS(getActiveByCode(code));
    }

    public ProductTypeDetailRS getActiveProductTypeRSById(Long id) {
        return ProductTypeMapper.INSTANCE.mapProductTypeToProductTypeDetailRS(getActiveById(id));
    }

    public List<ProductType> getActiveByIdIn(Set<Long> ids) {
        return productTypeRepository.findActiveByIdIn(ids);
    }

    private ProductType getById(Long id) {
        return productTypeRepository.findByIdOptional(id).orElseThrow(() -> new EntityNotFoundException("Loại sản phẩm không tồn tại!"));
    }

    public ProductType getActiveById(Long id) {
        return productTypeRepository.findActiveByIdOptional(id).orElseThrow(() -> new EntityNotFoundException("Loại sản phẩm không tồn tại!"));
    }

    private ProductType getActiveByCode(String code) {
        return productTypeRepository.findByCodeAndIsDeletedOptional(code, false).orElseThrow(() -> new EntityNotFoundException("Dữ liệu không tồn tại!"));
    }
}
