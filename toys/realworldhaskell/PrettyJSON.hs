import SimpleJSON
import Data.List (intercalate)

-- This is the module that will do the pretty-printing
-- The book calls this function "renderJValue". Type JValue -> Doc.
-- It uses functions defined in some other "Prettify" module, which
-- also output Doc values.

-- Screw it. I'm implementing it as JValue -> String first.
renderJValue :: JValue -> String
renderJValue (JString s)   = '"' : s ++ "\""
renderJValue (JNumber num) = show num
renderJValue (JBool b)     = if b then "true" else "false"
renderJValue JNull         = "null"
renderJValue (JArray vs)   = '[' : (intercalate ", " (map renderJValue vs)) ++ "]"
renderJValue (JObject ps)  = "{\\n" ++ intercalate "\\n" (map printPair ps) ++ "\\n}" where
                              printPair (key, val) = '"' : key ++ "\": " ++ renderJValue val
