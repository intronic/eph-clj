(ns eph-clj.dijk
  (:require [clojure.data.priority-map :as m]
            [eph-clj.graph :as g]))

;;; Dijkstra's algorithm
;;; fringe: priority map of node and [dist-from-root prev-node], in order of dist-from-root
;;; visited: map of {node [dist-from-root prev-node]}
;;; loop over each node in fringe-map,
;;; - add or update non-visited children in fringe if they are new or closer
;;; - add node to visited map and recur on rest of fringe-map

;; Finally, trace path back to root in visited map
(declare dijkstra update-fringe)

(defn shortest-dist-and-path
  [graph from to]
  (let [visited (dijkstra graph from to)]
    (if-let [d (first (get visited to))]
      {:distance d
       :route (rseq (loop [node to path [to]]
                      (if-let [prev (second (visited node))]
                        (recur prev (conj path prev))
                        path)))}
      :no-path)))

(defn- dijkstra
  "return "
  [graph from to]
  (loop [visited {}
         fringe (m/priority-map-keyfn first from [0 nil])]
    ;; take nearest node off fringe (if any), if it is target node, then we are done
    ;; otherwise update the fringe from this node, then add this to the visited, and repeat
    (if-let [[node [rdist prev] :as next] (peek fringe)]
      (let [visited (conj visited next)]
        (if (= node to)
          visited                        ; found path
          (recur visited (update-fringe graph node rdist visited (pop fringe)))))
      visited)))                        ; not found/all nodes traversed

(defn- update-fringe
  "given a node in a graph g, update the fringe with new distance and
  prev node of all children not already visited, where the new
  distance to this child is shorter."
  [g node dist visited fringe]
  (reduce (fn [fringe child]
            (let [new-d (+ dist (g/distance g node child))
                  old-d (first (fringe child))]
              (if (and old-d (<= old-d new-d))
                fringe               ; old path closer
                (assoc fringe child [new-d node])))) ; new node/new path closer
          fringe
          (remove visited (g/children g node))))
