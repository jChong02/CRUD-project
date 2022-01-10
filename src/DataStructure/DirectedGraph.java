package DataStructure;
/**
 * DirectedGraph.java 
 * CIS22C_Spring 2020_FINAL PART 1
 * This Class has all necessary methods for a functional of a directed graph
 * 
 * GROUP:22C||!22C
 * @author mastersam
 * @author Chong Jun Jie
 * @author Tun Pyay Sone LIN
 * 
 * Additional methods from startter code: 
 * 
 * public boolean removeEdge(T begin, T end)
 * public void displayAdjacentVertices(T begin)
 * public void writeFriendList(T begin, PrintWriter out)
 * public void displayMutualFriend(T begin)
 * public LinkedBag<T> commonVerticesBag(T origin)
 * public void printMutualVertices(T origin, T otherVertex)
 * public void deleteProfileFromFriends(T begin)
 * public int getVerticesCount()
 */

import java.io.PrintWriter;
import java.util.Iterator;

public class DirectedGraph<T> implements GraphInterface<T> {
	private DictionaryInterface<T, VertexInterface<T>> vertices;
	
	private int edgeCount;

	public DirectedGraph() {
		vertices = new LinkedDictionary<>();
		edgeCount = 0;
	} // end default constructor

	public boolean addVertex(T vertexLabel)
	{
		VertexInterface<T> addOutcome = vertices.add(vertexLabel, new Vertex<>(vertexLabel));
		return addOutcome == null;
	} //end vertex
	
	public boolean addEdge(T begin, T end, double edgeWeight)
	{
		boolean result = false;
		
		VertexInterface<T> beginVertex = vertices.getValue(begin);
		VertexInterface<T> endVertex = vertices.getValue(end);
		
		if((beginVertex != null) && (endVertex != null))
			result = beginVertex.connect(endVertex, edgeWeight);
		
		if(result)
			edgeCount++;
		
		return result;
	} //end addEdge
	
	public boolean addEdge(T begin, T end)
	{
		return addEdge(begin, end, 0);
	}
	
	public boolean hasEdge(T begin, T end)
	{
		boolean found = false;
		
		VertexInterface<T> beginVertex = vertices.getValue(begin);
		VertexInterface<T> endVertex = vertices.getValue(end);
		
		if((beginVertex != null) && (endVertex != null)) 
		{
			Iterator<VertexInterface<T>> neighbors = beginVertex.getNeighborIterator();
			while(!found && neighbors.hasNext())
			{
				VertexInterface<T> nextNeighbor = neighbors.next();
				if(endVertex.equals(nextNeighbor))
					found = true;
			}
		}
		return found;
	} //end hasEdge
	
	
	public boolean isEmpty()
	{
		return vertices.isEmpty();
	}
	
	public void clear()
	{
		vertices.clear();
		edgeCount = 0;
	}
	
	public int getNumberOfVertices()
	{
		return vertices.getSize();
	}
	
	public int getNumberOfEdges()
	{
		return edgeCount;
	}
	
	protected void resetVertices() {
		Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();
		while (vertexIterator.hasNext()) {
			VertexInterface<T> nextVertex = vertexIterator.next();
			nextVertex.unvisit();
			nextVertex.setCost(0);
			nextVertex.setPredecessor(null);
		} // end while
	} // end resetVertices
	
	
	public QueueInterface<T> getBreadthFirstTraversal(T origin)
	{
		resetVertices();
		QueueInterface<T> traversalOrder = new LinkedQueue<>();
		QueueInterface<VertexInterface<T>> vertexQueue = new LinkedQueue<>();
		
		VertexInterface<T> originVertex = vertices.getValue(origin);
		originVertex.visit();
		traversalOrder.enqueue(origin);
		vertexQueue.enqueue(originVertex);
		
		while(!vertexQueue.isEmpty())
		{
			VertexInterface<T> frontVertex = vertexQueue.dequeue();
			Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator();
			while(neighbors.hasNext())
			{
				VertexInterface<T> nextNeighbor = neighbors.next();
				if(!nextNeighbor.isVisited())
				{
					nextNeighbor.visit();
					traversalOrder.enqueue(nextNeighbor.getLabel());
					vertexQueue.enqueue(nextNeighbor);
				}
			}
		}
		return traversalOrder;
	} //end getBreadthFirstTraversal

