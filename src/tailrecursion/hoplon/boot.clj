(ns tailrecursion.hoplon.boot
  (:require
    [tailrecursion.boot.task                  :as t]
    [tailrecursion.boot.task.util.file        :as f]
    [tailrecursion.boot.core                  :refer [deftask]]
    [clojure.java.io                          :refer [file make-parents]]
    [tailrecursion.boot.tmpregistry           :refer [mk! mkdir!]]
    [tailrecursion.hoplon.compiler.compiler   :refer [compile-dirs]]))

(deftask hoplon
  "Build Hoplon web application."
  [boot & {:keys [static-dir cljs-opts]}]
  (let [{:keys [public src-paths system]} @boot
        tmp         (:tmpregistry system)
        static-dir  (doto (file (or static-dir "static")) (.mkdirs)) 
        src-paths   (map file src-paths)
        cljs-tmp    (mkdir! tmp ::cljs-tmp)
        public-tmp  (mkdir! tmp ::public-tmp)
        main-js     (file public-tmp "main.js")]
    (swap! boot update-in [:src-paths] conj (.getPath cljs-tmp))
    (comp
      #((t/pass-thru-wrap compile-dirs) % main-js src-paths cljs-tmp public-tmp)
      (t/cljs boot :output-to main-js :opts cljs-opts)
      (t/pre-task boot :process #(do (f/sync :time public public-tmp static-dir) %)))))
