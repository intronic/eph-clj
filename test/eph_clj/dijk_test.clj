(ns eph-clj.dijk-test
  (:require [clojure.test :refer :all]
            [clojure.data.priority-map :as m]
            [eph-clj.graph :as g]
            [eph-clj.dijk :refer :all :as d]))

(def g1 {:A {:B 100 :C 30} :B {:A 100 :F 300} :C {:A 30} :F {:B 300}})
(def g2 {:B {:F 300} :F {:B 300}})
(def g3 {:A {:B 100, :C 30}, :B {:A 100, :F 300}, :C {:A 30, :D 200}, :F {:B 300, :E 50, :G 70}, :D {:C 200, :H 90, :E 80}, :H {:D 90, :E 30, :G 50}, :E {:D 80, :H 30, :G 150, :F 50}, :G {:E 150, :F 70, :H 50}})

(deftest test-priority-map-ops
  (testing "peek/pop/assoc"
    (is (= [:B [20 :B1]] (peek (m/priority-map-keyfn first :A [100 :A1] :B [20 :B1] :C [50 :C1]))))
    (is (= {:A [100 :A1] :C [50 :C1]} (pop (m/priority-map-keyfn first :A [100 :A1] :B [20 :B1] :C [50 :C1]))))
    (is (= nil (peek (m/priority-map-keyfn first))))
    (is (= [[:A [0 nil]] [:B [10 nil]]]
           (seq (m/priority-map-keyfn first :B [10 nil]  :A [0 nil]))))
    (is (= [:A 3] (peek (conj (m/priority-map :A 1) [:A 3]))))
    (is (= [:A 3] (peek (assoc (m/priority-map :A 1) :A 3))))))

(deftest test-update-fringe
  (testing "update-fringe"
    ;; new path
    (is (= {:F [300 :B]} (#'d/update-fringe g2 :B 0 {:B [0 nil]} (m/priority-map-keyfn first))))
    ;; new path shorter than previous
    (is (= {:F [300 :B]} (#'d/update-fringe g2 :B 0 {:B [0 nil]} (m/priority-map-keyfn first :F [400 :B]))))
    ;; old path shorter than previous
    (is (= {:F [1 :B]} (#'d/update-fringe g2 :B 0 {:B [0 nil]} (m/priority-map-keyfn first :F [1 :B]))))))

(deftest test-dijkstra
  (testing "dijkstra"
    (is (= {:F [300 :B] :B [0 nil]} (#'d/dijkstra g2 :B :F)))
    (is (= {:F [400 :B] :B [100 :A] :C [30 :A] :A [0 nil]} (#'d/dijkstra g1 :A :F)))
    (is (= {:F [400 :B] :B [100 :A] :C [30 :A] :A [0 nil]} (#'d/dijkstra g1 :A :X)))
    (is (= {:X [0 nil]} (#'d/dijkstra g1 :X :A)))))

(deftest test-shortest-dist-and-path
  (testing "shortest-dist-and-path"
    (is (= {:distance 300 :route [:B :F]} (shortest-dist-and-path g2 :B :F)))
    (is (= {:distance 400 :route [:A :B :F]} (shortest-dist-and-path g1 :A :F)))
    (is (= {:distance 360 :route [:A :C :D :E :F]} (shortest-dist-and-path g3 :A :F)))
    (is (= :no-path (shortest-dist-and-path g1 :A :X)))
    (is (= :no-path (shortest-dist-and-path g1 :X :A)))))
