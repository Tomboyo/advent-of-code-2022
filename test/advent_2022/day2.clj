(ns advent-2022.day2
  (:require [clojure.test :refer [deftest is testing]]
            [advent-2022.day2 :as day2]))

(deftest part1test
  (testing "Day 2: Rock Paper Scissors"
    (is (= 15 (day2/part1 '("A Y" "B X" "C Z"))))))

