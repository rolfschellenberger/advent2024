package com.rolf.util

import java.util.*

open class Graph<T> {

    private val vertices = mutableMapOf<String, Vertex<T>>()
    private val edges = mutableMapOf<String, MutableSet<Edge>>()

    fun addVertex(vertex: Vertex<T>) {
        vertices[vertex.id] = vertex
    }

    fun getVertex(id: String): Vertex<T>? {
        return vertices[id]
    }

    fun hasVertex(id: String): Boolean {
        return vertices.containsKey(id)
    }

    fun vertices(): List<Vertex<T>> {
        return vertices.map { it.value }
    }

    fun addEdge(source: String, destination: String, edgeType: EdgeType = EdgeType.DIRECTED, weight: Double = 1.0) {
        addDirectedEdge(source, destination, weight)
        if (edgeType == EdgeType.UNDIRECTED) {
            addDirectedEdge(destination, source, weight)
        }
    }

    private fun addDirectedEdge(source: String, destination: String, weight: Double = 1.0) {
        val s = getVertex(source)
        val d = getVertex(destination)
        if (s != null && d != null) {
            val edgeList = edges.getOrDefault(source, mutableSetOf())
            // Overwrite edges
            edgeList.remove(Edge(source, destination, weight))
            edgeList.add(Edge(source, destination, weight))
            edges[source] = edgeList
        }
    }

    fun edge(source: String, destination: String): Edge? {
        return edges[source]?.firstOrNull { it.destination == destination }
    }

    fun edges(source: String): Set<Edge> {
        return edges[source] ?: setOf()
    }

    fun edges(): List<Edge> {
        return edges.flatMap { it.value }
    }

    fun neighbours(source: String): Set<String> {
        return edges(source).map { it.destination }.toSet()
    }

    fun getRootVertex(): Vertex<T>? {
        val roots = vertices().map { it.id }.toMutableSet()
        for (vertex in vertices()) {
            for (edge in edges(vertex.id)) {
                roots.remove(edge.destination)
            }
        }
        if (roots.size != 1) {
            return null
        }
        return getVertex(roots.first())
    }

    fun getWeight(id: String, includeEdges: Boolean = false): Double {
        var weight = getVertex(id)!!.weight
        for (edge in edges(id)) {
            if (includeEdges) {
                weight += edge.weight
            }
            weight += getWeight(edge.destination, includeEdges)
        }
        return weight
    }

    fun getPathsFrom(source: String, visited: Set<String> = emptySet()): List<List<String>> {
        val result: MutableList<List<String>> = mutableListOf()
        val v = visited.toMutableSet()
        v.add(source)
        for (edge in edges(source)) {
            if (!v.contains(edge.destination)) {
                val paths = getPathsFrom(edge.destination, v)
                for (path in paths) {
                    result.add(listOf(source) + path)
                }
            }
        }
        if (result.isEmpty()) {
            result.add(listOf(source))
        }
        return result
    }

    fun getPaths(source: String, destination: String): List<List<String>> {
        val result: MutableList<List<String>> = mutableListOf()
        val paths = mutableListOf(listOf(source))
        while (paths.isNotEmpty()) {
            val path = paths.removeLast()
            val pathEnd = path.last()
            if (pathEnd == destination) {
                result.add(path)
                continue
            }

            for (neighbour in neighbours(pathEnd)) {
                if (!path.contains(neighbour)) {
                    paths.add(path + neighbour)
                }
            }
        }
        return result
    }

    fun shortestPathAndWeight(source: String, destination: String): Pair<List<String>, Double> {
        return dijkstra(source, destination)
    }

