(ns pleinspel.core
    (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(def state (atom {:coordinates [[51.4461790, 5.4582792]
                                [51.4459063, 5.4580558]
                                [51.4456436, 5.4579880]
                                [51.4454931, 5.4585813]
                                [51.4459392, 5.4590086]]
                  :width       300
                  :height      500
                  :precision   50}))

(defn calculate-bounds [coordinates]
  (let [byx (map first (sort-by first  coordinates))
        byy (map second (sort-by second coordinates))]
    [[(first byx) (first byy)]
     [(last byx) (last byy)]]))

(defn gps->screen [coordinates width]
  (let [[[bound-x1 bound-y1] [bound-x2 _]] (calculate-bounds coordinates)
        bound-w                            (- bound-x2 bound-x1)
        factor                             (/ width bound-w)]
    (map (fn [[long lat]]
           [(* factor (- long bound-x1)) (* factor (- lat bound-y1))]) coordinates)))

(defn create-polygon-string [points]
  (apply str (interleave (map (fn [[long lat]] (str long ", " lat)) points)
                         (repeat " "))))


(defn map-svg []
  (let [{width       :width
         height      :height
         coordinates :coordinates
         precision   :precision} @state
        points                   (gps->screen coordinates width)]
    (fn []
      [:svg {:viewport (str "0 0 " width " " height)
             :width    width
             :height   height}
       [:polygon {:style  {:fill         "green"
                           :stroke       "black"
                           :stroke-width 1}
                  :points (create-polygon-string points)}]
       (doall (for [x (range (Math/floor (/ width precision)))]
                (for [y (range (Math/floor (/ height precision)))]
                  ^{:key (str x ":" y)}
                  [:rect {:on-click     #(.log js/console "!")
                          :fill         "none"
                          :stroke       "black"
                          :stroke-width 1
                          :x            (* x precision)
                          :y            (* y precision)
                          :width        precision
                          :height       precision}])))])))



;; -------------------------
;; Routes

(defn home-page []
  (fn []
    [:div
     [:h1 "Pleinspel"]
     [map-svg]]))

(defn mount-root []
  (reagent/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
