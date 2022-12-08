(ns advent-2022.day4
  (:require [clojure.test :refer [deftest is testing]]
            [advent-2022.day4 :as day4]))

(deftest day4part1
  (testing "Day 4 Part 1"
    (is (= 2 (day4/part1 '("2-4,6-8"
                           "2-3,4-5"
                           "5-7,7-9"
                           "2-8,3-7"
                           "6-6,4-6"
                           "2-6,4-8"))))))