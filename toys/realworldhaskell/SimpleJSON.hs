module SimpleJSON (JValue(..)) where

data JValue = JString String
            | JNumber Double
            | JBool Bool
            | JNull
            | JArray [JValue]
            | JObject [(String, JValue)]
              deriving (Show, Eq, Ord)


{- This is just me toying around with records
data A = B Int Int
       | C  { first :: String, last :: String }
         deriving (Show)
-}
