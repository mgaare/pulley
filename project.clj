(defproject pulley "0.1.0-SNAPSHOT"
  :description "A RESTful interface to shell scripts"
  :url "https://github.com/mgaare/pulley"
  :dependencies [[org.clojure/clojure "1.5.0"]
                 [compojure "1.1.5"]
                 [me.raynes/fs "1.4.0"]
                 [org.clojure/data.json "0.2.2"]
                 [ring/ring-jetty-adapter "1.1.8"]]
  :plugins [[lein-ring "0.8.2"]]
  :ring {:handler pulley.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.3"]]}})
