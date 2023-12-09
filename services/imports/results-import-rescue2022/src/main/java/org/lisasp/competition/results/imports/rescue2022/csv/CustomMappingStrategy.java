package org.lisasp.competition.results.imports.rescue2022.csv;

import com.opencsv.bean.BeanField;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.lisasp.competition.results.imports.rescue2022.RescueEntity;

public class CustomMappingStrategy extends ColumnPositionMappingStrategy<RescueEntity> {
    @Override
    public String[] generateHeader(RescueEntity bean) throws CsvRequiredFieldEmptyException {

        super.setColumnMapping(new String[FieldUtils.getAllFields(bean.getClass()).length]);
        super.setType(RescueEntity.class);
        final int numColumns = findMaxFieldIndex() + 1;
        if (numColumns == -1) {
            return super.generateHeader(bean);
        }

        String[] header = new String[numColumns + 1];

        BeanField<RescueEntity, Integer> beanField;
        for (int i = 0; i <= numColumns; i++) {
            beanField = findField(i);
            String columnHeaderName = extractHeaderName(beanField);
            header[i] = columnHeaderName;
        }
        return header;
    }

    private int findMaxFieldIndex() {
        return 10;
    }

    private String extractHeaderName(final BeanField<RescueEntity, Integer> beanField) {
        if (beanField == null || beanField.getField() == null
            || beanField.getField().getDeclaredAnnotationsByType(CsvBindByName.class).length == 0) {
            return StringUtils.EMPTY;
        }

        CsvBindByName bindByNameAnnotation = beanField.getField().getDeclaredAnnotationsByType(CsvBindByName.class)[0];
        return bindByNameAnnotation.column();
    }
}
