(ns chu.alex.core
  (:require [chu.alex
             [extract :refer [extraire-vers]]
             [phonetique :refer [riment?]]]
            [clojure.string :refer [join]]))

(defn-
  graphe-de-rimes
  [vers]
  (let [add-vers
        (fn [m v] (assoc m v (filter #(riment? % v) vers)))]
  (reduce add-vers {} vers)))

(defn- vec-or
  [vec]
  (if (empty? vec) false
      (let [[h & r] vec] (or h (vec-or r)))))

(defn pioche-un-vers
  [vers gdrime ne-rime-pas au-moins]
  (loop [v (rand-nth vers)]
    (if (and (>= (count (get gdrime v)) au-moins)
             (not (vec-or (map (partial riment? v) ne-rime-pas))))
      v
      (recur (rand-nth vers)))))

(defn sonnet
  [texte]
  (let [alexandrins (extraire-vers texte 12)
        gdr (graphe-de-rimes alexandrins)
        puv (partial pioche-un-vers alexandrins gdr)
        a (puv [] 4)
        [a1 a2 a3 a4] (take 4 (shuffle (get gdr a)))
        b (puv [a] 4)
        [b1 b2 b3 b4] (take 4 (shuffle (get gdr b)))
        c (puv [a b] 2)
        [c1 c2] (take 2 (shuffle (get gdr c)))
        d (puv [a b c] 2)
        [d1 d2] (take 2 (shuffle (get gdr d)))
        e (puv [a b c d] 2)
        [e1 e2] (take 2 (shuffle (get gdr e)))]
    (join )
    ))
