package com.mj.mysns.common;

import static org.geolatte.geom.builder.DSL.c;
import static org.geolatte.geom.builder.DSL.g;
import static org.geolatte.geom.builder.DSL.point;
import static org.geolatte.geom.crs.CoordinateReferenceSystems.WGS84;
import static org.geolatte.geom.crs.CrsRegistry.getProjectedCoordinateReferenceSystemForEPSG;

import geotrellis.proj4.CRS;
import geotrellis.proj4.Transform;
import org.geolatte.geom.C2D;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.geolatte.geom.crs.ProjectedCoordinateReferenceSystem;
import scala.Tuple2;

public class LocationUtils {

    private static final int EPSG_5179 = 5179;
    private static final int EPSG_4326 = 4326;
    private static final ProjectedCoordinateReferenceSystem CRS_5179 =
        getProjectedCoordinateReferenceSystemForEPSG(EPSG_5179);
    private static final double DEFAULT_LAT = 37.59554172008826;
    private static final double DEFAULT_LON = 126.96567699902982;

    public static Point<C2D> toEpsg5179(double longitude, double latitude) {
        Point<G2D> from = point(WGS84, g(longitude, latitude));

        var t = Transform.apply(CRS.fromEpsgCode(EPSG_4326), CRS.fromEpsgCode(EPSG_5179));
        Tuple2<Object, Object> to = t.apply(from.getPosition().getLon(),
            from.getPosition().getLat());
        return point(CRS_5179, c((Double) to._1(), (Double) to._2()));
    }

    public static Point<C2D> toEpsg5179(Point<G2D> from) {

        var t = Transform.apply(CRS.fromEpsgCode(EPSG_4326), CRS.fromEpsgCode(EPSG_5179));
        Tuple2<Object, Object> to = t.apply(from.getPosition().getLon(),
            from.getPosition().getLat());
        return point(CRS_5179, c((Double) to._1(), (Double) to._2()));
    }


    public static Point<G2D> toEpsg4326(double x, double y) {
        Point<C2D> from = point(CRS_5179, c(x, y));

        var t = Transform.apply(CRS.fromEpsgCode(EPSG_5179), CRS.fromEpsgCode(EPSG_4326));
        Tuple2<Object, Object> to = t.apply(from.getPosition().getX(), from.getPosition().getY());
        return point(WGS84, g((Double) to._1(), (Double) to._2()));
    }

    public static Point<G2D> toEpsg4326(Point<C2D> from) {

        var t = Transform.apply(CRS.fromEpsgCode(EPSG_5179), CRS.fromEpsgCode(EPSG_4326));
        Tuple2<Object, Object> to = t.apply(from.getPosition().getX(), from.getPosition().getY());
        return point(WGS84, g((Double) to._1(), (Double) to._2()));
    }
}
