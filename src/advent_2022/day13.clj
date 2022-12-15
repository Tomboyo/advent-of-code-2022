(ns advent-2022.day13
  (:require [clojure.java.io :as jio]
            [clojure.string :as str]
            [clojure.tools.trace :refer [trace]]))

(defn parse-input [seq]
  (->> seq
       (map #(str/replace % "," " "))
       (filter (comp not str/blank?))
       (map read-string)
       (partition 2)))

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

(inorder? [[1] [2 3 4]]
          [[1] 4])

(with-open [r (jio/reader (jio/resource "day13.txt"))]
  (->> (line-seq r)
       (parse-input)
       (map (fn [[left right]] (= :< (inorder? left right))))
       (keep-indexed (fn [index, bool] (if bool (+ 1 index) nil)))
       (reduce + 0)
       (println "Part 1:")))