package bullyv1;

import java.util.ArrayList;

public class Data {
	private String ip,port,id;

	public Data(String ip2, String port2) {
		setIp(ip2);
		setPort(port2);
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public static boolean containsIp(String ip,String port, ArrayList <Data> data){
		for(Data d :data){
			if(d.getIp().equals(ip) && d.getPort().equals(port))
				return true;
		}
		
		return false;
	}
	
	public static Data myData(String ip,String port,ArrayList <Data> data){
		for(Data d :data){
			if(d.getIp().equals(ip) && d.getPort().equals(port))
				return d;
		}
		return null;
	}
	
	public static Data myData(String ip,ArrayList <Data> data){
		for(Data d :data){
			if(d.getIp().equals(ip))
				return d;
		}
		return null;
	}
	
	public static Data myData(int pid, ArrayList <Data> data){
		for(Data d :data){
			if(d.getId().equals(Integer.toString(pid)))
				return d;
		}
		return null;
	}
	
	

}
