(ns eph-clj.graph)

(defn make-graph
  "Given an input graph description g, create an undirected graph structure.
Graph structure is a map of vertices to a map of adjacent vertices and distances,
  eg. {:map [{:A {:B 10}}]} -> {:A {:B 10}, :B {:A 10}}."
  [g]
  (let [{edge-vec :map} g]
    (and (vector? edge-vec)
         (reduce (fn [g m] (let [[e1 edge-dist-map] (first m)]
                             (reduce (fn [g' [e2 d]]
                                       (-> g'
                                           (assoc-in [e1 e2] d)
                                           (assoc-in [e2 e1] d))) g
                                     edge-dist-map))) {} edge-vec))))

(defn graph-nodes
  "Return set of all nodes in the graph g."
  [g]
  (set (keys g)))

(defn graph-child-map
  "Return map of child nodes and distances for node a in the graph g."
  [g a]
  (get g a))

(defn graph-child-nodes
  "Return set of all child nodes of node a in the graph g.
a could be the node key, or the child-dist map as returned from graph-child-map."
  [g a]
  (set (keys (if (map? a) a (get g a)))))
