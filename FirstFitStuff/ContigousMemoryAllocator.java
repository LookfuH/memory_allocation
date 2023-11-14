package FirstFitStuff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ContigousMemoryAllocator {
	private int size;    // maximum memory size in bytes (B)
	private Map<String, Partition> allocMap;   // map process to partition
	private List<Partition> partList;    // list of memory partitions
	// constructor
	public ContigousMemoryAllocator(int size) {
		this.size = size;
		this.allocMap = new HashMap<>();
		this.partList = new ArrayList<>();
		this.partList.add(new Partition(0, size)); //add the first hole, which is the whole memory at start up
	}
      
	// prints the list of available commands
	public void print_help_message() {
		//TODO: add code below
		System.out.println("RQ <process> <size>: Request a memory partition with <size> to <process> ");
		System.out.println("RL: releases the memory related to the process");
		System.out.println("STAT: shows all memory part" );
		//System.out.println();
	}
      
	// prints the allocation map (free + allocated) in ascending order of base addresses
	public void print_status() {
		//TODO: add code below
		System.out.printf("Partitions [Allocated =%d B. Free=%d B] \n", allocated_memory(), free_memory());
		for (Partition part: partList) {
			System.out.printf("address [%d : %d] %s (%d KB) \n", part.getBase(), part.getBase() + part.getLength()-1, part.isbFree()? "Free": part.getProcess(), part.getLength());
		}
	}
      
	// get the size of total allocated memory
	private int allocated_memory() {
		int size = 0;
		for (Partition part: partList) {
			if(!part.isbFree()) size += part.getLength();
			return size;
				
			
		}
		//TODO: add code below
		return size;
	}
      
	// get the size of total free memory
	private int free_memory() {
		int size = 0;
		for (Partition part: partList) {
			if(!part.isbFree()) size += part.getLength();
			return size;
				
			
		}
		//TODO: add code below
		return size;
	}
      
	// sort the list of partitions in ascending order of base addresses
	private void order_partitions() {
		Collections.sort(partList, (o1,o2) -> o1.getBase() - o2.getBase());
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
			if (part.isbFree() && part.getLength() >= size) 
			{
				Partition allocPart = new Partition(part.getBase(), size);
				allocPart.setbFree(false);
				partList.add(index, allocPart); //inserts allocated into index
				allocMap.put(process, allocPart);
				part.setBase(part.getBase() + size);
				part.setLength(part.getLength() - size);
				if(part.getLength() == 0)
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
			if(!part.isbFree() && process.equals(part.getProcess())) {
				part.setbFree(true);
				part.setProcess(null);
				size = part.getLength();
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
			
			if(part.isbFree()) {
				int endAddr = part.getBase() + part.getLength()-1;
				int j = i + 1;
				while (j < partList.size() && partList.get(j).isbFree()) {
					int start_j = partList.get(j).getBase();
					if( start_j == endAddr + 1) {
						part.setLength(part.getLength() + partList.get(j).getLength());
						partList.remove(partList.get(j));
					}
					
				}
			}
		}
		
		
		
	}
	public static void main(String[] args) {
			
			//use scanners to call methods. commands are help or h, stat, exit, rq
			//Scanner sc = new Scanner(System.in);
			
			
			
		}
} 
