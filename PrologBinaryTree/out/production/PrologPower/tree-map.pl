split(null, _, null, null, null) :- !.
split(node(Key, Val, Pr, L, R), X, node(Key, Val, Pr, L, RL), RC, RR) :-
    Key < X, split(R, X, RL, RC, RR), !.
split(node(Key, Val, Pr, L, R), X, LL, LC, node(Key, Val, Pr, LR, R)) :-
    Key > X, split(L, X, LL, LC, LR), !.
split(node(X, Val, Pr, L, R), X, L, node(Key, Val, Pr, null, null), R) :- !.

merge(null, Node, Node) :- !.
merge(Node, null, Node) :- !.
merge(node(Key1, Val1, Pr1, L1, R1), node(Key2, Val2, Pr2, L2, R2),
      node(Key1, Val1, Pr1, L1, MergedR)) :-
    Pr1 < Pr2, merge(R1, node(Key2, Val2, Pr2, L2, R2), MergedR), !.
merge(node(Key1, Val1, Pr1, L1, R1), node(Key2, Val2, Pr2, L2, R2),
      node(Key2, Val2, Pr2, MergedL, R2)) :-
    Pr1 >= Pr2, merge(node(Key1, Val1, Pr1, L1, R1), L2, MergedL), !.

map_put(null, Key, Val, node(Key, Val, Pr, null, null)) :- rand_int(99999999, Pr), !.
map_put(Node, Key, Val, Result) :-
    split(Node, Key, L, C, R),
    rand_int(99999999, Pr),
    merge(L, node(Key, Val, Pr, null, null), CurNode),
    merge(CurNode, R, Result),
    !.

map_remove(Node, X, Result) :-
    split(Node, X, L, C, R),
    merge(L, R, Result),
    !.

tree_append([], Node, Node).
tree_append([(Key, Val) | Rest], CurNode, Result) :-
    %write(["put", Key, Val, 1]), nl,
    map_put(CurNode, Key, Val, CurRes), write(CurRes), tree_append(Rest, CurRes, Result).
tree_build(ListMap, Node) :- tree_append(ListMap, null, Node).

map_get(node(FindKey, FindVal, _, _, _), FindKey, FindVal) :- !.
map_get(node(Key, _, _, L, _), FindKey, FindVal) :-
    FindKey < Key, map_get(L, FindKey, FindVal), !.
map_get(node(Key, _, _, _, R), FindKey, FindVal) :-
    FindKey > Key, map_get(R, FindKey, FindVal), !.

%map_replace(Node, Key, Val, Node) :-
%    split(Node, Key, _, R),
%    split(R, Key + 1, null, _),
%    !.

map_replace(Node, Key, Val, Result) :-
    map_get(Node, Key, _),
    map_put(Node, Key, Val, Result), !.
map_replace(Node, _, _, Node).

%map_floorKey(node(X, _, _, _, _), X, X) :- !.
%
%map_floorKey(node(X, _, _, L, null), Key, X) :- X =< Key, !.
%
%map_floorKey(node(X, _, _, L, R), Key, Ans) :-
%    X < Key,  map_floorKey(R, Key, Ans), Ans =< Key, !.
%
%map_floorKey(node(X, _, _, L, R), Key, X) :-
%    X < Key,  map_floorKey(R, Key, Ans), Ans > Key, !.
%
%map_floorKey(node(X, _, _, L, R), Key, FloorKey) :-
%    X > Key, map_floorKey(L, Key, FloorKey), !.

find_right(node(X, _, _, _, _), X, X) :- !.
find_right(node(X, _, _, _, R), Ans) :- find_right(R, Ans).

map_floorKey(Node, Key, FloorKey) :-
    split(Node, Key, L, null, R),
    find_right(L, FloorKey), !.

map_floorKey(Node, Key, FloorKey) :-
    split(Node, Key, L, node(FloorKey, _, _, _, _), R) ,!.


