(ns eph-clj.core-test
  (:require [clojure.test :refer :all]
            [eph-clj.core :refer :all]))

(deftest test-start-cmd
  (testing "start-cmd"
    (is (= :no-path (with-redefs [*graph* {}] (start-cmd {:start "A" :end "B"}))))
    (is (= {:distance 3 :route [:A :B]}
           (with-redefs [*graph* {:A {:B 3} :B {:A 3}}]
             (start-cmd {:start "A" :end "B"}))))))

(deftest test-map-cmd
  (testing "map-cmd"
    (is (= {:a {:x 1} :b {:c 3} :d {:x 4}}
           (merge-with merge {:a {:x 2} :d {:x 4}} {:a {:x 1} :b {:c 3}})))
    (is (= {} (merge-with merge {} {})))
    (is (= {} (with-redefs [*graph* {}] (map-cmd {:map [{}]}))))
    (is (= {:a {:b 1} :b {:a 1}}
           (with-redefs [*graph* {}] (map-cmd {:map [{:a {:b 1}}]}))))
    (is (= {:a {:b 1 :c 3 :d 4} :b {:a 1} :c {:a 3} :d {:a 4}}
           (with-redefs [*graph* {:a {:b 2 :c 3} :b {:a 2} :c {:a 3}}]
             (map-cmd {:map [{:a {:b 1}} {:d {:a 4}}]}))))))

(deftest test-clear-cmd
  (testing "clear-cmd"
    (is (= {} (with-redefs [*graph* {:a :b}] (clear-cmd :bob))))))

(deftest test-help-cmd
  (testing "help-cmd"
    (is (string? (with-out-str (help-cmd))))))
