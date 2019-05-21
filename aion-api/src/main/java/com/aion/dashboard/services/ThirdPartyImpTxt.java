package com.aion.dashboard.services;

import com.aion.dashboard.configs.CacheConfig;
import com.aion.dashboard.repositories.EventJpaRepository;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

@Component
public class ThirdPartyImpTxt implements ThirdPartyService{
    @Autowired
    private EventJpaRepository evtRepo;

    @Value("${circulating.supply.file}")
    private String circulatingSupplyFile;

    @Override
    public String getCirculatingSupply() throws IOException {
        StringBuilder circulatingSupply = new StringBuilder();

        try (
                Reader reader = Files.newBufferedReader(Paths.get(circulatingSupplyFile));
                CSVReader csvReader = new CSVReader(reader)
        ) {
            // Finding Today's Date and Comparing it with the CSV File's Date
            ZonedDateTime today = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("UTC"))
                    .truncatedTo(ChronoUnit.DAYS);

            String[] prevRecord = new String[0];
            List<String[]> records = csvReader.readAll();
            for(int i = 0; i < records.size(); i++) {
                String[] nextRecord = records.get(i);

                if(prevRecord.length == 0) prevRecord = nextRecord;
                else prevRecord = records.get(i - 1);

                if(nextRecord[1].equals("")) continue;
                if(prevRecord[1].equals("")) prevRecord = nextRecord;

                ZonedDateTime nextDate = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("UTC"))
                        .withDayOfMonth(Integer.parseInt(nextRecord[0].split("/")[0].trim()))
                        .withMonth(Integer.parseInt(nextRecord[0].split("/")[1].trim()))
                        .withYear(Integer.parseInt(nextRecord[0].split("/")[2].trim()))
                        .truncatedTo(ChronoUnit.DAYS);

                ZonedDateTime prevDate = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("UTC"))
                        .withDayOfMonth(Integer.parseInt(prevRecord[0].split("/")[0].trim()))
                        .withMonth(Integer.parseInt(prevRecord[0].split("/")[1].trim()))
                        .withYear(Integer.parseInt(prevRecord[0].split("/")[2].trim()))
                        .truncatedTo(ChronoUnit.DAYS);

                if(today.compareTo(nextDate) == 0) {
                    circulatingSupply.append(NumberFormat.getNumberInstance(Locale.US).format(Long.parseLong(nextRecord[1])));
                    break;
                } else if(today.compareTo(prevDate) > 0  && today.compareTo(nextDate) < 0) {
                    circulatingSupply.append(NumberFormat.getNumberInstance(Locale.US).format(Long.parseLong(prevRecord[1])));
                    break;
                }
            }
        }

        return circulatingSupply.toString();
    }
}
