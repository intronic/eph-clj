(ns eph-clj.core
  (:require [clojure.data.json :as json]))

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

(defn read-json
  "Read JSON string to clojure data, transforming any map key strings to keywords."
  [s]
  (json/read-str s :key-fn keyword))

(defn read-json-graph
  "Create an undirected graph structure from a json string."
  [s]
  (-> s read-json make-graph))
