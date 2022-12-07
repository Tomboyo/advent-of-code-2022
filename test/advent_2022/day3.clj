(ns advent-2022.day3
  (:require [clojure.test :refer [deftest is testing]]
            [advent-2022.day3 :as day3]))

(deftest part1test
  (testing "Day 3 part 1"
    (is (= 157 (day3/part1 '("vJrwpWtwJgWrhcsFMMfFFhFp"
                           "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL"
                           "PmmdzqPrVvPwwTWBwg"
                           "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn"
                           "ttgJtRGJQctTZtZT"
                           "CrZsJsPPZsGzwwsLwLmpwMDw"))))))

;; (deftest part2test
;;   (testing "Day 2 part 2"
;;     (is (= 12 (day2/part2 '("A Y", "B X", "C Z"))))))

