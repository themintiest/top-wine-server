package vn.topwines.regions;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import vn.topwines.core.query.PageRequest;
import vn.topwines.core.query.Pageable;
import vn.topwines.core.repository.specification.Specification;
import vn.topwines.core.utils.CodeGenerator;
import vn.topwines.core.utils.PageUtils;
import vn.topwines.exception.BadRequestException;
import vn.topwines.regions.domain.CreateRegionRQ;
import vn.topwines.regions.domain.GetRegionRQ;
import vn.topwines.regions.domain.RegionDetailRS;
import vn.topwines.regions.domain.RegionRS;
import vn.topwines.regions.domain.RegionSimpleRS;
import vn.topwines.regions.domain.UpdateRegionRQ;
import vn.topwines.regions.entity.Region;
import vn.topwines.regions.mapper.RegionMapper;
import vn.topwines.regions.repository.RegionRepository;
import vn.topwines.regions.specification.RegionSpecifications;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;

@ApplicationScoped
@RequiredArgsConstructor
public class RegionService {
    
    private final RegionRepository regionRepository;

    @Transactional
    public RegionDetailRS create(CreateRegionRQ regionRQ) {
        String code = StringUtils.isNotBlank(regionRQ.getCode())
                ? regionRQ.getCode()
                : CodeGenerator.generateObjectCode(regionRQ.getName());
        if (regionRepository.existsByCode(code)) {
            throw new BadRequestException("Mã vùng đã tồn tại");
        }
        regionRQ.setCode(code);
        Region brand = RegionMapper.INSTANCE.mapCreateRegionRQToRegion(regionRQ);
        return RegionMapper.INSTANCE.mapRegionToRegionDetailRS(regionRepository.save(brand));
    }

    @Transactional
    public RegionDetailRS update(Long id, UpdateRegionRQ updateRegionRQ) {
        Region brand = getById(id);
        if (StringUtils.isNotBlank(updateRegionRQ.getCode()) && regionRepository.existsByCodeAndIgnoreId(updateRegionRQ.getCode(), brand.getId())) {
            throw new BadRequestException("Mã vùng đã tồn tại");
        }

        RegionMapper.INSTANCE.merge(brand, updateRegionRQ);
        return RegionMapper.INSTANCE.mapRegionToRegionDetailRS(regionRepository.save(brand));
    }

    @Transactional
    public void delete(Long id) {
        Region brand = getById(id);
        brand.setIsDeleted(true);
        regionRepository.save(brand);
    }

    @Transactional
    public Pageable<RegionRS> getRegions(GetRegionRQ getRegionRQ) {
        PageRequest pageRequest = PageUtils.createPageRequest(getRegionRQ);
        Specification<Region> specification = RegionSpecifications.builder()
                .isDeleted(getRegionRQ.getIsDeleted())
                .withNameLike(getRegionRQ.getName())
                .withCode(getRegionRQ.getCode())
                .build();
        Pageable<Region> page = regionRepository.findAll(specification, pageRequest);
        return PageUtils.createPageResponse(page.getItems(), page.getTotal(), RegionMapper.INSTANCE::mapRegionToRegionRS);
    }

    @Transactional
    public Pageable<RegionSimpleRS> getActiveRegions(GetRegionRQ getRegionRQ) {
        PageRequest pageRequest = PageUtils.createPageRequest(getRegionRQ);
        Specification<Region> specification = RegionSpecifications.builder()
                .isDeleted(false)
                .withCode(getRegionRQ.getCode())
                .withNameLike(getRegionRQ.getName())
                .build();
        Pageable<Region> page = regionRepository.findAll(specification, pageRequest);
        return PageUtils.createPageResponse(page.getItems(), page.getTotal(), RegionMapper.INSTANCE::mapRegionToRegionSimpleRS);
    }

    public RegionDetailRS getRegionRSById(Long id) {
        return RegionMapper.INSTANCE.mapRegionToRegionDetailRS(getById(id));
    }

    public RegionDetailRS getActiveRegionRSByCode(String code) {
        return RegionMapper.INSTANCE.mapRegionToRegionDetailRS(getActiveByCode(code));
    }

    public RegionDetailRS getActiveRegionRSById(Long id) {
        return RegionMapper.INSTANCE.mapRegionToRegionDetailRS(getActiveById(id));
    }

    public List<Region> getActiveByIdIn(Set<Long> ids) {
        return regionRepository.findActiveByIdIn(ids);
    }

    private Region getById(Long id) {
        return regionRepository.findByIdOptional(id).orElseThrow(() -> new EntityNotFoundException("Vùng không tồn tại!"));
    }

    public Region getActiveById(Long id) {
        return regionRepository.findActiveByIdOptional(id).orElseThrow(() -> new EntityNotFoundException("Vùng không tồn tại!"));
    }

    private Region getActiveByCode(String code) {
        return regionRepository.findByCodeAndIsDeletedOptional(code, false).orElseThrow(() -> new EntityNotFoundException("Vùng không tồn tại!"));
    }
}
