(set-env!
 :source-paths #{"src"}
 :resource-paths #{"res"}
 :target-path "tmp"
 :dependencies '[[org.clojure/clojure "1.9.0-alpha14"]
                 [com.taoensso/tufte "1.1.0"]])

(task-options!
 pom {:project 'alex
      :version "0.0.3"}
 jar {:manifest {"Foo" "bar"}})
