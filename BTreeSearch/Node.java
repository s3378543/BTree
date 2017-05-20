import java.util.ArrayList;

import org.json.JSONObject;

public class Node {
	public int Index;
	public Node LeftNode;
	public Node RightNode;
	public int Height;
	
	private ArrayList<JSONObject> Rids;
	
	public Node(int ind, ArrayList<JSONObject> rids) {
		this.Index = ind; //This defines the "Hourly_Counts" as being the indexed value
		this.LeftNode = null;
		this.RightNode = null;
		this.Height = 0;
		
		this.Rids = new ArrayList<JSONObject>();
		this.Rids = rids;
	}
	
	//\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\\
	
	public void addNewData(JSONObject data) {
		this.Rids.add(data);
	}
	
	public ArrayList<JSONObject> getRids() {
		return this.Rids;
	}
	
	public ArrayList<Integer> getID() {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for(JSONObject obj:this.Rids) {
			ids.add(obj.getInt("ID"));
		}
		return ids;
	}
	
	public ArrayList<String> getDate_Time() {
		ArrayList<String> date_times = new ArrayList<String>();
		for(JSONObject obj:this.Rids) {
			date_times.add(obj.getString("Date_Time"));
		}
		return date_times;
	}
	public ArrayList<Integer> getYear() {
		ArrayList<Integer> years = new ArrayList<Integer>();
		for(JSONObject obj:this.Rids) {
			years.add(obj.getInt("Year"));
		}
		return years;
	}
	public ArrayList<String> getMonth() {
		ArrayList<String> months = new ArrayList<String>();
		for(JSONObject obj:this.Rids) {
			months.add(obj.getString("Month"));
		}
		return months;
	}
	public ArrayList<Integer> getMdate() {
		ArrayList<Integer> mDates = new ArrayList<Integer>();
		for(JSONObject obj:this.Rids) {
			mDates.add(obj.getInt("Mdate"));
		}
		return mDates;
	}
	public ArrayList<String> getDay() {
		ArrayList<String> days = new ArrayList<String>();
		for(JSONObject obj:this.Rids) {
			days.add(obj.getString("Month"));
		}
		return days;
	}
	public ArrayList<Integer> getTime() {
		ArrayList<Integer> times = new ArrayList<Integer>();
		for(JSONObject obj:this.Rids) {
			times.add(obj.getInt("Time"));
		}
		return times;
	}
	public ArrayList<Integer> getSensor_ID() {
		ArrayList<Integer> sensor_ids = new ArrayList<Integer>();
		for(JSONObject obj:this.Rids) {
			sensor_ids.add(obj.getInt("Sensor_ID"));
		}
		return sensor_ids;
	}
	public ArrayList<String> getSensor_Name() {
		ArrayList<String> sensor_names = new ArrayList<String>();
		for(JSONObject obj:this.Rids) {
			sensor_names.add(obj.getString("Month"));
		}
		return sensor_names;
	}
	public ArrayList<Integer> getHourly_Counts() {
		ArrayList<Integer> hourly_counts = new ArrayList<Integer>();
		for(JSONObject obj:this.Rids) {
			hourly_counts.add(obj.getInt("Hourly_Counts"));
		}
		return hourly_counts;
	}
}
