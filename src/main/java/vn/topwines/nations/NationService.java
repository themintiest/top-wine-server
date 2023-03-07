package vn.topwines.nations;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import vn.topwines.core.query.PageRequest;
import vn.topwines.core.query.Pageable;
import vn.topwines.core.repository.specification.Specification;
import vn.topwines.core.utils.CodeGenerator;
import vn.topwines.core.utils.PageUtils;
import vn.topwines.exception.BadRequestException;
import vn.topwines.nations.domain.CreateNationRQ;
import vn.topwines.nations.domain.GetNationRQ;
import vn.topwines.nations.domain.NationDetailRS;
import vn.topwines.nations.domain.NationRS;
import vn.topwines.nations.domain.NationSimpleRS;
import vn.topwines.nations.domain.UpdateNationRQ;
import vn.topwines.nations.entity.Nation;
import vn.topwines.nations.mapper.NationMapper;
import vn.topwines.nations.repository.NationRepository;
import vn.topwines.nations.specification.NationSpecifications;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@ApplicationScoped
@RequiredArgsConstructor
public class NationService {
    
    private final NationRepository nationRepository;

    @Transactional
    public NationDetailRS create(CreateNationRQ regionRQ) {
        String code = StringUtils.isNotBlank(regionRQ.getCode())
                ? regionRQ.getCode()
                : CodeGenerator.generateObjectCode(regionRQ.getName());
        if (nationRepository.existsByCode(code)) {
            throw new BadRequestException("Mã quốc gia đã tồn tại");
        }
        regionRQ.setCode(code);
        Nation nation = NationMapper.INSTANCE.mapCreateNationRQToNation(regionRQ);
        return NationMapper.INSTANCE.mapNationToNationDetailRS(nationRepository.save(nation));
    }

    @Transactional
    public NationDetailRS update(Long id, UpdateNationRQ updateNationRQ) {
        Nation nation = getById(id);
        if (StringUtils.isNotBlank(updateNationRQ.getCode()) && nationRepository.existsByCodeAndIgnoreId(updateNationRQ.getCode(), nation.getId())) {
            throw new BadRequestException("Mã quốc gia đã tồn tại");
        }

        NationMapper.INSTANCE.merge(nation, updateNationRQ);
        return NationMapper.INSTANCE.mapNationToNationDetailRS(nationRepository.save(nation));
    }

    @Transactional
    public void delete(Long id) {
        Nation nation = getById(id);
        nation.setIsDeleted(true);
        nationRepository.save(nation);
    }

    @Transactional
    public Pageable<NationRS> getNations(GetNationRQ getNationRQ) {
        PageRequest pageRequest = PageUtils.createPageRequest(getNationRQ);
        Specification<Nation> specification = NationSpecifications.builder()
                .isDeleted(getNationRQ.getIsDeleted())
                .withNameLike(getNationRQ.getName())
                .withCode(getNationRQ.getCode())
                .build();
        Pageable<Nation> page = nationRepository.findAll(specification, pageRequest);
        return PageUtils.createPageResponse(page.getItems(), page.getTotal(), NationMapper.INSTANCE::mapNationToNationRS);
    }

    @Transactional
    public Pageable<NationSimpleRS> getActiveNations(GetNationRQ getNationRQ) {
        PageRequest pageRequest = PageUtils.createPageRequest(getNationRQ);
        Specification<Nation> specification = NationSpecifications.builder()
                .isDeleted(false)
                .withCode(getNationRQ.getCode())
                .withNameLike(getNationRQ.getName())
                .build();
        Pageable<Nation> page = nationRepository.findAll(specification, pageRequest);
        return PageUtils.createPageResponse(page.getItems(), page.getTotal(), NationMapper.INSTANCE::mapNationToNationSimpleRS);
    }

    public NationDetailRS getNationRSById(Long id) {
        return NationMapper.INSTANCE.mapNationToNationDetailRS(getById(id));
    }

    public NationDetailRS getActiveNationRSByCode(String code) {
        return NationMapper.INSTANCE.mapNationToNationDetailRS(getActiveByCode(code));
    }

    public NationDetailRS getActiveNationRSById(Long id) {
        return NationMapper.INSTANCE.mapNationToNationDetailRS(getActiveById(id));
    }

    public List<Nation> getActiveByIdIn(Set<Long> ids) {
        return nationRepository.findActiveByIdIn(ids);
    }

    private Nation getById(Long id) {
        return nationRepository.findByIdOptional(id).orElseThrow(() -> new EntityNotFoundException("Quốc gia không tồn tại!"));
    }

    public Nation getActiveById(Long id) {
        return nationRepository.findActiveByIdOptional(id).orElseThrow(() -> new EntityNotFoundException("Quốc gia không tồn tại!"));
    }

    private Nation getActiveByCode(String code) {
        return nationRepository.findByCodeAndIsDeletedOptional(code, false).orElseThrow(() -> new EntityNotFoundException("Quốc gia không tồn tại!"));
    }
}
