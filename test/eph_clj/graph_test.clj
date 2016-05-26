(ns eph-clj.graph-test
  (:require [clojure.test :refer :all]
            [eph-clj.graph :refer :all]))

(def g1 {:A {:B 100 :C 30 } :B {:A 100 :F 300} :C {:A 30} :F {:B 300}})
(def g2 {:B {:F 300} :F {:B 300}})

(deftest test-make-graph
  (testing "making graph"
    (is (= {:B {:F 300} :F {:B 300}}
           (make-graph {:map [{:B {:F 300}}]})))
    (is (= g1 (make-graph {:map [{:A {:B 100, :C 30}} {:B {:F 300}}]})))))

(deftest test-graph-nodes
  (testing "graph distances "
    (is (= 300 (distance g2 :F :B)))
    (is (= 300 (distance g2 :B :F)))
    (is (= nil (distance g2 :B :NOWHERE)))
    (is (= nil (distance g2 :NOWHERE :NOWHERE))))
  (testing "graph children"
    (is (= #{:F} (set (children g2 :B))))
    (is (= #{:B :C} (set (children g1 :A))))))
