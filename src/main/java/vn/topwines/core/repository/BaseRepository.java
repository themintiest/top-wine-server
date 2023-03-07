package vn.topwines.core.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import vn.topwines.core.query.PageRequest;
import vn.topwines.core.query.Pageable;
import vn.topwines.core.query.PagingQuery;
import vn.topwines.core.query.panache.PanachePageQueryBuilder;
import vn.topwines.core.query.panache.PanachePagedQuery;
import vn.topwines.core.repository.specification.Specification;
import vn.topwines.core.repository.specification.SpecificationExecutor;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class BaseRepository<E, I> implements PanacheRepositoryBase<E, I>, SpecificationExecutor<E> {

    private PanachePageQueryBuilder queryBuilder;

    private final EntityManager entityManager = getEntityManager();

    public E save(E entity) {
        if (isPersistent(entity)) {
            return getEntityManager().merge(entity);
        }
        persist(entity);
        return entity;
    }

    public E save(E entity, Consumer<E> modify) {
        if (isPersistent(entity)) {
            return getEntityManager().merge(entity);
        }
        persist(entity);
        modify.accept(entity);
        return entity;
    }

    public <V> boolean exists(String fieldName, V value) {
        return count(fieldName, value) > 0;
    }

    public <T> Pageable<T> findAll(PagingQuery pagingQuery, Function<E, T> mapper) {
        PanacheQuery<E> panacheQuery = findAll();
        panacheQuery.page(pagingQuery.getPage(), pagingQuery.getSize());
        long count = panacheQuery.count();
        List<T> items = panacheQuery.list().stream().map(mapper).collect(Collectors.toList());
        return Pageable.<T>builder().total(count).items(items).build();
    }

    public Pageable<E> search(PagingQuery pagingQuery) {
        PanacheQuery<E> panacheQuery = query(pagingQuery);
        long count = panacheQuery.count();
        List<E> items = panacheQuery.list();
        return Pageable.<E>builder().total(count).items(items).build();
    }

    public <T> Pageable<T> search(PagingQuery pagingQuery, Function<E, T> mapper) {
        PanacheQuery<E> panacheQuery = query(pagingQuery);
        long count = panacheQuery.count();
        List<T> items = panacheQuery.list().stream().map(mapper).collect(Collectors.toList());
        return Pageable.<T>builder().total(count).items(items).build();
    }

    public <V> Set<E> findIn(String fieldName, Set<V> values) {
        Parameters parameters = Parameters.with(fieldName, values);
        return find(String.format("%s in :%s", fieldName, fieldName), parameters)
                .stream()
                .collect(Collectors.toSet());
    }

    public Query createNativeQuery(String query) {
        return createNativeQuery(query, null);
    }

    public Query createNativeQuery(String query, Parameters parameters) {
        Query nativeQuery = getEntityManager().createNativeQuery(query, getEntityType());
        if (parameters != null) {
            for (Map.Entry<String, Object> entry : parameters.map().entrySet()) {
                nativeQuery.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return nativeQuery;
    }

    public List<E> findAll(Specification<E> spec) {
        return getQuery(spec, getEntityType()).getResultStream().collect(Collectors.toList());
    }

    public Pageable<E> findAll(Specification<E> spec, PageRequest pageRequest) {
        TypedQuery<Long> countQuery = getCountQuery(spec, getEntityType());
        Long count = countQuery.getSingleResult();
        TypedQuery<E> typedQuery = getQuery(spec, getEntityType(), pageRequest.getSort());
        typedQuery.setFirstResult(pageRequest.getPage() * pageRequest.getSize());
        typedQuery.setMaxResults(pageRequest.getSize());
        List<E> data = typedQuery.getResultList();
        return Pageable.<E>builder()
                .total(count)
                .items(data)
                .build();
    }

    public List<E> findAll(Specification<E> spec, Sort sort) {
        TypedQuery<E> typedQuery = getQuery(spec, getEntityType(), sort);
        return typedQuery.getResultStream().collect(Collectors.toList());
    }

    public long count(Specification<E> spec) {
        TypedQuery<Long> countQuery = getCountQuery(spec, getEntityType());
        return countQuery.getSingleResult();
    }

    protected TypedQuery<E> getQuery(Specification<E> spec, Class<E> domainClass) {
        return getQuery(spec, domainClass, null);
    }

    protected TypedQuery<E> getQuery(Specification<E> spec, Class<E> domainClass, Sort sort) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> query = builder.createQuery(domainClass);

        Root<E> root = applySpecificationToCriteria(spec, domainClass, query);

        query.select(root);

        if (sort != null) {
            query.orderBy(toOrders(sort, root, builder));
        }

        return entityManager.createQuery(query);
    }

    protected TypedQuery<Long> getCountQuery(Specification<E> spec, Class<E> domainClass) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        Root<E> root = applySpecificationToCriteria(spec, domainClass, query);

        query.select(builder.count(root));

        return entityManager.createQuery(query);
    }

    private <S, U extends E> Root<U> applySpecificationToCriteria(Specification<U> spec, Class<U> domainClass, CriteriaQuery<S> query) {

        Root<U> root = query.from(domainClass);

        if (spec == null) {
            return root;
        }

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        Predicate predicate = spec.toPredicate(root, query, builder);

        if (predicate != null) {
            query.where(predicate);
        }

        return root;
    }

    public static List<Order> toOrders(Sort sort, From<?, ?> from, CriteriaBuilder cb) {

        if (sort == null) {
            return Collections.emptyList();
        }

        List<Order> orders = new ArrayList<>();

        for (Sort.Column column : sort.getColumns()) {
            Expression<?> expression = from.get(column.getName());
            Order order = Sort.Direction.Ascending.equals(column.getDirection())
                    ? cb.asc(expression)
                    : cb.desc(expression);
            orders.add(order);
        }

        return orders;
    }

    protected PanacheQuery<E> query(PagingQuery pagingQuery) {
        PanachePagedQuery panachePagedQuery = createPagedQuery(pagingQuery);
        PanacheQuery<E> panacheQuery;
        if (panachePagedQuery.getSort() != null) {
            panacheQuery = find(panachePagedQuery.getQuery(),
                    panachePagedQuery.getSort(),
                    panachePagedQuery.getParams());
        } else {
            panacheQuery = find(panachePagedQuery.getQuery(), panachePagedQuery.getParams());
        }
        if (panachePagedQuery.getPage() != null) {
            return panacheQuery.page(panachePagedQuery.getPage());
        }
        return panacheQuery;
    }

    private PanachePagedQuery createPagedQuery(PagingQuery pagingQuery) {
        return getQueryBuilder().build(pagingQuery);
    }

    private PanachePageQueryBuilder getQueryBuilder() {
        if (queryBuilder == null) {
            Class<?> entityClass = getEntityType();
            return new PanachePageQueryBuilder(getEntityManager().getMetamodel().managedType(entityClass));
        }
        return queryBuilder;
    }

    private Class<E> getEntityType() {
        Type genericSuperClass = getClass().getGenericSuperclass();

        ParameterizedType parametrizedType = null;
        while (parametrizedType == null) {
            if ((genericSuperClass instanceof ParameterizedType)) {
                parametrizedType = (ParameterizedType) genericSuperClass;
            } else {
                genericSuperClass = ((Class<?>) genericSuperClass).getGenericSuperclass();
            }
        }

        return (Class<E>) parametrizedType.getActualTypeArguments()[0];
    }
}
