(ns chu.alex.extract
  (:require [chu.alex.phonetique :refer [compte-syllabe]]
            [clojure.string :refer [join split trim]]
            [taoensso.tufte :refer [defnp p]]))

(defnp nettoyer
  [texte]
  (-> texte
      (clojure.string/replace #"\n" "")
      (clojure.string/replace #"\t" " ")
      (clojure.string/replace #" +" " ")))

(defnp phrases
  "Retourne les phrases d'un texte"
  [texte]
  (->> (split (nettoyer texte) #"[\.;!?]")
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
  (p :ext-premier-vers
     (loop [ls (rest jetons)
            phrase [(first jetons)]]
       (let [p (jetons-en-phrase phrase)
             v (compte-syllabe p)]
         (cond (= v longueur-vers) [true p]
               (< v longueur-vers) (if (empty? ls) [false p]
                                       (recur (rest ls) (conj phrase (first ls))))
               :else [false p])))))

(defn- extraire-vers-phrase
  [phrase longueur-vers]
  (p :ext-vers-phrase
     (loop [reste (phrase-en-jetons phrase)
            phrases []]
       (if (empty? reste) phrases
           (let [[vers? vers] (extraire-premier-vers reste longueur-vers)]
             (if vers?
               (recur (rest reste) (conj phrases vers))
               (recur (rest reste) phrases)))))))

(defnp extraire-vers
  [texte longueur-vers]
  (reduce concat (pmap #(extraire-vers-phrase % longueur-vers) (phrases texte))))
