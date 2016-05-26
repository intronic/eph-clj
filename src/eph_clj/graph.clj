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

(defn distance
  "Return distance from a to b in graph g or nil if no direct path."
  [g a b]
  (get-in g [a b]))

(defn children
  "Return all children of node a in the graph g. "
  [g a]
  (keys (g a)))
