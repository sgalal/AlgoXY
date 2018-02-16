{-
    PrefixTree.hs, Alphabetic Prefix Tree.
    Although it's called 'alphabetic', we abstract the key as sequence
    Copyright (C) 2010, Liu Xinyu (liuxinyu95@gmail.com)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
-}

module PrefixTree where

import Data.List (isPrefixOf, sort, sortBy)
import Data.Function (on)

-- definition
data PrefixTree k v = PrefixTree { value :: Maybe v
                                 , children :: [([k], PrefixTree k v)]}

empty = PrefixTree Nothing []

leaf x = PrefixTree (Just x) []

-- insertion
insert :: Eq k => PrefixTree k v -> [k] -> v -> PrefixTree k v
insert t ks x = PrefixTree (value t) (ins (children t) ks x) where
    ins []     ks x = [(ks, PrefixTree (Just x) [])]
    ins (p@(ks', t') : ps) ks x
        | ks' == ks
            = (ks, PrefixTree (Just x) (children t')) : ps  -- overwrite
        | match ks' ks
            = (branch ks x ks' t') : ps
        | otherwise
            = p : (ins ps ks x)

match x y = x /= [] && y /= [] && head x == head y

branch :: Eq k => [k] -> v -> [k] -> PrefixTree k v -> ([k], PrefixTree k v)
branch ks1 x ks2 t2
    | ks1 == ks
        -- ex: insert "an" into "another"
        = (ks, PrefixTree (Just x) [(ks2', t2)])
    | ks2 == ks
        -- ex: insert "another" into "an"
        = (ks, insert t2 ks1' x)
    | otherwise = (ks, PrefixTree Nothing [(ks1', leaf x), (ks2', t2)])
   where
      ks = lcp ks1 ks2
      m = length ks
      ks1' = drop m ks1
      ks2' = drop m ks2

-- longest common prefix
lcp :: Eq k => [k] -> [k] -> [k]
lcp [] _ = []
lcp _ [] = []
lcp (x:xs) (y:ys) = if x==y then x : (lcp xs ys) else []

-- lookup
find :: Eq k => PrefixTree k v -> [k] -> Maybe v
find t ks = find' (children t) ks where
    find' [] _ = Nothing
    find' (p@(ks', t') : ps) k
          | ks' == ks = value t'
          | ks' `isPrefixOf` ks = find t' (diff ks' ks)
          | otherwise = find' ps ks
    diff ks1 ks2 = drop (length (lcp ks1 ks2)) ks2

fromList :: Eq k => [([k], v)] -> PrefixTree k v
fromList = foldl ins empty where
    ins t (k, v) = insert t k v

fromString :: (Enum v, Num v) => String -> PrefixTree Char v
fromString = fromList . (flip zip [1..]) . words

-- Pre-order traverse to populate keys in lexicographical order.
keys :: Ord k => PrefixTree k v -> [[k]]
keys = keys' [] where
  keys' prefix t = case (value t) of
    Nothing -> kss
    (Just _) -> prefix : kss
    where
      kss = concatMap (\(ks, t') -> keys' (prefix ++ ks) t') ts
      ts = sortBy (compare `on` fst) (children t)

--
example = insert (fromString "a place where animals are for public to see") "zoo" 0

-- test data
assocs = [[("a", 1), ("an", 2), ("another", 7), ("boy", 3), ("bool", 4), ("zoo", 3)],
          [("zoo", 3), ("bool", 4), ("boy", 3), ("another", 7), ("an", 2), ("a", 1)]]

verify = all (\as ->
                 let t = fromList as in
                   all (\(k, v) -> maybe False (==v) (find t k)) as) assocs

verifyKeys = all (\as ->
                   keys (fromList as) == (sort $ fst $ unzip as)) assocs
