function Const(x) {
    this.value = x;
}

Const.prototype = {
    evaluate: function () {
        return this.value;
    },
    toString: function () {
        return this.value.toString();
    },
    prefix: function () {
        return this.toString();
    },
    diff: function () {
        return Const.ZERO;
    }
};
Const.ZERO = new Const(0);
Const.ONE = new Const(1);

function Variable(vName) {
    this.num = VARS[vName];
    this.name = vName;
}

Variable.prototype = {
    evaluate: function (...values) {
        return values[this.num];
    },
    toString: function () {
        return this.name;
    },
    prefix: function () {
        return this.toString();
    },
    diff: function (vName) {
        return vName === this.name ? Const.ONE : Const.ZERO;
    }
};

function operationGen(opName, f, fDiff) {
    function Operation(...values) {
        this.args = [...values];
    }

    Operation.prototype = {
        evaluate: function (...values) {
            let res = this.args.map(arg => arg.evaluate(...values));
            return f(...res);
        },
        toString: function () {
            return this.args.join(" ") + " " + opName;
        },
        prefix: function () {
            let res = this.args.map(arg => arg.prefix());
            return "(" + opName + " " + res.join(" ") + ")";
        },
        diff: function (vName) {
            return fDiff(vName, this.args);
        },
    };
    return Operation;
}

let Add = operationGen("+", (a, b) => a + b, (vName, expr) => {
    return new Add(expr[0].diff(vName), expr[1].diff(vName));
});

let Subtract = operationGen("-", (a, b) => a - b, (vName, expr) => {
    return new Subtract(expr[0].diff(vName), expr[1].diff(vName));
});

let Multiply = operationGen("*", (a, b) => a * b, (vName, expr) => {
    return new Add(
        new Multiply(expr[0].diff(vName), expr[1]),
        new Multiply(expr[0], expr[1].diff(vName))
    );
});

// (f / g)' = (f'g - fg') / g^2
let Divide = operationGen("/", (a, b) => a / b, (vName, expr) => {
    return new Divide(
        new Subtract(
            new Multiply(expr[0].diff(vName), expr[1]),
            new Multiply(expr[0], expr[1].diff(vName))
        ),
        new Multiply(expr[1], expr[1])
    );
});

let Negate = operationGen("negate", x => -x, (vName, expr) => {
    return new Negate(expr[0].diff(vName));
});

let Exp = operationGen("exp", x => Math.exp(x), (vName, expr) => {
    return new Multiply(expr[0].diff(vName), Exp(expr[0]));
});

// (atan(f))' = f' / (f^2 + 1)
let Sumexp = operationGen("sumexp", function (...args) {
    //console.log(args.toString());
    let res = args.map(arg => Math.exp(arg));
    //console.log(res);
    return res.reduce((a, b) => a + b, 0);
    //return res;
}, (vName, ...expr) => {
    let res = expr.map(arg => new Exp(arg).diff(vName));
    //return res.reduce((a, b) => new Add(a, b), Const.ZERO);
    return new Const(1);
});

// (atan2(f, g))' = (gf' - fg') / (f^2 + g^2)
/*let Softmax = operationGen("softmax", ...expr => {
    let exp1 = expr.slice(1);
    let exp0 = expr[0];
    return new Divide(new Exp(exp0), new Sumexp(exp1));
}, (vName, expr) => {
    let res = this.args.map(arg => new Exp(arg).diff(vName));
    return res.reduce((a, b) => new Add(a, b));
});*/

function errorGen(myMessage, myName) {
    function MyError(pos) {
        this.name = myName;
        this.message = myMessage + ", position: " + pos;
    }
    MyError.prototype = Object.create(Error.prototype);
    return MyError;
}

