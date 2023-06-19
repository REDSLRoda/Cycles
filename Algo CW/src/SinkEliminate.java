//Name: A.M.Rodrigo
//Student No: 20210646
//UoW ID: w1867684

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SinkEliminate {

    static HashMap<String, LinkedList<String>> adjacencyList = new HashMap<>();
    static ArrayList<String> sinkVertexes = new ArrayList<>();

    public static void main(String[] args) {

        File inputFile = new File("input.txt");
        Scanner input;
        try {
            input = new Scanner(inputFile);
        } catch (FileNotFoundException e) {
            System.err.println("Input file not found.");
            return;
        }

        int noOfEdges = 0;

        System.out.println();

        //Read input file
        while (input.hasNextLine()) {
            String inputLine = input.nextLine();
            String[] inputSplit = inputLine.split("\\s+");
            String vertex1 = inputSplit[0];
            String vertex2 = inputSplit[1];

            // Check if the vertex1 already exists in the HashMap
            if (!adjacencyList.containsKey(vertex1)) {
                adjacencyList.put(vertex1, new LinkedList<>());
            }

            // Check if the vertex2 is already in the LinkedList
            if (adjacencyList.get(vertex1).contains(vertex2)) {
                System.out.println("The edge pair '" + inputLine + "' is already added to the adjacency List.");
            } else {
                // Add the vertex2 to the LinkedList corresponding to the key
                adjacencyList.get(vertex1).add(vertex2);
                noOfEdges++;
                System.out.println("Added the edge pair '" + inputLine + "' to the adjacency List.");
            }

            // Check if the vertex2 already exists in the HashMap
            if (!adjacencyList.containsKey(vertex2)) {
                adjacencyList.put(vertex2, new LinkedList<>());
            }
        }

        //Print adjacency list before sink vertexes deleted
        System.out.println();
        System.out.println("Adjacency List:");
        printAdjacencyList();
        System.out.println("__________________________________________________________________");

        deleteSinkVertexes(noOfEdges);

        System.out.println();
        System.out.println("Is this graph acyclic?");
        if (adjacencyList.size() > 0) {
            System.out.println("No");
            printCycles();
        } else {
            System.out.println("Yes");
        }
    }

    //Print adjacency list
    public static void printAdjacencyList() {
        for (String vertex : adjacencyList.keySet()) {
            System.out.print(vertex + " : ");
            LinkedList<String> values = adjacencyList.get(vertex);
            for (int i = 0; i < values.size(); i++) {
                System.out.print(values.get(i));
                if (i < values.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
    }

    //Sink vertexes deletion
    public static void deleteSinkVertexes(int noOfEdges) {

        for (int i = 0; i < noOfEdges; i++) {
            Iterator<String> iterator = adjacencyList.keySet().iterator();
            while (iterator.hasNext()) {
                String vertex = iterator.next();
                if (adjacencyList.get(vertex).isEmpty()) {
                    sinkVertexes.add(vertex);
                    iterator.remove();
                    System.out.println();
                    System.out.println("Sink vertex '" + vertex + "' removed!");
                    System.out.println();
                }
            }

            if (!sinkVertexes.isEmpty()) {
                for (String sinkVertex : sinkVertexes) {
                    for (String vertex : adjacencyList.keySet()) {
                        LinkedList<String> values = adjacencyList.get(vertex);
                        for (int j = 0; j < values.size(); j++) {
                            if (sinkVertex.equals(values.get(j))) {
                                values.remove(j);
                                System.out.println("Removed sink vertex '" + sinkVertex + "' from adjacency list of vertex '" + vertex + "'");
                            }
                        }
                    }
                }
            }
        }

        if (adjacencyList.size() > 0) {
            System.out.println();
            System.out.println("Adjacency List (After deletion of sink vertexes):");
            printAdjacencyList();
        } else {
            System.out.println();
            System.out.println("Adjacency List (After deletion of sink vertexes):");
            System.out.println("Adjacency list is empty. All vertexes are deleted.");
        }

        System.out.println();
        System.out.println("__________________________________________________________________");
        System.out.println();
        System.out.print("Sink vertexes : ");
        for (int i = 0; i < sinkVertexes.size(); i++) {
            System.out.print(sinkVertexes.get(i));
            if (i < sinkVertexes.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }

    // Find and print all unique cycles in the graph
    public static void printCycles() {
        Set<String> visitedVertexes = new HashSet<>();
        List<String> path = new ArrayList<>();
        List<List<String>> cycles = new ArrayList<>();

        for (String vertex : adjacencyList.keySet()) {
            depthFirstSearch(vertex, visitedVertexes, path, cycles);
        }

        System.out.println();
        System.out.println("Cycles found: ");
        for (List<String> cycle : cycles) {
            System.out.print("[");
            for (int i = 0; i < cycle.size(); i++) {
                System.out.print(cycle.get(i));
                if (i < cycle.size() - 1) {
                    System.out.print("->");
                }
            }
            System.out.println("]");
        }
    }

    // Depth-first search for find cycles
    public static void depthFirstSearch(String vertex, Set<String> visited, List<String> path, List<List<String>> cycles) {
        visited.add(vertex);
        path.add(vertex);

        for (String neighborVertexes : adjacencyList.get(vertex)) {
            int index = path.indexOf(neighborVertexes);
            if (index != -1) {
                List<String> cycle = new ArrayList<>(path.subList(index, path.size()));
                cycle.add(neighborVertexes);
                if (isCycleUnique(cycles, cycle)) {
                    cycles.add(cycle);
                }
            } else if (!visited.contains(neighborVertexes)) {
                depthFirstSearch(neighborVertexes, visited, path, cycles);
            }
        }

        path.remove(path.size() - 1);
        visited.remove(vertex);
    }

    // Check the cycle is unique or not
    public static boolean isCycleUnique(List<List<String>> cycles, List<String> cycle) {
        Set<String> cycleSet = new HashSet<>(cycle);

        for (List<String> existingCycle : cycles) {
            Set<String> existingCycleSet = new HashSet<>(existingCycle);
            if (cycleSet.equals(existingCycleSet)) {
                return false;
            }
        }

        return true;
    }
}