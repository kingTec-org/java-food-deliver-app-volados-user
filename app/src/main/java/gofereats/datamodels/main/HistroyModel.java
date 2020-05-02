package gofereats.datamodels.main;


/**
 * @package com.trioangle.gofereats
 * @subpackage datamodels.main.home
 * @category Model Class
 * @author Trioangle Product Team
 * @version 1.0
 * <p>
 * Created by trioangle on 5/26/18.
 * <p>
 * Created by trioangle on 5/26/18.
 * <p>
 * Created by trioangle on 5/26/18.
 * <p>
 * Created by trioangle on 5/26/18.
 * <p>
 * Created by trioangle on 5/26/18.
 * <p>
 * Created by trioangle on 5/26/18.
 * <p>
 * Created by trioangle on 5/26/18.
 * <p>
 * Created by trioangle on 5/26/18.
 * <p>
 * Created by trioangle on 5/26/18.
 */


/**
 * Created by trioangle on 5/26/18.
 */


/*****************************************************************
 Histroy Model Class
 ****************************************************************/


public class HistroyModel {
    private String driver_name;
    private String status;

    public HistroyModel() {
    }

    public HistroyModel(String driver_name, String status) {
        this.driver_name = driver_name;
        this.status = status;
    }

    public String getDriverName() {
        return driver_name;
    }

    public void setDriverName(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
