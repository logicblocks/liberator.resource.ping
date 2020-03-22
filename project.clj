(defproject io.logicblocks/liberator-hal.ping-resource "0.1.2-RC2"
  :description "A HAL ping resource for liberator"
  :url "https://github.com/logicblocks/liberator-hal.ping-resource"

  :license {:name "The MIT License"
            :url  "https://opensource.org/licenses/MIT"}

  :dependencies [[halboy "5.1.0"]
                 [b-social/hype "1.0.0"]
                 [b-social/liberator-mixin "0.0.48"]]

  :plugins [[lein-cloverage "1.1.1"]
            [lein-shell "0.5.0"]
            [lein-ancient "0.6.15"]
            [lein-changelog "0.3.2"]
            [lein-eftest "0.5.8"]
            [lein-codox "0.10.7"]
            [lein-cljfmt "0.6.4"]
            [lein-kibit "0.1.6"]
            [lein-bikeshed "0.5.1"]
            [jonase/eastwood "0.3.3"]]

  :profiles
  {:shared
   {:dependencies [[org.clojure/clojure "1.10.1"]
                   [ring/ring-core "1.8.0"]
                   [ring/ring-mock "0.4.0"]
                   [nrepl "0.6.0"]
                   [eftest "0.5.8"]]}
   :dev
   [:shared {:source-paths ["dev"]
             :eftest       {:multithread? false}}]
   :test
   [:shared {:eftest {:multithread? false}}]
   :prerelease
   {:release-tasks
    [["shell" "git" "diff" "--exit-code"]
     ["change" "version" "leiningen.release/bump-version" "rc"]
     ["change" "version" "leiningen.release/bump-version" "release"]
     ["vcs" "commit" "Pre-release version %s [skip ci]"]
     ["vcs" "tag"]
     ["deploy"]]}
   :release
   {:release-tasks
    [["shell" "git" "diff" "--exit-code"]
     ["change" "version" "leiningen.release/bump-version" "release"]
     ["codox"]
     ["changelog" "release"]
     ["shell" "sed" "-E" "-i.bak" "s/\"[0-9]+\\.[0-9]+\\.[0-9]+\"/\"${:version}\"/g" "README.md"]
     ["shell" "rm" "-f" "README.md.bak"]
     ["shell" "git" "add" "."]
     ["vcs" "commit" "Release version %s [skip ci]"]
     ["vcs" "tag"]
     ["deploy"]
     ["change" "version" "leiningen.release/bump-version" "patch"]
     ["change" "version" "leiningen.release/bump-version" "rc"]
     ["change" "version" "leiningen.release/bump-version" "release"]
     ["vcs" "commit" "Pre-release version %s [skip ci]"]
     ["vcs" "tag"]
     ["vcs" "push"]]}}

  :cloverage
  {:ns-exclude-regex [#"^user"]}

  :codox
  {:namespaces  [#"^liberator-hal.ping-resource\."]
   :metadata    {:doc/format :markdown}
   :output-path "docs"
   :doc-paths   ["docs"]
   :source-uri  "https://github.com/logicblocks/liberator-hal.ping-resource/blob/{version}/{filepath}#L{line}"}

  :cljfmt {:indents ^:replace {#".*" [[:inner 0]]}}

  :eastwood {:config-files ["config/linter.clj"]}

  :deploy-repositories
  {"releases"  {:url "https://repo.clojars.org" :creds :gpg}
   "snapshots" {:url "https://repo.clojars.org" :creds :gpg}})
