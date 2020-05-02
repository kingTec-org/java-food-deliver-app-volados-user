package gofereats.drawpolyline;

/**
 * @package com.trioangle.gofereats
 * @subpackage drawpolyline
 * @category PolylineOptions Interface
 * @author Trioangle Product Team
 * @version 1.5
 */

import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/*************************************************************
 Interface for draw route
 *************************************************************** */
public interface PolylineOptionsInterface {
    void getPolylineOptions(PolylineOptions output, ArrayList points, String duration);
}
