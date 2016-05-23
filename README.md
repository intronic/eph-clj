# eph-clj

A Clojure library to find the shortest paths between nodes of an undirected graph.

## Usage

### Initialisation
Given an undirected graph (an edge from A->B implies an edge B->A) in the initialisation format:
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

### Shortest Path Command
With the (JSON data) command for the above map:
```json
{ "start":"A", "end":"F" }
```
then the result will be:

```json
{ "distance":360 }
```
or, better:
```json
{ "distance":360, "route": ["?","?",...,"?"]}
```

### Addition and Deletion of Roads
Update: the following command will update the path from A->B to be 80:
```json
{ "A": {"B":80} }
```
Using the following as input, add a path from A<->I (70) and A<->J (150):
```json
{ "map": [{ "A": {"I":70, "J":150} }]}
```

## Questions/Assumptions

* Road and highway terms are used interchangeably
* All roads are bi-directional (no one-way)

## License

Copyright © 2016 MJP

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
