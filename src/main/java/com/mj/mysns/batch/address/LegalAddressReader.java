package com.mj.mysns.batch.address;

import static org.geolatte.geom.crs.CoordinateReferenceSystems.WGS84;

import com.mj.mysns.location.entity.Address;
import com.mj.mysns.location.entity.AddressMetadata;
import com.mj.mysns.location.entity.Geodata;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.geolatte.geom.C2D;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.Point;
import org.geolatte.geom.codec.Wkt;
import org.geolatte.geom.crs.CrsRegistry;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;

public class LegalAddressReader extends FlatFileItemReader<Address> {

    public LegalAddressReader(Path path) {
        setResource(new FileSystemResource(path));
        // 컬럼명 1행 제거
        setLinesToSkip(1);

        DefaultLineMapper<Address> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter("|");

        // 'code', 'c1', 'c2', 'c3', 'c4', 'order', 'created_at', 'deleted_at', 'prev_code', 'eng_name', 'name', 'geometry',
        tokenizer.setNames("index", "code", "c1", "c2", "c3", "c4", "order", "created_at", "deleted_at",
            "prev_code", "eng_name", "name", "geometry", "geo_g", "p_center", "g_center");
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSet -> {
//            WKTReader wktReader = new WKTReader();
//            Geometry geometry = null;
//            try {
//                geometry = wktReader.read(fieldSet.readString("geometry"));
//            } catch (ParseException e) {
//                throw new RuntimeException(e);
//            }
//            // EPSG:4326 좌표계를 세팅해서 저장해야함
//            geometry.setSRID(4326);

            // geolatte
            Geometry<G2D> geoG = Wkt.fromWkt(fieldSet.readString("geo_g"),
                WGS84);
            Point<G2D> centerG = (Point<G2D>) Wkt.fromWkt(fieldSet.readString("g_center"),
                WGS84);


            Geometry<C2D> geoP = Wkt.fromWkt(fieldSet.readString("geometry"),
                CrsRegistry.getProjectedCoordinateReferenceSystemForEPSG(5179));
            Point<C2D> centerP = (Point<C2D>) Wkt.fromWkt(fieldSet.readString("p_center"),
                CrsRegistry.getProjectedCoordinateReferenceSystemForEPSG(5179));


            return Address.builder()
                .code(fieldSet.readString("code"))
                .sido(fieldSet.readString("c1"))
                .gungu(fieldSet.readString("c2"))
                .eupmyundong(fieldSet.readString("c3"))
                .li(fieldSet.readString("c4"))
                .metadata(AddressMetadata.builder()
                    .sunwi(fieldSet.readString("order"))
                    .metaCreatedAt(fieldSet.readString("created_at").isEmpty() ? null
                        : LocalDate.parse(fieldSet.readString("created_at"),
                                DateTimeFormatter.ISO_LOCAL_DATE)
                            .atStartOfDay())
                    .metaDeletedAt(fieldSet.readString("deleted_at").isEmpty() ? null
                        : LocalDate.parse(fieldSet.readString("deleted_at"),
                            DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay())
                    .prevCode(fieldSet.readString("prev_code"))
                    .build())
                .geo(Geodata.builder()
                    .geoG(geoG)
                    .centerG(centerG)
                    .geoP(geoP)
                    .centerP(centerP)
                    .build())
                .build();
        });
        setLineMapper(lineMapper);
    }
}
