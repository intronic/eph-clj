(ns eph-clj.repl
  (:require [clojure.data.json :as json]
            [clojure.string :as str]))

(defn- quit! [] (System/exit 0))

(defn my-read
  "Print prompt and read a JSON object from *stdin*, returning
  clojure data."
  [prompt]
  (try (let [l (loop [s []]
                 (print prompt)
                 (flush)
                 (let [l (read-line)]
                   (if (= "" l) (str/join " " s) (recur (conj s l)))))]
         (json/read-str l :key-fn keyword :eof-error? false))
       (catch Exception e ["Exception:" " reader:" (.getMessage e)])))

(defn my-eval
  "Eval command cmd which was returned by my-read.
Call start-cmd/1, map-cmd/1, clear-cmd/1, or help-cmd/0 functions,
or return an exception [\"Exception:\" ...] if this cannot be evaluated.
\"quit\" will exit process.
Return result of evaluating command."
  [cmd start-cmd map-cmd clear-cmd help-cmd]
  (try
    (case cmd
      nil nil
      "quit" (quit!)
      "clear" (clear-cmd cmd)
      "help" (help-cmd)
      (cond
        ;; vector with first element "Exception:" represents
        ;; exception from earlier (reader)
        (and (vector? cmd) (-> cmd first (= "Exception:")))
        cmd

        (map? cmd)
        (condp #(%1 %2) cmd
          :start (start-cmd cmd)

          ;; map key has a vector of single-element maps of node -> {node dist} maps
          :map (map-cmd cmd)

          ;; otherwise if this map consists of only keyword keys,
          ;; and map values, assume it is a map of node -> {node dist},
          ;; convert it to the "map" format above and pass to map command.
          #(and (every? keyword? (keys %)) (every? map? (vals %)))
          (map-cmd {:map (for [[k v] cmd] {k v})})

          ;; otherwise some unexpected map format
          ["Exception:" " Unknown map object:" cmd])

        :else ["Exception:" " Unknown input:" cmd]))
    (catch Exception e ["Exception:" " eval cmd:" cmd " message:" (.getMessage e)])))

(defn my-print
  "Print result res as JSON object. Silent if result is nil."
  [res]
  (if-not (nil? res)
    (println (try (json/write-str res)
                 (catch Exception e ["Exception:" " print:" (.getMessage e)])))))
