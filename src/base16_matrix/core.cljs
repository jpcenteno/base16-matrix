(ns base16-matrix.core
  (:require [rum.core :as rum]))

(def base-colors [:base00 :base01 :base02 :base03 :base04 :base05 :base06 :base07 :base08 :base09 :base0a :base0b :base0c :base0d :base0e :base0f])

(defonce global-state
  (atom
   {:scheme "Dracula"
    :author "Mike Barkmin (http://github.com/mikebarkmin) based on Dracula Theme (http://github.com/dracula)"
    :base00 "282936"
    :base01 "3a3c4e"
    :base02 "4d4f68"
    :base03 "626483"
    :base04 "62d6e8"
    :base05 "e9e9f4"
    :base06 "f1f2f8"
    :base07 "f7f7fb"
    :base08 "ea51b2"
    :base09 "b45bcf"
    :base0a "00f769"
    :base0b "ebff87"
    :base0c "a1efe4"
    :base0d "62d6e8"
    :base0e "b45bcf"
    :base0f "00f769"}))

(defn cell-text
  [_fg-keyword _bg-keywod]
  "{:#?}")

(defn color->hex
  "Takes a color keyword and returns it's hex value as a string."
  [k]
  (-> k name (subs 4)))

(defn css-color
  "Returns a valid css color from the colorscheme."
  [theme color-keyword]
  (str "#" (get theme color-keyword)))

(defn color-mix-cell [theme fg bg]
  [:td {:style {:background-color (css-color theme bg)
                :color            (css-color theme fg)}}
   (cell-text fg bg)])

(rum/defc color-th [color-keyword]
  (let [number (color->hex color-keyword)]
    [:th (str color-keyword)]))

(rum/defc table-header []
  (concat [[:th]]
          (for [color base-colors]
            (color-th color))))

(rum/defc table-color-row [bg-color]
  [:tr (concat [[:th (color->hex bg-color)]]
               (for [fg base-colors]
                 (color-mix-cell @global-state fg bg-color)))])

(rum/defc table < rum/reactive []
  [:table (concat [(table-header)]
                  (for [bg base-colors]
                    (table-color-row bg)))])

(declare app)
(rum/defc app < rum/reactive []
  [[:div
    [:p [:b "Colorscheme name:"] " " (:scheme (rum/react global-state))]
    [:p [:b "Colorscheme author"] " " (:author (rum/react global-state))]]
   [:div.matrix (table)]])

(rum/mount (app) (js/document.getElementById "app"))
