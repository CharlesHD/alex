(ns chu.alex.extract
  (:require [chu.alex.phonetique :refer [compte-syllabe]]
            [clojure.string :refer [join split trim]]))

(defn phrases
  "Retourne les phrases d'un texte"
  [texte]
  (->> (split texte #"[\.;!?\-]")
       (map trim)
       (remove #(= 0 (count %)))))

(defn phrase-en-jetons
  [phrase]
  (split phrase #" "))

(defn jetons-en-phrase
  [jetons]
  (join " " jetons))

(defn- extraire-premier-vers
  [jetons longueur-vers]
  (loop [ls (rest jetons)
         phrase [(first jetons)]]
    (let [p (jetons-en-phrase phrase)
          v (compte-syllabe p)]
      (cond (= v longueur-vers) [true p]
            (< v longueur-vers) (if (empty? ls) [false p]
                                    (recur (rest ls) (conj phrase (first ls))))
            :else [false p]))))

(defn- extraire-vers-phrase
  [phrase longueur-vers]
  (loop [reste (phrase-en-jetons phrase)
         phrases []]
    (if (empty? reste) phrases
        (let [[vers? vers] (extraire-premier-vers reste longueur-vers)]
          (if vers?
            (recur (rest reste) (conj phrases vers))
            (recur (rest reste) phrases))))))

(defn extraire-vers
  [texte longueur-vers]
  (reduce concat (map #(extraire-vers-phrase % longueur-vers) (phrases texte))))
