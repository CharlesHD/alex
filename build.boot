(set-env!
 :source-paths #{"src"}
 :resource-paths #{"res"}
 :target-path "tmp"
 :dependencies '[[org.clojure/clojure "1.9.0-alpha14"]])

(task-options!
 pom {:project 'alex
      :version "0.0.1"}
 jar {:manifest {"Foo" "bar"}})
