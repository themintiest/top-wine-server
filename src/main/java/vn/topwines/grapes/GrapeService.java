package vn.topwines.grapes;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import vn.topwines.core.query.PageRequest;
import vn.topwines.core.query.Pageable;
import vn.topwines.core.repository.specification.Specification;
import vn.topwines.core.utils.CodeGenerator;
import vn.topwines.core.utils.PageUtils;
import vn.topwines.exception.BadRequestException;
import vn.topwines.grapes.domain.CreateGrapeRQ;
import vn.topwines.grapes.domain.GetGrapeRQ;
import vn.topwines.grapes.domain.GrapeDetailRS;
import vn.topwines.grapes.domain.GrapeRS;
import vn.topwines.grapes.domain.GrapeSimpleRS;
import vn.topwines.grapes.domain.UpdateGrapeRQ;
import vn.topwines.grapes.entity.Grape;
import vn.topwines.grapes.mapper.GrapeMapper;
import vn.topwines.grapes.repository.GrapeRepository;
import vn.topwines.grapes.specification.GrapeSpecifications;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;

@ApplicationScoped
@RequiredArgsConstructor
public class GrapeService {
    
    private final GrapeRepository grapeRepository;

    @Transactional
    public GrapeDetailRS create(CreateGrapeRQ regionRQ) {
        String code = StringUtils.isNotBlank(regionRQ.getCode())
                ? regionRQ.getCode()
                : CodeGenerator.generateObjectCode(regionRQ.getName());
        if (grapeRepository.existsByCode(code)) {
            throw new BadRequestException("Mã giống nho đã tồn tại");
        }
        regionRQ.setCode(code);
        Grape grape = GrapeMapper.INSTANCE.mapCreateGrapeRQToGrape(regionRQ);
        return GrapeMapper.INSTANCE.mapGrapeToGrapeDetailRS(grapeRepository.save(grape));
    }

    @Transactional
    public GrapeDetailRS update(Long id, UpdateGrapeRQ updateGrapeRQ) {
        Grape grape = getById(id);
        if (StringUtils.isNotBlank(updateGrapeRQ.getCode()) && grapeRepository.existsByCodeAndIgnoreId(updateGrapeRQ.getCode(), grape.getId())) {
            throw new BadRequestException("Mã giống nho đã tồn tại");
        }

        GrapeMapper.INSTANCE.merge(grape, updateGrapeRQ);
        return GrapeMapper.INSTANCE.mapGrapeToGrapeDetailRS(grapeRepository.save(grape));
    }

    @Transactional
    public void delete(Long id) {
        Grape grape = getById(id);
        grape.setIsDeleted(true);
        grapeRepository.save(grape);
    }

    @Transactional
    public Pageable<GrapeRS> getGrapes(GetGrapeRQ getGrapeRQ) {
        PageRequest pageRequest = PageUtils.createPageRequest(getGrapeRQ);
        Specification<Grape> specification = GrapeSpecifications.builder()
                .isDeleted(getGrapeRQ.getIsDeleted())
                .withNameLike(getGrapeRQ.getName())
                .withCode(getGrapeRQ.getCode())
                .build();
        Pageable<Grape> page = grapeRepository.findAll(specification, pageRequest);
        return PageUtils.createPageResponse(page.getItems(), page.getTotal(), GrapeMapper.INSTANCE::mapGrapeToGrapeRS);
    }

    @Transactional
    public Pageable<GrapeSimpleRS> getActiveGrapes(GetGrapeRQ getGrapeRQ) {
        PageRequest pageRequest = PageUtils.createPageRequest(getGrapeRQ);
        Specification<Grape> specification = GrapeSpecifications.builder()
                .isDeleted(false)
                .withCode(getGrapeRQ.getCode())
                .withNameLike(getGrapeRQ.getName())
                .build();
        Pageable<Grape> page = grapeRepository.findAll(specification, pageRequest);
        return PageUtils.createPageResponse(page.getItems(), page.getTotal(), GrapeMapper.INSTANCE::mapGrapeToGrapeSimpleRS);
    }

    public GrapeDetailRS getGrapeRSById(Long id) {
        return GrapeMapper.INSTANCE.mapGrapeToGrapeDetailRS(getById(id));
    }

    public GrapeDetailRS getActiveGrapeRSByCode(String code) {
        return GrapeMapper.INSTANCE.mapGrapeToGrapeDetailRS(getActiveByCode(code));
    }

    public GrapeDetailRS getActiveGrapeRSById(Long id) {
        return GrapeMapper.INSTANCE.mapGrapeToGrapeDetailRS(getActiveById(id));
    }

    public List<Grape> getActiveByIdIn(Set<Long> ids) {
        return grapeRepository.findActiveByIdIn(ids);
    }

    private Grape getById(Long id) {
        return grapeRepository.findByIdOptional(id).orElseThrow(() -> new EntityNotFoundException("Vùng không tồn tại!"));
    }

    public Grape getActiveById(Long id) {
        return grapeRepository.findActiveByIdOptional(id).orElseThrow(() -> new EntityNotFoundException("Giống nho không tồn tại!"));
    }

    private Grape getActiveByCode(String code) {
        return grapeRepository.findByCodeAndIsDeletedOptional(code, false).orElseThrow(() -> new EntityNotFoundException("Giống nho không tồn tại!"));
    }
}
