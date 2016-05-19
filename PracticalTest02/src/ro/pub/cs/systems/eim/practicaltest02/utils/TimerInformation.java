package ro.pub.cs.systems.eim.practicaltest02.utils;


public class TimerInformation {
	
	private String hour;
	private String minute;
	private String status;
	
	
	public TimerInformation(String hour, String minute) {
		this.hour = hour;
		this.minute = minute;
		this.status = "inactive";
	}
	
	
	public String getHour() {
		return hour;
	}
	
	
	public void setHour(String hour) {
		this.hour = hour;
	}
	
	
	public String getMinute() {
		return minute;
	}
	
	
	public void setMinute(String minute) {
		this.minute = minute;
	}
	
	
	
	public String getStatus() {
		return status;
	}
	
	
	public void setStatus(String status) {
		this.status = status;
	}

}
