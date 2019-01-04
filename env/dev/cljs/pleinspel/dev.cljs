(ns ^:figwheel-no-load pleinspel.dev
  (:require
    [pleinspel.core :as core]
    [devtools.core :as devtools]))

(devtools/install!)

(enable-console-print!)

(core/init!)
