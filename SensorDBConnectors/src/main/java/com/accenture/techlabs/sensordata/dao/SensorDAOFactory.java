package com.accenture.techlabs.sensordata.dao;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SensorDAOFactory {
	public static final int TIMESERIES = 1;
	public static final int APPDATA = 2;
	public static final int BLOBDATA = 3;
	
	
	public static SensorDAO getSensorDAO(int type) { 
        if (type == TIMESERIES) {
            return new CassandraSensorDAO();
        } else {
            new NotImplementedException();
        }
		return null;
    }
}
