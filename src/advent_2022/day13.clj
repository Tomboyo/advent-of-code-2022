(ns advent-2022.day13
  (:require [clojure.java.io :as jio]
            [clojure.string :as str]))

(defn parse-input [seq]
  (->> seq
       (map #(str/replace % "," " "))
       (filter (comp not str/blank?))
       (map read-string)))

(defn inorder? [left right]
  (cond
    (and (number? left) (number? right))
    (cond (< left right) :<
          (= left right) :=
          :default :>)
    (and (sequential? left) (sequential? right))
    (loop [xs left
           ys right]
      (let [x (first xs) y (first ys)]
        (cond
          (and x y) (case (inorder? x y)
                      :< :<
                      := (recur (rest xs) (rest ys))
                      :> :>)
          x :>
          y :<
          ; Both (nested) lists exhausted
          (and (nil? x) (nil? y)) :=
          :default (assert false "Default"))))
    (sequential? left)
    (inorder? left [right])
    (sequential? right)
    (inorder? [left] right)))

(def input (slurp (jio/resource "day13.txt")))

; Part 1
(->> (str/split-lines input)
     (parse-input)
     (partition 2)
     (map (fn [[left right]] (= :< (inorder? left right))))
     (keep-indexed (fn [index, bool] (if bool (+ 1 index) nil)))
     (reduce + 0)
     (println "Part 1:"))

; Part 2
(->> (str/split-lines input)
     (parse-input)
     (concat [[[2]] [[6]]])
     (sort (fn [left right] (case (inorder? left right)
                              :< -1
                              := 0
                              :> 1)))
     (keep-indexed (fn [index, packet]
                     (if
                       (or (= [[2]] packet) (= [[6]] packet))
                       (+ index 1)
                       nil)))
     (reduce *)
     (println "Part 2:"))