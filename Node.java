import java.util.ArrayList;

import org.json.JSONObject;

public class Node {
	private int Index;
	private Node LeftNode;
	private Node RightNode;
	
	private ArrayList<JSONObject> rids;
	
	public Node(JSONObject rid) {
		this.Index = rid.getInt("Hourly_Counts"); //This defines the "Hourly_Counts" as being the indexed value
		this.LeftNode = null;
		this.RightNode = null;
		
		this.rids = new ArrayList<JSONObject>();
		this.rids.add(rid);
	}
	
	//\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\\
	
	public void addNewData(JSONObject data) {
		this.rids.add(data);
	}
	
	public ArrayList<Integer> getID() {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for(JSONObject obj:this.rids) {
			ids.add(obj.getInt("ID"));
		}
		return ids;
	}
	
	public ArrayList<String> getDate_Time() {
		ArrayList<String> date_times = new ArrayList<String>();
		for(JSONObject obj:this.rids) {
			date_times.add(obj.getString("Date_Time"));
		}
		return date_times;
	}
	public ArrayList<Integer> getYear() {
		ArrayList<Integer> years = new ArrayList<Integer>();
		for(JSONObject obj:this.rids) {
			years.add(obj.getInt("Year"));
		}
		return years;
	}
	public ArrayList<String> getMonth() {
		ArrayList<String> months = new ArrayList<String>();
		for(JSONObject obj:this.rids) {
			months.add(obj.getString("Month"));
		}
		return months;
	}
	public ArrayList<Integer> getMdate() {
		ArrayList<Integer> mDates = new ArrayList<Integer>();
		for(JSONObject obj:this.rids) {
			mDates.add(obj.getInt("Mdate"));
		}
		return mDates;
	}
	public ArrayList<String> getDay() {
		ArrayList<String> days = new ArrayList<String>();
		for(JSONObject obj:this.rids) {
			days.add(obj.getString("Month"));
		}
		return days;
	}
	public ArrayList<Integer> getTime() {
		ArrayList<Integer> times = new ArrayList<Integer>();
		for(JSONObject obj:this.rids) {
			times.add(obj.getInt("Time"));
		}
		return times;
	}
	public ArrayList<Integer> getSensor_ID() {
		ArrayList<Integer> sensor_ids = new ArrayList<Integer>();
		for(JSONObject obj:this.rids) {
			sensor_ids.add(obj.getInt("Sensor_ID"));
		}
		return sensor_ids;
	}
	public ArrayList<String> getSensor_Name() {
		ArrayList<String> sensor_names = new ArrayList<String>();
		for(JSONObject obj:this.rids) {
			sensor_names.add(obj.getString("Month"));
		}
		return sensor_names;
	}
	public ArrayList<Integer> getHourly_Counts() {
		ArrayList<Integer> hourly_counts = new ArrayList<Integer>();
		for(JSONObject obj:this.rids) {
			hourly_counts.add(obj.getInt("Hourly_Counts"));
		}
		return hourly_counts;
	}
	
	//\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\\
	
	public int getIndex() {
		return this.Index;
	}
	
	public void setLeft(Node left) {
		this.LeftNode = left;
	}
	
	public void setRight(Node right) {
		this.RightNode = right;
	}
	
	public Node getLeft() {
		return this.LeftNode;
	}
	
	public Node getRight() {
		return this.RightNode;
	}
	
	public void linkNode (Node newNode) {
		if(newNode.getIndex() <= this.Index) {
			if(this.LeftNode == null) {
				this.LeftNode = newNode;
			} else {
				this.LeftNode.linkNode(newNode);
			}
		} else {
			if(this.RightNode == null) {
				this.RightNode = newNode;
			} else {
				this.RightNode.linkNode(newNode);
			}
		}
	}
}