const parsePrefix = str => {
    let ind = 0;
    let expr = str + '\0';

    let endError = errorGen("Expected end of expression", "endError");
    let missedOperatorError = errorGen("Missed operator", "missedOperatorError");
    let notEnoughArgumentsError = errorGen("Not enough arguments for operator", "notEnoughArgumentsError");
    // let tooManyArgumentsError = errorGen("Too many arguments for operator", "tooManyArgumentsError");
    let wrongIdentifierError = errorGen("Found wrong identifier", "wrongIdentifierError");
    let invalidConstError = errorGen("Found invalid const", "invalidConstError");
    // let oddClosingBracketError = errorGen("Found odd closing bracket", "oddClosingBracketError");
    let missedClosingBracketError = errorGen("Expected closing bracket", "missedClosingBracketError");

    let isWhiteSpace = ch => (ch === ' ');
    function between(lower, upper) {
        return function(c) {
            return lower <= c && c <= upper;
        }
    }
    let isLetter = between('a', 'z');
    let isDigit = between('0', '9');
    const getCurrChar = () => expr[ind];
    const skipSpaces = () => {
        while(isWhiteSpace(getCurrChar())) {
            ind++;
        }
    };
    let getSequence = criterion => {
        let st = ind;
        // console.log(criterion, expr[ind], criterion(expr[ind]));
        while(criterion(expr[ind])) {
            ind++;
        }
        return expr.slice(st, ind);
    };
    const getBlock = () => getSequence(ch => {
        return (!isWhiteSpace(ch)) && (ch !== '\0') && (ch !== '(') && (ch !== ')');
    });
    let getWord = () => getSequence(ch => {
        return isLetter(ch);
    });

    const parse = () => {
        skipSpaces();
        // console.log(getCurrChar(), ind);
        if (getCurrChar() === '(') {
            ind++;
            skipSpaces();
            let stInd = ind;
            let id = getBlock();
            // console.log(id);
            if (!(id in OPERATIONS)) {
                //mb something
                throw new missedOperatorError(stInd);
                // console.log('here');
                // if const or var and currSymb - ')'?
            }
            let [op, numArgs] = OPERATIONS[id];
            let args = [];
            if (numArgs === -1) {
                while (getCurrChar() !== ')') {
                    if (getCurrChar() === '\0') {
                        throw new missedClosingBracketError(ind);
                    }
                    args.push(parse());
                }
            } else {
                for (let i = 0; i < numArgs; ++i) {
                    if (getCurrChar() === ')') {
                        throw new notEnoughArgumentsError(stInd);
                    }
                    args.push(parse());
                }
            }

            skipSpaces();
            if (getCurrChar() !== ')') {
                if (getCurrChar() === '\0') {
                    throw new missedClosingBracketError(ind);
                }
                throw new missedClosingBracketError(ind);
            }
            ind++;
            return new op(...args);
        } else if (isLetter(getCurrChar())) {
            let stInd = ind;
            let varName = getWord();
            // console.log(expr, varName, ind, isLetter(expr[0]), isLetter(expr[1]));
            if (varName in VARS) {
                return CREATED_VARS[varName];
            } else {
                throw new wrongIdentifierError(stInd);
            }
        } else if (isDigit(getCurrChar()) || getCurrChar() === '+' || getCurrChar() === '-') {
            let stInd = ind;
            let num = Number(getBlock());
            if (isNaN(num)) {
                throw new invalidConstError(stInd);
            }
            return new Const(num);
        } else if (getCurrChar() !== '\0') {
            throw new endError(ind);
        }
    };
    let ans = parse();
    // console.log(ind);
    skipSpaces();
    if (getCurrChar() !== '\0') {
        throw new endError(ind);
    }
    return ans;
};

/*const parse = expr => {
    const tokens = expr.split(" ").filter(str => str.length > 0);
    let st = [];
    for (const token of tokens) {
        if (token in OPERATIONS) {
            let [op, numArgs] = OPERATIONS[token];
            st.push(new op(...st.splice(st.length - numArgs, numArgs)));
        } else if (token in CREATED_VARS) {
            st.push(CREATED_VARS[token]);
        } else {
            st.push(new Const(Number(token)));
        }
    }
    return st.pop();
};*/

const VARS = {
    x: 0,
    y: 1,
    z: 2,
    hello: 0
};

let CREATED_VARS = {};
for (let curVar in VARS) {
    CREATED_VARS[curVar] = new Variable(curVar);
}

const OPERATIONS = {
    "+": [Add, 2],
    "-": [Subtract, 2],
    "*": [Multiply, 2],
    "/": [Divide, 2],
    "negate": [Negate, 1],
    "sumexp": [Sumexp, -1],
    //"softmax": [Softmax, -1],
};

let e = parsePrefix('(sumexp x)');
console.log(e.evaluate(0, 0, 0));