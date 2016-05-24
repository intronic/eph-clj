(ns eph-clj.core
  (:require [clojure.data.json :as json]
            [eph-clj [graph :as g]]))

(defn read-json
  "Read JSON string to clojure data, transforming any map key strings to keywords."
  [s]
  (json/read-str s :key-fn keyword))

(defn read-json-graph
  "Create an undirected graph structure from a json string."
  [s]
  (-> s read-json g/make-graph))
