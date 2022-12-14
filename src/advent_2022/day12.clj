(ns advent-2022.day12
  (:require [clojure.java.io :as jio]
            [clojure.string :as str]
            [clojure.tools.trace :refer [trace]]))

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

(defn neighbors-up-1 [graph [y x :as p]]
  "Returns neighbors in the cardinal directions which are no more than one
  z-level higher than the origin"
  (let [max-height (+ 1 (get-in graph p))
        up [(- y 1) x]
        down [(+ y 1) x]
        left [y (- x 1)]
        right [y (+ x 1)]]
    (->> (list up down left right)
         (filter (fn [q] (not (nil? (get-in graph q)))))
         (filter (fn [q]
                   (<= (get-in graph q) max-height))))))

; For part 2, we can start the search from E and basically invert the traversal
; rules to imitate walking in the opposite direction. SO, instead of being able
; to go at most one level up and any levels down, we can go any levels up and at
; most one down.
(defn neighbors-down-1 [graph [y x :as p]]
  "Returns neighbors in the cardinal directions which are no more than one
  z-level lower than the origin."
  (let [min-height (- (get-in graph p) 1)
        up [(- y 1) x]
        down [(+ y 1) x]
        left [y (- x 1)]
        right [y (+ x 1)]]
    (->> (list up down left right)
         (filter (fn [q] (not (nil? (get-in graph q)))))
         (filter (fn [q]
                   (>= (get-in graph q) min-height))))))

(defn bfs
  "Performs breadth-first search on the graph from the starting point until the
  end function returns true. Neighbors defines which points in the graph are
  reachable from a point."
  [graph neighbors start end]
  (loop [parents {start :start}
         horizon [start]
         visited #{start}]
    (let [x (first horizon)]
      (assert (not (nil? x)) "neighbors did not return points")
      (if (end graph x)
        [x parents]
        (let [next (->> (neighbors graph x)
                        (filter (comp not visited)))]
          (recur (reduce (fn [p n] (assoc p n x)) parents next)
                 (into [] (concat (subvec horizon 1) next))
                 (into #{} (concat visited next))))))))

(defn path-length [[path-end paths]]
  (loop [counter 1
         x (paths path-end)]
    (let [parent (paths x)]
      (if (= :start parent)
        counter
        (recur (+ counter 1) parent)))))

; Part 1
(println
  "Part 1:"
  (path-length (bfs G neighbors-up-1 S (fn [_graph x] (= E x)))))

; Part 2
; Both part 1 and part 2 are the same problem if we parameterize the starting
; point, how to traverse, and how to identify the stopping point.
(println
  "Part 2:"
  (path-length
    (bfs G neighbors-down-1 E (fn [graph x] (= (get-in graph x) (int \a))))))