	public QueueInterface<T> getDepthFirstTraversal(T origin) {
		// Assumes graph is not empty
		resetVertices();
		QueueInterface<T> traversalOrder = new LinkedQueue<T>();
		StackInterface<VertexInterface<T>> vertexStack = new LinkedStack<>();

		VertexInterface<T> originVertex = vertices.getValue(origin);
		originVertex.visit();
		traversalOrder.enqueue(origin); // Enqueue vertex label
		vertexStack.push(originVertex); // Enqueue vertex

		while (!vertexStack.isEmpty()) {
			VertexInterface<T> topVertex = vertexStack.peek();
			VertexInterface<T> nextNeighbor = topVertex.getUnvisitedNeighbor();

			if (nextNeighbor != null) {
				nextNeighbor.visit();
				traversalOrder.enqueue(nextNeighbor.getLabel());
				vertexStack.push(nextNeighbor);
			} else // All neighbors are visited
				vertexStack.pop();
		} // end while

		return traversalOrder;
	} // end getDepthFirstTraversal

	public StackInterface<T> getTopologicalOrder() {
		resetVertices();

		StackInterface<T> vertexStack = new LinkedStack<>();
		int numberOfVertices = getNumberOfVertices();
		for (int counter = 1; counter <= numberOfVertices; counter++) {
			VertexInterface<T> nextVertex = findTerminal();
			nextVertex.visit();
			vertexStack.push(nextVertex.getLabel());
		} // end for

		return vertexStack;
	} // end getTopologicalOrder

	/** Precondition: path is an empty stack (NOT null) */
	public int getShortestPath(T begin, T end, StackInterface<T> path) {
		resetVertices();
		boolean done = false;
		QueueInterface<VertexInterface<T>> vertexQueue = new LinkedQueue<>();

		VertexInterface<T> originVertex = vertices.getValue(begin);
		VertexInterface<T> endVertex = vertices.getValue(end);

		originVertex.visit();
		// Assertion: resetVertices() has executed setCost(0)
		// and setPredecessor(null) for originVertex

		vertexQueue.enqueue(originVertex);

		while (!done && !vertexQueue.isEmpty()) {
			VertexInterface<T> frontVertex = vertexQueue.dequeue();

			Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator();
			while (!done && neighbors.hasNext()) {
				VertexInterface<T> nextNeighbor = neighbors.next();

				if (!nextNeighbor.isVisited()) {
					nextNeighbor.visit();
					nextNeighbor.setCost(1 + frontVertex.getCost());
					nextNeighbor.setPredecessor(frontVertex);
					vertexQueue.enqueue(nextNeighbor);
				} // end if

				if (nextNeighbor.equals(endVertex))
					done = true;
			} // end while
		} // end while

		// Traversal ends; construct shortest path
		int pathLength = (int) endVertex.getCost();
		path.push(endVertex.getLabel());

		VertexInterface<T> vertex = endVertex;
		while (vertex.hasPredecessor()) {
			vertex = vertex.getPredecessor();
			path.push(vertex.getLabel());
		} // end while

		return pathLength;
	} // end getShortestPath

