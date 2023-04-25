package org.lisasp.competition.results.imports.orangecup2022;

import com.opencsv.bean.*;
import com.opencsv.bean.comparator.LiteralComparator;
import com.opencsv.exceptions.CsvBadConverterException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.collections4.comparators.ComparableComparator;
import org.apache.commons.collections4.comparators.ComparatorChain;
import org.apache.commons.collections4.comparators.FixedOrderComparator;
import org.apache.commons.collections4.comparators.NullComparator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResultWriter {

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface CsvBindByNameOrder {
        String[] value() default {};
    }

    public class HeaderColumnNameAndOrderMappingStrategy<T> extends HeaderColumnNameMappingStrategy<T> {

        public HeaderColumnNameAndOrderMappingStrategy(Class<T> type) {
            setType(type);
        }

        @Override
        public String[] generateHeader(T bean) throws CsvRequiredFieldEmptyException {
            // overriding this method to allow us to preserve the header column name casing

            String[] header = super.generateHeader(bean);
            final int numColumns =  headerIndex.findMaxIndex()+1;
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
                List<String> predefinedList = Arrays.stream(type.getAnnotation(CsvBindByNameOrder.class).value())
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
    private static class CustomMappingStrategy extends ColumnPositionMappingStrategy<OrangeCupEntry> {
        @Override
        public String[] generateHeader(OrangeCupEntry bean) throws CsvRequiredFieldEmptyException {

            super.setColumnMapping(new String[FieldUtils.getAllFields(bean.getClass()).length]);
            super.setType(OrangeCupEntry.class);
            final int numColumns = findMaxFieldIndex()+1;
            if (numColumns == -1) {
                return super.generateHeader(bean);
            }

            String[] header = new String[numColumns + 1];

            BeanField<OrangeCupEntry, Integer> beanField;
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

        private String extractHeaderName(final BeanField<OrangeCupEntry, Integer> beanField) {
            if (beanField == null || beanField.getField() == null
                || beanField.getField().getDeclaredAnnotationsByType(CsvBindByName.class).length == 0) {
                return StringUtils.EMPTY;
            }

            final CsvBindByName bindByNameAnnotation = beanField.getField()
                                                                .getDeclaredAnnotationsByType(CsvBindByName.class)[0];
            return bindByNameAnnotation.column();
        }
    }
    public void write(Stream<OrangeCupResult> data, Path path) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        try (OutputStream os = Files.newOutputStream(path); OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            new StatefulBeanToCsvBuilder<OrangeCupEntry>(writer).withMappingStrategy(new HeaderColumnNameAndOrderMappingStrategy<>(OrangeCupEntry.class)).withSeparator(';').build().write(data.filter(e -> e.isEntry() && e.isValid()).map(e -> e.toEntry()));
        }
    }

    public void writeInvalid(Stream<OrangeCupResult> data, Path path) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        try (OutputStream os = Files.newOutputStream(path); OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            new StatefulBeanToCsvBuilder<OrangeCupEntry>(writer).withMappingStrategy(new HeaderColumnNameAndOrderMappingStrategy<>(OrangeCupEntry.class)).withSeparator(';').build().write(data.filter(e -> e.isEntry() && !e.isValid()).map(e -> e.toEntry()));
        }
    }

    public void writeOther(Stream<OrangeCupResult> data, Path path) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        try (OutputStream os = Files.newOutputStream(path); OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            new StatefulBeanToCsvBuilder<OrangeCupEntry>(writer).withMappingStrategy(new HeaderColumnNameAndOrderMappingStrategy<>(OrangeCupEntry.class)).withSeparator(';').build().write(data.filter(e -> !e.isEntry()).map(e -> e.toEntry()));
        }
    }
}
