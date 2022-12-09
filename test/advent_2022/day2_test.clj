(ns advent-2022.day2-test
  (:require [clojure.test :refer [deftest is testing]]
            [advent-2022.day2 :as day2]))

(deftest part1test
  (testing "Day 2 part 1"
    (is (= 15 (day2/part1 '("A Y" "B X" "C Z"))))))

(deftest part2test
  (testing "Day 2 part 2"
    (is (= 12 (day2/part2 '("A Y", "B X", "C Z"))))))

