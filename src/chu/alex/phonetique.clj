(ns chu.alex.phonetique
  (:require [clojure.java.shell :as shell]
            [clojure.string :refer [split]]))

(def phonetics-path (atom "./phonetics"))

(defn set-phonetic-path
  [path]
  (reset! phonetics-path path))

(defn phonetisation
  "Transforme une phrase en une suite de phonèmes."
  [phrase]
  (-> (shell/sh @phonetics-path (str phrase "\n"))
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

(defn extraire-rime
  [phrase]
  (-> phrase
      phonetisation
      reverse
      ((partial take-until (complement voyelle?)))))

(defn riment?
  "Prédicat selon que deux phrases riment ou non."
  [phrase1 phrase2]
  (let [[r1 r2] (map extraire-rime [phrase1 phrase2])]
    (= r1 r2)))
