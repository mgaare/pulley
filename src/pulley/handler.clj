(ns pulley.handler
  (:use [compojure.core]
        [clojure.java.shell :only [sh]]
        [ring.adapter.jetty])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [me.raynes.fs :as fs]
            [clojure.data.json :as json]
            [clojure.string :as string]))

(defn list-files [dir]
  (let [files (fs/list-dir dir)]
    (flatten (map (fn [file] (if (fs/directory? (str dir "/" file))
                               (flatten (list-files (str dir "/" file)))
                               (str dir "/" file))) files))))

(defn strip-cwd [file cwd]
  (string/replace file (str cwd "/") ""))

(defn scripts []
  (let [cwd (str fs/*cwd*)
        files (list-files cwd)
        execs (filter fs/executable? files)]
    (json/write-str (map #(strip-cwd % cwd) execs))))

(defn exec-script [req]
  (let [params (req :params)
        flags (params :flags)
        uri (req :uri)
        cwd (str fs/*cwd*)]
    (if (fs/exists? (str cwd uri))
      (json/write-str (sh (str cwd "/" uri (map #(str % " ") flags))))
      "Not Found")))


(defroutes app-routes
  (GET "/" [] (scripts))
  (POST "*" req (exec-script req))
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))

(defn -main [& m]
  (run-jetty app {:port 8080}))
