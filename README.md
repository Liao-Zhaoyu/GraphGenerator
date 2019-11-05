GraphGenerator
==============

A graph data generator based on the R-Mat generation method.

Usage
java -jar GraphGenerator [options]
-n <integer> // Number of nodes (mandatory)
-t [0|1]     // Type of edges: 0 = undirected (default), 1 = directed
-d [0|1]     // Statistical distribution of edges: 0 = normal (default), 1 = powerlaw
-m [0|1|2]   // Generation method: 0 = Matrix (default), 1 = Primitive Array, 2 = Hash-based Array
-f [0|1]     // Output data format: 0 = edge list (default), 1 = graphml
-s <integer> // Random seed
