import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

//find biggest and add it there
public class WorstFit{

    private int size;    // maximum memory size in bytes (B)
	private HashMap<String, Partition> allocMap;   // map process to partition
	private ArrayList<Partition> partList;    // list of memory partitions

    public WorstFit (int size) {
        this.size = size;
		this.allocMap = new HashMap<>();
		this.partList = new ArrayList<>();
		this.partList.add(new Partition(0, size));

        
        //add processes (TODO: THIS IS TEMPORARY). 
        add("first", 10);
		
        add("second", 550); 
		
        add("thrid", 10);
		remove("second");

		add("forth", 50);
        
    }
    
    public int add (String process, int size) {
        //TODO: add code below
		if(allocMap.containsKey(process))
		{
			return -1;
		}

		int index = 0;
		int alloc = -1; 
		int max = isLargest();

		if (max < size){
			// TODO: cant fit into array so will put into a queue

		}
		while (index < partList.size()) 
		{
            // if (index >= partList.size()) {
            //     index = 0;
            // }

			Partition part = partList.get(index);
			

			if (part.bFree &&  part.length == max) 
			{
				//TODO find a way to make the base dynamic
                //change to be Nxtfit
				Partition allocPart = new Partition(part.base, size);
				allocPart.bFree = false;
                allocPart.process = process;
				partList.add(index, allocPart); //inserts allocated into index
				allocMap.put(process, allocPart);
				part.base = part.base + size;
				part.length = part.length - size;
				if(part.length == 0)
					partList.remove(part);
				alloc = size;
				break;
			
				
			}
			index++;
		}
            print();
			return alloc;
    }

	public int isLargest(){
		int max = -1;
		for (Partition part : partList){
			if(part.bFree && part.length > max){
				max = part.length;
			}
			
		}
		return max;
	}


    private void order_partitions() {
		Collections.sort(partList, (o1,o2) -> o1.base - o2.base);
	}

	public int remove (String process) {
        if(!allocMap.containsKey(process)) { System.err.println("FAILED TO REMOVE :("); return -1;}
		
		int size = -1;
		for (Partition part : partList) {
			if(!part.bFree && process.equals(part.process)) {
				part.bFree = true;
				part.process = "Free Space";
				size = part.length;
                print();
				break;
			}
		}
		if (size < 0) { print(); return size; }
		
		merge_holes();
        print();
		return size;
    }

    private void merge_holes() {
		//TODO: add code below
		order_partitions();
		int i = 0;
		while(i < partList.size()) {
			Partition part = partList.get(i);
			
			if(part.bFree) {
				int endAddr = part.base + part.length-1;
				int j = i + 1;
				while (j < partList.size() && partList.get(j).bFree) {
					int start_j = partList.get(j).base;
					if( start_j == endAddr + 1) {
						part.length = part.length + partList.get(j).length;
						partList.remove(partList.get(j));
					}
					
				}
			}
		}
    }

    public void print() {
        String print = "";
        for (Partition part : partList) {
            print += part.toString() + " \n ";
        }
        System.out.println(print);
    }
}