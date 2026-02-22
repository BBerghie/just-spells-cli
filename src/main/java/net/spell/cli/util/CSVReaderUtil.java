package net.spell.cli.util;

import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class CSVReaderUtil {

    public static <T> List<T> readCSV(String path, Class<T> objectclass) throws FileNotFoundException {
            return new CsvToBeanBuilder<T>(new FileReader(ResourceUtils.getFile(path)))
                    .withType(objectclass).withSeparator(';').build().parse();
    }
}
