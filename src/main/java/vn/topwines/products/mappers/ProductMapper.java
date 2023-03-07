package vn.topwines.products.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.topwines.brands.mapper.BrandMapper;
import vn.topwines.grapes.mapper.GrapeMapper;
import vn.topwines.nations.mapper.NationMapper;
import vn.topwines.product_type.mapper.ProductTypeMapper;
import vn.topwines.products.domain.CreateProductRQ;
import vn.topwines.products.domain.ProductDetailRS;
import vn.topwines.products.domain.ProductRS;
import vn.topwines.products.domain.ProductSimpleRS;
import vn.topwines.products.domain.UpdateProductRQ;
import vn.topwines.products.entity.Product;
import vn.topwines.regions.mapper.RegionMapper;

@Mapper(
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        uses = { ProductImageMapper.class, RegionMapper.class, GrapeMapper.class,
                BrandMapper.class, NationMapper.class, ProductTypeMapper.class }
)
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product mapCreateProductRQToProduct(CreateProductRQ createProductRQ);

    ProductSimpleRS mapProductToProductSimpleRS(Product product);

    ProductRS mapProductToProductRS(Product product);

    @Mapping(target = "productImages", source = "productImages")
    ProductDetailRS mapProductToProductDetailRS(Product product);

    @Mapping(target = "id", ignore = true)
    void merge(@MappingTarget Product target, UpdateProductRQ source);
}
