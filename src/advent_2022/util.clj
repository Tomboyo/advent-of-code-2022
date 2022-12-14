(ns advent-2022.util 
  (:require [clojure.java.io :as jio]))

(defn solve [solver resource]
  (let [url (jio/resource resource)]
    (with-open [r (jio/reader url)]
      (solver (line-seq r)))))

(defn read-lines-eager [resource]
  "Reads lines eagerly (not lazy)"
  (with-open [r (jio/reader (jio/resource resource))]
    (doall (line-seq r))))