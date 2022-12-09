(ns advent-2022.day4-test
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

(deftest day4part2
  (testing "Day 4 Part 2"
    (is (= 5 (day4/part2 '("1-2,3-4"
                           "3-4,1-2"
                           "1-2,2-3"
                           "2-3,1-2"
                           "1-3,2-2"
                           "2-2,1-3"
                           "1-2,1-2"))))))