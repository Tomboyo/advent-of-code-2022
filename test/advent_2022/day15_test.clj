(ns advent-2022.day15-test
  (:require [advent-2022.day15 :refer [part1 part2]]
            [clojure.test :refer [deftest testing is run-tests]]
            [clojure.java.io :as io]))

(deftest day15
  (testing "part 1"
    (is (= 26
           (part1 10 (io/resource "day15-test.txt")))))
  (testing "part 2"
    (is (= 56000011
           (part2 (io/resource "day15-test.txt"))))))

(comment (run-tests))