    private fun dijkstra(source: String, destination: String): Pair<List<String>, Double> {
        // Use Dijkstra's algorithm: https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
        // Mark all nodes unvisited. Create a set of all the unvisited nodes called the unvisited set.
        val unvisited = vertices().map { it.id }.toMutableSet()

        if (!unvisited.contains(source) || !unvisited.contains(destination)) {
            return emptyList<String>() to Double.MAX_VALUE
        }

        // Assign to every node a tentative distance value: set it to zero for our initial node and to
        // infinity for all other nodes. The tentative distance of a node v is the length of the shortest
        // path discovered so far between the node v and the starting node. Since initially no path is known
        // to any other vertex than the source itself (which is a path of length zero), all other tentative
        // distances are initially set to infinity. Set the initial node as current.
        val distances = mutableMapOf<String, Double>()
        val paths = mutableMapOf<String, MutableList<String>>()
        for (id in unvisited) {
            distances[id] = Double.MAX_VALUE
            paths[id] = mutableListOf()
        }
        distances[source] = getVertex(source)!!.weight
        paths[source]?.add(source)

        return dijkstraRecursion(source, destination, unvisited, distances, paths)
    }

    private tailrec fun dijkstraRecursion(
        current: String,
        destination: String,
        unvisited: MutableSet<String>,
        distances: MutableMap<String, Double>,
        paths: MutableMap<String, MutableList<String>>,
    ): Pair<List<String>, Double> {
        // For the current node, consider all of its unvisited neighbors and calculate their tentative distances
        // through the current node. Compare the newly calculated tentative distance to the current assigned
        // value and assign the smaller one. For example, if the current node A is marked with a distance of 6,
        // and the edge connecting it with a neighbor B has length 2, then the distance to B through A will be
        // 6 + 2 = 8. If B was previously marked with a distance greater than 8 then change it to 8. Otherwise,
        // the current value will be kept.
        val currentValue = distances.getOrDefault(current, 0.0)

        for (edge in edges(current)) {
            val neighbourId = edge.destination
            // Only visit the unvisited
            if (unvisited.contains(neighbourId)) {
                val neighbour = getVertex(neighbourId)!!
                val distanceValue = currentValue + edge.weight + neighbour.weight
                val oldDistanceValue = distances.getOrDefault(neighbourId, Double.MAX_VALUE)
                if (distanceValue < oldDistanceValue) {
                    distances[neighbourId] = distanceValue
                    val path = paths.getOrDefault(current, mutableListOf()) + listOf(neighbourId)
                    paths[neighbourId] = path.toMutableList()
                }
            }
        }

        // When we are done considering all of the unvisited neighbors of the current node, mark the current
        // node as visited and remove it from the unvisited set. A visited node will never be checked again.
        unvisited.remove(current)

        // If the destination node has been marked visited (when planning a route between two specific nodes)
        // or if the smallest tentative distance among the nodes in the unvisited set is infinity (when planning
        // a complete traversal; occurs when there is no connection between the initial node and remaining
        // unvisited nodes), then stop. The algorithm has finished.
        if (current == destination) {
            return paths[destination]!! to distances[destination]!!
        }

        var minUnvisited = ""
        var minDistance = Double.MAX_VALUE
        for (id in unvisited) {
            val distance = distances.getOrDefault(id, Double.MAX_VALUE)
            if (distance < minDistance) {
                minDistance = distance
                minUnvisited = id
            }
        }
        // No path possible
        if (minDistance == Double.MAX_VALUE) {
            return emptyList<String>() to minDistance
        }

        // Otherwise, select the unvisited node that is marked with the smallest tentative distance, set it as
        // the new current node, and go back to step 3.
        return dijkstraRecursion(minUnvisited, destination, unvisited, distances, paths)
    }

