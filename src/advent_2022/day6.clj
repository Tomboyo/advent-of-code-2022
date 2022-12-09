(ns advent-2022.day6
  (:require [clojure.java.io :as jio]))

(defn part1 [line size]
  (let [xform (comp
                ;; [a b c d]
                (map (fn [chars] (= size (count (dedupe (set chars))))))
                ;; boolean
                (map-indexed (fn [index v] [index v]))
                ;; [int boolean]
                (filter (fn [[index v]] v))
                ;; [int true]
                (map (fn [[index _]] index))
                ;; int
                )]
    (transduce xform
               (fn
                 ([a] (+ size a))
                 ([a b] (if a a b)))
               false
               (partition size 1 line))))

(let [res (jio/resource "day6.txt")]
  (println (part1 (slurp res) 4))
  (println (part1 (slurp res) 14)))