(ns advent-2022.day12
  (:require [clojure.java.io :as jio]
            [clojure.string :as str]))

; PART 1
(def input (->> (jio/resource "day12.txt")
                (slurp)
                (str/split-lines)))

(defn find-coord [char col]
  (->> col
       (map #(str/index-of % char))
       (keep-indexed (fn [a b] (if (nil? b) nil [a b])))
       (first)))

(defn toint [char] (case char
                     \S (int \s)
                     \E (int \z)
                     (int char)))
(def G (into [] (map #(into [] (map toint %)) input)))
(def S (find-coord \S input))
(def E (find-coord \E input))

(defn neighbors [graph [y x :as p]]
  (let [max-height (+ 1 (get-in graph p))
        up [(- y 1) x]
        down [(+ y 1) x]
        left [y (- x 1)]
        right [y (+ x 1)]]
    (->> (list up down left right)
         (filter (fn [q] (not (nil? (get-in graph q)))))
         (filter (fn [q]
                   (<= (get-in graph q) max-height))))))

(defn BFS
  [graph start end]
  (loop [parents {start :start}
         horizon [start]
         visited #{}]
    (let [x (first horizon)]
      (if (= end x)
        parents
        (let [next (->> (neighbors graph x)
                        (filter (comp not visited)))]
          (recur (reduce (fn [p n] (assoc p n x)) parents next)
                 (into [] (concat (subvec horizon 1) next))
                 (into #{} (concat visited next))))))))

; Part 1
(println
  "Part 1: "
  (let [path (BFS G S E)]
    (loop [counter 1
           x (path E)]
      (let [parent (path x)]
        (if (= :start parent)
          counter
          (recur (+ counter 1) parent))))))