    fun lowestPathAndWeightVisitAll(source: String? = null, destination: String? = null): Pair<List<String>, Double> {
        // Use a modified version of Dijkstra's algorithm: https://www.baeldung.com/cs/shortest-path-visiting-all-nodes
        // Initially, we declare an array called cost, which stores the shortest path to some node visiting a
        // subset of nodes.
        val costs = mutableMapOf<Cost, Double>()
        val paths = mutableMapOf<Cost, MutableList<String>>()

        // We also declare a priority queue that stores a node and a bitmask. The bitmask represents all visited
        // nodes to get to this node. This priority queue will sort states in acceding order according to the cost
        // of the state.
        val queue = PriorityQueue<Cost>()

        // Next, we try to start the shortest path from each node by adding each node to the priority queue and
        // turning their bit on. Then, we run the Dijkstra algorithm.
        for (vertex in vertices()) {
            val cost = Cost(vertex.id, setOf(vertex.id), 0.0)
            costs[cost] = 0.0
            paths[cost] = mutableListOf(vertex.id)
            queue.add(cost)
        }

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            val currentId = current.id
            val visitedVertices = current.visited
            for (edge in edges(currentId)) {
                val neighbourId = edge.destination
                val neighbour = getVertex(neighbourId)!!
                val distanceValue = edge.weight + neighbour.weight

                val newVisitedVertices = visitedVertices + neighbourId
                val newCost = costs[Cost(currentId, visitedVertices)]!! + distanceValue
                val neighbourCost = Cost(neighbourId, newVisitedVertices, newCost)
                val currentCost = costs.getOrDefault(neighbourCost, Double.MAX_VALUE)
                if (newCost < currentCost) {
                    val path = paths.getOrDefault(Cost(currentId, visitedVertices), mutableListOf())
                    costs[neighbourCost] = newCost
                    paths[neighbourCost] = (path + listOf(neighbourId)).toMutableList()
                    queue.add(neighbourCost)
                }
            }
        }

        val vertices = vertices()
        val visitedAll = vertices.map { it.id }.toSet()
        var minCost = Double.MAX_VALUE
        var path = mutableListOf<String>()
        for (vertex in vertices) {
            if (destination == null || destination == vertex.id) {
                val costObj = Cost(vertex.id, visitedAll)
                val cost = costs.getOrDefault(costObj, Double.MAX_VALUE)
                val pathFound = paths.getOrDefault(costObj, mutableListOf())
                if (source == null || source == pathFound.first()) {
                    if (cost < minCost) {
                        minCost = cost
                        path = paths.getOrDefault(costObj, mutableListOf())
                    }
                }
            }
        }

