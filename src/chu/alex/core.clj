(ns chu.alex.core
  (:require [chu.alex
             [extract :refer [extraire-vers]]
             [phonetique :refer [extraire-rime riment?]]]
            [clojure.string :refer [join]]
            [taoensso.tufte :refer [defnp p profile]]))

(def *alexandrins* (atom []))
(def *gdr* (atom {}))

(defn-
  graphe-de-rimes
  [vers]
  (group-by extraire-rime vers))

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

(defn charger-texte
  [texte]
  (reset! *alexandrins* (extraire-vers texte 12))
  (reset! *gdr* (graphe-de-rimes (deref *alexandrins*))))

(defn ajouter-texe
  [texte]
  (swap! *alexandrins* concat (extraire-vers texte 12)))

(defn construire-gdr
  []
  (reset! *gdr* (graphe-de-rimes (deref *alexandrins*))))

(defnp sonnet
  []
  (let [alexandrins (p :extract (deref *alexandrins*))
        gdr (p :gdr (deref *gdr*))
        gdr (reduce-kv #(if (>= (count %3) 4) (assoc %1 %2 %3) %1) {} gdr)
        [a b c d e] (take 5 (shuffle (keys gdr)))
        [a1 a2 a3 a4] (take 4 (shuffle (get gdr a)))
        [b1 b2 b3 b4] (take 4 (shuffle (get gdr b)))
        [c1 c2] (take 2 (shuffle (get gdr c)))
        [d1 d2] (take 2 (shuffle (get gdr d)))
        [e1 e2] (take 2 (shuffle (get gdr e)))]
    (join "\n\n"
          [(join "\n" [a1 b1 b2 a2])
           (join "\n" [a3 b3 b4 a4])
           (join "\n" [c1 c2 d1])
           (join "\n" [e1 e2 d2])])))
