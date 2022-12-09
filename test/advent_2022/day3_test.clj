(ns advent-2022.day3-test
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

(deftest part2test
  (testing "Day 3 part 2"
    (is (= 70 (day3/part2 '("vJrwpWtwJgWrhcsFMMfFFhFp"
                            "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL"
                            "PmmdzqPrVvPwwTWBwg"
                            "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn" "ttgJtRGJQctTZtZT" "CrZsJsPPZsGzwwsLwLmpwMDw"))))))

