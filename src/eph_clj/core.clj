(ns eph-clj.core
  (:require [eph-clj [graph :as g] [dijk :as dijk] [repl :as r]])
  (:gen-class))

(def ^:dynamic *graph* {})              ; var for graph structure

(declare start-cmd map-cmd clear-cmd help-cmd)

(defn -main
  "Basic JSON REPL for shortest path problem."
  []
  (help-cmd)
  (loop []
    (-> (r/my-read "json cmd> ")
        (r/my-eval start-cmd map-cmd clear-cmd help-cmd)
        (r/my-print))
    (recur)))

(defn start-cmd
  "Find the shortest distance and route between the start and end, or
  \"no-path\" if none."
  [{:keys [start end]}]
  (dijk/shortest-dist-and-path *graph* (keyword start) (keyword end)))

(defn map-cmd
  "Merge the new graph into the existing graph. New paths are created,
  existing ones overwritten."
  [m]
  (alter-var-root (var *graph*) #(merge-with merge % (g/make-graph m))))

(defn clear-cmd
  "Reset the *graph* to {}."
  [_]
  (alter-var-root (var *graph*) (constantly {})))

(defn help-cmd
  "Print help message."
  []
  (println "
Usage: Enter JSON data (can be over multiple lines).
Enter a blank line to complete the input data.
Initially the graph is empty.

Enter {\"map\": [{\"A\":{\"B\": 1, \"C\":2}},
               {\"C\":{\"D\":3}}]}
  to create or update the graph with roads A<->B, A<->C, C<->D
  with distances 1,2,3 resp.
  If a graph already exists, new roads will be added
  and existing ones will have their distances updated.
  All roads are two-way.

Alternatively, use the format {\"A\": {\"B\":2, \"C\":3}, \"B\":{\"D\":1}}
  to add or update roads or distances.
  (update A<->B to 2, A<->C to 3 and add B<->D distance 1.)
  (same effect the \"map\" command above.)

Enter {\"start\":\"A\", \"end\":\"D\"}
  to find the shortest distance and route from A to D.
  For example:
      {\"distance\":5,\"route\":[\"A\",\"C\",\"D\"]}
  If there is no path between the nodes, the result will be: \"no-path\"

Enter \"clear\" to clear the current graph structure.
Enter \"help\" for this message.

Enter \"quit\" or ^C to quit.
"))
