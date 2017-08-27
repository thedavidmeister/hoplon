(ns hoplon.elements-test
 (:require
  goog.dom
  [hoplon.core :as h]
  [cljs.test :refer-macros [deftest is]]))

(def elements
 [[h/html "html"]
  ; Calling h/body from phantomjs triggers an error.
  ; https://github.com/hoplon/hoplon/issues/197
  ; [h/body "body"]
  [h/head "head"]
  [h/a "a"]
  [h/abbr "abbr"]
  [h/address "address"]
  [h/area "area"]
  [h/article "article"]
  [h/aside "aside"]
  [h/audio "audio"]
  [h/b "b"]
  [h/base "base"]
  [h/bdi "bdi"]
  [h/bdo "bdo"]
  [h/blockquote "blockquote"]
  [h/br "br"]
  [h/button "button"]
  [h/canvas "canvas"]
  [h/caption "caption"]
  [h/cite "cite"]
  [h/code "code"]
  [h/col "col"]
  [h/colgroup "colgroup"]
  [h/data "data"]
  [h/datalist "datalist"]
  [h/dd "dd"]
  [h/del "del"]
  [h/details "details"]
  [h/dfn "dfn"]
  [h/dialog "dialog"]
  [h/div "div"]
  [h/dl "dl"]
  [h/dt "dt"]
  [h/em "em"]
  [h/embed "embed"]
  [h/fieldset "fieldset"]
  [h/figcaption "figcaption"]
  [h/figure "figure"]
  [h/footer "footer"]
  [h/form "form"]
  [h/h1 "h1"]
  [h/h2 "h2"]
  [h/h3 "h3"]
  [h/h4 "h4"]
  [h/h5 "h5"]
  [h/h6 "h6"]
  [h/header "header"]
  [h/hgroup "hgroup"]
  [h/hr "hr"]
  [h/i "i"]
  [h/iframe "iframe"]
  [h/img "img"]
  [h/input "input"]
  [h/ins "ins"]
  [h/kbd "kbd"]
  [h/label "label"]
  [h/legend "legend"]
  [h/li "li"]
  [h/link "link"]
  [h/main "main"]
  [h/html-map "map"]
  [h/mark "mark"]
  [h/menu "menu"]
  [h/menuitem "menuitem"]
  [h/html-meta "meta"]
  [h/meter "meter"]
  [h/nav "nav"]
  [h/noframes "noframes"]
  [h/noscript "noscript"]
  [h/html-object "object"]
  [h/ol "ol"]
  [h/optgroup "optgroup"]
  [h/option "option"]
  [h/output "output"]
  [h/p "p"]
  [h/param "param"]
  [h/pre "pre"]
  [h/progress "progress"]
  [h/q "q"]
  [h/rp "rp"]
  [h/rt "rt"]
  [h/rtc "rtc"]
  [h/ruby "ruby"]
  [h/s "s"]
  [h/samp "samp"]
  [h/script "script"]
  [h/section "section"]
  [h/select "select"]
  [h/small "small"]
  [h/source "source"]
  [h/span "span"]
  [h/strong "strong"]
  [h/style "style"]
  [h/sub "sub"]
  [h/summary "summary"]
  [h/sup "sup"]
  [h/table "table"]
  [h/tbody "tbody"]
  [h/td "td"]
  [h/textarea "textarea"]
  [h/tfoot "tfoot"]
  [h/th "th"]
  [h/thead "thead"]
  [h/html-time "time"]
  [h/title "title"]
  [h/tr "tr"]
  [h/track "track"]
  [h/u "u"]
  [h/ul "ul"]
  [h/html-var "var"]
  [h/video "video"]
  [h/wbr "wbr"]])

(deftest ??elements
 (doseq [[f s] elements]
  (let [el (f)]
   (is (goog.dom/isElement el))
   (is (.webkitMatchesSelector el s) (str "Element did not match selector: " s)))))

(deftest ??element-creation
 ; we want to handle at least as many children as the arity of invoke!
 (apply
  h/div
  (conj (vec (take 20 (repeatedly h/div)))
   [(h/div)]))

 ; we want to handle an arbitrary number of children
 (apply
  h/div
  (take
   (+ 22 (rand-int 20))
   (repeatedly h/div))))

(deftest ??element-attributes
 (let [el-fn (first (rand-nth elements))]
  ; simply adding "data-foo" as an attribute
  (doseq [e [(el-fn #{:data-foo})
             (el-fn :data-foo true)
             (el-fn {:data-foo true})]]
   (is (.webkitMatchesSelector e "[data-foo=\"data-foo\"]")))

  ; setting a specific cljs value (such as a keyword) for "data-foo"
  (doseq [e [(el-fn :data-foo :data-foo)
             (el-fn {:data-foo :data-foo})]]
   (is (.webkitMatchesSelector e "[data-foo=\":data-foo\"]")))
  ; ability to set "true" is important for some HTML APIs, e.g. "draggable" for
  ; HTML5 drag and drop.
  (is (.webkitMatchesSelector
       (el-fn {:data-foo "true"})
       "[data-foo=\"true\"]"))))