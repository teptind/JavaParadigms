"use strict";

const VARS = {
    x: 0,
    "y": 1,
    "z": 2
};

const CONSTS = {
    "e" : Math.E,
    "pi" : Math.PI
};

const operation = f => (...args) => (...values) => {
    /*let res = [];
    for (const arg of args) {
        res.push(arg(...values));
    }*/
    const res = args.map(arg => arg(...values));
    return f(...res)
};

const cnst = a => () => a;
const variable = name => (...values) => values[VARS[name]];
const add = operation((a, b) => a + b);
const subtract = operation((a, b) => a - b);
const multiply = operation((a, b) => a * b);
const divide = operation((a, b) => a / b);
const negate = operation((a) => -a);
const avg5 = operation((...args) => args.reduce((a, b) => a + b) / 5);
const med3 = operation((...args) => args.sort((x, y) => (x - y))[1]);
const pi = cnst(CONSTS.pi);
const e = cnst(CONSTS["e"]);

const OPERATIONS = {
    "+": [add, 2],
    "-": [subtract, 2],
    "*": [multiply, 2],
    "/": [divide, 2],
    "negate": [negate, 1],
    "avg5": [avg5, 5],
    "med3": [med3, 3],
};

const parse = expr => {
    const tokens = expr.split(" ").filter(str => str.length > 0);
    let st = [];
    let numArgs;
    let func;
    for (const token of tokens) {
        if (token in OPERATIONS) {
            func = OPERATIONS[token][0];
            numArgs = OPERATIONS[token][1];
            st.push(func(...st.splice(st.length - numArgs, numArgs)));
        } else if (token in VARS) {
            st.push(variable(token));
        } else if (token in CONSTS) {
            st.push(cnst(CONSTS[token]));
        } else {
            st.push(cnst(parseInt(token)));
        }
    }
    return st.pop();
};

let exp = avg5(variable('x'), variable('y'), variable('z'), cnst(3), cnst(5))(1, 1, 1);
console.log(exp);