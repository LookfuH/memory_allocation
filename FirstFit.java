
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirstFit {
	private int size;    // maximum memory size in bytes (B)
	private Map<String, Partition> allocMap;   // map process to partition
	private List<Partition> partList;    // list of memory partitions
	// constructor
	public FirstFit(int size) {
		this.size = size;
		this.allocMap = new HashMap<>();
		this.partList = new ArrayList<>();
		this.partList.add(new Partition(0, size)); //add the first hole, which is the whole memory at start up
	}
	
	/* 
	// get the size of total allocated memory
	private int allocated_memory() {
		int size = 0;
		for (Partition part: partList) {
			if(!part.bFree) size += part.length;
			return size;
		}
		//TODO: add code below
		return size;
	}
      
	// get the size of total free memory
	private int free_memory() {
		int size = 0;
		for (Partition part: partList) {
			if(!part.bFree) size += part.length;
			return size;		
		}
		//TODO: add code below
		return size;
	}
    */  
	// sort the list of partitions in ascending order of base addresses
	private void order_partitions() {
		Collections.sort(partList, (o1,o2) -> o1.base - o2.base);
	}
	
	// implements the first fit memory allocation algorithm
	public int first_fit(String process, int size) 
	{
		//TODO: add code below
		if(allocMap.containsKey(process))
		{
			return -1;
		}
		int index = 0;
		int alloc = -1;
		while (index < partList.size()) 
		{
			Partition part = partList.get(index);
			if (part.bFree && part.length >= size) 
			{
				Partition allocPart = new Partition(part.base, size);
				allocPart.bFree = false;
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
			return alloc;
		}
	
      
	// release the allocated memory of a process
	public int release(String process) {
		if(allocMap.containsKey(process)) return -1;
		
		int size = -1;
		for (Partition part : partList) {
			if(!part.bFree && process.equals(part.process)) {
				part.bFree = true;
				part.process = null;
				size = part.length;
				break;
				
			}
		
		}
		if (size < 0) return size;
		
		merge_holes();
		return size;
	}      
      
	// procedure to merge adjacent holes
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
} 
