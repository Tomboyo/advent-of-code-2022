(ns advent-2022.day14
  (:require [clojure.java.io :as io]
             [clojure.string :as str]
             clojure.set))

;; We receivve lines of input of the form
;;   x,y -> x,y -> x,y
;; describing paths made of line segments between coordinates. We interpret
;; these as solid paths of rock on a 2D plane. All other space extending
;; infinitely across the plane is empty. At point 500,0 there is a source of
;; sand that falls downwards in the positive-y direction. When sand "collides"
;; with rock or sand directly below it, it can fall diagonally down and left,
;; down and right, or stop moving (in that order). We're meant to simulate
;; falling sand and count the number of instances until 100% of all subsequent
;; sand would fall infinitely down, and return the count.

(defn expand [p1 p2]
  (let [[x1 x2] (sort [ (:x p1) (:x p2)])
        [y1 y2] (sort [ (:y p1) (:y p2)])]
    (for [x (range x1 (+ 1 x2))
          y (range y1 (+ 1 y2))]
      {:x x :y y})))

(comment
  (expand {:x 1 :y 5} {:x 1 :y 3}))

(defn draw [world]
  (let [max-y (reduce max (keys world))
        min-y 0 
        vals (apply clojure.set/union (vals world))
        max-x (reduce max vals)
        min-x (reduce min vals)]
    ; do is not redundant; silences a bunch of nil's in the REPL.
    (do (doall (for [y (range min-y (+ 1 max-y))]
                 (do
                   (doall
                     (for [x (range min-x (+ 1 max-x))]
                       (if (and  (= 500 x) (= 0 y))
                         (print "+")
                         (print (if (contains? (get world y) x) "#" ".")))
                       )) 
                   (println ""))))
        nil)))

(defn trace [k it]
  (println "TRACE" k it)
  it)

(with-open [reader (io/reader  (io/resource "day14.txt"))]
  (as-> (line-seq reader) it
    (map (fn [y] (str/split y #" -> ")) it)
    (map (fn [path]
           (->> path
                (map (fn [s] (str/split s #",")))
                (map (fn [[a b]] {
                                  :x (Integer/parseInt a)
                                  :y (Integer/parseInt b)
                                  }))))
         it)
    (mapcat (partial partition 2 1) it)
    (mapcat (fn [[a b]] (expand a b)) it)
    (reduce (fn [acc p]
              (update acc
                      (:y p)
                      (fn [old] (conj (or old (sorted-set)) (:x p)))))
            {}
            it)
    ; We now have a { y => #{ x1 x2 x3 ...}} of occupied spaces
    (do (draw it)
        (let [max-y (reduce max (keys it))
              vals (apply clojure.set/union (vals it))
              max-x (reduce max vals)
              min-x (reduce min vals)]
          (println min-x max-x max-y)
          (loop [x 500 y 0 counter 0 world it]
            (cond
              (or (< 1000 counter) (< x min-x) (> x max-x) (> y max-y)) counter
              (not (contains? (get world (+ 1 y)) x)) (recur x (+ 1 y) counter world)
              (not (contains? (get world (+ 1 y)) (- x 1))) (recur (- x 1) (+ 1 y) counter world)
              (not (contains? (get world (+ 1 y)) (+ 1 x))) (recur (+ 1 x) (+ 1 y) counter world)
              :else (recur 500 0 (+ 1 counter) (update world y (fn [xs] (into #{} (conj xs x)))))))                
          ))))


