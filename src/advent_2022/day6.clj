(ns advent-2022.day6
  (:require [clojure.java.io :as jio]))

(defn solution [line size]
  (first
    (keep-indexed #(if (= size (count (distinct %2))) (+ size %1) nil)
                  (partition size 1 line))))

(let [res (jio/resource "day6.txt")]
  ;; Part 1
  (println (solution (slurp res) 4))
  (println (solution (slurp res) 14)))