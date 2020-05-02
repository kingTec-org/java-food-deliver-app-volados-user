package gofereats.interfaces;
/**
 * @package com.trioangle.gofereats
 * @subpackage interfaces
 * @category ActivityListener
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.res.Resources;

import gofereats.views.main.MainActivity;

/*****************************************************************
 ActivityListener
 ****************************************************************/

public interface ActivityListener {

    Resources getRes();

    MainActivity getInstance();

    //HomeActivity getInstance();

}
