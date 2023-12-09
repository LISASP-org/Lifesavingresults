package org.lisasp.competition.results.imports.rescue2022.csv;

import com.opencsv.bean.BeanField;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.exceptions.CsvBadConverterException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.collections4.comparators.ComparableComparator;
import org.apache.commons.collections4.comparators.ComparatorChain;
import org.apache.commons.collections4.comparators.FixedOrderComparator;
import org.apache.commons.collections4.comparators.NullComparator;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HeaderColumnNameAndOrderMappingStrategy<T> extends HeaderColumnNameMappingStrategy<T> {

    public HeaderColumnNameAndOrderMappingStrategy(Class<T> type) {
        setType(type);
    }

    @Override
    public String[] generateHeader(T bean) throws CsvRequiredFieldEmptyException {
        // overriding this method to allow us to preserve the header column name casing

        String[] header = super.generateHeader(bean);
        final int numColumns = headerIndex.findMaxIndex() + 1;
        if (!isAnnotationDriven() || numColumns == -1) {
            return header;
        }

        header = new String[numColumns + 1];

        BeanField beanField;
        for (int i = 0; i <= numColumns; i++) {
            beanField = findField(i);
            String columnHeaderName = extractHeaderName(beanField);
            header[i] = columnHeaderName;
        }
        return header;
    }


    @Override
    protected void loadFieldMap() throws CsvBadConverterException {
        // overriding this method to support setting column order by the custom `CsvBindByNameOrder` annotation
        if (writeOrder == null && type.isAnnotationPresent(CsvBindByNameOrder.class)) {
            List<String> predefinedList =
                    Arrays.stream(type.getAnnotation(CsvBindByNameOrder.class).value())
                          .map(String::toUpperCase).collect(Collectors.toList());
            FixedOrderComparator<String> fixedComparator = new FixedOrderComparator<>(predefinedList);
            fixedComparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
            Comparator<String> comparator = new ComparatorChain<>(Arrays.asList(
                    fixedComparator,
                    new NullComparator<>(false),
                    new ComparableComparator<>()));
            setColumnOrderOnWrite(comparator);
        }
        super.loadFieldMap();
    }

    private String extractHeaderName(final BeanField beanField) {
        if (beanField == null || beanField.getField() == null
            || beanField.getField().getDeclaredAnnotationsByType(CsvBindByName.class).length == 0) {
            return StringUtils.EMPTY;
        }

        if (beanField.getField().isAnnotationPresent(CsvBindByName.class)) {
            return beanField.getField().getDeclaredAnnotationsByType(CsvBindByName.class)[0].column();
        } else if (beanField.getField().isAnnotationPresent(CsvCustomBindByName.class)) {
            return beanField.getField().getDeclaredAnnotationsByType(CsvCustomBindByName.class)[0].column();
        }
        return StringUtils.EMPTY;

    }
}