	/** Precondition: path is an empty stack (NOT null) */
	public double getCheapestPath(T begin, T end, StackInterface<T> path) {
		resetVertices();
		boolean done = false;

		// Use EntryPQ instead of Vertex because multiple entries contain
		// the same vertex but different costs - cost of path to vertex is EntryPQ's priority value
		PriorityQueueInterface<EntryPQ> priorityQueue = new HeapPriorityQueue<>();

		VertexInterface<T> originVertex = vertices.getValue(begin);
		VertexInterface<T> endVertex = vertices.getValue(end);

		priorityQueue.add(new EntryPQ(originVertex, 0, null));

		while (!done && !priorityQueue.isEmpty()) {
			EntryPQ frontEntry = priorityQueue.remove();
			VertexInterface<T> frontVertex = frontEntry.getVertex();

			if (!frontVertex.isVisited()) {
				frontVertex.visit();
				frontVertex.setCost(frontEntry.getCost());
				frontVertex.setPredecessor(frontEntry.getPredecessor());

				if (frontVertex.equals(endVertex))
					done = true;
				else {
					Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator();
					Iterator<Double> edgeWeights = frontVertex.getWeightIterator();
					while (neighbors.hasNext()) {
						VertexInterface<T> nextNeighbor = neighbors.next();
						Double weightOfEdgeToNeighbor = edgeWeights.next();

						if (!nextNeighbor.isVisited()) {
							double nextCost = weightOfEdgeToNeighbor + frontVertex.getCost();
							priorityQueue.add(new EntryPQ(nextNeighbor, nextCost, frontVertex));
						} // end if
					} // end while
				} // end if
			} // end if
		} // end while

		// Traversal ends, construct cheapest path
		double pathCost = endVertex.getCost();
		path.push(endVertex.getLabel());

		VertexInterface<T> vertex = endVertex;
		while (vertex.hasPredecessor()) {
			vertex = vertex.getPredecessor();
			path.push(vertex.getLabel());
		} // end while

		return pathCost;
	} // end getCheapestPath

	protected VertexInterface<T> findTerminal() {
		boolean found = false;
		VertexInterface<T> result = null;

		Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();

		while (!found && vertexIterator.hasNext()) {
			VertexInterface<T> nextVertex = vertexIterator.next();

			// If nextVertex is unvisited AND has only visited neighbors)
			if (!nextVertex.isVisited()) {
				if (nextVertex.getUnvisitedNeighbor() == null) {
					found = true;
					result = nextVertex;
				} // end if
			} // end if
		} // end while

		return result;
	} // end findTerminal

	// Used for testing
	public void displayEdges() {
		System.out.println("\nEdges exist from the first vertex in each line to the other vertices in the line.");
		System.out.println("(Edge weights are given; weights are zero for unweighted graphs):\n");
		Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();
		while (vertexIterator.hasNext()) {
			((Vertex<T>) (vertexIterator.next())).display();
		} // end while
	} // end displayEdges

	private class EntryPQ implements Comparable<EntryPQ> {
		private VertexInterface<T> vertex;
		private VertexInterface<T> previousVertex;
		private double cost; // cost to nextVertex

		private EntryPQ(VertexInterface<T> vertex, double cost, VertexInterface<T> previousVertex) {
			this.vertex = vertex;
			this.previousVertex = previousVertex;
			this.cost = cost;
		} // end constructor

		public VertexInterface<T> getVertex() {
			return vertex;
		} // end getVertex

		public VertexInterface<T> getPredecessor() {
			return previousVertex;
		} // end getPredecessor

		public double getCost() {
			return cost;
		} // end getCost

		public int compareTo(EntryPQ otherEntry) {
			// Using opposite of reality since our priority queue uses a maxHeap;
			// could revise using a minheap
			return (int) Math.signum(otherEntry.cost - cost);
		} // end compareTo

		public String toString() {
			return vertex.toString() + " " + cost;
		} // end toString
	} // end EntryPQ
	
	/**
	 * 
	 * @param begin - the value of vertex of interest 
	 * @param end - the value of the other vertex of interest
	 * @return isSuccessful - boolean - whether the add is successful
	 */
	
	public boolean removeEdge(T begin, T end)
	{
		boolean isSuccessful = false;
		if(hasEdge(begin, end))
		{
			VertexInterface<T> beginVertex = vertices.getValue(begin);
			beginVertex.remove(end);
			isSuccessful = true;
		}
		return isSuccessful;
	}
	
	/**
	 * This method will display the adjacent vertices (7 per row)
	 * @param begin - the value of vertex of interest
	 */
	
