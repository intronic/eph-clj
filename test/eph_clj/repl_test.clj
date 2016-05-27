(ns eph-clj.repl-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.data.json :as json]
            [eph-clj.repl :refer :all :as r]))

(deftest test-read
  (testing "my-read"
    (is (= [99 "prompt>prompt>"] (let [msg (new java.io.StringWriter)
                                       res (with-in-str "99\n\n"
                                             (binding [*out* msg] (my-read "prompt>")))]
                                   [res (str msg)])))
    (is (= [{:key "map"} ">>"] (let [msg (new java.io.StringWriter)
                                     res (with-in-str "{\"key\": \"map\"}\n\n"
                                           (binding [*out* msg] (my-read ">")))]
                                 [res (str msg)])))
    ;; empty input
    (is (= [nil ">"] (let [msg (new java.io.StringWriter)
                           res (with-in-str "\n"
                                 (binding [*out* msg] (my-read ">")))]
                       [res (str msg)])))
    ;; unparseable json
    (is (= [["Exception:" " reader:" "JSON error (end-of-file inside object)"] ">>"]
           (let [msg (new java.io.StringWriter)
                 res (with-in-str "{ \"broken map\"\n\n"
                       (binding [*out* msg] (my-read ">")))]
             [res (str msg)])))))

(deftest test-eval
  (testing "my-eval"
    (is (= nil (my-eval nil nil nil nil nil)))
    ;; start-cmd
    (is (= 1 (my-eval {:start 1} :start nil nil nil)))
    ;; map-cmd
    (is = ({:map [{:a 1}]} (my-eval {:map [{:a 1}]} nil identity nil nil)))
    (is (= {:map [{:a {:b 1 :c 2}} {:d {:e 3}}]}
           (my-eval {:a {:b 1 :c 2} :d {:e 3}} nil identity nil nil)))
    ;; clear-cmd
    (is (= "CLEAR" (my-eval "clear" nil nil str/upper-case nil)))
    ;; quit-cmd
    (is (= "QUIT" (with-redefs [r/quit! (constantly "QUIT")]
                    (my-eval "quit" nil nil nil nil))))
    ;; help-cmd
    (is (= "OK" (my-eval "help" nil nil nil (constantly "OK"))))
    ;; unknown command
    (is (= ["Exception:" " Unknown input:" :bad-cmd] (my-eval :bad-cmd nil nil nil nil)))
    ;; eval exception
    (is (= ["Exception:" " eval cmd:" {:map [1 0]} " message:" "Divide by zero"]
           (my-eval {:map [1 0]} nil #(apply / (:map %)) nil nil)))))

(deftest test-print
  (testing "my-print"
    (is (= "{\"A\":1}\n" (with-out-str (my-print {:A 1}))))
    ;; print exception
    (is (= "[Exception:  print: boom 99]\n"
           (with-out-str
             (with-redefs [json/write-str #(throw (Exception. (str "boom " %)))]
               (my-print 99)))))))
