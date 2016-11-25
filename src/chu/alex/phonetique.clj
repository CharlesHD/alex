(ns chu.alex.phonetique
  (:require [clojure.string :refer [split]]))

(defn phonetisation
  "Transforme une phrase en une suite de phonèmes."
  [phrase]
  (-> (clojure.java.shell/sh "./phonetics" (str phrase "\n"))
      :out
      (split #" ")
      rest
      rest
      drop-last))

(defn voyelle?
  "le phonème est-il une voyelle ?"
  [phoneme]
  (case (first phoneme)
    (\a \e \i \o \u \y) true
    false))

(defn compte-syllabe
  "Compte le nombre de syllabe dans une phrase."
  [phrase]
  (->> phrase
       phonetisation
       (filter voyelle?)
       count))

(defn- take-until
  [pred coll]
  (let [head (take-while pred coll)
        tail (drop-while pred coll)]
    (reverse (conj (reverse head) (first tail)))))

(defn riment?
  "Prédicat selon que deux phrases riment ou non."
  [phrase1 phrase2]
  (let [[r1 r2] (map (comp
                      (partial take-until (complement voyelle?))
                      reverse
                      phonetisation)
                     [phrase1 phrase2])]
    (= r1 r2)))
