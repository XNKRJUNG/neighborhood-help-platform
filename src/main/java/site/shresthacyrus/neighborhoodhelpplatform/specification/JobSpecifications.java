package site.shresthacyrus.neighborhoodhelpplatform.specification;

import org.springframework.data.jpa.domain.Specification;
import site.shresthacyrus.neighborhoodhelpplatform.model.Job;

public class JobSpecifications {

    public static Specification<Job> hasSkill(Long skillId) {
        return (root, query, cb) ->
                skillId == null ? null : cb.equal(root.get("skill").get("id"), skillId);
    }

    public static Specification<Job> hasZipCode(String zipCode) {
        return (root, query, cb) ->
                zipCode == null ? null : cb.equal(root.get("zipCode"), zipCode);
    }

    public static Specification<Job> hasTitle(String title) {
        return (root, query, cb) ->
                title == null ? null : cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Job> hasMinPrice(Double minPrice) {
        return (root, query, cb) ->
                minPrice == null ? null : cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Job> hasMaxPrice(Double maxPrice) {
        return (root, query, cb) ->
                maxPrice == null ? null : cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }
}
