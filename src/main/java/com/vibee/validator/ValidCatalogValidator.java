package com.vibee.validator;

import com.vibee.annotation.ValidCatalog;
import com.vibee.enums.CatalogEnum;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ValidCatalogValidator implements ConstraintValidator<ValidCatalog, Object> {
    @Value("${server.port}")
    private String serverPort;
    private static Map<String, Set<String>> catalogCacheMap = new HashMap<>();
    private CatalogEnum catalog;

    public void initialize(final ValidCatalog constraintAnnotation) {
        this.catalog = constraintAnnotation.catalog();
    }

    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        final String checkValue = (String) value;
        String sourceId = catalog.getSourceId();
        if (!catalogCacheMap.containsKey(sourceId)) {
            loadCache(sourceId);
        }
        Set<String> catalogSet = catalogCacheMap.get(sourceId);
        return catalogSet.contains(checkValue);
    }

    private void loadCache(String sourceId) {
        // TODO: load data from catalog service
        Set<String> catalogSet = new HashSet<>();
        for (int i = 1; i <= 5; i++) {
            catalogSet.add(sourceId + "_" + i);
        }
        catalogCacheMap.put(sourceId, catalogSet);
    }
}
