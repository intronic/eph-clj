(ns eph-clj.core-test
  (:require [clojure.test :refer :all]
            [eph-clj.core :refer :all]))

(def s1 "{\"map\": [{\"A\": { \"B\": 100, \"C\": 30 }}, {\"B\": { \"F\": 300}}]}")
(def m1 {:A {:B 100 :C 30 } :B {:A 100 :F 300} :C {:A 30} :F {:B 300}})

(deftest test-json
  (testing "read json"
    (is (= {:a 1 :b 2} (read-json "{\"a\":1,\"b\":2}")))
    (is (= {:a {:b 80}} (read-json "{\"a\": {\"b\": 80}}")))
    (is (= {:start "a" :end "b"} (read-json "{\"start\": \"a\" \"end\": \"b\"}")))
    (is (= {:map [{ :A {:I 70, :J 150} }]}
           (read-json "{\"map\": [{\"A\": {\"I\":70, \"J\":150} }]}")))
    (is (= [{:A {:B 100, :C 30}} {:B {:F 300}}]
           (:map (read-json s1))))
    (is (= {:A {:B 100, :C 30}}
           (first (:map (read-json s1)))))
    (is (= [:A {:B 100, :C 30}]
           (first (first (:map (read-json s1))))))
    (is (= {:B 100, :C 30}
           (second (first (first (:map (read-json s1)))))))))

(deftest test-read-json-graph
  (testing "making graph from json"
    (is (= m1 (read-json-graph s1)))))