	public void displayAdjacentVertices(T begin) {
        Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();
        int count = 0;
        while (vertexIterator.hasNext()) {
            T vertexNext = vertexIterator.next().getLabel();
            if (hasEdge(begin,vertexNext)) {
                System.out.print("<" + vertexNext + "> "); 
                count++;
                //Display 7 vertices per line
                if (count%7 == 0)
                {
                	System.out.print("\n");
                }
            }
            
        }
    }
	
	/**\
	 * This method will write the adjacent vertices of the vertex of interest
	 * into a file of choice with some format control.
	 * @param begin - the value of vertex of interest
	 * @param out - PrintWriter for write to file
	 */
	public void writeAdjacentVertices(T begin, PrintWriter out) {
        Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();
        while (vertexIterator.hasNext()) {
            T vertexNext = vertexIterator.next().getLabel();
            if (hasEdge(begin,vertexNext)) {
                out.print(vertexNext + ":"); 
            }
        }
    }
	
	/**
	 * This methods will store all of the far neighbors into a Linked Bag
	 * @param origin - the value of vertex of interest
	 * @return commonVertices - LinkedBag <T> the Bag that contains values of 
	 * neighbors of neighbors (far-neighbors)
	 */
	public LinkedBag<T> farNeighborsBag(T origin) {
        LinkedBag<T> farNeighbors = new LinkedBag<T>();
        T farNeighbor;
        T neighbor;

        VertexInterface<T> selfVertex = vertices.getValue(origin);
        Iterator<VertexInterface<T>> neighbors = selfVertex.getNeighborIterator();

        while (neighbors.hasNext()) {
        	neighbor = neighbors.next().getLabel();
            VertexInterface<T> friendVertex = vertices.getValue(neighbor);
            Iterator<VertexInterface<T>> suggestedFriend = friendVertex.getNeighborIterator();
            if(friendVertex.hasNeighbor())
            {
                while(suggestedFriend.hasNext()) {
                	farNeighbor = suggestedFriend.next().getLabel();
                    if(vertices.getValue(origin) != vertices.getValue(farNeighbor) && !hasEdge(origin,farNeighbor))
                    	farNeighbors.add(farNeighbor);
                }
            }
        }

        return farNeighbors;
    }
	
	/**
	 * This method will display the value of common vertices that the origin
	 * and the otherVertex share 
	 * @param begin - value of the vertex of interest
	 * @param friend - value of the adjacent vertex of the vertex of interest
	 */
	public void printMutualVertices(T origin, T otherVertex) { 
        int count = 0;
        String display = "";

        Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();
        while (vertexIterator.hasNext()) {
            T vertexNext = vertexIterator.next().getLabel();
            if (hasEdge(origin, vertexNext) && hasEdge(otherVertex, vertexNext)) {

            	display += "<" + vertexNext + "> ";
                //System.out.print("<" + vertexNext + "> ");
                count ++;
            }
            }
        System.out.println("\nNumber of mutual friends: " + count);
        System.out.println(display);
    }
	/**
	 * This method will remove all the edges associated with the vertex of interest
	 * then remove the vertex from the vertices dictionary
	 * @param begin - value of the vertex of interest
	 */
	public void removeVerticeFromAdjacentVertices(T begin) {
        Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();
        while (vertexIterator.hasNext()) {
            T vertexNext = vertexIterator.next().getLabel();
            if (hasEdge(begin, vertexNext)) {
                removeEdge(begin, vertexNext); // remove friend
                removeEdge(vertexNext, begin);
            }
        }
        vertices.remove(begin);
    }
	
	/**
	 * This method return the number of account in the graph
	 * @return - the number of account in the graph
	 */
	public int getVerticesCount() {
		return vertices.getSize(); 
	}
	
	

	/**
	 * This method will display the far neighbor (neighbor of neighbor) of vertex of interest
	 * @param begin - the value of vertex of interest
	 */
	
	/*public void displayFarNeighbor(T begin) { 
		Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();
		StackInterface<T> path = new LinkedStack <T>();
    
		while (vertexIterator.hasNext()) {
			T vertexNext = vertexIterator.next().getLabel();
			if (getShortestPath(begin,vertexNext, path) > 1) {
				System.out.print( "<" + vertexNext + ">");
			}
		}
	}*/
	
} // end DirectedGraph