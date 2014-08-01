package com.accenture.sensordata.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.JodaTimePermission;

import com.google.gson.Gson;


public class DeviceObservationData {
	String deviceName;
	String sensorUUID;
	Map<String, SensorObservationData> observationValues; 
	
	
	public DeviceObservationData(String device, String uuid){
		observationValues = new HashMap<String, SensorObservationData>();
		this.deviceName = device;
		this.sensorUUID = uuid;
	}
	
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getSensorUUID() {
		return sensorUUID;
	}
	public void setSensorUUID(String sensorUUID) {
		this.sensorUUID = sensorUUID;
	}
	public Map<String, SensorObservationData> getObservationValues() {
		return observationValues;
	}
	public void setObservationValues(Map<String, SensorObservationData> observationValues) {
		this.observationValues = observationValues;
	}
	
	public void addObservationValue(String sensor, SensorObservationData obs){
		observationValues.put(sensor, obs);
	}
	
	
	public static void main(String[] args) {
		Gson gson = new Gson();
		DateTime time = DateTime.now();
		
		DeviceObservationData data = new DeviceObservationData("VM", "123");
		SensorObservationData sData = data.new SensorObservationData("memory_sensor");
		List<Object> obs = new ArrayList<Object>();
		obs.add(55);
		sData.addSensorEntry(time, obs);
		data.addObservationValue("memory_sensor", sData);;
		String json = gson.toJson(data);
		System.out.println(json);
		DeviceObservationData xData  = gson.fromJson(json, DeviceObservationData.class);
		System.out.println(xData.getObservationValues().get("memory_sensor"));
	}
	
	public class Pair<K, V>{
		K time;
		V observation;
		
		public Pair(K key, V value){
			this.time = key;
			this.observation = value;
		}
		
		public Object getKey() {
			return time;
		}
		public void setKey(K key) {
			this.time = key;
		}
		public V getValue() {
			return observation;
		}
		public void setValue(V value) {
			this.observation = value;
		}
		
		
	}
	
	public class SensorObservationData{
		String property;
		List<Pair<Long, List<Object>>> sensorData;
		
		public SensorObservationData(String property){
			this.property = property;
			sensorData = new ArrayList<Pair<Long,List<Object>>>();
		
		}
		
		public void addSensorEntry(DateTime time, List<Object> observations){
			sensorData.add(new Pair<Long, List<Object>>(time.getMillis(), observations));
		}
		
		public String getSensorName() {
			return property;
		}
		public void setSensorName(String sensorName) {
			this.property = sensorName;
		}
		public List<Pair<Long, List<Object>>> getSensorData() {
			return sensorData;
		}
		public void setSensorData(List<Pair<Long, List<Object>>> sensorData) {
			this.sensorData = sensorData;
		}
		
	}
}
