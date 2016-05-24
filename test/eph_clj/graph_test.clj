(ns eph-clj.graph-test
  (:require [clojure.test :refer :all]
            [eph-clj.graph :refer :all]))

(def m1 {:A {:B 100 :C 30 } :B {:A 100 :F 300} :C {:A 30} :F {:B 300}})
(def m2 {:B {:F 300} :F {:B 300}})

(deftest test-make-graph
  (testing "making graph"
    (is (= {:B {:F 300} :F {:B 300}}
           (make-graph {:map [{:B {:F 300}}]})))
    (is (= m1 (make-graph {:map [{:A {:B 100, :C 30}} {:B {:F 300}}]})))))

(deftest test-graph-nodes
  (testing "all graph nodes"
    (is (= #{:B :F} (graph-nodes m2))))
  (testing "graph child map"
    (is (= {:B 300} (graph-child-map m2 :F))))
  (testing "graph child nodes"
    (is (= #{:F} (graph-child-nodes m2 :B)))
    (is (= #{:F} (graph-child-nodes m2 (graph-child-map m2 :B))))))
