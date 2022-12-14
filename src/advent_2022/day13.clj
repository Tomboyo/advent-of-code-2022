(ns advent-2022.day13
  (:require [clojure.java.io :as jio]
            [clojure.string :as str]))

(defn parse-input [seq]
  (->> seq
       (map #(str/replace % "," " "))
       (filter (comp not str/blank?))
       (map read-string)
       (partition 2)))

(with-open [r (jio/reader (jio/resource "day13.test.txt"))]
  (->> (line-seq r)
       (parse-input)
       (doall)
       (map println)))