        return path to minCost
    }

    fun highestPathAndWeightVisitAll(source: String? = null, destination: String? = null): Pair<List<String>, Double> {
        // Use a modified version of Dijkstra's algorithm: https://www.baeldung.com/cs/shortest-path-visiting-all-nodes
        // Initially, we declare an array called cost, which stores the shortest path to some node visiting a
        // subset of nodes.
        val costs = mutableMapOf<Cost, Double>()
        val paths = mutableMapOf<Cost, MutableList<String>>()

        // We also declare a priority queue that stores a node and a bitmask. The bitmask represents all visited
        // nodes to get to this node. This priority queue will sort states in acceding order according to the cost
        // of the state.
        val queue = PriorityQueue<Cost>()

        // Next, we try to start the shortest path from each node by adding each node to the priority queue and
        // turning their bit on. Then, we run the Dijkstra algorithm.
        for (vertex in vertices()) {
            val cost = Cost(vertex.id, setOf(vertex.id), 0.0)
            costs[cost] = 0.0
            paths[cost] = mutableListOf(vertex.id)
            queue.add(cost)
        }

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            val currentId = current.id
            val visitedVertices = current.visited
            for (edge in edges(currentId)) {
                val neighbourId = edge.destination
                if (!visitedVertices.contains(neighbourId)) {
                    val neighbour = getVertex(neighbourId)!!
                    val distanceValue = edge.weight + neighbour.weight

                    val newVisitedVertices = visitedVertices + neighbourId
                    val newCost = costs[Cost(currentId, visitedVertices)]!! + distanceValue
                    val neighbourCost = Cost(neighbourId, newVisitedVertices, newCost)
                    val currentCost = costs.getOrDefault(neighbourCost, 0.0)
                    if (newCost > currentCost) {
                        val path = paths.getOrDefault(Cost(currentId, visitedVertices), mutableListOf())
                        costs[neighbourCost] = newCost
                        paths[neighbourCost] = (path + listOf(neighbourId)).toMutableList()
                        queue.add(neighbourCost)
                    }
                }
            }
        }

        val vertices = vertices()
        val visitedAll = vertices.map { it.id }.toSet()
        var maxCost = 0.0
        var path = mutableListOf<String>()
        for (vertex in vertices) {
            if (destination == null || destination == vertex.id) {
                val costObj = Cost(vertex.id, visitedAll)
                val cost = costs.getOrDefault(costObj, 0.0)
                val pathFound = paths.getOrDefault(costObj, mutableListOf())
                if (source == null || source == pathFound.first()) {
                    if (cost > maxCost) {
                        maxCost = cost
                        path = paths.getOrDefault(costObj, mutableListOf())
                    }
                }
            }
        }

        return path to maxCost
    }

    // https://en.wikipedia.org/wiki/Bron%E2%80%93Kerbosch_algorithm
    fun largestCliques(): List<Set<String>> {
        // Find the neighbours for each vertex
        val neighbours: MutableMap<String, Set<String>> = mutableMapOf()
        for (vertex in vertices()) {
            neighbours[vertex.id] = neighbours(vertex.id)
        }

        // Find all cliques of vertices that are connected
        val cliques: MutableMap<Int, MutableList<Set<String>>> = mutableMapOf()
        findCliques(cliques, neighbours, neighbours.keys)

        // Return the largest set of cliques
        val maxClique = cliques.maxByOrNull { it.key } ?: return emptyList()
        return maxClique.value
    }

    private fun findCliques(
        cliques: MutableMap<Int, MutableList<Set<String>>>,
        neighbours: Map<String, Set<String>>,
        p: Set<String>,
        r: Set<String> = emptySet(),
        x: Set<String> = emptySet(),
    ) {
        if (p.isEmpty() && x.isEmpty()) {
            // We have found a clique. Add it to the list matching the clique size.
            val list = cliques.computeIfAbsent(r.size) { mutableListOf() }
            // Adding the vertices sorted, perhaps this is a too high performance penalty?
            list.add(r.sorted().toSet())
        } else {
            val mostNeighborsOfPandX: String = (p + x).maxByOrNull { neighbours.getValue(it).size }!!
            val pWithoutNeighbors = p.minus(neighbours[mostNeighborsOfPandX]!!)
            pWithoutNeighbors.forEach { v ->
                val neighborsOfV = neighbours[v]!!
                findCliques(
                    cliques,
                    neighbours,
                    p.intersect(neighborsOfV),
                    r + v,
                    x.intersect(neighborsOfV)
                )
            }
        }
    }

    // Find how many connected (separated) components this graph has and return the list of group of vertices
    // https://en.wikipedia.org/wiki/Component_(graph_theory)
    fun findConnectedComponents(): MutableList<MutableSet<String>> {
        fun dfs(vertex: String, visited: MutableSet<String>, component: MutableSet<String>) {
            visited.add(vertex)
            component.add(vertex)
            for (neighbor in neighbours(vertex)) {
                if (!visited.contains(neighbor)) {
                    dfs(neighbor, visited, component)
                }
            }
        }

        val components = mutableListOf<MutableSet<String>>()
        val visited = mutableSetOf<String>()
        for (vertex in vertices()) {
            if (!visited.contains(vertex.id)) {
                val component = mutableSetOf<String>()
                dfs(vertex.id, visited, component)
                components.add(component)
            }
        }

        return components
    }
}

class Vertex<T>(val id: String, val data: T? = null, val weight: Double = 0.0) {
    override fun toString(): String {
        return "Vertex(id='$id', data=$data, weight=$weight)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vertex<*>

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

class Edge(val source: String, val destination: String, val weight: Double = 1.0) {
    override fun toString(): String {
        return "Edge(source=$source, destination=$destination, weight=$weight)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Edge

        if (source != other.source) return false
        if (destination != other.destination) return false

        return true
    }

    override fun hashCode(): Int {
        var result = source.hashCode()
        result = 31 * result + destination.hashCode()
        return result
    }
}

enum class EdgeType {
    DIRECTED,
    UNDIRECTED
}

class Cost(val id: String, val visited: Set<String>, private val cost: Double = Double.MAX_VALUE) : Comparable<Cost> {
    override fun compareTo(other: Cost): Int {
        return cost.compareTo(other.cost)
    }

    override fun toString(): String {
        return "Cost(id='$id', visited=$visited, cost=$cost)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cost

        if (id != other.id) return false
        if (visited != other.visited) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + visited.hashCode()
        return result
    }
}
