package com.hashtag;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by Surya on 6/21/2017.
 */
public class HashtagCounter {
    public static void main(String [] args) {

        // creating a writer object to write the output to output_file.txt.
        PrintWriter writer;
        try{
            writer = new PrintWriter("output_file.txt", "UTF-8");
        } catch (Exception e) {
            System.out.println("Exception in writing output file.");
            return;
        }
        long startTime = System.currentTimeMillis();

        //Using hashTable to store the tags as keys and the corresponding nodes as its value.
        Map<String, Node> nodeTable = new Hashtable<>();

        //Initializing fibonacciMaxHeap object.
        FibonacciMaxHeap fibonacciMaxHeap = new FibonacciMaxHeap();

        try {
            File f=new File(args[0]);
            BufferedReader br=new BufferedReader(new FileReader(f));
            String content;
            while((content=br.readLine())!=null){
                if (content.charAt(0) == '#') {
                    int spaceIndex = content.indexOf(" ");
                    String tag = content.substring(1, spaceIndex);
                    int value = Integer.parseInt(content.substring(spaceIndex+1, content.length()));

                    //checks in the hash table if the tag is already inserted.
                    if (nodeTable.containsKey(tag)) {
                        // if the node is already inserted, we just do increaseKey operation.
                        fibonacciMaxHeap.increaseKey(nodeTable.get(tag), value);
                    } else {
                        // if it is a new node then insert a new node into heap and
                        // accordingly add that node to hash table.
                        Node insertedNode = fibonacciMaxHeap.insert(tag, value);
                        nodeTable.put(tag, insertedNode);
                    }
                } else if (content.equalsIgnoreCase("stop")) {
                    break;
                } else {
                    int queryCount = Integer.parseInt(content);
                    List<Node> tempStoreNodes = new ArrayList<>();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < queryCount; i++) {
                        if (i != 0) {
                            sb.append(",");
                        }
                        // Calls removeMax method to remove the max  element from the heap.
                        // and temporarly stores that node to a node List.
                        Node maxNode = fibonacciMaxHeap.removeMax();
                        tempStoreNodes.add(i, maxNode);
                        sb.append(maxNode.getName());
                    }

                    // reinserting back the removed nodes from heap.
                    for (Node tempStoreNode : tempStoreNodes) {
                        fibonacciMaxHeap.insertNode(tempStoreNode, false);
                    }

                    // writes the output to a file.
                    writer.println(sb.toString());
                }
            }
            //finally closes the writer object.
            writer.close();
            long endTime = System.currentTimeMillis();
            System.out.println("Time elapsed: " + (endTime - startTime));
        } catch (IOException e) {
            System.out.println("Exception in reading from input file.");
        }
    }
}
