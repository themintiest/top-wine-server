package vn.topwines.brands;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import vn.topwines.brands.domain.BrandDetailRS;
import vn.topwines.brands.domain.BrandRS;
import vn.topwines.brands.domain.BrandSimpleRS;
import vn.topwines.brands.domain.CreateBrandRQ;
import vn.topwines.brands.domain.GetBrandRQ;
import vn.topwines.brands.domain.UpdateBrandRQ;
import vn.topwines.brands.entity.Brand;
import vn.topwines.brands.mapper.BrandMapper;
import vn.topwines.brands.repository.BrandRepository;
import vn.topwines.brands.specification.BrandSpecifications;
import vn.topwines.core.query.PageRequest;
import vn.topwines.core.query.Pageable;
import vn.topwines.core.repository.specification.Specification;
import vn.topwines.core.utils.CodeGenerator;
import vn.topwines.core.utils.PageUtils;
import vn.topwines.exception.BadRequestException;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@ApplicationScoped
@RequiredArgsConstructor
public class BrandService {
    
    private final BrandRepository brandRepository;

    @Transactional
    public BrandDetailRS create(CreateBrandRQ regionRQ) {
        String code = StringUtils.isNotBlank(regionRQ.getCode())
                ? regionRQ.getCode()
                : CodeGenerator.generateObjectCode(regionRQ.getName());
        if (brandRepository.existsByCode(code)) {
            throw new BadRequestException("Mã thương hiệu đã tồn tại");
        }
        regionRQ.setCode(code);
        Brand brand = BrandMapper.INSTANCE.mapCreateBrandRQToBrand(regionRQ);
        return BrandMapper.INSTANCE.mapBrandToBrandDetailRS(brandRepository.save(brand));
    }

    @Transactional
    public BrandDetailRS update(Long id, UpdateBrandRQ updateBrandRQ) {
        Brand brand = getById(id);
        if (StringUtils.isNotBlank(updateBrandRQ.getCode()) && brandRepository.existsByCodeAndIgnoreId(updateBrandRQ.getCode(), brand.getId())) {
            throw new BadRequestException("Mã thương hiệu đã tồn tại");
        }

        BrandMapper.INSTANCE.merge(brand, updateBrandRQ);
        return BrandMapper.INSTANCE.mapBrandToBrandDetailRS(brandRepository.save(brand));
    }

    @Transactional
    public void delete(Long id) {
        Brand brand = getById(id);
        brand.setIsDeleted(true);
        brandRepository.save(brand);
    }

    @Transactional
    public Pageable<BrandRS> getBrands(GetBrandRQ getBrandRQ) {
        PageRequest pageRequest = PageUtils.createPageRequest(getBrandRQ);
        Specification<Brand> specification = BrandSpecifications.builder()
                .isDeleted(getBrandRQ.getIsDeleted())
                .withNameLike(getBrandRQ.getName())
                .withCode(getBrandRQ.getCode())
                .build();
        Pageable<Brand> page = brandRepository.findAll(specification, pageRequest);
        return PageUtils.createPageResponse(page.getItems(), page.getTotal(), BrandMapper.INSTANCE::mapBrandToBrandRS);
    }

    @Transactional
    public Pageable<BrandSimpleRS> getActiveBrands(GetBrandRQ getBrandRQ) {
        PageRequest pageRequest = PageUtils.createPageRequest(getBrandRQ);
        Specification<Brand> specification = BrandSpecifications.builder()
                .isDeleted(false)
                .withCode(getBrandRQ.getCode())
                .withNameLike(getBrandRQ.getName())
                .build();
        Pageable<Brand> page = brandRepository.findAll(specification, pageRequest);
        return PageUtils.createPageResponse(page.getItems(), page.getTotal(), BrandMapper.INSTANCE::mapBrandToBrandSimpleRS);
    }

    public BrandDetailRS getBrandRSById(Long id) {
        return BrandMapper.INSTANCE.mapBrandToBrandDetailRS(getById(id));
    }

    public BrandDetailRS getActiveBrandRSByCode(String code) {
        return BrandMapper.INSTANCE.mapBrandToBrandDetailRS(getActiveByCode(code));
    }

    public BrandDetailRS getActiveBrandRSById(Long id) {
        return BrandMapper.INSTANCE.mapBrandToBrandDetailRS(getActiveById(id));
    }

    public List<Brand> getActiveByIdIn(Set<Long> ids) {
        return brandRepository.findActiveByIdIn(ids);
    }

    private Brand getById(Long id) {
        return brandRepository.findByIdOptional(id).orElseThrow(() -> new EntityNotFoundException("Thương hiệu không tồn tại!"));
    }

    public Brand getActiveById(Long id) {
        return brandRepository.findActiveByIdOptional(id).orElseThrow(() -> new EntityNotFoundException("Thương hiệu không tồn tại!"));
    }

    private Brand getActiveByCode(String code) {
        return brandRepository.findByCodeAndIsDeletedOptional(code, false).orElseThrow(() -> new EntityNotFoundException("Thương hiệu không tồn tại!"));
    }
}
