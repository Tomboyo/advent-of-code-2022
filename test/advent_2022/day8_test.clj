(ns advent-2022.day8-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-2022.day8 :refer :all]))

(def T [[3 0 3 7 3]
        [2 5 5 1 2]
        [6 5 3 3 2]
        [3 3 5 4 9]
        [3 5 3 9 0]])
(deftest visible-trees-test
         (testing "Visible trees"
                  (is (= #{[1 0] [1 1]}
                         (visible-trees-p1 T [1 0] step-down)))
                  (is (= #{[0 3] [2 3] [4 3]}
                         (visible-trees-p1 T [0 3] step-right)))
                  (is (= #{[4 3]}
                         (visible-trees-p1 T [4 3] step-left)))
                  (is (= #{[2 4] [2 3]}
                         (visible-trees-p1 T [2 4] step-up)))))

(deftest visible-trees-p2-test
  (testing "Visible trees"
    (is (= 0
           (visible-trees-p2 T [0 0] step-left)))
    (is (= 1
           (visible-trees-p2 T [2 2] step-up)))
    (is (= 3
           (visible-trees-p2 T [3 3] step-up)))
    (is (= 2
           (visible-trees-p2 T [2 3] step-left)))))

(deftest part-one-test
  (testing "Part 1 - Sum of distinct visible trees"
    (is (= 21
           (part1 T)))))