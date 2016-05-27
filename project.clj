(defproject eph-clj "0.1.0-SNAPSHOT"
  :description "Shortest path graph problem"
  :url "http://github.com/intronic/eph-clj"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/data.priority-map "0.0.7"]]
  :profiles {:uberjar {:aot :all}}
  :main eph-clj.core)
