(ns advent-2022.day4
  (:require [advent-2022.util :refer [solve]]
            [clojure.string :as str]))

(defn subset [[a1 a2] [b1 b2]]
  (and (<= a1 b1) (<= b2 a2)))

(defn part1 [lines]
  (let [xform (comp
               ;=>1-12,3-4
               (map #(str/split % #"\D"))
               ;=> ["1" "12" "3" "4"]
               (map (fn [list] (map #(Integer/parseInt %) list)))
               ;=> [1 12 3 4]
               (map #(partition-all 2 %))
               ;=> [(1 2) (3 4)]
               (map (fn [[a b]]
                      (or
                       (subset a b)
                       (subset b a))))
               ;=> [bool]
               (map (fn [x] (if x 1 0)))
               ;=> 0 | 1
               )]
    (transduce xform + 0 lines)))

(comment
  (solve part1 "day4.txt")
  :ref)
