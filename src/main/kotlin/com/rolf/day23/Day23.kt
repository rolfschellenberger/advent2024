package com.rolf.day23

import com.rolf.Day
import com.rolf.util.EdgeType
import com.rolf.util.Graph
import com.rolf.util.Vertex

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        val network = parseNetwork(lines)
        val triads = mutableSetOf<Set<String>>()
        for (a in network.vertices()) {
            for (b in network.neighbours(a.id)) {
                for (c in network.neighbours(b)) {
                    if (network.neighbours(c).contains(a.id)) {
                        triads.add(setOf(a.id, b, c))
                    }
                }
            }
        }
        println(
            triads.count { it.any { it.startsWith("t") } }
        )
    }

    override fun solve2(lines: List<String>) {
        val network = parseNetwork(lines)
        val clique = network.largestCliques().first().sorted()
        println(clique.joinToString(","))
    }

    private fun parseNetwork(lines: List<String>): Graph<String> {
        val network = Graph<String>()
        for (connection in lines) {
            val (a, b) = connection.split("-")
            network.addVertex(Vertex(a))
            network.addVertex(Vertex(b))
            network.addEdge(a, b, EdgeType.UNDIRECTED)
        }
        return network
    }
}
