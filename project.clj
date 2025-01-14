(defproject scheduler-component "0.1.0-SNAPSHOT"

  :description "FIXME: write description"

  :url "http://example.com/FIXME"

  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}

  :dependencies [[org.clojure/clojure "1.12.0"]
                 [jarohen/chime "0.3.3"]
                 [integrant "0.13.1"]]

  :profiles {:dev {:plugins        [[lein-shell "0.5.0"]
                                    [com.github.liquidz/antq "RELEASE"]
                                    [com.github.clojure-lsp/lein-clojure-lsp "1.4.15"]]

                   :resource-paths ["resources" "test/resources/"]

                   :test-paths     ["test/unit" "test/integration" "test/helpers"]

                   :dependencies   [[hashp "0.2.2"]]

                   :injections     [(require 'hashp.core)]

                   :aliases        {"clean-ns"     ["clojure-lsp" "clean-ns" "--dry"] ;; check if namespaces are clean
                                    "format"       ["clojure-lsp" "format" "--dry"] ;; check if namespaces are formatted
                                    "diagnostics"  ["clojure-lsp" "diagnostics"] ;; check if project has any diagnostics (clj-kondo findings)
                                    "lint"         ["do" ["clean-ns"] ["format"] ["diagnostics"]] ;; check all above
                                    "clean-ns-fix" ["clojure-lsp" "clean-ns"] ;; Fix namespaces not clean
                                    "format-fix"   ["clojure-lsp" "format"] ;; Fix namespaces not formatted
                                    "lint-fix"     ["do" ["clean-ns-fix"] ["format-fix"]]}}}) ;; Fix both
