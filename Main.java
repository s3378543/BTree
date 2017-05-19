import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONObject;

public abstract class Main {

	public static void main(String[] args) {
		BufferedReader readBuff = null;
		File csv = new File("Pedestrian_volume__updated_monthly_.csv");
		int PageSize;
		
		if(args.length != 1) {
			System.out.println("Please enter desired page size as an integer in the comand line");
			System.out.println("EG: java -jar BTree.jar 4096");
			return;
		}
		else {
			PageSize = Integer.parseInt(args[0]);
		}
		
		ArrayList<BTree> trees = new ArrayList<BTree>();
		
		try {
			readBuff = new BufferedReader(new FileReader(csv));
		} catch (FileNotFoundException e1) {
			System.out.println("ERROR: 'Pedestrian_volume__updated_monthly_.csv' not found!\n Is it in the same directory this program is being run from?");
			return;
		}
		System.out.println("Building trees from 'Pedestrian_volume__updated_monthly_.csv'\n THIS MAY TAKE SOME TIME!");
		try {
			readBuff.readLine(); //Skip header line
		} catch (IOException e1) {
			System.out.println("ERROR: Could not read data from 'Pedestrian_volume__updated_monthly_.csv'");
		}
		String line = ""; //Represents a single line from the file
		while(true) {
			try {
				line = readBuff.readLine();
				if(line == null) {
					readBuff.close();
					break;
				}
				
				//Split CSV at the commas
				String[] array = line.split(",");
				JSONObject obj = new JSONObject();
				obj.put("ID", Integer.parseInt(array[0]));
				obj.put("Date_Time", array[1]);
				obj.put("Year", Integer.parseInt(array[2]));
				obj.put("Month", array[3]);
				obj.put("Mdate", Integer.parseInt(array[4]));
				obj.put("Day", array[5]);
				obj.put("Time", Integer.parseInt(array[6]));
				obj.put("Sensor_ID", Integer.parseInt(array[7]));
				obj.put("Sensor_Name", array[8]);
				obj.put("Hourly_Counts", Integer.parseInt(array[9]));
				int index = Integer.parseInt(array[9]);
				if(trees.size() == 0) {
					trees.add(new BTree(index, obj, PageSize));
				} else {
					for(BTree tree:trees) {
						try {
							tree.addNode(index, obj);
							break;
						} catch (BTreeException e) { //This exception gets thrown when trying to add to a tree that is full
						}
						if(tree == trees.get(trees.size() - 1)) {
							trees.add(new BTree(index, obj, PageSize));
							break;
						}
					}
				}
			} catch (IOException e) {
				System.out.println("ERROR: Could not read data from 'Pedestrian_volume__updated_monthly_.csv'");
			}
		}
		for(BTree tree:trees) { //Balances the trees once import is finished
			tree.balanceTree();
		}
		System.out.println("Trees successfully built!");
		System.out.println();
	}
}
