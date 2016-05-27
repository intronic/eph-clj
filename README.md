# eph-clj

A Clojure library to find the shortest routes between nodes in a graph of nodes connected by roads of different distances.

#### How to Run This

* If you have ```lein``` installed, you can clone this repo, then run ```lein run``` from the project directory.
* Otherwise, contact me and I can send you a ```jar``` file instead.

## Usage

### Initialisation

Create a graph (a road from A->B implies a return path B->A) using the JSON format:
```json
{
    "map": [
        {"A": { "B": 100, "C": 30 }},
        {"B": { "F": 300}},
        {"C": { "D": 200}},
        {"D": { "H": 90, "E":80}},
        {"E": { "H": 30, "G":150, "F":50}},
        {"F": { "G": 70}},
        {"G": { "H": 50}}
    ]
}
```
Alternatively, add the map directly as JSON map of nodes to a map of connected nodes and distances:

```json
{"A": {"B":80, "I":10}, "I": {"F":20}}
```

### Shortest Path Command

With the (JSON data) command for the above map:
```json
{ "start":"A", "end":"F" }
```
then the result will be:

```json
{"distance":360,"route":["A","C","D","E","F"]}
```

### Add/Update Roads

Use either of the initialisation formats above to add or update nodes.
The following two commands are equivalent and will update the path from A->B to be 80:

```json
{ "A": {"B":80} }
{"map": [{ "A": {"B":80} }]}
```
The following two commands are equivalent and will add paths from A<->I (70) and A<->J (150) and from I<->J (800):
```json
{"A": {"I":70, "J":150}, "I": {"J": 800}}
{ "map": [{"A": {"I":70, "J":150}} {"I": {"J": 800}}]}
```

### Clearing / Resetting the Graph

To clear or reset the graph, enter ```"clear"```.

### Quitting

To quit, enter ```"quit"``` or hit ```^C``` (ctrl-C).

## License

Copyright © 2016 MJP

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
